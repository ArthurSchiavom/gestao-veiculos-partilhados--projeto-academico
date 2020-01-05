--ANTES DE COMPILAR O TRIGGER, PRIMEIRO VEM A FUNCAO !!!!!!

SET SERVEROUTPUT ON;
create or replace function getAltitude(parkId parks.park_id%type) return number is
    altitudeRet points_of_interest.altitude_m%type;
begin
    with 
        tabelaLatitudeLongitude as
        (Select latitude,longitude from parks where park_id = parkId)
    Select poi.altitude_m into altitudeRet
         from points_of_interest poi inner join tabelaLatitudeLongitude tbLatLot on poi.latitude=tbLatLot.latitude and poi.longitude=tbLatLot.longitude;
    
    if altitudeRet is not null then
        return altitudeRet;
    else
        raise_application_error(-20900,'No such point of interest');
    end if;    
end;
/

create or replace function interval_to_minutes(a interval day to second) return int is
begin
    return 
        extract(day from a) * 1440 -- 60 * 24
        + extract(hour from a) * 60
        + extract(minute from a)
        + extract(second from a) / 60;
end;
/

-- end - start
create or replace function time_difference_minutes(p_start timestamp, p_end timestamp) return int is
begin
    return interval_to_minutes(p_end - p_start);
end;
/

-- If no invoice exists for this month, create a new one
create or replace function get_latest_invoice_start_date(p_user_email invoices.user_email%type) return invoices.payment_start_date%type is
    v_latest_payment_start_date invoices.payment_start_date%type;
    v_this_year char(4);
    v_this_month char(2);
    v_new_payment_start_date_char varchar(10);
    v_new_payment_start_date invoices.payment_start_date%type;
    v_user_current_points clients.points%type;
begin
    select max(payment_start_date) into v_latest_payment_start_date from invoices where user_email = p_user_email;
    
    if v_latest_payment_start_date is null
            OR ((extract(year from sysdate) != extract(year from v_latest_payment_start_date)
            OR extract(month from sysdate) != extract(month from v_latest_payment_start_date))
                AND extract(day from sysdate) >= 5) then -- Invoice é enviado dia 5.
        select points into v_user_current_points from clients where user_email = p_user_email;
        v_this_month := to_char(extract(month from sysdate));
        v_this_year := to_char(extract(year from sysdate));
        v_new_payment_start_date_char := '05/' || v_this_month || '/' ||v_this_year;
        v_new_payment_start_date := to_date(v_new_payment_start_date_char, 'DD/MM/YYYY');
        insert into invoices(previous_points, user_email, payment_start_date, amount_left_to_pay, usage_cost, penalisation_cost, points_used)
            values (v_user_current_points, p_user_email, v_new_payment_start_date, 0, 0, 0, 0);
        
        v_latest_payment_start_date := v_new_payment_start_date;
    end if;
    
    return v_latest_payment_start_date;
end;
/

create or replace function calculateTripPrice(trip_duration_min int) return invoices.usage_cost%type is
	v_trip_cost invoices.usage_cost%type;
begin
	v_trip_cost := (trip_duration_min - 60) * 0.025; -- 0.025 = 1.5/60 = preço por minuto dado que o custo é 1.5 euros por hora
	if v_trip_cost < 0 then
		v_trip_cost := 0;
	end if;
	return v_trip_cost;
end;
/

create or replace trigger on_trips_update before update on trips
for each row    
declare
    v_altitudeInicial points_of_interest.altitude_m%type;
    v_altitudeFinal points_of_interest.altitude_m%type;
    v_pontosAltitude number(4);
    v_user_points clients.points%type;
    v_diferencaDeAltitude int;
    v_invoice_payment_date invoices.payment_start_date%type;
    v_trip_duration_min int;
    v_usage_cost invoices.usage_cost%type;
begin
    if :new.end_time is not null then 
        -- adicionar 5 pontos ao utilizador caso a viagem tenha durado menos de 15min
        v_trip_duration_min := time_difference_minutes(:new.start_time, :new.end_time);
        if v_trip_duration_min < 15 then
            update clients set points = points + 5 where user_email = :new.user_email;
        end if;
        
        --Diferenca das altitudes
        v_altitudeInicial := getAltitude(:new.start_park_id);
        v_altitudeFinal := getAltitude(:new.end_park_id);
        v_diferencaDeAltitude := v_altitudeFinal - v_altitudeInicial;
        if v_diferencaDeAltitude > 25 THEN
            if v_diferencaDeAltitude > 50 THEN
                v_pontosAltitude := 15;
            else
                v_pontosAltitude := 5;
            end if;
            
            update clients set points = points + v_pontosAltitude where user_email = :new.user_email;
        end if;
        
        -- If no invoice exists for this month, create a new one
        v_invoice_payment_date := get_latest_invoice_start_date(:new.user_email);
        
        -- Add amount to pay to invoice
        -- After the gratuitous period (1h), each following hour costs 1,5€.
        if v_trip_duration_min > 60 then
            v_usage_cost := calculateTripPrice(v_trip_duration_min);
            update invoices set usage_cost = usage_cost + v_usage_cost, amount_left_to_pay = amount_left_to_pay + v_usage_cost where user_email = :new.user_email and payment_start_date = v_invoice_payment_date;
        end if;
    end if;
END;
/

begin
    dbms_output.put_line(get_latest_invoice_start_date('a@a.a'));
end;
/

Update trips set end_park_id = 'park2', end_time = to_timestamp('06/01/2020-23:23', 'DD/MM/YYYY-HH24:MI') where user_email = 'a@a.a' and vehicle_description = 'PT003';