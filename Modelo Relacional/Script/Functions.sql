--Finds the scooter with the minimum battery+10% in the park
CREATE OR REPLACE FUNCTION find_minimum_battery_scooter(start_park_id_in IN varchar2, minimum_battery_in IN numeric)
RETURN electric_scooters%rowtype IS
	scooter electric_scooters%rowtype;
BEGIN
	WITH parked_electric_scooters AS (
		SELECT v.DESCRIPTION v_id
		FROM vehicles v
		INNER JOIN park_vehicle pv
		ON pv.VEHICLE_DESCRIPTION=v.DESCRIPTION AND pv.park_id=start_park_id_in
		WHERE v.vehicle_type_name LIKE 'electric_scooter'
	)
	SELECT es.VEHICLE_DESCRIPTION, es.electric_scooter_type_name, es.actual_battery_capacity, es.max_battery_capacity, es.VEHICLE_DESCRIPTION
	INTO scooter
	FROM electric_scooters es, parked_electric_scooters pes
	WHERE pes.v_id=es.VEHICLE_DESCRIPTION AND es.actual_battery_capacity*es.max_battery_capacity>minimum_battery_in*1.1
	ORDER BY es.actual_battery_capacity*es.max_battery_capacity ASC
	FETCH FIRST ROW ONLY;
	RETURN scooter;
EXCEPTION
	WHEN no_data_found THEN
		RETURN null;
END;
/

--Finds the scooter with the highest amount of battery in the park
CREATE OR REPLACE FUNCTION find_highest_charge_scooter(start_park_id_in IN varchar2)
RETURN electric_scooters%rowtype IS
	scooter electric_scooters%rowtype;
BEGIN
	WITH parked_electric_scooters AS (
		SELECT v.DESCRIPTION v_id
		FROM vehicles v
		INNER JOIN park_vehicle pv
		ON pv.VEHICLE_DESCRIPTION=v.DESCRIPTION AND pv.park_id=start_park_id_in
		WHERE v.vehicle_type_name LIKE 'electric_scooter'
	)
	SELECT es.VEHICLE_DESCRIPTION, es.electric_scooter_type_name, es.actual_battery_capacity, es.max_battery_capacity, es.VEHICLE_DESCRIPTION
	INTO scooter
	FROM electric_scooters es, parked_electric_scooters pes
	WHERE pes.v_id=es.VEHICLE_DESCRIPTION
	ORDER BY es.actual_battery_capacity*es.max_battery_capacity DESC
	FETCH FIRST ROW ONLY;	
	RETURN scooter;
EXCEPTION
	WHEN no_data_found THEN
		RETURN null;
END;
/

--Finds an available vehicle at a park
CREATE OR REPLACE FUNCTION findAvailableBicycle(start_park_id_in IN varchar2)
RETURN vehicles%rowtype IS
	vehicle vehicles%rowtype;
BEGIN
	SELECT v.DESCRIPTION, v.vehicle_type_name, v.unique_number, v.available, v.weight, v.aerodynamic_coefficient, v.frontal_area
	INTO vehicle
	FROM vehicles v 
	INNER JOIN park_vehicle pv 
	ON pv.VEHICLE_DESCRIPTION=v.DESCRIPTION AND pv.park_id=start_park_id_in
	WHERE v.vehicle_type_name LIKE 'bicycle'
	ORDER BY weight ASC
	FETCH FIRST ROW ONLY;
	RETURN vehicle;
EXCEPTION
	WHEN no_data_found THEN
		RETURN NULL;
END;
/

--This function unlocks vehicles and updates the database accordingly
CREATE OR REPLACE FUNCTION funcUnlockVehicle(start_time_in IN timestamp, user_email_in IN varchar2, vehicle_description_in in varchar2,
		start_park_id_in IN varchar2) RETURN trips%rowtype IS
	trip trips%rowtype;
BEGIN
	INSERT INTO trips(start_time, user_email, VEHICLE_DESCRIPTION, start_park_id) values(start_time_in, user_email_in, vehicle_description_in, start_park_id_in);
	UPDATE vehicles SET available = 1 WHERE DESCRIPTION=vehicle_description_in;
	DELETE FROM park_vehicle WHERE park_id=start_park_id_in AND VEHICLE_DESCRIPTION=vehicle_description_in;
	SELECT * INTO trip FROM trips WHERE start_park_id=start_park_id_in AND user_email=user_email_in;
	COMMIT;
	RETURN trip;
END;
/

Create or replace function find_user_email_riding(p_vehicle_description park_vehicle.vehicle_description%type) return REGISTERED_USERS.user_email%type is
    v_email REGISTERED_USERS.user_email%type;
begin
    select USER_EMAIL into v_email from TRIPS where VEHICLE_DESCRIPTION = p_vehicle_description
                                            AND END_TIME is null;
    return v_email;
end;
/

begin
    dbms_output.put_line(find_user_email_riding('PT002'));
end;
/