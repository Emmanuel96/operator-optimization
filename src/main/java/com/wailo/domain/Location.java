package com.wailo.domain;

import com.wailo.domain.enumeration.LocationType;
import lombok.Data;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Location.
 */
@Data
@Entity
@Table(name = "location")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "uwi")
    private String uwi;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @NotNull
    @Column(name = "ezops_id", nullable = false)
    private Long ezopsId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LocationType type;

    @ManyToOne
    private Pad pad;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Location id(Long id) {
        this.setId(id);
        return this;
    }

    public Location ezopsId(Long ezopsId) {
        this.setEzopsId(ezopsId);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Location name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUwi() {
        return this.uwi;
    }

    public Location uwi(String uwi) {
        this.setUwi(uwi);
        return this;
    }

    public void setUwi(String uwi) {
        this.uwi = uwi;
    }

    public Float getLatitude() {
        return this.latitude;
    }

    public Location latitude(Float latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return this.longitude;
    }

    public Location longitude(Float longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public LocationType getType() {
        return this.type;
    }

    public Location type(LocationType type) {
        this.setType(type);
        return this;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public Pad getPad() {
        return this.pad;
    }

    public void setPad(Pad pad) {
        this.pad = pad;
    }

    public Location pad(Pad pad) {
        this.setPad(pad);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", uwi='" + getUwi() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", type='" + getType() + "'" +
            "}";
    }
}
