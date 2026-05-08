package com.upm.gym.model;

import com.upm.gym.enums.Role;

public class User {

    private String userId;
    private String fullName;
    private String password;
    private Role role;

    public User() {}

    public User(String userId, String fullName,
                String password, Role role) {

        this.userId = userId;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}