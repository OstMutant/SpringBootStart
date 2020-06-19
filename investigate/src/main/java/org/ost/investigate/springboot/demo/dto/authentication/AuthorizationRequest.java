package org.ost.investigate.springboot.demo.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorizationRequest {
    @JsonProperty("grant_type")
    @Builder.Default
    private String grantType = "password";
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
