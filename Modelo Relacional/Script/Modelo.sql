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
  capacity          number(10) NOT NULL, 
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
ALTER TABLE vehicles ADD CONSTRAINT fk_vehicles_vehicle_type_name FOREIGN KEY (vehicle_type_name) REFERENCES vehicle_types (name);
ALTER TABLE bicycles ADD CONSTRAINT fk_bicycles_vehicle_id FOREIGN KEY (vehicle_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE electric_scooters ADD CONSTRAINT fk_electric_scooters_vehicle_id FOREIGN KEY (vehicle_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE electric_scooters ADD CONSTRAINT fk_electric_scooters_electric_scooter_type_name FOREIGN KEY (electric_scooter_type_name) REFERENCES electric_scooter_types (electric_scooter_type_name);
ALTER TABLE park_vehicle ADD CONSTRAINT FKpark_vehic722748 FOREIGN KEY (park_id) REFERENCES parks (park_id);
ALTER TABLE park_vehicle ADD CONSTRAINT FKpark_vehic824993 FOREIGN KEY (vehicle_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE invoices ADD CONSTRAINT FKinvoices584211 FOREIGN KEY (user_email) REFERENCES clients (user_email);
ALTER TABLE park_capacity ADD CONSTRAINT FKpark_capac299055 FOREIGN KEY (park_id) REFERENCES parks (park_id);
ALTER TABLE park_capacity ADD CONSTRAINT FKpark_capac300630 FOREIGN KEY (vehicle_type_name) REFERENCES vehicle_types (name);
ALTER TABLE trip ADD CONSTRAINT FKtrip526617 FOREIGN KEY (user_email) REFERENCES clients (user_email);
ALTER TABLE trip ADD CONSTRAINT FKtrip281057 FOREIGN KEY (start_park_id) REFERENCES parks (park_id);
ALTER TABLE trip ADD CONSTRAINT FKtrip123174 FOREIGN KEY (end_park_id) REFERENCES parks (park_id);
ALTER TABLE receipts ADD CONSTRAINT FKreceipts808514 FOREIGN KEY (user_email, due_date) REFERENCES invoices (user_email, due_date);
ALTER TABLE trip ADD CONSTRAINT FKtrip321568 FOREIGN KEY (vehicles_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE clients ADD CONSTRAINT fk_clients_user_email FOREIGN KEY (user_email) REFERENCES registered_users (user_email);
ALTER TABLE users ADD CONSTRAINT FKusers201629 FOREIGN KEY (user_type_name) REFERENCES user_type (name);
