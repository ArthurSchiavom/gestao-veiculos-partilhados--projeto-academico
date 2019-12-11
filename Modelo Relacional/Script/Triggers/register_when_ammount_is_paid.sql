SET SERVEROUTPUT ON;

CREATE OR REPLACE TRIGGER register_when_amount_is_paid AFTER INSERT OR UPDATE ON pending_registrations FOR EACH ROW
DECLARE
        user_type_n user_type.user_type_name%type;
        generic_password varchar(50) := 'qwerty';
        email_n   registered_users.user_email%type;
BEGIN
        IF :new.amount_left_to_pay <= 0 THEN
                SELECT user_type_name INTO user_type_n FROM user_type WHERE UPPER(user_type_name) LIKE 'CLIENT';
                INSERT INTO registered_users VALUES (:new.email,user_type_n, generic_password);
                SELECT user_email INTO email_n FROM registered_users WHERE user_email = :new.email;
                INSERT INTO clients VALUES(email_n,0,:new.credit_card_number,:new.credit_card_expiration, :new.credit_card_secret,:new.height,:new.weight,:new.gender,'1',:new.age);
        END IF;
EXCEPTION
        WHEN no_data_found THEN
                RAISE_APPLICATION_ERROR(-20001,'There are no user types');
END;
/
-- Serviu para testar:
select * from registered_users;
select * from clients;
update pending_registrations set AMOUNT_LEFT_TO_PAY = 0;
Insert into user_type(user_type_name)
values ('ADMIN');
Insert into user_type(user_type_name)
values ('CLIENT');

Insert into registered_users(user_email,user_type_name,user_password)
values ('francisca.moutas@gmail.com','CLIENT','reifranciscao');

Insert into pending_registrations(email,paid,amount_left_to_pay,credit_card_number,credit_card_expiration,credit_card_secret,height,weight,gender,age)
values ('francisca.moutas@gmail.com',0,20,'1234567891234567', TO_DATE('11-10-2022','dd/mm/yyyy'), 123,70,30,'m',21);