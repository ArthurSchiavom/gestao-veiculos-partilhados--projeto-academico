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
DROP TABLE trip_point_of_interest CASCADE CONSTRAINTS;

-- Tabela Vehicles
CREATE TABLE vehicles (
  vehicle_id        number(8) GENERATED AS IDENTITY CONSTRAINT pk_vehicles_vehicle_id PRIMARY KEY, 
  vehicle_type_name varchar2(50) constraint nn_vehicles_vehicle_type_name NOT NULL,
  available         number(1)    constraint nn_vehicles_available NOT NULL
                                 CONSTRAINT ck_vehicles_available CHECK (available = 0 OR available = 1),
  latitude          number(9, 6) constraint nn_vehicles_latitude NOT NULL
                                 constraint ck_vehicles_latitude CHECK (latitude between -90 and 90), 
  longitude         number(9, 6) constraint nn_vehicles_longitude NOT NULL
                                 constraint ck_vehicles_longitude CHECK (longitude between -180 and 180), 
  weight			number(3)    constraint nn_vehicles_weight NOT NULL
								 constraint ck_vehicles_weight CHECK (weight > 0),
  altitude_m          number(6, 2) constraint nn_vehicles_altitude NOT NULL,
  aerodynamic_coefficient number(4, 2) constraint nn_vehicles_aerodynamic_coefficient NOT NULL,
  frontal_area number(4,1) constraint nn_vehicles_frontal_area NOT NULL,
                           constraint ck_vehicles_fraontal_area check(frontal_area > 0),
  constraint uk_vehicles_longitude_latitude_altitude unique(latitude,longitude,altitude_m)
);

-- Tabela Vehicle_Types
CREATE TABLE vehicle_types (
  vehicle_type_name varchar2(50) constraint pk_vehicle_types_name PRIMARY KEY
  );

-- Tabela Bicycles 
CREATE TABLE bicycles (
  vehicle_id        number(8) constraint pk_bicycles_vehicle_id PRIMARY KEY,                  
  bicycle_size      number(2) constraint nn_bicycles_bicycle_size NOT NULL
  bicyble_description varchar2(50) constraint nn_bicycles_bicycle_description NOT NULL
);

-- Tabela Electric_Scooters  
CREATE TABLE electric_scooters (
  vehicle_id                number(8) constraint pk_electric_scooters_vehicle_id   PRIMARY KEY, 
  electric_scooter_type_name varchar2(50),
  electric_scooter_description varchar2(50) constraint nn_electric_scooters_description NOT NULL,
  max_battery_capacity             number(3,2) constraint nn_electric_scooters_battery_level NOT NULL,
  actual_battery_capacity number(3) constraint nn_electric_scooters_actual_battery_capacity NOT NULL
									constraint ck_electric_scooters_actual_battery_capacity CHECK (actual_battery_capacity >= 0 and actual_battery_capacity <= 100)
  );

-- Tabela Electric_Scooter_Types  
CREATE TABLE electric_scooter_types (
  electric_scooter_type_name   varchar2(50) constraint pk_eletric_scooter_types_eletric_scooter_type_name PRIMARY KEY, 
  battery_per_min             number(5, 2) constraint nn_eletric_scooter_types_battery_per_min NOT NULL, 
  battery_per_km              number(5, 2) constraint nn_eletric_scooter_types_battery_per_km NOT NULL 
);

-- Tabela Clients
CREATE TABLE clients (
  user_email             varchar2(50) constraint pk_clients_user_email PRIMARY KEY, 
  points                 number(6)   DEFAULT 0 
                                      constraint nn_clients_points NOT NULL, 
  credit_card_number     char(16)     constraint nn_clients_credit_card_number NOT NULL,
  credit_card_expiration date         constraint nn_clients_credit_card_expiration NOT NULL, 
  credit_card_secret     number(3)    constraint nn_clients_credit_card_secret NOT NULL
                                      constraint ck_clients_credit_card_secret CHECK (REGEXP_LIKE(credit_card_secret, '\d{3}$')), 
  height_m               number(3) constraint nn_clients_height_m NOT NULL, 
  weight_kg              number(3) constraint nn_clients_weight_kg NOT NULL, 
  gender                 char(1)      constraint nn_clients_gender NOT NULL
                                      constraint ck_clients_gender CHECK (REGEXP_LIKE(gender, 'M|F', 'i')), 
  is_riding              char(1)      DEFAULT '0' 
                                      constraint nn_clients_is_riding NOT NULL
                                      constraint ck_clients_is_riding  CHECK (is_riding = 0 OR is_riding = 1), 
  age                    number(3)    constraint nn_clients_age NOT NULL,
  cycling_average_speed  number(4,2)  constraint nn_cycling_average_speed NOT NULL
  );

-- Tabela Parks    
CREATE TABLE parks (
  park_id   number(6) GENERATED AS IDENTITY
                      constraint pk_parks_park_id PRIMARY KEY,
  latitude  number(9, 6) constraint nn_parks_latitude NOT NULL,
  longitude number(9, 6) constraint nn_parks_longitude NOT NULL,
  park_description varchar2(150) constraint nn_parks_park_description NOT NULL,
  park_input_voltage number(5,1) constraint parks_park_input_voltage NOT NULL,
  park_input_current number(4,1) constraint parks_park_input_current NOT NULL,
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
  amount_left_to_pay     number(6, 2) DEFAULT 10 CONSTRAINT nn_pending_registrations_amount_left_to_pay NOT NULL, 
  credit_card_number     CHAR(16) CONSTRAINT nn_pending_registrations_credit_card_number NOT NULL, 
  credit_card_expiration date CONSTRAINT nn_pending_registrations_credit_card_expiration NOT NULL, 
  credit_card_secret     number(3) CONSTRAINT nn_pending_registrations_credit_card_secret NOT NULL, 
  height                 number(3) CONSTRAINT nn_pending_registrations_height NOT NULL, 
  weight                 number(3) CONSTRAINT nn_pending_registrations_weight NOT NULL, 
  gender                 char(1) CONSTRAINT nn_pending_registrations_gender NOT NULL
                                 CONSTRAINT ck_pending_registrations_gender CHECK(REGEXP_LIKE(gender, 'M|F', 'i')),
  age                    number(3) CONSTRAINT nn_pending_registrations_age NOT NULL,
  cycling_average_speed number(4,2) constraint nn_pending_registrations_cycling_average_speed NOT NULL,
  user_password  varchar2(20) CONSTRAINT nn_pending_registrations_user_password NOT NULL,
  user_name      varchar2(50) CONSTRAINT nn_pending_registrations_user_name NOT NULL,
  Constraint ck_pending_registrations_email CHECK (REGEXP_LIKE(email, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$'))
);

-- Tabela invoices
CREATE TABLE invoices (
  user_email         varchar2(50), 
  payment_start_date date, 
  amount             number(9, 2) CONSTRAINT nn_invoices_amount NOT NULL, 
  amount_left_to_pay number(9, 2) DEFAULT null, 
  usage_cost         number(9, 2) DEFAULT 0 CONSTRAINT nn_invoices_usage_cost NOT NULL, 
  penalisation_cost  number(9, 2) DEFAULT 0 CONSTRAINT nn_invoices_penalisation_cost NOT NULL, 
  CONSTRAINT pk_invoices_user_email_payment_start_date PRIMARY KEY (user_email, payment_start_date) 
);

-- Tabela park_capacity
CREATE TABLE park_capacity (
  park_id           number(6), 
  vehicle_type_name varchar2(50), 
  park_capacity     number(5) CONSTRAINT nn_park_capacity_park_capacity NOT NULL, 
  amount_occupied   number(5) DEFAULT 0 CONSTRAINT nn_park_capacity_amount_occupied NOT NULL, 
  CONSTRAINT pk_park_capacity_park_id_vehicle_type_name PRIMARY KEY (park_id, vehicle_type_name)
);

-- Tabela user_type
CREATE TABLE user_type (
  user_type_name varchar2(50) CONSTRAINT pk_user_type_name PRIMARY KEY
);

-- Tabela trips
CREATE TABLE trips (
  start_time    timestamp DEFAULT systimestamp, 
  user_email    varchar2(50), 
  vehicle_id   number(8), 
  start_park_id number(6), 
  end_park_id   number(6), 
  end_time      timestamp, 
  CONSTRAINT pk_trips_start_time_user_email PRIMARY KEY (start_time, user_email)
  );

-- Tabela receipts
CREATE TABLE receipts (
  user_email       varchar2(50), 
  payment_start_date date,
  payment_end_date date CONSTRAINT nn_receipts_payment_end_date NOT NULL,
  points_used      number(6) CONSTRAINT nn_receipts_points_used NOT NULL, 
  amount_paid_cash number(9, 2) CONSTRAINT nn_receipts_amount_paid_cash NOT NULL, 
  CONSTRAINT pk_receipts_user_email_payment_start_date PRIMARY KEY (user_email, payment_start_date),
  CONSTRAINT ck_receipts_start_end_payment_date CHECK(payment_start_date < payment_end_date)
);


--Tabela registered_users
CREATE TABLE registered_users (
  user_email     varchar2(50) CONSTRAINT pk_registered_users_user_email PRIMARY KEY, 
  user_type_name varchar2(50), 
  user_password  varchar2(20) CONSTRAINT nn_registered_users_user_password NOT NULL,
  user_name      varchar2(50) CONSTRAINT nn_registered_users_user_name NOT NULL,
  Constraint ck_registered_users_user_email CHECK (REGEXP_LIKE(user_email, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$'))
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
constraint pk_paths_latitudes_longitudes PRIMARY KEY (latitudeA, longitudeA, latitudeB, longitudeB)
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
CREATE TABLE trip_point_of_interest (
  start_time timestamp, 
  user_email varchar2(50), 
  latitudeA  number(9), 
  longitudeA number(9), 
  latitudeB  number(9), 
  longitudeB number(9), 
  constraint pk_trip_points_of_interest PRIMARY KEY (start_time, user_email, latitudeA, longitudeA, latitudeB, longitudeB)
);

ALTER TABLE vehicles ADD CONSTRAINT fk_vehicles_vehicle_type_name FOREIGN KEY (vehicle_type_name) REFERENCES vehicle_types (vehicle_type_name);
ALTER TABLE bicycles ADD CONSTRAINT fk_bicycles_vehicle_id FOREIGN KEY (vehicle_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE electric_scooters ADD CONSTRAINT fk_electric_scooters_vehicle_id FOREIGN KEY (vehicle_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE electric_scooters ADD CONSTRAINT fk_electric_scooters_electric_scooter_type_name FOREIGN KEY (electric_scooter_type_name) REFERENCES electric_scooter_types (electric_scooter_type_name);
ALTER TABLE park_vehicle ADD CONSTRAINT fk_park_vehicle_park_id FOREIGN KEY (park_id) REFERENCES parks (park_id);
ALTER TABLE park_vehicle ADD CONSTRAINT fk_park_vehicle_vehicle_id FOREIGN KEY (vehicle_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE invoices ADD CONSTRAINT fk_invoices_user_email FOREIGN KEY (user_email) REFERENCES clients (user_email);
ALTER TABLE park_capacity ADD CONSTRAINT fk_park_capacity_park_id FOREIGN KEY (park_id) REFERENCES parks (park_id);
ALTER TABLE park_capacity ADD CONSTRAINT fk_park_capacity_vehicle_type_name FOREIGN KEY (vehicle_type_name) REFERENCES vehicle_types (vehicle_type_name);
ALTER TABLE trips ADD CONSTRAINT fk_trip_user_email FOREIGN KEY (user_email) REFERENCES clients (user_email);
ALTER TABLE trips ADD CONSTRAINT fk_trip_start_park_id FOREIGN KEY (start_park_id) REFERENCES parks (park_id);
ALTER TABLE trips ADD CONSTRAINT fk_trip_end_park_id FOREIGN KEY (end_park_id) REFERENCES parks (park_id);
ALTER TABLE receipts ADD CONSTRAINT fk_receipts_user_email_payment_start_date FOREIGN KEY (user_email, payment_start_date) REFERENCES invoices (user_email, payment_start_date);
ALTER TABLE trips ADD CONSTRAINT fk_trip_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles (vehicle_id);
ALTER TABLE clients ADD CONSTRAINT fk_clients_user_email FOREIGN KEY (user_email) REFERENCES registered_users (user_email);
ALTER TABLE registered_users ADD CONSTRAINT fk_registered_users_user_type_name FOREIGN KEY (user_type_name) REFERENCES user_type (user_type_name);
ALTER TABLE trip_point_of_interest ADD CONSTRAINT fk_trip_point_of_interest_table_trips FOREIGN KEY (start_time, user_email) REFERENCES trips (start_time, user_email);
ALTER TABLE trip_point_of_interest ADD CONSTRAINT fk_trip_point_of_interest_table_paths FOREIGN KEY (latitudeA, longitudeA, latitudeB, longitudeB) REFERENCES paths (latitudeA, longitudeA, latitudeB, longitudeB);
ALTER TABLE parks ADD CONSTRAINT fk_parks_latitude_longitude FOREIGN KEY (latitude, longitude) REFERENCES points_of_interest (latitude, longitude);
ALTER TABLE paths ADD CONSTRAINT fk_parks_latitudeB_longitudeB FOREIGN KEY (latitudeB, longitudeB) REFERENCES points_of_interest (latitude, longitude);
ALTER TABLE paths ADD CONSTRAINT fk_parks_latitudeA_longitudeA FOREIGN KEY (latitudeA, longitudeA) REFERENCES points_of_interest (latitude, longitude);

