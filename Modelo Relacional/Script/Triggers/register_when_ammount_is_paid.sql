SET SERVEROUTPUT ON;

CREATE OR REPLACE TRIGGER register_when_amount_is_paid AFTER INSERT OR UPDATE ON pending_registrations FOR EACH ROW
DECLARE
        user_type_n user_type.user_type_name%type;
BEGIN
        IF :new.amount_left_to_pay <= 0 THEN
                SELECT user_type_name INTO user_type_n FROM user_type WHERE UPPER(user_type_name) LIKE 'CLIENT';
                INSERT INTO registered_users VALUES (:new.email,user_type_n, :new.user_password,:new.user_name);
                INSERT INTO clients VALUES(:new.email,0,:new.visa,:new.height,:new.weight,:new.gender,'0',:new.cycling_average_speed);
        END IF;
EXCEPTION
        WHEN no_data_found THEN
                RAISE_APPLICATION_ERROR(-20001,'There are no user types');
END;
/
-- Serviu para testar:
delete from PENDING_REGISTRATIONS;
select * from clients;
update pending_registrations set AMOUNT_LEFT_TO_PAY = 0;
Insert into user_type(user_type_name)
values ('ADMIN');
Insert into user_type(user_type_name)
values ('CLIENT');
commit;
Insert into registered_users(user_email,user_type_name,user_password,user_name)
values ('francisca.moutas@gmail.com','CLIENT','reifranciscao','user');
select * from clients;
Insert into pending_registrations(email,amount_left_to_pay,visa,height,weight,gender,age,cycling_average_speed,user_password,user_name)
values ('francisca.moutas@gmail.com',20,'1234567891234567',70,30,'m',21,12,'password','userame');
INSERT INTO pending_registrations(email, amount_left_to_pay, visa, height, weight, gender, age, cycling_average_speed, user_password, user_name) 
VALUES('ol1a.moutas@gmail.com',0,'1234567891234567',70,30,'m',21,20,'password','user_an');