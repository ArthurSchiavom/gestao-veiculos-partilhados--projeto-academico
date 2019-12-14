select * from vehicles where vehicle_id = 1;

create or replace procedure register_bicycle(
            p_available vehicles.available%type, 
            p_latitude vehicles.latitude%type, 
            p_longitude vehicles.longitude%type, 
            p_altitude_m vehicles.altitude_m%type, 
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
    
    INSERT INTO vehicles(vehicle_type_name, available, latitude, longitude, altitude_m, weight, aerodynamic_coefficient, frontal_area) 
        VALUES (v_vehicle_type_name, p_available, p_latitude, p_longitude, p_altitude_m, p_weight, p_aerodynamic_coefficient, p_frontal_area) 
        returning vehicle_id into v_vehicle_id ;
    
    insert into bicycles(vehicle_id, bicycle_size, BICYBLE_DESCRIPTION) values(v_vehicle_id, p_bicycle_size, p_bicycle_description);
    COMMIT;
end;
/

create or replace procedure register_electric_scooter(
            p_available vehicles.available%type, 
            p_latitude vehicles.latitude%type, 
            p_longitude vehicles.longitude%type, 
            p_altitude_m vehicles.altitude_m%type, 
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
    INSERT INTO vehicles(vehicle_type_name, available, latitude, longitude, altitude_m, weight, aerodynamic_coefficient, frontal_area) 
        VALUES (v_vehicle_type_name, p_available, p_latitude, p_longitude, p_altitude_m, p_weight, p_aerodynamic_coefficient, p_frontal_area) 
        returning vehicle_id into v_vehicle_id ;
    
    insert into electric_scooters(vehicle_id, electric_scooter_type_name, electric_scooter_description, max_battery_capacity, actual_battery_capacity) 
        values(v_vehicle_id, p_electric_scooter_type_name, p_electric_scooter_description, p_max_battery_capacity, p_actual_battery_capacity);
    COMMIT;
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