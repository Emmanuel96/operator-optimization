package com.wailo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wailo.domain.enumeration.OperationalItemTypes;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A OperationalItem.
 */
@Entity
@Table(name = "operational_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OperationalItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private OperationalItemTypes type;

    @NotNull
    @Column(name = "priority_score", nullable = false)
    private Double priorityScore;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "pad" }, allowSetters = true)
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OperationalItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperationalItemTypes getType() {
        return this.type;
    }

    public OperationalItem type(OperationalItemTypes type) {
        this.setType(type);
        return this;
    }

    public void setType(OperationalItemTypes type) {
        this.type = type;
    }

    public Double getPriorityScore() {
        return this.priorityScore;
    }

    public OperationalItem priorityScore(Double priorityScore) {
        this.setPriorityScore(priorityScore);
        return this;
    }

    public void setPriorityScore(Double priorityScore) {
        this.priorityScore = priorityScore;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public OperationalItem dueDate(LocalDate dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public OperationalItem location(Location location) {
        this.setLocation(location);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperationalItem)) {
            return false;
        }
        return id != null && id.equals(((OperationalItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperationalItem{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", priorityScore=" + getPriorityScore() +
            ", dueDate='" + getDueDate() + "'" +
            "}";
    }
}
