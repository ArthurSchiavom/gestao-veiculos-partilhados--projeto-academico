
--------------- INSERT ---------------------

-- parks
insert into parks(park_name, latitude, longitude, altitude) 
values('Parque do Diogo', 18.222, 20.12, 10);

insert into parks(park_name, latitude, longitude, altitude) 
values('Parque do Ivo', 18.222, 22.12, 14);

insert into parks(park_name, latitude, longitude, altitude) 
values('Parque do Arthur', -80.222, 172.12, -20);

insert into parks(park_name, latitude, longitude, altitude) 
values('Parque do Arthur', -20.222, 122.12, 14);

-- vehicle_types
insert into vehicle_types(vehicle_type_name)
values('electric scooter');

insert into vehicle_types(vehicle_type_name)
values('bicycle');

-- vehicles
insert into vehicles (vehicle_type_name, available, latitude, longitude, altitude) 
values('bicycle', 1, 20.123, 13.223, 12);

insert into vehicles (vehicle_type_name, available, latitude, longitude, altitude) 
values('bicycle', 1, 22.123, -49.223, 0);

insert into vehicles (vehicle_type_name, available, latitude, longitude, altitude) 
values('bicycle', 0, 45.123, 23.423, 100);

insert into vehicles (vehicle_type_name, available, latitude, longitude, altitude) 
values('electric scooter', 1, 10.230, 15.323, 2);

insert into vehicles (vehicle_type_name, available, latitude, longitude, altitude) 
values('electric scooter', 0, 10.000, -27.123, 12);

insert into vehicles (vehicle_type_name, available, latitude, longitude, altitude) 
values('electric scooter', 0, 23.123, -23.423, -2);

-- park_vehicle
insert into park_vehicle(park_id, vehicle_id)
values(1, 1);

insert into park_vehicle(park_id, vehicle_id)
values(1, 2);

insert into park_vehicle(park_id, vehicle_id)
values(1, 3);

insert into park_vehicle(park_id, vehicle_id)
values(1, 4);

insert into park_vehicle(park_id, vehicle_id)
values(1, 5);

insert into park_vehicle(park_id, vehicle_id)
values(1, 6);

-- bicycles
insert into bicycles(vehicle_id, bicycle_size)
values(1, 15);

insert into bicycles(vehicle_id, bicycle_size)
values(2, 17);

insert into bicycles(vehicle_id, bicycle_size)
values(3, 19);

-- electric _scooter_types
insert into electric_scooter_types(electric_scooter_type_name, battery_per_min, battery_per_km, time_to_charge_fully_minute)
values('offroad', 2, 10, 3);

insert into electric_scooter_types(electric_scooter_type_name, battery_per_min, battery_per_km, time_to_charge_fully_minute)
values('urban', 2, 1, 2);

-- electric_scooters
insert into electric_scooters(vehicle_id, electric_scooter_type_name, battery_level)
values(4, 'offroad', 50);

insert into electric_scooters(vehicle_id, electric_scooter_type_name, battery_level)
values(5, 'urban', 70);

insert into electric_scooters(vehicle_id, electric_scooter_type_name, battery_level)
values(6, 'urban', 10);

-- user_type
insert into user_type(user_type_name)
values('client');

insert into user_type(user_type_name)
values('administrator');

-- registered_users
insert into registered_users(user_email, user_type_name, user_password)  
values('a@a.a', 'client', '111');

insert into registered_users(user_email, user_type_name, user_password)  
values('b@b.b', 'client', '222');

insert into registered_users(user_email, user_type_name, user_password)  
values('c@c.c', 'client', '333');

insert into registered_users(user_email, user_type_name, user_password)  
values('d@d.d', 'client', '444');

insert into registered_users(user_email, user_type_name, user_password)  
values('k@k.k', 'administrator', '555');

-- clients
insert into clients(user_email, points, credit_card_number, credit_card_expiration, credit_card_secret, height_m, weight_kg, gender, is_riding, age)
values('a@a.a', 10, '1111111111111111', TO_DATE('10-10-2020', 'dd/mm/yyyy'), 111, 1.72, 74, 'M', '0', 20);

insert into clients(user_email, points, credit_card_number, credit_card_expiration, credit_card_secret, height_m, weight_kg, gender, is_riding, age)
values('b@b.b', 0, '2222222222222222', TO_DATE('12-02-2021', 'dd/mm/yyyy'), 222, 1.62, 54, 'F', '1', 19);

insert into clients(user_email, points, credit_card_number, credit_card_expiration, credit_card_secret, height_m, weight_kg, gender, is_riding, age)
values('c@c.c', 300, '3333333333333333', TO_DATE('02-12-2032', 'dd/mm/yyyy'), 333, 1.90, 120, 'M', '1', 25);

insert into clients(user_email, points, credit_card_number, credit_card_expiration, credit_card_secret, height_m, weight_kg, gender, is_riding, age)
values('d@d.d', 1000, '4444444444444444', TO_DATE('01-10-2022', 'dd/mm/yyyy'), 444, 1.40, 40, 'M', '1', 13);

-- pending_registrations
insert into pending_registrations(email, paid, amount_left_to_pay, credit_card_number, credit_card_expiration, credit_card_secret, height, weight, gender, age)
values('e@e.e', 0, 8, '6666666666666666',  TO_DATE('10-10-2020', 'dd/mm/yyyy'), 666, 1.80, 80, 'M', 19);

-- invoices
insert into invoices(user_email, payment_start_date, amount, amount_left_to_pay, usage_cost, penalisation_cost)
values('a@a.a', TO_DATE('01-10-2019', 'dd/mm/yyyy'), 5, 10, 2, 3);

insert into invoices(user_email, payment_start_date, amount, amount_left_to_pay, usage_cost, penalisation_cost)
values('b@b.b', TO_DATE('01-12-2019', 'dd/mm/yyyy'), 10, 0, 4, 2);

-- park_capacity
insert into park_capacity(park_id, vehicle_type_name, park_capacity, amount_occupied)
values(1, 'bicycle', 20, 10);

insert into park_capacity(park_id, vehicle_type_name, park_capacity, amount_occupied)
values(1, 'electric scooter', 20, 10);

insert into park_capacity(park_id, vehicle_type_name, park_capacity, amount_occupied)
values(2, 'electric scooter', 40, 10);

insert into park_capacity(park_id, vehicle_type_name, park_capacity, amount_occupied)
values(2, 'bicycle', 30, 20);

-- trip
insert into trip (start_time, user_email, vehicles_id, start_park_id, end_park_id)
values(TO_DATE('04-12-2019', 'dd/mm/yyyy'), 'a@a.a', 1, 1, 2);

insert into trip (start_time, user_email, vehicles_id, start_park_id, end_park_id, end_time)
values(TO_DATE('21-12-2019', 'dd/mm/yyyy'), 'a@a.a', 2, 1, 2, TO_DATE('21-12-2019', 'dd/mm/yyyy'));

-- receipts
insert into receipts(user_email, payment_start_date, payment_end_date, points_used, amount_paid_cash)
values('a@a.a', TO_DATE('01-10-2019', 'dd/mm/yyyy'), TO_DATE('20-10-2019', 'dd/mm/yyyy'), 0, 30);
