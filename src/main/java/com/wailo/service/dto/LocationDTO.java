package com.wailo.service.dto;

import com.wailo.domain.enumeration.LocationType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wailo.domain.Location} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LocationDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String uwi;

    @NotNull
    private Float latitude;

    @NotNull
    private Float longitude;

    @NotNull
    private LocationType type;

    private PadDTO pad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUwi() {
        return uwi;
    }

    public void setUwi(String uwi) {
        this.uwi = uwi;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public PadDTO getPad() {
        return pad;
    }

    public void setPad(PadDTO pad) {
        this.pad = pad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationDTO)) {
            return false;
        }

        LocationDTO locationDTO = (LocationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, locationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", uwi='" + getUwi() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", type='" + getType() + "'" +
            ", pad=" + getPad() +
            "}";
    }
}
