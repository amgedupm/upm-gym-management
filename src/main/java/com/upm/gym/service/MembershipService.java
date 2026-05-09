package com.upm.gym.service;

import com.upm.gym.model.Membership;

import java.time.LocalDate;

/**
 * STUB IMPLEMENTATION — frontend placeholder.
 * The real implementation will be added by the backend lead.
 * Once the real version exists, delete this file or replace its body
 * with a call to the actual DAO layer.
 */
public class MembershipService {

    /**
     * Calculates the price (in SAR) for a given plan type.
     * These prices are placeholders; the backend may load them from the database.
     */
    public double getPriceForPlan(String planType) {
        return switch (planType) {
            case "1 Month"    -> 150.0;
            case "1 Semester" -> 500.0;
            case "1 Year"     -> 900.0;
            default           -> 0.0;
        };
    }

    /**
     * Calculates the expiry date based on the plan type and start date.
     */
    public LocalDate calculateExpiryDate(String planType, LocalDate startDate) {
        return switch (planType) {
            case "1 Month"    -> startDate.plusMonths(1);
            case "1 Semester" -> startDate.plusMonths(4);
            case "1 Year"     -> startDate.plusYears(1);
            default           -> startDate;
        };
    }

    /**
     * STUB — pretends to register a membership.
     * Real version will insert into the database via MembershipDAO.saveMembership().
     */
    public void register(Membership membership) {
        // Pretend the registration succeeded. No DB call yet.
        System.out.println("[STUB] Registered membership: " +
                membership.getMembershipType() +
                " for user " + membership.getUserId() +
                " from " + membership.getStartDate() +
                " to " + membership.getExpiryDate());
    }
}