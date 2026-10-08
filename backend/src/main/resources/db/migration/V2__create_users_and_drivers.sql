CREATE TABLE users (
  id        BIGSERIAL PRIMARY KEY,
  email     VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  full_name VARCHAR(120) NOT NULL,
  phone     VARCHAR(20),
  role      VARCHAR(20) NOT NULL CHECK (role IN ('RIDER', 'DRIVER', 'ADMIN')),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE drivers (
  id            BIGINT PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
  vehicle_model VARCHAR(80) NOT NULL,
  vehicle_plate VARCHAR(20) NOT NULL UNIQUE,
  created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);


