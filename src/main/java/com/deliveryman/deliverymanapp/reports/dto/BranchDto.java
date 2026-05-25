package com.deliveryman.deliverymanapp.reports.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchDto {

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("nameA")
    private String nameA;
}
