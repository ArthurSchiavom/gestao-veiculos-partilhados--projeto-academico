create or replace trigger update_park_capacity before insert or delete
on park_vehicle

begin 
    if inserting then
        UPDATE park_capacity SET amount_occupied=amount_occupied+1 WHERE park_id=:new.park_id;
    end if;
    
    if deleting then 
        UPDATE park_capacity SET amount_occupied= amount_occupied-1 WHERE park_id=:old.park_id;
    end if;
end;

Alter trigger update_park_capacity enable;
set serveroutput on;