--Check if the email that is being inserted in the table is registered

create or replace trigger verify_if_email_is_registered before insert or update
of email
on pending_registrations
for each row
declare
    verification boolean;
    registeredEmail registered_users.user_email%type;
    cursor registered_Emails is
         Select user_email 
         from registered_users;    
begin
    verification:=false;
    open registered_Emails;
    loop
        fetch registered_Emails into registeredEmail;        
        exit when registered_Emails%notfound;
        if ltrim(:new.email)=ltrim(registeredEmail) then
            verification:=true;
        end if;
    end loop;
    close registered_Emails;
    
    if (not verification) then
         Raise_application_error(-20310, 'Unregistered Email');
    end if;
end;
/

Alter trigger verify_if_email_is_registered enable;
set serveroutput on;

--teste 

Insert into user_type(user_type_name)
values ('ADMINA');

Insert into registered_users(user_email,user_type_name,user_password)
values ('francisca.moutas@gmail.com','ADMINA','reifranciscao');

Insert into pending_registrations(email,paid,amount_left_to_pay,credit_card_number,credit_card_expiration,credit_card_secret,height,weight,gender,age)
values ('francisca.moutas@gmail.com',0,20,1234567891234567, TO_DATE('11-10-2022','dd/mm/yyyy'), 123,70,30,'m',21);
