create or replace function get_vehicle_type(p_vehicle_description vehicles.description%type) return park_capacity.vehicle_type_name%type is
    v_type park_capacity.vehicle_type_name%type;
begin
    select vehicle_type_name into v_type from vehicles where description = p_vehicle_description;
    return v_type;
end;
/

create or replace trigger on_park_vehicle_update before insert or delete on park_vehicle
for each row
declare 
    quantidade integer;
    emailUser trips.user_email%type;
begin 
    if inserting then
        UPDATE park_capacity SET amount_occupied = amount_occupied + 1 WHERE park_id = :new.park_id and vehicle_type_name = get_vehicle_type(:new.vehicle_description);
        /* O caso de veículos não estarem associados a viagens não é verificado porque os veículos não têm necessáriamente de estar em uso. Pode ser a primeira vez
           que são adicionados ou serem coletados por funcionários após certos incidentes. */
        Select nvl(count(*),0) into quantidade from trips where vehicle_description = :new.vehicle_description and end_park_id is null;
        if quantidade = 1 then
            Select user_email into emailUser from trips where vehicle_description = :new.vehicle_description and end_park_id is null;
            UPDATE trips SET end_park_id = :new.park_id, end_time = current_timestamp WHERE (end_park_id is null) and (vehicle_description = :new.vehicle_description);
            UPDATE clients SET is_riding = 0 where user_email = emailUser;
        end if;
    end if;
    if deleting then 
        UPDATE park_capacity SET amount_occupied = amount_occupied - 1 WHERE park_id = :old.park_id and vehicle_type_name = get_vehicle_type(:old.vehicle_description);
        -- Create new trip in java, where the user is known
    end if;
end;
/

delete from park_vehicle where vehicle_description = 'PT002';
insert into park_vehicle(vehicle_description, park_id) values('PT002', 'park2');
