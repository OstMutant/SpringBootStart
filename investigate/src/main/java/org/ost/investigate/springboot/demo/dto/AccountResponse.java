package org.ost.investigate.springboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    @JsonProperty("Name")
    String name;
    @JsonProperty("id")
    String id;
    @JsonProperty("success")
    Boolean success;
    @JsonProperty("errors")
    List<String> errors;
}
