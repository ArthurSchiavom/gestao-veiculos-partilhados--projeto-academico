
create or replace procedure register_bicycle(
            p_weight vehicles.weight%type, 
            p_aerodynamic_coefficient vehicles.aerodynamic_coefficient%type, 
            p_frontal_area vehicles.frontal_area%type, 
            p_bicycle_size bicycles.bicycle_size%type, 
            p_description vehicles.DESCRIPTION%type,
            p_park_lat parks.latitude%type,
            p_park_lon parks.longitude%type
        )
is
    v_vehicle_type_name vehicles.vehicle_type_name%type;
    v_park_id park_vehicle.park_id%type;
begin
    v_vehicle_type_name := 'bicycle';
    
    INSERT INTO vehicles(description, vehicle_type_name, available, weight, aerodynamic_coefficient, frontal_area) 
        VALUES (p_description, v_vehicle_type_name, 1, p_weight, p_aerodynamic_coefficient, p_frontal_area);
    
    insert into bicycles(vehicle_description, bicycle_size) values(p_description, p_bicycle_size);
    
    select park_id into v_park_id from parks where parks.latitude = p_park_lat and parks.longitude = p_park_lon;
    insert into park_vehicle(park_id, vehicle_description) VALUES(v_park_id, p_description);
end;
/

create or replace procedure register_park(
                    p_park_id parks.park_id%type,
                    p_latitude points_of_interest.latitude%type, 
                    p_longitude points_of_interest.latitude%type, 
                    p_altitude_m points_of_interest.altitude_m%type, 
                    p_poi_description points_of_interest.poi_description%type,
                    p_park_Input_Voltage parks.park_Input_Voltage%type,
                    p_park_input_current parks.park_input_current%type,
                    p_max_eletric_scooters park_capacity.park_capacity%type,
                    p_max_bicycles park_capacity.park_capacity%type
                    )
is
begin
    insert into points_of_interest(latitude, longitude, altitude_m, poi_description) VALUES(p_latitude, p_longitude, p_altitude_m, p_poi_description);
    insert into parks(park_id, latitude, longitude, park_Input_Voltage, park_input_current) VALUES(p_park_id, p_latitude, p_longitude, p_park_Input_Voltage, p_park_input_current);
    insert into park_capacity values (p_park_id, 'electric_scooter', p_max_eletric_scooters, 0);
    insert into park_capacity values (p_park_id, 'bicycle', p_max_bicycles, 0);
end;
/
COMMIT;


-- begin
--     register_park('doggo park', 10.2, 10.5, 1, 'cute place', 2, 2, 10, 20);
-- end;
-- /

-- begin
--     register_bicycle(1, 10, 10, 1, 'whateverrsr', 10.2, 10.5);
-- end;
-- /