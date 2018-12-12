delimiter /

DROP TABLE "LAPR3_G50"."SAILORS" cascade constraints
/

--------------------------------------------------------
--  DDL for Table SAILORS
--------------------------------------------------------

  CREATE TABLE "LAPR3_G50"."SAILORS"
   (	"SID" NUMBER,
	"SNAME" VARCHAR2(200 BYTE),
	"RATING" NUMBER,
	"AGE" NUMBER
   )
/
--------------------------------------------------------
--  DDL for Index SAILORS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "LAPR3_G50"."SAILORS_PK" ON "LAPR3_G50"."SAILORS" ("SID")
  PCTFREE 10 INITRANS 2 MAXTRANS 255
  TABLESPACE "USERS"
/
--------------------------------------------------------
--  Constraints for Table SAILORS
--------------------------------------------------------

  ALTER TABLE "LAPR3_G50"."SAILORS" ADD CONSTRAINT "SAILORS_PK" PRIMARY KEY ("SID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255
  TABLESPACE "USERS"  ENABLE
/
  ALTER TABLE "LAPR3_G50"."SAILORS" MODIFY ("SID" NOT NULL ENABLE)
/


-- Função retorna a referência de um objeto cursor
CREATE OR REPLACE FUNCTION getSailor(sid NUMBER)
RETURN SYS_REFCURSOR
AS
  curSailor SYS_REFCURSOR;	-- variável de cursor do tipo predefinido SYS_REFCURSOR 
BEGIN
  -- criar um objeto cursor e armazenar a referÍncia desse objeto na vari·vel de cursor curSailor
  OPEN curSailor FOR SELECT * FROM SAILORS WHERE SAILORS.SID = getSailor.sid; RETURN curSailor; END;
/

CREATE OR REPLACE PROCEDURE addSailor(sid NUMBER, sname VARCHAR, rating NUMBER, age NUMBER) 
AS
BEGIN
  INSERT INTO sailors VALUES(sid,sname,rating,age);   
END;
/

CREATE OR REPLACE PROCEDURE removeSailor(sid NUMBER) 
IS
BEGIN
  DELETE FROM SAILORS WHERE SAILORS.SID = removeSailor.sid;
END;
/