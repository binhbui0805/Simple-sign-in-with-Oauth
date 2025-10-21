package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "letters, numbers, dot, underscore, dash only")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty
    @Size(min = 8, max = 128)
    private char[] password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public char[] getPassword() { return password; }
    public void setPassword(char[] password) { this.password = password; }
}
