
--------------- INSERT ---------------------

-- points_of_interest
insert into points_of_interest(latitude, longitude, altitude_m, poi_description)
values(10.2, 30.2, 12.0, 'Cl�rigos');

insert into points_of_interest(latitude, longitude, altitude_m, poi_description)
values(-10.6, 37.2, 12.0, 'Trindade');

insert into points_of_interest(latitude, longitude, altitude_m, poi_description)
values(30.4, -20.2, 12.0, 'Baixa');

insert into points_of_interest(latitude, longitude, altitude_m, poi_description)
values(3.4, -2.2, 2.0, 'Via Catarina');

insert into points_of_interest(latitude, longitude, altitude_m, poi_description)
values(-1.3, 3.4, 8.0, 'Ribeira');

insert into points_of_interest(latitude, longitude, altitude_m, poi_description)
values(-20.222, 122.12, 14, 'Parque do kevin');

insert into points_of_interest(latitude, longitude, altitude_m, poi_description)
values(-80.222, 172.12, -20, 'Parque do Arthur');

insert into points_of_interest(latitude, longitude, altitude_m, poi_description)
values(18.222, 22.12, 14, 'Parque do Ivo');

insert into points_of_interest(latitude, longitude, altitude_m, poi_description)
values(18.222, 20.12, 10, 'Parque do Diogo');

-- parks
insert into parks(park_id,latitude, longitude, park_input_voltage, park_input_current)
values('park1',18.222, 20.12, 220, 16);

insert into parks(park_id,latitude, longitude, park_input_voltage, park_input_current)
values('park2',18.222, 22.12, 220, 16);

insert into parks(park_id,latitude, longitude, park_input_voltage, park_input_current)
values('park3',-80.222, 172.12, 220, 16);

insert into parks(park_id,latitude, longitude, park_input_voltage, park_input_current)
values('park4',-20.222, 122.12,220, 16);

-- vehicle_types
insert into vehicle_types(vehicle_type_name)
values('electric_scooter');

insert into vehicle_types(vehicle_type_name)
values('bicycle');

-- vehicles
insert into vehicles (description, vehicle_type_name, available, weight, aerodynamic_coefficient, frontal_area)
values('PT001', 'bicycle', 1, 50, 1.10, 0.3);

insert into vehicles (description, vehicle_type_name, available, weight, aerodynamic_coefficient, frontal_area)
values('PT002', 'bicycle', 1, 12, 1.21, 0.2);

insert into vehicles (description, vehicle_type_name, available, weight, aerodynamic_coefficient, frontal_area)
values('PT003', 'bicycle', 0, 30, 1.2, 2.4);

insert into vehicles (description, vehicle_type_name, available, weight, aerodynamic_coefficient, frontal_area)
values('PT004', 'electric_scooter', 1, 2, 1.10, 0.9);

insert into vehicles (description, vehicle_type_name, available, weight, aerodynamic_coefficient, frontal_area) 
values('PT005', 'electric_scooter', 0, 70, 1.21, 0.1);

insert into vehicles (description, vehicle_type_name, available, weight, aerodynamic_coefficient, frontal_area) 
values('PT006', 'electric_scooter', 0, 20, 1.1, 0.3);

-- bicycles
insert into bicycles(vehicle_description, bicycle_size)
values('PT001', 15);

insert into bicycles(vehicle_description, bicycle_size)
values('PT002', 17);

insert into bicycles(vehicle_description, bicycle_size)
values('PT003', 19);

-- electric _scooter_types
insert into electric_scooter_types(electric_scooter_type_name)
values('offroad');

insert into electric_scooter_types(electric_scooter_type_name)
values('urban');

-- electric_scooters
insert into electric_scooters(vehicle_description, electric_scooter_type_name, max_battery_capacity, actual_battery_capacity, engine_power)
values('PT004', 'offroad', 1, 75, 300);

insert into electric_scooters(vehicle_description, electric_scooter_type_name, max_battery_capacity, actual_battery_capacity, engine_power)
values('PT005', 'urban', 1, 20, 700);

insert into electric_scooters(vehicle_description, electric_scooter_type_name, max_battery_capacity, actual_battery_capacity, engine_power)
values('PT006', 'urban', 1, 60, 1500);

-- user_type
insert into user_type(user_type_name)
values('client');

insert into user_type(user_type_name)
values('administrator');

-- registered_users
insert into registered_users(user_email, user_type_name, user_password, user_name)    
values('a@a.a', 'client', '111', 'diogo64');

insert into registered_users(user_email, user_type_name, user_password, user_name)    
values('b@b.b', 'client', '222', 'arthur22');

insert into registered_users(user_email, user_type_name, user_password, user_name)    
values('c@c.c', 'client', '333', 'ivo1');

insert into registered_users(user_email, user_type_name, user_password, user_name)      
values('d@d.d', 'client', '444', 'elMan');

insert into registered_users(user_email, user_type_name, user_password, user_name)      
values('k@k.k', 'administrator', '555', 'MissMaam');

-- clients
insert into clients (user_email, points, visa, height_cm, weight_kg, gender, is_riding, cycling_average_speed)
values('a@a.a', 10, '1111111111111111', 1.72, 74, 'M', '1', 18.2);

insert into clients (user_email, points, visa, height_cm, weight_kg, gender, is_riding, cycling_average_speed)
values('b@b.b', 0, '2222222222222222', 1.62, 54, 'F', '0', 14.3);

insert into clients (user_email, points, visa, height_cm, weight_kg, gender, is_riding, cycling_average_speed)
values('c@c.c', 300, '3333333333333333', 1.90, 120, 'M', '1', 4.9);

insert into clients (user_email, points, visa, height_cm, weight_kg, gender, is_riding, cycling_average_speed)
values('d@d.d', 1000, '4444444444444444', 1.40, 40, 'M', '1', 12.2);

-- pending_registrations
insert into pending_registrations(email, amount_left_to_pay, visa, height_cm, weight, gender, cycling_average_speed, user_password, user_name)
values('e@e.e', 0, '6666666666666666', 1.80, 80,'M',13.1, '999', 'zeh');

-- invoices
insert into invoices(user_email, payment_start_date, amount_left_to_pay, usage_cost, penalisation_cost, points_used, previous_points)
values('a@a.a', TO_DATE('01-10-2019', 'dd/mm/yyyy'), 10, 10, 2, 20, 10);

insert into invoices(user_email, payment_start_date, amount_left_to_pay, usage_cost, penalisation_cost, points_used, previous_points)
values('b@b.b', TO_DATE('01-12-2019', 'dd/mm/yyyy'), 0, 4, 2, 17, 10);

-- park_capacity
insert into park_capacity(park_id, vehicle_type_name, park_capacity, amount_occupied)
values('park1', 'bicycle', 20, 10);

insert into park_capacity(park_id, vehicle_type_name, park_capacity, amount_occupied)
values('park1', 'electric_scooter', 20, 10);

insert into park_capacity(park_id, vehicle_type_name, park_capacity, amount_occupied)
values('park2', 'electric_scooter', 40, 10);

insert into park_capacity(park_id, vehicle_type_name, park_capacity, amount_occupied)
values('park2', 'bicycle', 30, 20);

-- trips
insert into trips (start_time, user_email, vehicle_description, start_park_id)
values(TO_TIMESTAMP('2019-10-01 10:50', 'yyyy-mm-dd hh24:mi'), 'a@a.a', 'PT001', 'park1');

insert into trips (start_time, user_email, vehicle_description, start_park_id)
values(TO_TIMESTAMP('2019-12-03 13:00', 'yyyy-mm-dd hh24:mi'), 'b@b.b', 'PT002', 'park1');

-- receipts
insert into receipts (user_email, payment_start_date, payment_end_date, amount_paid_cash)
values('a@a.a', TO_DATE('01-10-2019', 'dd/mm/yyyy'), TO_DATE('20-10-2019', 'dd/mm/yyyy'), 30);

-- paths
insert into paths(latitudeA, longitudeA, latitudeB, longitudeB, kinetic_coefficient, wind_direction_degrees, wind_speed)
values(18.222, 20.12, 10.2, 30.2, 0.002, 220, 12.1); -- parque Diogo -> clerigos

insert into paths(latitudeA, longitudeA, latitudeB, longitudeB, kinetic_coefficient, wind_direction_degrees, wind_speed)
values(10.2, 30.2, 30.4, -20.2, 0.001, 210, 10.0); -- clerigos -> baixa

insert into paths(latitudeA, longitudeA, latitudeB, longitudeB, kinetic_coefficient, wind_direction_degrees, wind_speed)
values(30.4, -20.2, 18.222, 22.12, 0.004, 110, 1.0); -- baixa -> parque ivo

-- trip_points_of_interest
insert into trip_paths(start_time, user_email, latitudeA, longitudeA, latitudeB, longitudeB)
values(TO_TIMESTAMP('2019-10-01 10:50', 'yyyy-mm-dd hh24:mi'), 'a@a.a', 18.222, 20.12, 10.2, 30.2);

insert into trip_paths(start_time, user_email, latitudeA, longitudeA, latitudeB, longitudeB)
values(TO_TIMESTAMP('2019-10-01 10:50', 'yyyy-mm-dd hh24:mi'), 'a@a.a', 10.2, 30.2, 30.4, -20.2);

insert into trip_paths(start_time, user_email, latitudeA, longitudeA, latitudeB, longitudeB)
values(TO_TIMESTAMP('2019-10-01 10:50', 'yyyy-mm-dd hh24:mi'), 'a@a.a', 30.4, -20.2, 18.222, 22.12);

-- park_vehicle
insert into park_vehicle(park_id, vehicle_description)
values('park1', 'PT001');

insert into park_vehicle(park_id, vehicle_description)
values('park1', 'PT002');

insert into park_vehicle(park_id, vehicle_description)
values('park1', 'PT003');

insert into park_vehicle(park_id, vehicle_description)
values('park1', 'PT004');

insert into park_vehicle(park_id, vehicle_description)
values('park2', 'PT005');

insert into park_vehicle(park_id, vehicle_description)
values('park3', 'PT006');

COMMIT;