CREATE TABLE IF NOT EXISTS agricultural_federation_app.cooperative_federation (
    id UUID PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS agricultural_federation_app.cooperative (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) UNIQUE NOT NULL,
    number VARCHAR(20) UNIQUE NOT NULL,
    specialty VARCHAR(20) NOT NULL,
    creation_date DATE DEFAULT CURRENT_DATE,
    location VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    coop_fed_id UUID REFERENCES agricultural_federation_app.cooperative_federation(id),
    status VARCHAR(10) DEFAULT 'active' NOT NULL,
    federation_auth BOOLEAN DEFAULT FALSE NOT NULL
    );

CREATE TYPE agricultural_federation_app.gender AS ENUM ('M', 'F');
CREATE TYPE agricultural_federation_app.role AS ENUM (
    'JUNIOR',
    'SENIOR',
    'SECRETARY',
    'TREASURER',
    'VICE_PRESIDENT',
    'PRESIDENT'
);

CREATE TABLE IF NOT EXISTS agricultural_federation_app.member (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    birth_date DATE NOT NULL,
    gender agricultural_federation_app.gender NOT NULL,
    address VARCHAR(100) NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    profession VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role agricultural_federation_app.role NOT NULL,
    joined_at TIMESTAMP DEFAULT NOW(),
    coop_id UUID REFERENCES agricultural_federation_app.cooperative(id),
    status VARCHAR(10) DEFAULT 'active' NOT NULL
    );

CREATE TABLE IF NOT EXISTS agricultural_federation_app.referrer (
    id UUID PRIMARY KEY,
    referrer_at TIMESTAMP DEFAULT NOW() NOT NULL,
    new_member UUID REFERENCES agricultural_federation_app.member(id) NOT NULL,
    referrer_id UUID REFERENCES agricultural_federation_app.member(id) NOT NULL
    );