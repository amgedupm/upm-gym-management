USE upm_gym;

INSERT INTO users
VALUES (
    '4410097',
    'Ahmed Hakimi',
    '1234',
    'STUDENT'
);

INSERT INTO memberships (
    user_id,
    membership_type,
    start_date,
    expiry_date,
    auto_renew
)
VALUES (
    '4410097',
    'Semester',
    '2026-05-01',
    '2026-09-01',
    true
);