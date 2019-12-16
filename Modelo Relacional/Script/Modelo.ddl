DROP TABLE vehicles CASCADE CONSTRAINTS;
DROP TABLE vehicle_types CASCADE CONSTRAINTS;
DROP TABLE bicycles CASCADE CONSTRAINTS;
DROP TABLE electric_scooters CASCADE CONSTRAINTS;
DROP TABLE eletric_scooter_types CASCADE CONSTRAINTS;
DROP TABLE clients CASCADE CONSTRAINTS;
DROP TABLE parks CASCADE CONSTRAINTS;
DROP TABLE park_vehicle CASCADE CONSTRAINTS;
DROP TABLE pending_registrations CASCADE CONSTRAINTS;
DROP TABLE invoices CASCADE CONSTRAINTS;
DROP TABLE park_capacity CASCADE CONSTRAINTS;
DROP TABLE user_type CASCADE CONSTRAINTS;
DROP TABLE trip CASCADE CONSTRAINTS;
DROP TABLE receipts CASCADE CONSTRAINTS;
DROP TABLE users CASCADE CONSTRAINTS;
CREATE TABLE vehicles (
  vehicle_id        number(8) GENERATED AS IDENTITY, 
  vehicle_type_name varchar2(50) NOT NULL, 
  available         number(1) NOT NULL CHECK(1), 
  latitude          number(9, 6) NOT NULL, 
  longitude         number(9, 6) NOT NULL, 
  altitude          number(6, 2) NOT NULL, 
  PRIMARY KEY (vehicle_id), 
  CONSTRAINT ck_vehicles_available 
    CHECK (available = 0 OR available = 1));
CREATE TABLE vehicle_types (
  name varchar2(50) NOT NULL, 
  PRIMARY KEY (name));
CREATE TABLE bicycles (
  vehicle_id        number(8) NOT NULL, 
  "size"            number(2) NOT NULL, 
  vehicle_type_name varchar2(50) NOT NULL, 
  PRIMARY KEY (vehicle_id));
CREATE TABLE electric_scooters (
  vehicle_id                number(8) NOT NULL, 
  eletric_scooter_type_name varchar2(50) NOT NULL, 
  type_id                   number(3) NOT NULL, 
  battery_level             number(3) NOT NULL, 
  vehicle_type_name         varchar2(50) NOT NULL, 
  PRIMARY KEY (vehicle_id), 
  CONSTRAINT ck_eletric_scooters_battery_level 
    CHECK (battery_level >= 0 AND battery_level <= 100));
CREATE TABLE eletric_scooter_types (
  eletric_scooter_type_name   varchar2(50) NOT NULL, 
  battery_per_min             number(5, 2) NOT NULL, 
  battery_per_km              number(5, 2) NOT NULL, 
  time_to_charge_fully_minute number(3) NOT NULL, 
  PRIMARY KEY (eletric_scooter_type_name));
CREATE TABLE clients (
  user_email             varchar2(50) NOT NULL, 
  points                 number(10) DEFAULT 0 NOT NULL, 
  credit_card_number     char(16) NOT NULL, 
  credit_card_expiration date NOT NULL, 
  credit_card_secret     number(3) NOT NULL, 
  height_m               number(4, 2) NOT NULL, 
  weight_kg              number(5, 2) NOT NULL, 
  gender                 char(1) NOT NULL, 
  is_riding              char(1) DEFAULT '0' NOT NULL, 
  age                    number(3) NOT NULL, 
  PRIMARY KEY (user_email), 
  CONSTRAINT ck_users_gender 
    CHECK (gender = 'm' OR gender = 'f'), 
  CONSTRAINT ck_users_is_riding 
    CHECK (is_riding = 0 OR is_riding = 1), 
  CONSTRAINT ck_users_is_admin 
    CHECK (is_admin = 0 OR is_admin = 1));
CREATE TABLE parks (
  id        number(6) GENERATED AS IDENTITY, 
  name      varchar2(100) NOT NULL, 
  latitude  number(9, 6) NOT NULL CHECK(Check if latitude and longitude are, together, unique), 
  longitude number(9, 6) NOT NULL, 
  altitude  number(6, 2) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE park_vehicle (
  park_id    number(6) NOT NULL, 
  vehicle_id number(8) NOT NULL, 
  PRIMARY KEY (park_id, 
  vehicle_id));
CREATE TABLE pending_registrations (
  email                  varchar2(50) NOT NULL, 
  paid                   number(6, 2) DEFAULT 0 NOT NULL, 
  amount_left_to_pay     number(6, 2) DEFAULT 10 NOT NULL, 
  credit_card_number     number(16) NOT NULL, 
  credit_card_expiration date NOT NULL, 
  credit_card_secret     number(3) NOT NULL, 
  height                 number(4, 2) NOT NULL, 
  weight                 number(4, 2) NOT NULL, 
  gender                 char(1) NOT NULL, 
  age                    number(3) NOT NULL, 
  PRIMARY KEY (email), 
  CONSTRAINT ck_pending_registration_paid 
    CHECK (paid = 0 OR paid = 1), 
  CONSTRAINT ck_pending_registrations_gender 
    CHECK (gender = 'm' OR gender = 'f'));
CREATE TABLE invoices (
  user_email         varchar2(50) NOT NULL, 
  due_date           date NOT NULL, 
  amount             number(9, 2) NOT NULL, 
  amount_left_to_pay number(9, 2) DEFAULT null NOT NULL, 
  usage_cost         number(9, 2) DEFAULT 0 NOT NULL, 
  penalisation_cost  number(9, 2) DEFAULT 0 NOT NULL, 
  PRIMARY KEY (user_email, 
  due_date), 
  CONSTRAINT "function to update amount left to pay to be equal to the amount" 
    CHECK (a));
CREATE TABLE park_capacity (
  park_id           number(6) NOT NULL, 
  vehicle_type_name varchar2(50) NOT NULL, 
  parkCapacity          number(10) NOT NULL,
  amount_occupied   number(10) DEFAULT 0 NOT NULL, 
  PRIMARY KEY (park_id, 
  vehicle_type_name));
CREATE TABLE user_type (
  name varchar2(50) NOT NULL, 
  PRIMARY KEY (name));
CREATE TABLE trip (
  start_time    timestamp(0) DEFAULT systimestamp NOT NULL, 
  user_email    varchar2(50) NOT NULL, 
  vehicles_id   number(8) NOT NULL, 
  start_park_id number(6) NOT NULL, 
  end_park_id   number(6), 
  end_time      timestamp(0), 
  PRIMARY KEY (start_time, 
  user_email));
CREATE TABLE receipts (
  user_email       varchar2(50) NOT NULL, 
  due_date         date NOT NULL, 
  points_used      number(10) NOT NULL, 
  amount_paid_cash number(9, 2) NOT NULL, 
  PRIMARY KEY (user_email, 
  due_date));
CREATE TABLE users (
  user_email     varchar2(50) NOT NULL, 
  user_type_name varchar2(50) NOT NULL, 
  password       varchar2(20) NOT NULL, 
  PRIMARY KEY (user_email));
ALTER TABLE vehicles ADD CONSTRAINT FKvehicles221805 FOREIGN KEY (vehicle_type_name) REFERENCES vehicle_types (name);
ALTER TABLE bicycles ADD CONSTRAINT FKbicycles673707 FOREIGN KEY (vehicle_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE electric_scooters ADD CONSTRAINT FKelectric_s751542 FOREIGN KEY (vehicle_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE electric_scooters ADD CONSTRAINT FKelectric_s398869 FOREIGN KEY (eletric_scooter_type_name) REFERENCES eletric_scooter_types (eletric_scooter_type_name);
ALTER TABLE park_vehicle ADD CONSTRAINT FKpark_vehic492903 FOREIGN KEY (park_id) REFERENCES parks (id);
ALTER TABLE park_vehicle ADD CONSTRAINT FKpark_vehic824993 FOREIGN KEY (vehicle_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE invoices ADD CONSTRAINT FKinvoices584211 FOREIGN KEY (user_email) REFERENCES clients (user_email);
ALTER TABLE park_capacity ADD CONSTRAINT FKpark_capac456883 FOREIGN KEY (park_id) REFERENCES parks (id);
ALTER TABLE park_capacity ADD CONSTRAINT FKpark_capac300630 FOREIGN KEY (vehicle_type_name) REFERENCES vehicle_types (name);
ALTER TABLE trip ADD CONSTRAINT FKtrip526617 FOREIGN KEY (user_email) REFERENCES clients (user_email);
ALTER TABLE trip ADD CONSTRAINT FKtrip496709 FOREIGN KEY (start_park_id) REFERENCES parks (id);
ALTER TABLE trip ADD CONSTRAINT FKtrip92478 FOREIGN KEY (end_park_id) REFERENCES parks (id);
ALTER TABLE receipts ADD CONSTRAINT FKreceipts808514 FOREIGN KEY (user_email, due_date) REFERENCES invoices (user_email, due_date);
ALTER TABLE trip ADD CONSTRAINT FKtrip321568 FOREIGN KEY (vehicles_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE clients ADD CONSTRAINT FKclients536680 FOREIGN KEY (user_email) REFERENCES users (user_email);
ALTER TABLE users ADD CONSTRAINT FKusers201629 FOREIGN KEY (user_type_name) REFERENCES user_type (name);
