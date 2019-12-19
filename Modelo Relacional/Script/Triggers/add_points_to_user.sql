--ANTES DE COMPILAR O TRIGGER, PRIMEIRO VEM A FUNCAO !!!!!!

create or replace function getAltitude(parkId parks.park_id%type) return number is
altitudeRet points_of_interest.altitude_m%type;
begin
    with 
        tabelaLatitudeLongitude as
        (Select latitude,longitude from parks where park_id=parkId)
         Select poi.altitude_m into altitudeRet
         from points_of_interest poi inner join tabelaLatitudeLongitude tbLatLot on poi.latitude=tbLatLot.latitude and poi.longitude=tbLatLot.longitude;
    if altitudeRet is not null then
        return altitudeRet;
    end if;    
end;
/



create or replace trigger add_points_to_user before update 
on trips
for each row    
declare
minsStart_time integer;
minsEnd_time integer;
altitudeInicial points_of_interest.altitude_m%type;
altitudeFinal points_of_interest.altitude_m%type;
vehicleType vehicles.vehicle_type_name%type;
pontosAltitude number(4);
pontosAnteriores clients.points%type;
diferencaEmMinutos int;
diferencaDeAltitude int;
begin
    Select points into pontosAnteriores from clients where user_email=:old.user_email;
    if :new.end_time is not null then 
        SELECT to_char(:old.start_time,'HH24')*60+to_char(:old.start_time,'MI')  into minsStart_time from dual;
        SELECT to_char(:new.end_time,'HH24')*60+to_char(:new.end_time,'MI') into minsEnd_time from dual;
        diferencaEmMinutos:=minsEnd_Time-minsStart_time;
        dbms_output.put_line('Diferenca em minutos: ' || diferencaEmMinutos);
        Select ltrim(vehicle_type_name) into vehicleType from vehicles where vehicle_id= :new.vehicle_id;
        dbms_output.put_line('Tipo veiculo: ' || vehicleType);
        --Caso o intervalo seja menor que 15min 
        if (diferencaEmMinutos<15) and vehicleType='electric_scooter' then
            pontosAnteriores:= pontosAnteriores+5;
            dbms_output.put_line('Pontos adicionados aos anteriores: '||pontosAnteriores);
            UPDATE clients set points=pontosAnteriores WHERE user_email=:old.user_email;
        end if;  
        --Diferenca das altitudes 
        if :new.end_park_id is not null then
            altitudeInicial:= getAltitude(:old.start_park_id);
            altitudeFinal:=  getAltitude(:new.end_park_id);
            diferencaDeAltitude:= altitudeFinal-altitudeInicial;
             CASE
                    WHEN diferencaDeAltitude > 50 THEN
                        pontosAltitude:=15;
                    WHEN diferencaDeAltitude > 25 and diferencaDeAltitude <=50 THEN 
                        pontosAltitude:=5;
             ELSE 
                        pontosAltitude:=0; 
             END CASE;
             UPDATE clients set points= pontosAnteriores + pontosAltitude WHERE user_email=:old.user_email;
        end if;    
    end if;    
END;
/

Alter trigger add_points_to_user enable;
set serveroutput on;