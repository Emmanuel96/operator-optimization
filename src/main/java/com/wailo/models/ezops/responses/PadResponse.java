package com.wailo.models.ezops.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PadResponse {
    private List<PadResponseData> data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PadResponseData{
        private Long id;
        private String alias;
    }
}
