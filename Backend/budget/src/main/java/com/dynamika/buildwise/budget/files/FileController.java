package com.dynamika.buildwise.budget.files;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;

@RestController
@RequestMapping("/budget-lines/{lineId}/invoices")
public class FileController {

    private static final String PREFIX = "invoices/";
    private static final Set<String> ALLOWED = Set.of("application/pdf");

    private final FileStorageService storage;

    public FileController(FileStorageService storage) {
        this.storage = storage;
    }

    // Envoi à travers le service (multipart). Renvoie la clé : c'est elle qu'on garde en base.
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> upload(@PathVariable String lineId,
                                      @RequestParam("file") MultipartFile file) throws IOException {
        String type = checkType(file.getContentType());
        String key = newKey(lineId, type);
        storage.upload(key, file.getInputStream(), file.getSize(), type);
        return Map.of("key", key);
    }

    @GetMapping
    public List<String> list(@PathVariable String lineId) {
        return storage.list(PREFIX + lineId + "/");
    }

    // Téléchargement à travers le service.
    @GetMapping("/{fileName}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String lineId,
                                                        @PathVariable String fileName) {
        ResponseInputStream<GetObjectResponse> in = storage.download(PREFIX + lineId + "/" + fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(in.response().contentType()))
                .contentLength(in.response().contentLength())
                .body(new InputStreamResource(in));
    }

    // URL signée de lecture (5 min) : le front lit le fichier directement sur Floci.
    @GetMapping("/{fileName}/url")
    public Map<String, String> downloadUrl(@PathVariable String lineId, @PathVariable String fileName) {
        return Map.of("url", storage.presignedGetUrl(PREFIX + lineId + "/" + fileName));
    }

    // URL signée d'envoi (5 min) : le front envoie le fichier par PUT avec ce Content-Type.
    @PostMapping("/upload-url")
    public Map<String, String> uploadUrl(@PathVariable String lineId, @RequestParam String contentType) {
        String type = checkType(contentType);
        String key = newKey(lineId, type);
        return Map.of("key", key, "url", storage.presignedPutUrl(key, type));
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> delete(@PathVariable String lineId, @PathVariable String fileName) {
        storage.delete(PREFIX + lineId + "/" + fileName);
        return ResponseEntity.noContent().build();
    }

    private static String checkType(String contentType) {
        if (contentType == null || !ALLOWED.contains(contentType)) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "PDF seulement");
        }
        return contentType;
    }

    private static String newKey(String lineId, String contentType) {
        String ext = switch (contentType) {
            case "application/pdf" -> ".pdf";
            case "image/png" -> ".png";
            default -> ".jpg";
        };
        return PREFIX + lineId + "/" + UUID.randomUUID() + ext;
    }

    // Erreurs Floci traduites en codes HTTP lisibles.
    @ExceptionHandler(NoSuchKeyException.class)
    public ResponseEntity<String> notFound(NoSuchKeyException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fichier introuvable");
    }

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<String> s3Error(S3Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body("Floci a répondu " + e.statusCode() + " : " + e.awsErrorDetails().errorCode());
    }

    @ExceptionHandler(SdkClientException.class)
    public ResponseEntity<String> unreachable(SdkClientException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Floci injoignable ou clés absentes : " + e.getMessage());
    }
}
