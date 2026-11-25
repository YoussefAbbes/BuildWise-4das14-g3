package com.dynamika.buildwise.budget.model;

import com.dynamika.buildwise.budget.enums.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// projectId vient du service planning : on garde seulement son identifiant, jamais l'objet.
@Entity
@Table(name = "budget_line")
public class BudgetLine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)   // Hibernate génère l'UUID, colonne de type uuid
    private UUID id;

    @Column(nullable = false)
    private UUID projectId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, precision = 14, scale = 2)   // NUMERIC(14,2) : jamais de double pour de l'argent
    private BigDecimal amount;

    /**
     * Côté inverse de BudgetLine 1 -- * CostEntry (la clé étrangère est dans cost_entry).
     * Supprimer la ligne supprime ses coûts ; retirer un coût de la liste le supprime aussi.
     */
    @OneToMany(mappedBy = "budgetLine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CostEntry> costEntries = new ArrayList<>();

    public BudgetLine() {
    }

    /** Garde les deux côtés de la relation synchronisés. */
    public void addCostEntry(CostEntry entry) {
        costEntries.add(entry);
        entry.setBudgetLine(this);
    }

    public void removeCostEntry(CostEntry entry) {
        costEntries.remove(entry);
        entry.setBudgetLine(null);
    }

    public UUID getId() { return id; }

    public UUID getProjectId() { return projectId; }
    public void setProjectId(UUID projectId) { this.projectId = projectId; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public List<CostEntry> getCostEntries() { return costEntries; }
}
