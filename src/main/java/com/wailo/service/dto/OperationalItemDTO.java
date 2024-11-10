package com.wailo.service.dto;

import com.wailo.domain.enumeration.OperationalItemTypes;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wailo.domain.OperationalItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OperationalItemDTO implements Serializable {

    private Long id;

    @NotNull
    private OperationalItemTypes type;

    @NotNull
    private Double priorityScore;

    private LocalDate dueDate;

    private LocationDTO location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperationalItemTypes getType() {
        return type;
    }

    public void setType(OperationalItemTypes type) {
        this.type = type;
    }

    public Double getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(Double priorityScore) {
        this.priorityScore = priorityScore;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperationalItemDTO)) {
            return false;
        }

        OperationalItemDTO operationalItemDTO = (OperationalItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, operationalItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperationalItemDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", priorityScore=" + getPriorityScore() +
            ", dueDate='" + getDueDate() + "'" +
            ", location=" + getLocation() +
            "}";
    }
}
