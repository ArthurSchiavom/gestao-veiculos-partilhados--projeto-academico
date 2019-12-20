--Finds the scooter with the minimum battery+10% in the park
CREATE OR REPLACE FUNCTION findMinimumBatteryScooter(start_park_id_in IN varchar2, minimum_battery_in IN numeric)
RETURN electric_scooters%rowtype IS
	scooter electric_scooters%rowtype;
BEGIN
	WITH parked_electric_scooters AS (
		SELECT v.vehicle_id v_id
		FROM vehicles v
		INNER JOIN park_vehicle pv
		ON pv.vehicle_id=v.vehicle_id AND pv.park_id=start_park_id_in
		WHERE v.vehicle_type_name LIKE 'electric_scooter'
	)
	SELECT es.vehicle_id, es.electric_scooter_type_name, es.actual_battery_capacity, es.max_battery_capacity, es.electric_scooter_description
	INTO scooter
	FROM electric_scooters es, parked_electric_scooters pes
	WHERE pes.v_id=es.vehicle_id AND es.actual_battery_capacity*es.max_battery_capacity>minimum_battery_in*1.1
	ORDER BY es.actual_battery_capacity*es.max_battery_capacity ASC
	FETCH FIRST ROW ONLY;
	RETURN scooter;
EXCEPTION
	WHEN no_data_found THEN
		RETURN null;
END;
/

--Finds the scooter with the highest amount of battery in the park
CREATE OR REPLACE FUNCTION findHighestChargeScooter(start_park_id_in IN varchar2) 
RETURN electric_scooters%rowtype IS
	scooter electric_scooters%rowtype;
BEGIN
	WITH parked_electric_scooters AS (
		SELECT v.vehicle_id v_id
		FROM vehicles v
		INNER JOIN park_vehicle pv
		ON pv.vehicle_id=v.vehicle_id AND pv.park_id=start_park_id_in
		WHERE v.vehicle_type_name LIKE 'electric_scooter'
	)
	SELECT es.vehicle_id, es.electric_scooter_type_name, es.actual_battery_capacity, es.max_battery_capacity, es.electric_scooter_description
	INTO scooter
	FROM electric_scooters es, parked_electric_scooters pes
	WHERE pes.v_id=es.vehicle_id 
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
	SELECT v.vehicle_id, v.vehicle_type_name, v.available, v.weight, v.aerodynamic_coefficient, v.frontal_area 
	INTO vehicle
	FROM vehicles v 
	INNER JOIN park_vehicle pv 
	ON pv.vehicle_id=v.vehicle_id AND pv.park_id=start_park_id_in
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
CREATE OR REPLACE FUNCTION funcUnlockVehicle(start_time_in IN timestamp, user_email_in IN varchar2, vehicle_id_in in int, 
		start_park_id_in IN varchar2) RETURN trips%rowtype IS
	trip trips%rowtype;
BEGIN
	INSERT INTO trips(start_time, user_email, vehicle_id, start_park_id) values(start_time_in, user_email_in, vehicle_id_in, start_park_id_in);
	UPDATE vehicles SET available = true WHERE vehicle_id=vehicle_id_in;
	DELETE FROM park_vehicle WHERE park_id=start_park_id_in AND vehicle_id=vehicle_id_in;
	SELECT * INTO trip FROM trips WHERE start_park_id=start_park_id_in AND user_email=user_email_in;
	COMMIT;
	RETURN trip;
END;
/