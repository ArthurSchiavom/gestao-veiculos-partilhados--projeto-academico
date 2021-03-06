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
DROP TABLE trips CASCADE CONSTRAINTS;
DROP TABLE receipts CASCADE CONSTRAINTS;
DROP TABLE registered_users CASCADE CONSTRAINTS;
DROP TABLE paths CASCADE CONSTRAINTS;
DROP TABLE points_of_interest CASCADE CONSTRAINTS;
DROP TABLE trip_paths CASCADE CONSTRAINTS;

-- Tabela Vehicles
CREATE TABLE vehicles (
  description varchar2(50) constraint nn_vehicles_description NOT NULL
                        CONSTRAINT pk_vehicles_description PRIMARY KEY,
  vehicle_type_name varchar2(50) constraint nn_vehicles_vehicle_type_name NOT NULL,
  unique_number number(5) GENERATED AS IDENTITY 
						constraint nn_vehicles_unique_number NOT NULL
						constraint uk_vehicles_unique_number UNIQUE,
  available         number(1)    constraint nn_vehicles_available NOT NULL
                                 CONSTRAINT ck_vehicles_available CHECK (available = 0 OR available = 1),
  weight			number(3)    constraint nn_vehicles_weight NOT NULL
								 constraint ck_vehicles_weight CHECK (weight > 0),
  aerodynamic_coefficient number(4, 2) constraint nn_vehicles_aerodynamic_coefficient NOT NULL,
  frontal_area number(4,1) constraint nn_vehicles_frontal_area NOT NULL,
                           constraint ck_vehicles_fraontal_area check(frontal_area > 0)
);

-- Tabela Vehicle_Types
CREATE TABLE vehicle_types (
  vehicle_type_name varchar2(50) constraint pk_vehicle_types_name PRIMARY KEY
  );

-- Tabela Bicycles 
CREATE TABLE bicycles (
  vehicle_description       varchar(50) constraint pk_bicycles_vehicle_description PRIMARY KEY,                  
  bicycle_size              number(2) constraint nn_bicycles_bicycle_size NOT NULL
                                    constraint ck_bicycles_bicycle_size CHECK(bicycle_size > 0)
);

-- Tabela Electric_Scooters  
CREATE TABLE electric_scooters (
  vehicle_description               varchar(50) constraint pk_electric_scooters_vehicle_description   PRIMARY KEY, 
  electric_scooter_type_name        varchar2(50),
  actual_battery_capacity           number(3) constraint nn_electric_scooters_actual_battery_capacity NOT NULL
									constraint ck_electric_scooters_actual_battery_capacity CHECK (actual_battery_capacity >= 0 and actual_battery_capacity <= 100),
  max_battery_capacity              number(3,2) constraint nn_electric_scooters_battery_level NOT NULL,
  engine_power                      number(6) constraint nn_electric_scooters_engine_power NOT NULL
                                            constraint ck_electric_scooters_engine_power CHECK (engine_power >= 0)
  );

-- Tabela Electric_Scooter_Types  
CREATE TABLE electric_scooter_types (
  electric_scooter_type_name   varchar2(50) constraint pk_eletric_scooter_types_eletric_scooter_type_name PRIMARY KEY
);

-- Tabela Clients
CREATE TABLE clients (
  user_email             varchar2(50) constraint pk_clients_user_email PRIMARY KEY, 
  points                 number(6)   DEFAULT 0 
                                      constraint nn_clients_points NOT NULL, 
  visa     char(16)     constraint nn_clients_visa NOT NULL,
  height_cm               number(3) constraint nn_clients_height_m NOT NULL, 
  weight_kg              number(3) constraint nn_clients_weight_kg NOT NULL, 
  gender                 char(1)      constraint nn_clients_gender NOT NULL
                                      constraint ck_clients_gender CHECK (REGEXP_LIKE(gender, 'M|F', 'i')), 
  is_riding              char(1)      DEFAULT '0' 
                                      constraint nn_clients_is_riding NOT NULL
                                      constraint ck_clients_is_riding  CHECK (is_riding = 0 OR is_riding = 1), 
  cycling_average_speed  number(4,2)  constraint nn_cycling_average_speed NOT NULL
  );

-- Tabela Parks    
CREATE TABLE parks (
  park_id   varchar2(50)
                      constraint pk_parks_park_id PRIMARY KEY,
  latitude  number(9, 6) constraint nn_parks_latitude NOT NULL,
  longitude number(9, 6) constraint nn_parks_longitude NOT NULL,
  park_input_voltage number(5,1) constraint parks_park_input_voltage NOT NULL,
  park_input_current number(4,1) constraint parks_park_input_current NOT NULL,
  available number(1) DEFAULT 1 constraint parks_available NOT NULL,
  constraint uk_parks_latitude_longitude unique(latitude,longitude),
  constraint ck_available CHECK (available = 1 OR available = 0)
  );

-- Tabela park_vehicle
CREATE TABLE park_vehicle (
  vehicle_description varchar2(50) CONSTRAINT pk_park_vehicle_description PRIMARY KEY,
  park_id    varchar2(50)
);

-- Tabela pending_registrations 
CREATE TABLE pending_registrations (
  email                  varchar2(50) CONSTRAINT pk_pending_registrations_email PRIMARY KEY, 
  amount_left_to_pay     number(6, 2) DEFAULT 0 CONSTRAINT nn_pending_registrations_amount_left_to_pay NOT NULL, 
  visa     CHAR(16) CONSTRAINT nn_pending_registrations_visa NOT NULL,
  height_cm                 number(3) CONSTRAINT nn_pending_registrations_height NOT NULL, 
  weight                 number(3) CONSTRAINT nn_pending_registrations_weight NOT NULL, 
  gender                 char(1) CONSTRAINT nn_pending_registrations_gender NOT NULL
                                 CONSTRAINT ck_pending_registrations_gender CHECK(REGEXP_LIKE(gender, 'M|F', 'i')),
  cycling_average_speed number(4,2) constraint nn_pending_registrations_cycling_average_speed NOT NULL,
  user_password  varchar2(20) DEFAULT 'qwerty' CONSTRAINT nn_pending_registrations_user_password NOT NULL,
  user_name      varchar2(50) CONSTRAINT nn_pending_registrations_user_name NOT NULL
				CONSTRAINT uk_pending_registrations_user_name UNIQUE,
  Constraint ck_pending_registrations_email CHECK (REGEXP_LIKE(email, '^.*@.*$'))
);

-- Tabela invoices
CREATE TABLE invoices (
  user_email         varchar2(50), 
  payment_start_date date, 
  amount_left_to_pay number(9, 2) DEFAULT 0 CONSTRAINT nn_invoices_amount_left_to_pay NOT NULL, 
  usage_cost         number(9, 2) DEFAULT 0 CONSTRAINT nn_invoices_usage_cost NOT NULL, 
  penalisation_cost  number(9, 2) DEFAULT 0 CONSTRAINT nn_invoices_penalisation_cost NOT NULL,
  points_used        number(6), -- set no final
  previous_points    number(6)    constraint nn_invoices_previous_points NOT NULL,
  earned_points      number(6) DEFAULT 0 CONSTRAINT nn_invoices_earned_points NOT NULL, 
  CONSTRAINT pk_invoices_user_email_payment_start_date PRIMARY KEY (user_email, payment_start_date) 
);

-- Tabela park_capacity
CREATE TABLE park_capacity (
  park_id           varchar2(50), 
  vehicle_type_name varchar2(50), 
  park_capacity     number(5) CONSTRAINT nn_park_capacity_park_capacity NOT NULL, 
  amount_occupied   number(5) DEFAULT 0 CONSTRAINT nn_park_capacity_amount_occupied NOT NULL,
  CONSTRAINT ck_park_capacity_park_capacity_amount_occupied CHECK (amount_occupied <= park_capacity),
  CONSTRAINT pk_park_capacity_park_id_vehicle_type_name PRIMARY KEY (park_id, vehicle_type_name)
);

-- Tabela user_type
CREATE TABLE user_type (
  user_type_name varchar2(50) CONSTRAINT pk_user_type_name PRIMARY KEY
);

-- Tabela trips
CREATE TABLE trips (
  start_time    timestamp DEFAULT systimestamp
                         constraint nn_trips_start_time not null, 
  user_email    varchar2(50)
                         constraint nn_trips_user_email not null,  
  vehicle_description   varchar2(50) 
                        constraint nn_trips_vehicle_description not null, 
  start_park_id varchar2(50)
                        constraint nn_trips_start_park_id not null, 
  end_park_id   varchar2(50), 
  end_time      timestamp, 
  CONSTRAINT ck_trips_end_park_id_end_time CHECK ((end_park_id is null and end_time is null) or (end_park_id is not null and end_time is not null)),
  CONSTRAINT ck_trips_start_time_end_time CHECK ((end_time is null) or (start_time<end_time)),
  CONSTRAINT pk_trips_start_time_user_email PRIMARY KEY (start_time, user_email)
  );
-- Tabela receipts
CREATE TABLE receipts (
  user_email       varchar2(50), 
  payment_start_date date,
  payment_end_date date CONSTRAINT nn_receipts_payment_end_date NOT NULL,
  amount_paid_cash number(9, 2) CONSTRAINT nn_receipts_amount_paid_cash NOT NULL, 
  CONSTRAINT pk_receipts_user_email_payment_start_date PRIMARY KEY (user_email, payment_start_date),
  CONSTRAINT ck_receipts_start_end_payment_date CHECK(payment_start_date < payment_end_date)
);


--Tabela registered_users
CREATE TABLE registered_users (
  user_email     varchar2(50) CONSTRAINT pk_registered_users_user_email PRIMARY KEY, 
  user_type_name varchar2(50), 
  user_password  varchar2(20) CONSTRAINT nn_registered_users_user_password NOT NULL,
  user_name      varchar2(50) CONSTRAINT nn_registered_users_username NOT NULL
				CONSTRAINT uk_registered_users_username UNIQUE,
  Constraint ck_registered_users_user_email CHECK (REGEXP_LIKE(user_email, '^.*@.*$'))
);

-- Tabela paths
CREATE TABLE paths (
  latitudeA              number(9, 6), 
  longitudeA             number(9, 6), 
  latitudeB              number(9, 6), 
  longitudeB             number(9, 6), 
  kinetic_coefficient    number(4, 3) DEFAULT 0 constraint nn_paths_kinetic_coefficient NOT NULL, 
  wind_direction_degrees number(3) DEFAULT 0 constraint nn_paths_wind_direction_degrees NOT NULL, 
  wind_speed             number(4, 1) DEFAULT 0 constraint nn_paths_wind_speed NOT NULL, 
constraint pk_paths_latitudes_longitudes PRIMARY KEY (latitudeA, longitudeA, latitudeB, longitudeB),
constraint ck_paths_latitudes_longitudes CHECK(latitudeA != latitudeB and longitudeA != longitudeB)
);

-- Tabela Points_of_interest
CREATE TABLE points_of_interest (
  latitude        number(9, 6), 
  longitude       number(9, 6), 
  altitude_m      number(4) DEFAULT 0 constraint nn_points_of_interest_altitude_m NOT NULL, 
  poi_description varchar2(50) constraint points_of_interest_poi_description NOT NULL, 
  constraint pk_points_of_interest_latitude_longitude PRIMARY KEY (latitude, longitude)
);

-- Tabela trip_points_of_interest  
CREATE TABLE trip_paths (
  trip_paths_id number(5) GENERATED AS IDENTITY constraint pk_trip_paths_trip_paths_id PRIMARY KEY,
  start_time timestamp, 
  user_email varchar2(50), 
  latitudeA  number(9, 6), 
  longitudeA number(9, 6), 
  latitudeB  number(9, 6), 
  longitudeB number(9, 6)
);

ALTER TABLE vehicles ADD CONSTRAINT fk_vehicles_vehicle_type_name FOREIGN KEY (vehicle_type_name) REFERENCES vehicle_types (vehicle_type_name);
ALTER TABLE bicycles ADD CONSTRAINT fk_bicycles_vehicle_description FOREIGN KEY (vehicle_description) REFERENCES vehicles (description);
ALTER TABLE electric_scooters ADD CONSTRAINT fk_electric_scooters_vehicle_description FOREIGN KEY (vehicle_description) REFERENCES vehicles (description);
ALTER TABLE electric_scooters ADD CONSTRAINT fk_electric_scooters_electric_scooter_type_name FOREIGN KEY (electric_scooter_type_name) REFERENCES electric_scooter_types (electric_scooter_type_name);
ALTER TABLE park_vehicle ADD CONSTRAINT fk_park_vehicle_park_id FOREIGN KEY (park_id) REFERENCES parks (park_id) initially deferred;
ALTER TABLE park_vehicle ADD CONSTRAINT fk_park_vehicle_vehicle_description FOREIGN KEY (vehicle_description) REFERENCES vehicles (description);
ALTER TABLE invoices ADD CONSTRAINT fk_invoices_user_email FOREIGN KEY (user_email) REFERENCES clients (user_email);
ALTER TABLE park_capacity ADD CONSTRAINT fk_park_capacity_park_id FOREIGN KEY (park_id) REFERENCES parks (park_id) initially deferred;
ALTER TABLE park_capacity ADD CONSTRAINT fk_park_capacity_vehicle_type_name FOREIGN KEY (vehicle_type_name) REFERENCES vehicle_types (vehicle_type_name);
ALTER TABLE trips ADD CONSTRAINT fk_trip_user_email FOREIGN KEY (user_email) REFERENCES clients (user_email);
ALTER TABLE trips ADD CONSTRAINT fk_trip_start_park_id FOREIGN KEY (start_park_id) REFERENCES parks (park_id) initially deferred;
ALTER TABLE trips ADD CONSTRAINT fk_trip_end_park_id FOREIGN KEY (end_park_id) REFERENCES parks (park_id) initially deferred;
ALTER TABLE receipts ADD CONSTRAINT fk_receipts_user_email_payment_start_date FOREIGN KEY (user_email, payment_start_date) REFERENCES invoices (user_email, payment_start_date);
ALTER TABLE trips ADD CONSTRAINT fk_trip_vehicles FOREIGN KEY (vehicle_description) REFERENCES vehicles (description);
ALTER TABLE clients ADD CONSTRAINT fk_clients_user_email FOREIGN KEY (user_email) REFERENCES registered_users (user_email);
ALTER TABLE registered_users ADD CONSTRAINT fk_registered_users_user_type_name FOREIGN KEY (user_type_name) REFERENCES user_type (user_type_name);
ALTER TABLE trip_paths ADD CONSTRAINT fk_trip_paths_table_trips FOREIGN KEY (start_time, user_email) REFERENCES trips (start_time, user_email);
ALTER TABLE trip_paths ADD CONSTRAINT fk_trip_paths_table_paths FOREIGN KEY (latitudeA, longitudeA, latitudeB, longitudeB) REFERENCES paths (latitudeA, longitudeA, latitudeB, longitudeB);
ALTER TABLE parks ADD CONSTRAINT fk_parks_latitude_longitude FOREIGN KEY (latitude, longitude) REFERENCES points_of_interest (latitude, longitude);
ALTER TABLE paths ADD CONSTRAINT fk_parks_latitudeB_longitudeB FOREIGN KEY (latitudeB, longitudeB) REFERENCES points_of_interest (latitude, longitude);
ALTER TABLE paths ADD CONSTRAINT fk_parks_latitudeA_longitudeA FOREIGN KEY (latitudeA, longitudeA) REFERENCES points_of_interest (latitude, longitude);

COMMIT;