package com.upm.gym.service;

import com.upm.gym.model.Membership;

import java.time.LocalDate;

/**
 * STUB IMPLEMENTATION — frontend placeholder.
 * The real implementation will be added by the backend lead.
 * Once the real version exists, delete this file or replace its body
 */
public class MembershipService {

    /**
     * Calculates the price (in SAR) for a given plan type.
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
        System.out.println("[STUB] Registered membership: " +
                membership.getMembershipType() +
                " for user " + membership.getUserId() +
                " from " + membership.getStartDate() +
                " to " + membership.getExpiryDate());
    }

    /**
     * STUB — pretends to fetch the user's active membership.
     * Real version will query MembershipDAO.getActiveMembershipByUserId(userId).
     */
    public Membership getActiveMembership(String userId) {
        Membership fake = new Membership();
        fake.setMembershipId(1);
        fake.setUserId(userId);
        fake.setMembershipType("1 Semester");
        fake.setStartDate(LocalDate.of(2026, 5, 1));
        fake.setExpiryDate(LocalDate.of(2026, 9, 1));
        fake.setAutoRenew(false);
        return fake;
    }

    /**
     * STUB — pretends to cancel a membership.
     * Real version will update the DB row's status to CANCELLED.
     */
    public boolean cancel(int membershipId) {
        System.out.println("[STUB] Cancelled membership #" + membershipId);
        return true;
    }
    /**
     * STUB — count of active memberships.
     * Real version: SELECT COUNT(*) FROM memberships WHERE expiry_date >= CURDATE() AND status = 'ACTIVE'.
     */
    public int countActiveMemberships() {
        return 23;
    }

    /**
     * STUB — count of all members ever registered.
     * Real version: SELECT COUNT(*) FROM users WHERE role IN ('STUDENT', 'FACULTY').
     */
    public int countTotalMembers() {
        return 31;
    }
}