package com.wailo.models.ezops.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationalItemResponse {
    private List<OperationalItemResponseData> data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OperationalItemResponseData{
        private long id;

        @JsonProperty("priority_score")
        private String priorityScore;

        @JsonProperty("due_date")
        private LocalDate dueDate;

        @JsonProperty("locationable_id")
        private Long locationableId;
    }
}
