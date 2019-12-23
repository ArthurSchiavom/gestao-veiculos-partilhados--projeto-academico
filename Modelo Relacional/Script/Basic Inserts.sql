
-- vehicle_types
insert into vehicle_types(vehicle_type_name)
values('electric_scooter');

insert into vehicle_types(vehicle_type_name)
values('bicycle');

-- user_type
insert into user_type(user_type_name)
values('client');

insert into user_type(user_type_name)
values('administrator');

-- electric _scooter_types
insert into electric_scooter_types(electric_scooter_type_name)
values('offroad');

insert into electric_scooter_types(electric_scooter_type_name)
values('urban');

COMMIT;