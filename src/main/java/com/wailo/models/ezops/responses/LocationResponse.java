package com.wailo.models.ezops.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationResponse {
    private List<LocationResponseDataObject> data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LocationResponseDataObject{
        private Long id;
        private String name;
        private String uwi;

        @JsonProperty("surface_latitude")
        private String surfaceLatitude;

        @JsonProperty("surface_longitude")
        private String surfaceLongitude;

        private String type;

        @JsonProperty("pad_id")
        private String padId;
    }
}
