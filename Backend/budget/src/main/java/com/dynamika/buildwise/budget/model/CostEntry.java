package com.dynamika.buildwise.budget.model;

import com.dynamika.buildwise.budget.enums.CostSource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cost_entry")
public class CostEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CostSource source;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    // Id de l'événement qui a produit ce coût : unique pour ne jamais compter deux fois le même. Null = saisie à la main.
    @Column(unique = true)
    private UUID eventId;

    @Column(nullable = false)
    private Instant occurredAt;

    /** Côté propriétaire : cost_entry.budget_line_id -> budget_line.id. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "budget_line_id")
    private BudgetLine budgetLine;

    public CostEntry() {
    }

    public UUID getId() { return id; }

    public CostSource getSource() { return source; }
    public void setSource(CostSource source) { this.source = source; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }

    public BudgetLine getBudgetLine() { return budgetLine; }
    public void setBudgetLine(BudgetLine budgetLine) { this.budgetLine = budgetLine; }
}
