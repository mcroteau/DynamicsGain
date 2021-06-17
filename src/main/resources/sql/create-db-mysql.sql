create table if not exists users (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	phone varchar(40) NOT NULL,
	username varchar(254) NOT NULL,
	password varchar(155) NOT NULL,
	uuid varchar(154),
	stripe_customer_id text,
	organization_id bigint,
	stripe_account_id text,
	charity boolean default false,
	activated boolean default false,
	date_created bigint default 0
);

create table if not exists roles (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name varchar(155) NOT NULL UNIQUE
);

create table if not exists user_permissions(
	user_id bigint REFERENCES users(id),
	permission varchar(55)
);

create table if not exists user_roles(
	role_id bigint NOT NULL REFERENCES roles(id),
	user_id bigint NOT NULL REFERENCES users(id)
);

create table if not exists nations (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name varchar(254) NOT NULL
);

create table if not exists states (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name varchar(254) NOT NULL,
	abbreviation varchar(12),
	nation_id bigint NOT NULL REFERENCES nations(id)
);

create table if not exists towns (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name varchar(254) NOT NULL,
    count bigint default 0,
    population bigint default 0,
    town_uri varchar(255),
	state_id bigint NOT NULL REFERENCES states(id)
);

create table if not exists organizations (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name varchar(255) NOT NULL,
	description text,
	uri varchar(254),
	latitude varchar(254),
	longitude varchar(254),
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
	name varchar(254) NOT NULL,
	email varchar(254),
	phone varchar(254),
	date_requested bigint,
	approved boolean,
	organization_id bigint REFERENCES organizations(id)
);

create table products(
	id bigint PRIMARY KEY AUTO_INCREMENT,
    nickname varchar(255),
	stripe_id text
);

create table prices(
	id bigint PRIMARY KEY AUTO_INCREMENT,
	stripe_id text,
    amount decimal default 0.0,
    nickname varchar(255),
	product_id bigint NOT NULL REFERENCES products(id)
);
