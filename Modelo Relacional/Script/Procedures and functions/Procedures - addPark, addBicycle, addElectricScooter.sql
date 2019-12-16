select * from vehicles where vehicle_id = 1;

create or replace procedure register_bicycle(
            p_available vehicles.available%type,
            p_weight vehicles.weight%type, 
            p_aerodynamic_coefficient vehicles.aerodynamic_coefficient%type, 
            p_frontal_area vehicles.frontal_area%type, 
            p_bicycle_size bicycles.bicycle_size%type, 
            p_bicycle_description bicycles.BICYBLE_DESCRIPTION%type
        )
is
    v_vehicle_id vehicles.vehicle_id%type;
    v_vehicle_type_name vehicles.vehicle_type_name%type;
begin
    v_vehicle_type_name := 'bicycle';
    
    INSERT INTO vehicles(vehicle_type_name, available, weight, aerodynamic_coefficient, frontal_area) 
        VALUES (v_vehicle_type_name, p_available, p_weight, p_aerodynamic_coefficient, p_frontal_area) 
        returning vehicle_id into v_vehicle_id ;
    
    insert into bicycles(vehicle_id, bicycle_size, BICYBLE_DESCRIPTION) values(v_vehicle_id, p_bicycle_size, p_bicycle_description);
    COMMIT;
end;
/

create or replace procedure register_electric_scooter(
            p_available vehicles.available%type, 
            p_weight vehicles.weight%type, 
            p_aerodynamic_coefficient vehicles.aerodynamic_coefficient%type, 
            p_frontal_area vehicles.frontal_area%type, 
            p_electric_scooter_type_name electric_scooters.electric_scooter_type_name%type, 
            p_electric_scooter_description electric_scooters.electric_scooter_description%type, 
            p_max_battery_capacity electric_scooters.max_battery_capacity%type, 
            p_actual_battery_capacity electric_scooters.actual_battery_capacity%type
        )
is
    v_vehicle_id vehicles.vehicle_id%type;
    v_vehicle_type_name vehicles.vehicle_type_name%type;
begin
    v_vehicle_type_name := 'electric_scooter';
    INSERT INTO vehicles(vehicle_type_name, available, weight, aerodynamic_coefficient, frontal_area) 
        VALUES (v_vehicle_type_name, p_available, p_weight, p_aerodynamic_coefficient, p_frontal_area) 
        returning vehicle_id into v_vehicle_id ;
    
    insert into electric_scooters(vehicle_id, electric_scooter_type_name, electric_scooter_description, max_battery_capacity, actual_battery_capacity) 
        values(v_vehicle_id, p_electric_scooter_type_name, p_electric_scooter_description, p_max_battery_capacity, p_actual_battery_capacity);
    COMMIT;
end;
/

create or replace procedure register_park(
                    p_park_id parks.park_id%type,
                    p_latitude points_of_interest.latitude%type, 
                    p_longitude points_of_interest.latitude%type, 
                    p_altitude_m points_of_interest.altitude_m%type, 
                    p_poi_description points_of_interest.poi_description%type,
                    p_park_Input_Voltage parks.park_Input_Voltage%type,
                    p_park_input_current parks.park_input_current%type
                    )
is
begin
    insert into points_of_interest(latitude, longitude, altitude_m, poi_description) VALUES(p_latitude, p_longitude, p_altitude_m, p_poi_description);
    insert into parks(park_id, latitude, longitude, park_Input_Voltage, park_input_current) VALUES(p_park_id, p_latitude, p_longitude, p_park_Input_Voltage, p_park_input_current);
end;
/


begin
    register_park('new park', 10.1, 10.4, 1, 'cool place', 2, 2);
end;
/

begin
    register_bicycle(1, 10.1, 10.1, 1, 8, 2, 2, 6, 'whatever');
end;
/

begin
    register_electric_scooter(1, 30.3, 30.3, 1, 1, 1, 1, 'offroad', 'PT001', 1, 75);
end;
/