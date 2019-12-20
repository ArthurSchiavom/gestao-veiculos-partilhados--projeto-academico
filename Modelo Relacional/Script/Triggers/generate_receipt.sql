CREATE OR REPLACE TRIGGER generate_receipt AFTER INSERT OR UPDATE ON invoices FOR EACH ROW
DECLARE
        amount_cash receipts.amount_paid_cash%type;
BEGIN
        IF :new.amount_left_to_pay <= 0 THEN
                amount_cash:= (:new.usage_cost + :new.penalisation_cost) - (floor(:new.points_used/10));
                INSERT INTO receipts VALUES(:new.user_email,:new.payment_start_date,SYSDATE,amount_cash);
        END IF;
END;
/