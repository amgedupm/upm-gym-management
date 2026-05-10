USE upm_gym;

-- =========================
-- USERS
-- =========================

INSERT INTO users
VALUES (
    '4410097',
    'Ahmed Hakimi',
    '1234',
    'STUDENT'
);

INSERT INTO users
VALUES (
    '4413828',
    'Sara Al-Otaibi',
    '1234',
    'STUDENT'
);

INSERT INTO users
VALUES (
    '4510353',
    'Khaled Al-Ghamdi',
    '1234',
    'FACULTY'
);

INSERT INTO users
VALUES (
    'coach01',
    'Coach Ali',
    'coach123',
    'COACH'
);

INSERT INTO users
VALUES (
    'staff01',
    'Ahmad Helper',
    'staff123',
    'STAFF'
);

INSERT INTO users
VALUES (
    'sec01',
    'Omar Security',
    'sec123',
    'SECURITY'
);

-- =========================
-- MEMBERSHIPS
-- =========================

INSERT INTO memberships (
    user_id,
    membership_type,
    start_date,
    expiry_date,
    auto_renew,
    status
)
VALUES (
    '4410097',
    '1 Semester',
    '2026-05-01',
    '2026-09-01',
    true,
    'ACTIVE'
);

INSERT INTO memberships (
    user_id,
    membership_type,
    start_date,
    expiry_date,
    auto_renew,
    status
)
VALUES (
    '4413828',
    '1 Year',
    '2026-01-01',
    '2027-01-01',
    false,
    'ACTIVE'
);

INSERT INTO memberships (
    user_id,
    membership_type,
    start_date,
    expiry_date,
    auto_renew,
    status
)
VALUES (
    '4510353',
    '1 Month',
    CURDATE() - INTERVAL 40 DAY,
    CURDATE() - INTERVAL 10 DAY,
    false,
    'EXPIRED'
);

-- =========================
-- BOOKINGS
-- =========================

INSERT INTO bookings (
    user_id,
    facility_name,
    booking_date,
    start_time,
    end_time,
    status
)
VALUES
(
    '4410097',
    'Football Field',
    CURDATE() + INTERVAL 2 DAY,
    '18:00:00',
    '19:00:00',
    'APPROVED'
),
(
    '4413828',
    'Basketball Court',
    CURDATE() + INTERVAL 3 DAY,
    '20:00:00',
    '21:30:00',
    'PENDING'
),
(
    '4510353',
    'Football Field',
    CURDATE() + INTERVAL 5 DAY,
    '17:00:00',
    '18:00:00',
    'PENDING'
),
(
    '4410097',
    'Football Field',
    CURDATE(),
    '16:00:00',
    '17:00:00',
    'APPROVED'
);