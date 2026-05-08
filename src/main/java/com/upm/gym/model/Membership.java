package com.upm.gym.model;

import java.time.LocalDate;

public class Membership {

    private int membershipId;
    private String userId;
    private String membershipType;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private boolean autoRenew;

    public Membership() {}

    public Membership(int membershipId,
                      String userId,
                      String membershipType,
                      LocalDate startDate,
                      LocalDate expiryDate,
                      boolean autoRenew) {

        this.membershipId = membershipId;
        this.userId = userId;
        this.membershipType = membershipType;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.autoRenew = autoRenew;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(boolean autoRenew) {
        this.autoRenew = autoRenew;
    }
}