create or replace trigger update_park_capacity before insert or delete
on park_vehicle
for each row
declare 
    quantidade integer;
    emailUser trips.user_email%type;
begin 
    if inserting then
        UPDATE park_capacity SET amount_occupied=amount_occupied+1 WHERE park_id=:new.park_id;
        /*podia ter posto group by e agrupar pelo user_email e inserir-lo na mesma no emailUser. Nao
        coloquei pois se quisermos adicionar ja parques associados a veiculos sem nenhum pedido de viagem associado é possivel desta maneira*/
        --nesta
        Select nvl(count(*),0) into quantidade from trips where  vehicle_id =:new.vehicle_id and (end_time is null and end_park_id is null);
        if (quantidade>0) then
            --nesta
            Select user_email into emailUser  from trips where  vehicle_id =:new.vehicle_id and (end_time is null and end_park_id is null);
            UPDATE trips SET end_park_id=:new.park_id, end_time=current_timestamp WHERE  (end_time is null and end_park_id is null) and (vehicle_id = :new.vehicle_id);
            UPDATE clients SET is_riding=0 where user_email=emailUser;  
        else 
            dbms_output.put_line('No trips found');
        /*else 
           Raise_application_error(-20650, 'Trips not found');
        */
        end if;
    end if;
    if deleting then 
        UPDATE park_capacity SET amount_occupied= amount_occupied-1 WHERE park_id=:old.park_id;
        Select nvl(count(*),0) into quantidade  from trips where (start_park_id=:old.park_id and vehicle_id= :old.vehicle_id) and (end_time is null and end_park_id is null);
        if quantidade>0 then
            Select user_email into  emailUser  from trips where (start_park_id=:old.park_id and vehicle_id= :old.vehicle_id) and (end_time is null and end_park_id is null);
            UPDATE clients SET is_riding=1 where user_email=emailUser;
        else
            dbms_output.put_line('No trips found');
        /*else 
           Raise_application_error(-20650, 'Trips not found');
        */
        end if;
    end if;
end;
/

Alter trigger update_park_capacity enable;
set serveroutput on;

--delete test
DELETE FROM park_vehicle WHERE park_id='park1' and vehicle_id=2;


