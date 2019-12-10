DROP TABLE vehicles CASCADE CONSTRAINTS;
DROP TABLE vehicle_types CASCADE CONSTRAINTS;
DROP TABLE bicycles CASCADE CONSTRAINTS;
DROP TABLE electric_scooters CASCADE CONSTRAINTS;
DROP TABLE electric_scooter_types CASCADE CONSTRAINTS;
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

-- Tabela Vehicles
CREATE TABLE vehicles (
  vehicle_id        number(8) GENERATED AS IDENTITY, 
  vehicle_type_name varchar2(50) constraint nn_vehicles_vehicle_type_name NOT NULL
                                 constraint pk_vehicles_vehicle_type_name PRIMARY KEY, 
  available         number(1)    constraint nn_vehicles_available NOT NULL
                                 CONSTRAINT ck_vehicles_available CHECK (available = 0 OR available = 1),
  latitude          number(9, 6) constraint nn_vehicles_latitude NOT NULL
                                 constraint ck_vehicles_latitude CHECK (latitude between -90 and 90), 
  longitude         number(9, 6) constraint nn_vehicles_longitude NOT NULL
                                 constraint ck_vehicles_longitude CHECK (longitude between -180 and 180), 
  altitude          number(6, 2) constraint nn_vehicles_altitude NOT NULL,
  constraint uk_vehicles_longitude_latitude_altitude unique(latitude,longitude,altitude)
);

-- Tabela Vehicle_Types

CREATE TABLE vehicle_types (
  name varchar2(50) constraint pk_vehicle_types_name PRIMARY KEY
  );

-- Tabela Bicycles 
CREATE TABLE bicycles (
  vehicle_id        number(8) constraint pk_bicycles_vehicle_id PRIMARY KEY,                  
  bicycle_size      number(2) constraint nn_bicycles_bicycle_size NOT NULL
                              constraint ck_bicycles_bicycle_size check (bicycle_size>=15)
  );

-- Tabela Electric_Scooters  
CREATE TABLE electric_scooters (
  vehicle_id                number(8) constraint pk_electric_scooters_vehicle_id   PRIMARY KEY, 
  electric_scooter_type_name varchar2(50) constraint nn_electric_scooters_electric_scooter_type_name NOT NULL, 
  type_id                   number(3)    constraint nn_electric_scooters_type_id NOT NULL, 
  battery_level             number(3) constraint nn_electric_scooters_battery_level NOT NULL
                                      constraint ck_electric_scooters_battery_level CHECK (battery_level >= 0 AND battery_level <= 100)
  );

-- Tabela Electric_Scooter_Types  
CREATE TABLE electric_scooter_types (
  electric_scooter_type_name   varchar2(50) constraint pk_eletric_scooter_types_eletric_scooter_type_name PRIMARY KEY, 
  battery_per_min             number(5, 2) constraint nn_eletric_scooter_types_battery_per_min NOT NULL, 
  battery_per_km              number(5, 2) constraint nn_eletric_scooter_types_battery_per_km NOT NULL, 
  time_to_charge_fully_minute number(3)    constraint nn_electric_scooter_types_time_to_charge_fully_minute NOT NULL
);

-- Tabela Clients
CREATE TABLE clients (
  user_email             varchar2(50) constraint pk_clients_user_email PRIMARY KEY, 
  points                 number(10)   DEFAULT 0 
                                      constraint nn_clients_points NOT NULL, 
  credit_card_number     char(16)     constraint nn_clients_credit_card_number NOT NULL,
  credit_card_expiration date         constraint nn_clients_credit_card_expiration NOT NULL, 
  credit_card_secret     number(3)    constraint nn_clients_credit_card_secret NOT NULL
                                      constraint ck_clients_credit_card_secret CHECK (REGEXP_LIKE(credit_card_secret, '\d{3}$')), 
  height_m               number(4, 2) constraint nn_clients_height_m NOT NULL, 
  weight_kg              number(5, 2) constraint nn_clients_weight_kg NOT NULL, 
  gender                 char(1)      constraint nn_clients_gender NOT NULL
                                      constraint ck_clients_gender CHECK (REGEXP_LIKE(gender, 'M|F', 'i')), 
  is_riding              char(1)      DEFAULT '0' 
                                      constraint nn_clients_is_riding NOT NULL
                                      constraint ck_clients_is_riding  CHECK (is_riding = 0 OR is_riding = 1), 
  age                    number(3)    constraint nn_clients_age NOT NULL
  );
  

-- Tabela Parks    
CREATE TABLE parks (
  park_id   number(6) GENERATED AS IDENTITY
                      constraint pk_parks_park_id PRIMARY KEY,
  park_name      varchar2(100) constraint nn_parks_park_name NOT NULL, 
  latitude  number(9, 6) constraint nn_parks_latitude NOT NULL,
  longitude number(9, 6) constraint nn_parks_longitude NOT NULL,
  altitude  number(6, 2) constraint nn_parks_altitude NOT NULL,
  constraint uk_parks_longitude_latitude_altitude unique(latitude,longitude,altitude)
  );
  
-- Tabela park_vehicle
CREATE TABLE park_vehicle (
  park_id    number(6), 
  vehicle_id number(8), 
  CONSTRAINT pk_park_vehicle_park_id_vehicle_id PRIMARY KEY (park_id, vehicle_id)
);

-- Tabela pending_registrations 
CREATE TABLE pending_registrations (
  email                  varchar2(50) CONSTRAINT pk_pending_registrations_email PRIMARY KEY, 
  paid                   number(6, 2) DEFAULT 0 CONSTRAINT nn_pending_registrations_paid NOT NULL
                                                 CONSTRAINT ck_pending_registration_paid CHECK (paid = 0 OR paid = 1),
  amount_left_to_pay     number(6, 2) DEFAULT 10 CONSTRAINT nn_pending_registrations_amount_left_to_pay NOT NULL, 
  credit_card_number     number(16) CONSTRAINT nn_pending_registrations_credit_card_number NOT NULL, 
  credit_card_expiration date CONSTRAINT nn_pending_registrations_credit_card_expiration NOT NULL, 
  credit_card_secret     number(3) CONSTRAINT nn_pending_registrations_credit_card_secret NOT NULL, 
  height                 number(4, 2) CONSTRAINT nn_pending_registrations_height NOT NULL, 
  weight                 number(4, 2) CONSTRAINT nn_pending_registrations_weight NOT NULL, 
  gender                 char(1) CONSTRAINT nn_pending_registrations_gender NOT NULL
                                 CONSTRAINT ck_pending_registrations_gender CHECK(REGEXP_LIKE(gender, 'M|F', 'i')),
  age                    number(3) CONSTRAINT nn_pending_registrations_age NOT NULL 
);

-- Tabela invoices
CREATE TABLE invoices (
  user_email         varchar2(50), 
  payment_start_date date, 
  amount             number(9, 2) CONSTRAINT nn_invoices_amount NOT NULL, 
  amount_left_to_pay number(9, 2) DEFAULT null, 
  usage_cost         number(9, 2) DEFAULT 0 NOT NULL, 
  penalisation_cost  number(9, 2) DEFAULT 0 NOT NULL, 
  CONSTRAINT pk_invoices_user_email_payment_start_date PRIMARY KEY (user_email, payment_start_date), 
);

-- Tabela park_capacity
CREATE TABLE park_capacity (
  park_id           number(6), 
  vehicle_type_name varchar2(50), 
  park_capacity     number(10) nn_park_capacity_park_capacity NOT NULL, 
  amount_occupied   number(10) DEFAULT 0 nn_park_capacity_amount_occupied NOT NULL, 
  CONSTRAINT pk_park_capacity_park_id_vehicle_type_name PRIMARY KEY (park_id, vehicle_type_name)
);

-- Tabela user_type
CREATE TABLE user_type (
  name varchar2(50) CONSTRAINT pk_user_type_name PRIMARY KEY 
);

-- Tabela trip
CREATE TABLE trip (
  start_time    timestamp(0) DEFAULT systimestamp, 
  user_email    varchar2(50), 
  vehicles_id   number(8) nn_trip_vehicles_id NOT NULL, 
  start_park_id number(6), 
  end_park_id   number(6), 
  end_time      timestamp(0), 
  CONSTRAINT pk_trip_start_time_user_email PRIMARY KEY (start_time, user_email),
  CONSTRAINT ck_trip_start_park_different_end_park CHECK (start_park_id != end_park_id)
  );

-- Tabela receipts
CREATE TABLE receipts (
  user_email       varchar2(50), 
  payment_start_date date, 
  points_used      number(10) nn_receipts_points_used NOT NULL, 
  amount_paid_cash number(9, 2) nn_receipts_points_used NOT NULL, 
  CONSTRAINT pk_receipts_user_email, payment_start_date PRIMARY KEY (user_email, payment_start_date)
);


--Tabela registered_users
CREATE TABLE registered_users (
  user_email     varchar2(50) CONSTRAINT pk_registered_users_user_email PRIMARY KEY, 
  user_type_name varchar2(50), 
  user_password  varchar2(20) CONSTRAINT nn_registered_users_user_password NOT NULL 
);
  
ALTER TABLE vehicles ADD CONSTRAINT fk_vehicles_vehicle_type_name FOREIGN KEY (vehicle_type_name) REFERENCES vehicle_types (name);
ALTER TABLE bicycles ADD CONSTRAINT fk_bicycles_vehicle_id FOREIGN KEY (vehicle_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE electric_scooters ADD CONSTRAINT fk_electric_scooters_vehicle_id FOREIGN KEY (vehicle_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE electric_scooters ADD CONSTRAINT fk_electric_scooters_electric_scooter_type_name FOREIGN KEY (electric_scooter_type_name) REFERENCES electric_scooter_types (electric_scooter_type_name);
ALTER TABLE park_vehicle ADD CONSTRAINT fk_park_vehicle_park_id FOREIGN KEY (park_id) REFERENCES parks (park_id);
ALTER TABLE park_vehicle ADD CONSTRAINT fk_park_vehicle_vehicle_id FOREIGN KEY (vehicle_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE invoices ADD CONSTRAINT fk_invoices_user_email FOREIGN KEY (user_email) REFERENCES clients (user_email);
ALTER TABLE park_capacity ADD CONSTRAINT fk_park_capacity_park_id FOREIGN KEY (park_id) REFERENCES parks (park_id);
ALTER TABLE park_capacity ADD CONSTRAINT fk_park_capacity_vehicle_type_name FOREIGN KEY (vehicle_type_name) REFERENCES vehicle_types (vehicle_type_name);
ALTER TABLE trip ADD CONSTRAINT fk_trip_user_email FOREIGN KEY (user_email) REFERENCES clients (user_email);
ALTER TABLE trip ADD CONSTRAINT fk_trip_start_park_id FOREIGN KEY (start_park_id) REFERENCES parks (park_id);
ALTER TABLE trip ADD CONSTRAINT fk_trip_end_park_id FOREIGN KEY (end_park_id) REFERENCES parks (park_id);
ALTER TABLE receipts ADD CONSTRAINT fk_receipts_user_email_payment_start_date FOREIGN KEY (user_email, payment_start_date) REFERENCES invoices (user_email, payment_start_date);
ALTER TABLE trip ADD CONSTRAINT fk_trip_vehicles FOREIGN KEY (vehicles_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE clients ADD CONSTRAINT fk_clients_user_email FOREIGN KEY (user_email) REFERENCES registered_users (user_email);
ALTER TABLE registered_users ADD CONSTRAINT fk_registered_users_user_type_name FOREIGN KEY (user_type_name) REFERENCES user_type (name);
