package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private char[] password;

    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }
    public char[] getPassword() { return password; }
    public void setPassword(char[] p) { this.password = p; }
}
