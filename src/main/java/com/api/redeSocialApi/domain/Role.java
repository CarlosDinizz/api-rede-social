package com.api.redeSocialApi.domain;

public enum Role {

    ADMIN("admin"),
    USER("user");

    private String role;

    Role(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
