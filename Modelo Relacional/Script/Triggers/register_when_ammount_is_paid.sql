SET SERVEROUTPUT ON;

CREATE OR REPLACE TRIGGER register_when_amount_is_paid AFTER INSERT OR UPDATE ON pending_registrations FOR EACH ROW
DECLARE
        user_type_n user_type.user_type_name%type;
        email_n   registered_users.user_email%type;
BEGIN
        IF :new.amount_left_to_pay <= 0 THEN
                SELECT user_type_name INTO user_type_n FROM user_type WHERE UPPER(user_type_name) LIKE 'CLIENT';
                INSERT INTO registered_users VALUES (:new.email,user_type_n, :new.user_password,:new.user_name);
                SELECT user_email INTO email_n FROM registered_users WHERE user_email = :new.email;
                INSERT INTO clients VALUES(email_n,0,:new.credit_card_number,:new.credit_card_expiration, :new.credit_card_secret,:new.height,:new.weight,:new.gender,'1',:new.age,:new.cycling_average_speed);
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

Insert into registered_users(user_email,user_type_name,user_password,user_name)
values ('francisca.moutas@gmail.com','CLIENT','reifranciscao','user');

Insert into pending_registrations(email,paid,amount_left_to_pay,credit_card_number,credit_card_expiration,credit_card_secret,height,weight,gender,age)
values ('francisca.moutas@gmail.com',0,20,'1234567891234567', TO_DATE('11-10-2022','dd/mm/yyyy'), 123,70,30,'m',21);
INSERT INTO pending_registrations(email, amount_left_to_pay, credit_card_number, credit_card_expiration, credit_card_secret, height, weight, gender, age, cycling_average_speed, user_password, user_name) 
VALUES('francisca.moutas@gmail.com',0,'1234567891234567', TO_DATE('11-10-2022','dd/mm/yyyy'), 123,70,30,'m',21,20,'password','user_an');