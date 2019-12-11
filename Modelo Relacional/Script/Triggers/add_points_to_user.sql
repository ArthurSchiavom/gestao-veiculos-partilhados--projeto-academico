create or replace trigger add_points_to_user before update 
on trip
for each row
declare 
minsStart_time integer;
minsEnd_time integer;
pontosAnteriores integer;
altitudeInicial parks.altitude%type;
altitudeFinal parks.altitude%type;
begin
    if :new.end_time is not null and :old.end_time is null then 
        SELECT to_char(:old.start_time,'HH24')*60+to_char(:old.start_time,'MM')+to_char(:old.start_time,'SS')/60  into minsStart_time from dual;
        SELECT to_char(:new.end_time,'HH24')*60+to_char(:new.end_time,'MM')+to_char(:new.end_time,'SS')/60  into minsEnd_time from dual;
        if minsEnd_time-minsStart_time<15 then
             Select pontos into pontosAnteriores from clients where user_email=:old.user_email;  --Vai buscar os pontos anteriores do utilizador
             UPDATE clients set pontos= pontosAnteirores +10 WHERE user_email=:old.user_email;   --Da update aos pontos do utilizador (AINDA NAO SE SABE OS PONTOS ATRIBUIR--Substituir onde tem 10)
        end if;    
    end if;
    if :new.end_park_id is not null and :old.end_park_id is null then 
        Select altitude into altitudeInicial from parks where park_id=:start_park_id;
        Select altitude into altitudeFinal from parks where park_id=:end_park_id;
        if (altitudeFinal-altitudeInicial>0) then 
                                                                                                  --Recebe pontos
            UPDATE clients set pontos= pontosAnteirores +20 WHERE user_email=:old.user_email;
        end if;
    end if;
    
end;
