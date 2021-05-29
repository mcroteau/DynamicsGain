create table if not exists users (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	phone character varying(40) NOT NULL,
	username character varying(254) NOT NULL,
	password character varying(155) NOT NULL,
	uuid character varying(155),
	stripe_customer_id text,
	organization_id bigint,
	stripe_account_id text,
	charity boolean default false,
	date_created bigint default 0
);

create table if not exists roles (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(155) NOT NULL UNIQUE
);

create table if not exists user_permissions(
	user_id bigint REFERENCES users(id),
	permission character varying(55)
);

create table if not exists user_roles(
	role_id bigint NOT NULL REFERENCES roles(id),
	user_id bigint NOT NULL REFERENCES users(id)
);

create table if not exists nations (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(254) NOT NULL
);

create table if not exists states (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(254) NOT NULL,
	abbreviation character varying(12),
	nation_id bigint NOT NULL REFERENCES nations(id)
);

create table if not exists towns (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(254) NOT NULL,
    count bigint default 0,
    town_uri character varying(255),
	state_id bigint NOT NULL REFERENCES states(id)
);

create table if not exists organizations (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(255) NOT NULL,
	description text,
	uri character varying (254),
	latitude character varying (254),
	longitude character varying (254),
	town_id bigint NOT NULL REFERENCES towns(id),
	stripe_account_id text,
	constraint unique_uri unique(uri)
);

create table if not exists donations(
	id bigint PRIMARY KEY AUTO_INCREMENT,
	amount decimal,
	processed boolean,
	charge_id text,
	subscription_id text,
	cancelled boolean default false,
	donation_date bigint,
	user_id bigint NOT NULL REFERENCES users(id),
	organization_id bigint REFERENCES organizations(id)
);

create table if not exists ownership_requests (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(254) NOT NULL,
	email character varying (254),
	phone character varying (254),
	date_requested bigint,
	approved boolean
);