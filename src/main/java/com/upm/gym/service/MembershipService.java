package com.upm.gym.service;

import com.upm.gym.dao.MembershipDAO;
import com.upm.gym.model.Membership;

import java.time.LocalDate;

/**
 * Service layer for membership-related operations.
 */
public class MembershipService {

    private final MembershipDAO membershipDAO;

    public MembershipService() {
        membershipDAO = new MembershipDAO();
    }

    /**
     * Calculates the price (in SAR) for a given plan type.
     */
    public double getPriceForPlan(String planType) {

        return switch (planType) {

            case "1 Month" -> 150.0;

            case "1 Semester" -> 500.0;

            case "1 Year" -> 900.0;

            default -> 0.0;
        };
    }

    /**
     * Calculates the expiry date based on the plan type and start date.
     */
    public LocalDate calculateExpiryDate(
            String planType,
            LocalDate startDate) {

        return switch (planType) {

            case "1 Month" ->
                    startDate.plusMonths(1);

            case "1 Semester" ->
                    startDate.plusMonths(4);

            case "1 Year" ->
                    startDate.plusYears(1);

            default -> startDate;
        };
    }

    /**
     * Registers a membership in the database.
     */
    public void register(Membership membership) {

        membershipDAO.saveMembership(
                membership
        );
    }

    /**
     * Retrieves the user's active membership.
     */
    public Membership getActiveMembership(
            String userId) {

        return membershipDAO
                .getActiveMembershipByUserId(
                        userId
                );
    }

    /**
     * Cancels a membership.
     */
    public boolean cancel(int membershipId) {

        return membershipDAO
                .cancelMembership(
                        membershipId
                );
    }

    /**
     * Count of active memberships.
     */
    public int countActiveMemberships() {

        return membershipDAO
                .countActiveMemberships();
    }

    /**
     * Count of total registered members.
     */
    public int countTotalMembers() {

        return membershipDAO
                .countTotalMembers();
    }
}