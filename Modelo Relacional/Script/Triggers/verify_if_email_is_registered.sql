
--Check if the email that is being inserted in the table is registered

create or replace trigger verify_if_email_is_registered before insert or update
on pending_registrations
for each row
declare
    v_count int;   
begin
    select count(*) into v_count from clients where user_email like :new.email;
    if v_count = 1 then
        RAISE_APPLICATION_ERROR(-20185, 'Email already exists.');
    end if;
end;
/

-- Alter trigger verify_if_email_is_registered enable;
-- set serveroutput on;

--teste 

-- Insert into user_type(user_type_name)
-- values ('ADMINA');

-- Insert into registered_users(user_email,user_type_name,user_password)
-- values ('francisca.moutas@gmail.com','ADMINA','reifranciscao');

-- Insert into pending_registrations(email,paid,amount_left_to_pay,credit_card_number,credit_card_expiration,credit_card_secret,height,weight,gender,age)
-- values ('francisca.moutas@gmail.com',0,20,1234567891234567, TO_DATE('11-10-2022','dd/mm/yyyy'), 123,70,30,'m',21);
