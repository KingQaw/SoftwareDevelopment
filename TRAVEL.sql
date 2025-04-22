/*
 Navicat Premium Dump SQL

 Source Server         : TRAVEL
 Source Server Type    : Oracle
 Source Server Version : 110200 (Oracle Database 11g Enterprise Edition Release 11.2.0.1.0 - 64bit Production
With the Partitioning, OLAP, Data Mining and Real Application Testing options)
 Source Host           : localhost:1521
 Source Schema         : TRAVEL

 Target Server Type    : Oracle
 Target Server Version : 110200 (Oracle Database 11g Enterprise Edition Release 11.2.0.1.0 - 64bit Production
With the Partitioning, OLAP, Data Mining and Real Application Testing options)
 File Encoding         : 65001

 Date: 23/04/2025 00:38:19
*/


-- ----------------------------
-- Table structure for GUIDES
-- ----------------------------
DROP TABLE "TRAVEL"."GUIDES";
CREATE TABLE "TRAVEL"."GUIDES" (
  "ID" NUMBER NOT NULL,
  "USERS_ID" NUMBER NOT NULL,
  "TITLE" VARCHAR2(255 BYTE) NOT NULL,
  "COVER" BLOB,
  "CONTENT" CLOB NOT NULL,
  "GUIDES_AUDIT_STATUS_ID" NUMBER DEFAULT 2 NOT NULL,
  "CREATE_TIME" TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
  "AUDIT_TIME" TIMESTAMP(6)
)
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for GUIDES_AUDIT_STATUS
-- ----------------------------
DROP TABLE "TRAVEL"."GUIDES_AUDIT_STATUS";
CREATE TABLE "TRAVEL"."GUIDES_AUDIT_STATUS" (
  "ID" NUMBER NOT NULL,
  "NAME" VARCHAR2(255 BYTE) NOT NULL
)
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for GUIDES_COMMENTS
-- ----------------------------
DROP TABLE "TRAVEL"."GUIDES_COMMENTS";
CREATE TABLE "TRAVEL"."GUIDES_COMMENTS" (
  "ID" NUMBER NOT NULL,
  "GUIDES_ID" NUMBER NOT NULL,
  "USERS_ID" NUMBER NOT NULL,
  "CONTENT" CLOB NOT NULL,
  "CREATE_TIME" TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP
)
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for GUIDES_FAVORITES
-- ----------------------------
DROP TABLE "TRAVEL"."GUIDES_FAVORITES";
CREATE TABLE "TRAVEL"."GUIDES_FAVORITES" (
  "ID" NUMBER NOT NULL,
  "GUIDES_ID" NUMBER NOT NULL,
  "USERS_ID" NUMBER NOT NULL,
  "CREATE_TIME" TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP
)
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for GUIDES_LIKES
-- ----------------------------
DROP TABLE "TRAVEL"."GUIDES_LIKES";
CREATE TABLE "TRAVEL"."GUIDES_LIKES" (
  "ID" NUMBER NOT NULL,
  "GUIDES_ID" NUMBER NOT NULL,
  "USERS_ID" NUMBER NOT NULL,
  "CREATE_TIME" TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP
)
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for ROLES
-- ----------------------------
DROP TABLE "TRAVEL"."ROLES";
CREATE TABLE "TRAVEL"."ROLES" (
  "ID" NUMBER NOT NULL,
  "NAME" VARCHAR2(255 BYTE) NOT NULL
)
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for USERS
-- ----------------------------
DROP TABLE "TRAVEL"."USERS";
CREATE TABLE "TRAVEL"."USERS" (
  "ID" NUMBER NOT NULL,
  "ACCOUNT" VARCHAR2(255 BYTE) NOT NULL,
  "PASSWORD" VARCHAR2(255 BYTE) NOT NULL,
  "ROLES_ID" NUMBER DEFAULT 2 NOT NULL,
  "NICKNAME" VARCHAR2(255 BYTE),
  "AVATAR" BLOB,
  "USERS_STATUS_ID" NUMBER DEFAULT 1,
  "CREATE_TIME" TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP
)
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for USERS_STATUS
-- ----------------------------
DROP TABLE "TRAVEL"."USERS_STATUS";
CREATE TABLE "TRAVEL"."USERS_STATUS" (
  "ID" NUMBER NOT NULL,
  "NAME" VARCHAR2(255 BYTE) NOT NULL
)
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for VISITS
-- ----------------------------
DROP TABLE "TRAVEL"."VISITS";
CREATE TABLE "TRAVEL"."VISITS" (
  "ID" NUMBER NOT NULL,
  "USERS_ID" NUMBER NOT NULL,
  "CREATE_TIME" TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
  "IP" VARCHAR2(255 BYTE),
  "STATUS" VARCHAR2(255 BYTE),
  "REGION" VARCHAR2(255 BYTE)
)
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- View structure for V_APPROVED_GUIDES
-- ----------------------------
CREATE OR REPLACE VIEW "TRAVEL"."V_APPROVED_GUIDES" AS SELECT 
    g.id,
    g.title,
    -- 作者信息
    u.id AS user_id,
    u.nickname AS nickname,
    u.avatar AS avatar,
    -- 审核状态信息
    gas.name AS audit_status,
    -- 攻略内容基本信息（不含统计字段）
    g.cover AS cover,
    g.create_time,
    g.content AS content
FROM 
    guides g
JOIN 
    users u ON g.users_id = u.id
JOIN 
    guides_audit_status gas ON g.guides_audit_status_id = gas.id
WHERE 
    gas.name = 'approved';

-- ----------------------------
-- View structure for V_GUIDES_COMMENTS_DETAILS
-- ----------------------------
CREATE OR REPLACE VIEW "TRAVEL"."V_GUIDES_COMMENTS_DETAILS" AS SELECT 
    gc.ID,
    gc.GUIDES_ID,
    gc.USERS_ID,
    gc.CONTENT,
    gc.CREATE_TIME,
    u.NICKNAME,
    u.AVATAR
FROM 
    GUIDES_COMMENTS gc
JOIN 
    USERS u ON gc.USERS_ID = u.ID;

-- ----------------------------
-- View structure for V_GUIDE_DETAILS
-- ----------------------------
CREATE OR REPLACE VIEW "TRAVEL"."V_GUIDE_DETAILS" AS SELECT 
    g.id,
    g.title,
    g.content,
    g.create_time,
    g.audit_time,
    gas.name AS audit_status,
    u.id AS user_id,
    u.nickname AS nickname,
    u.avatar AS avatar,
    g.cover AS cover,
    -- 使用函数获取统计数量
    get_guide_likes_count(g.id) AS likes_count,
    get_guide_favorites_count(g.id) AS favorites_count,
    get_guide_comments_count(g.id) AS comments_count
FROM 
    guides g
JOIN 
    users u ON g.users_id = u.id
JOIN 
    guides_audit_status gas ON g.guides_audit_status_id = gas.id;

-- ----------------------------
-- View structure for V_PENDING_GUIDES
-- ----------------------------
CREATE OR REPLACE VIEW "TRAVEL"."V_PENDING_GUIDES" AS SELECT 
    g.id,
    g.title,
    -- 作者信息
    u.id AS user_id,
    u.nickname AS nickname,
    u.avatar AS avatar,
    -- 审核状态信息
    gas.name AS audit_status,
    -- 攻略内容基本信息（不含统计字段）
    g.cover AS cover,
    g.create_time,
    g.content AS content
FROM 
    guides g
JOIN 
    users u ON g.users_id = u.id
JOIN 
    guides_audit_status gas ON g.guides_audit_status_id = gas.id
WHERE 
    gas.name = 'pending';

-- ----------------------------
-- View structure for V_REJECTED_GUIDES
-- ----------------------------
CREATE OR REPLACE VIEW "TRAVEL"."V_REJECTED_GUIDES" AS SELECT 
    g.id,
    g.title,
    -- 作者信息
    u.id AS user_id,
    u.nickname AS nickname,
    u.avatar AS avatar,
    -- 审核状态信息
    gas.name AS audit_status,
    -- 攻略内容基本信息（不含统计字段）
    g.cover AS cover,
    g.create_time,
    g.content AS content
FROM 
    guides g
JOIN 
    users u ON g.users_id = u.id
JOIN 
    guides_audit_status gas ON g.guides_audit_status_id = gas.id
WHERE 
    gas.name = 'rejected';

-- ----------------------------
-- View structure for V_USERS_VISITS
-- ----------------------------
CREATE OR REPLACE VIEW "TRAVEL"."V_USERS_VISITS" AS SELECT 
    u.ID AS USER_ID,
    u.AVATAR,
    u.ACCOUNT,
    u.NICKNAME,
    l.CREATE_TIME AS LOGIN_TIME,
    l.IP,
    l.REGION,
    l.STATUS
FROM 
    users u
JOIN 
    visits l ON u.ID = l.USERS_ID;

-- ----------------------------
-- View structure for V_USER_DETAILS
-- ----------------------------
CREATE OR REPLACE VIEW "TRAVEL"."V_USER_DETAILS" AS SELECT 
    u.id,
    u.account,
    u.nickname,
    u.avatar,
    r.name AS role,
    us.name AS status,
    u.create_time,
    -- 用户收藏的攻略数量
    get_user_favorites_count(u.id) AS favorites_given_count,
    -- 用户发布的攻略数量
    get_user_guides_count(u.id) AS guides_published_count,
    -- 用户获得的点赞数量
    get_user_total_likes(u.id) AS likes_received_count,
    -- 用户获得的收藏数量
    get_user_total_favorites(u.id) AS favorites_received_count
FROM 
    users u
JOIN 
    roles r ON u.roles_id = r.id
JOIN 
    users_status us ON u.users_status_id = us.id;

-- ----------------------------
-- Function structure for GET_GUIDE_COMMENTS_COUNT
-- ----------------------------
CREATE OR REPLACE
FUNCTION "TRAVEL"."GET_GUIDE_COMMENTS_COUNT" AS
BEGIN
    SELECT COUNT(*)
    INTO v_comments_count
    FROM guides_comments
    WHERE guides_id = p_guide_id;
    
    RETURN v_comments_count;
EXCEPTION
    WHEN OTHERS THEN
        RETURN 0;
END get_guide_comments_count;
/

-- ----------------------------
-- Function structure for GET_GUIDE_FAVORITES_COUNT
-- ----------------------------
CREATE OR REPLACE
FUNCTION "TRAVEL"."GET_GUIDE_FAVORITES_COUNT" AS
BEGIN
    SELECT COUNT(*)
    INTO v_favorites_count
    FROM guides_favorites
    WHERE guides_id = p_guide_id;
    
    RETURN v_favorites_count;
EXCEPTION
    WHEN OTHERS THEN
        RETURN 0;
END get_guide_favorites_count;
/

-- ----------------------------
-- Function structure for GET_GUIDE_LIKES_COUNT
-- ----------------------------
CREATE OR REPLACE
FUNCTION "TRAVEL"."GET_GUIDE_LIKES_COUNT" AS
BEGIN
    SELECT COUNT(*)
    INTO v_likes_count
    FROM guides_likes
    WHERE guides_id = p_guide_id;
    
    RETURN v_likes_count;
EXCEPTION
    WHEN OTHERS THEN
        RETURN 0;
END get_guide_likes_count;
/

-- ----------------------------
-- Function structure for GET_USER_FAVORITES_COUNT
-- ----------------------------
CREATE OR REPLACE
FUNCTION "TRAVEL"."GET_USER_FAVORITES_COUNT" AS
BEGIN
    SELECT COUNT(*)
    INTO v_favorites_count
    FROM guides_favorites gf
    JOIN guides g ON gf.guides_id = g.id
    WHERE gf.users_id = p_user_id
    AND g.users_id <> p_user_id; -- 排除收藏自己的攻略
    
    RETURN v_favorites_count;
EXCEPTION
    WHEN OTHERS THEN
        RETURN 0;
END get_user_favorites_count;
/

-- ----------------------------
-- Function structure for GET_USER_GUIDES_COUNT
-- ----------------------------
CREATE OR REPLACE
FUNCTION "TRAVEL"."GET_USER_GUIDES_COUNT" AS
BEGIN
    SELECT COUNT(*)
    INTO v_guides_count
    FROM guides
    WHERE users_id = p_user_id;
    
    RETURN v_guides_count;
EXCEPTION
    WHEN OTHERS THEN
        RETURN 0;
END get_user_guides_count;
/

-- ----------------------------
-- Function structure for GET_USER_TOTAL_FAVORITES
-- ----------------------------
CREATE OR REPLACE
FUNCTION "TRAVEL"."GET_USER_TOTAL_FAVORITES" AS
BEGIN
    SELECT COUNT(*)
    INTO v_favorites_count
    FROM guides_favorites gf
    JOIN guides g ON gf.guides_id = g.id
    WHERE g.users_id = p_user_id;
    
    RETURN v_favorites_count;
EXCEPTION
    WHEN OTHERS THEN
        RETURN 0;
END get_user_total_favorites;
/

-- ----------------------------
-- Function structure for GET_USER_TOTAL_LIKES
-- ----------------------------
CREATE OR REPLACE
FUNCTION "TRAVEL"."GET_USER_TOTAL_LIKES" AS
BEGIN
    SELECT COALESCE(SUM(gl.likes), 0)
    INTO v_total_likes
    FROM (
        SELECT COUNT(*) AS likes
        FROM guides_likes gl
        JOIN guides g ON gl.guides_id = g.id
        WHERE g.users_id = p_user_id
        GROUP BY gl.guides_id
    ) gl;
    
    RETURN v_total_likes;
EXCEPTION
    WHEN OTHERS THEN
        RETURN 0;
END get_user_total_likes;
/

-- ----------------------------
-- Package structure for DBMS_CRYPTO
-- ----------------------------
CREATE OR REPLACE PACKAGE "TRAVEL"."DBMS_CRYPTO" AS
  FUNCTION HASH (data IN RAW, algorithm IN VARCHAR2 := 'SHA1') RETURN RAW;
END DBMS_CRYPTO;
CREATE OR REPLACE PACKAGE BODY "TRAVEL"."DBMS_CRYPTO" AS
  FUNCTION HASH (data IN RAW, algorithm IN VARCHAR2 := 'SHA1') RETURN RAW IS
    mac DBMS_CRYPTO_DESCRIPTOR;
    result RAW(32);
  BEGIN
    IF algorithm = 'SHA1' THEN
      mac := DBMS_CRYPTO.create_mac('HMAC', 'SHA1', 'your_secret_key_here');
    ELSE
      RAISE_APPLICATION_ERROR(-20001, 'Unsupported algorithm');
    END IF;
    DBMS_CRYPTO.mac_data(mac, data);
    result := DBMS_CRYPTO.mac_final(mac);
    RETURN result;
  END;
END DBMS_CRYPTO;
/

-- ----------------------------
-- Sequence structure for GUIDES_AUDIT_STATUS_ID_SEQ
-- ----------------------------
DROP SEQUENCE "TRAVEL"."GUIDES_AUDIT_STATUS_ID_SEQ";
CREATE SEQUENCE "TRAVEL"."GUIDES_AUDIT_STATUS_ID_SEQ" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 NOCACHE;

-- ----------------------------
-- Sequence structure for GUIDES_COMMENTS_ID_SEQ
-- ----------------------------
DROP SEQUENCE "TRAVEL"."GUIDES_COMMENTS_ID_SEQ";
CREATE SEQUENCE "TRAVEL"."GUIDES_COMMENTS_ID_SEQ" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 NOCACHE;

-- ----------------------------
-- Sequence structure for GUIDES_FAVORITES_ID_SEQ
-- ----------------------------
DROP SEQUENCE "TRAVEL"."GUIDES_FAVORITES_ID_SEQ";
CREATE SEQUENCE "TRAVEL"."GUIDES_FAVORITES_ID_SEQ" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 NOCACHE;

-- ----------------------------
-- Sequence structure for GUIDES_ID_SEQ
-- ----------------------------
DROP SEQUENCE "TRAVEL"."GUIDES_ID_SEQ";
CREATE SEQUENCE "TRAVEL"."GUIDES_ID_SEQ" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 NOCACHE;

-- ----------------------------
-- Sequence structure for GUIDES_LIKES_ID_SEQ
-- ----------------------------
DROP SEQUENCE "TRAVEL"."GUIDES_LIKES_ID_SEQ";
CREATE SEQUENCE "TRAVEL"."GUIDES_LIKES_ID_SEQ" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 NOCACHE;

-- ----------------------------
-- Sequence structure for ROLES_ID_SEQ
-- ----------------------------
DROP SEQUENCE "TRAVEL"."ROLES_ID_SEQ";
CREATE SEQUENCE "TRAVEL"."ROLES_ID_SEQ" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 NOCACHE;

-- ----------------------------
-- Sequence structure for USERS_ID_SEQ
-- ----------------------------
DROP SEQUENCE "TRAVEL"."USERS_ID_SEQ";
CREATE SEQUENCE "TRAVEL"."USERS_ID_SEQ" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 NOCACHE;

-- ----------------------------
-- Sequence structure for USERS_STATUS_ID_SEQ
-- ----------------------------
DROP SEQUENCE "TRAVEL"."USERS_STATUS_ID_SEQ";
CREATE SEQUENCE "TRAVEL"."USERS_STATUS_ID_SEQ" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 NOCACHE;

-- ----------------------------
-- Sequence structure for VISITS_ID_SEQ
-- ----------------------------
DROP SEQUENCE "TRAVEL"."VISITS_ID_SEQ";
CREATE SEQUENCE "TRAVEL"."VISITS_ID_SEQ" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 NOCACHE;

-- ----------------------------
-- Primary Key structure for table GUIDES
-- ----------------------------
ALTER TABLE "TRAVEL"."GUIDES" ADD CONSTRAINT "SYS_C0011450" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table GUIDES
-- ----------------------------
ALTER TABLE "TRAVEL"."GUIDES" ADD CONSTRAINT "SYS_C0011446" CHECK ("USERS_ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE "TRAVEL"."GUIDES" ADD CONSTRAINT "SYS_C0011447" CHECK ("TITLE" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE "TRAVEL"."GUIDES" ADD CONSTRAINT "SYS_C0011448" CHECK ("CONTENT" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE "TRAVEL"."GUIDES" ADD CONSTRAINT "SYS_C0011449" CHECK ("GUIDES_AUDIT_STATUS_ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Indexes structure for table GUIDES
-- ----------------------------
CREATE INDEX "TRAVEL"."IDX_GUIDES_AUDIT_TIME"
  ON "TRAVEL"."GUIDES" ("AUDIT_TIME" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);
CREATE INDEX "TRAVEL"."IDX_GUIDES_CREATE_TIME"
  ON "TRAVEL"."GUIDES" ("CREATE_TIME" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);
CREATE INDEX "TRAVEL"."IDX_GUIDES_STATUS_ID"
  ON "TRAVEL"."GUIDES" ("GUIDES_AUDIT_STATUS_ID" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);
CREATE INDEX "TRAVEL"."IDX_GUIDES_TITLE"
  ON "TRAVEL"."GUIDES" ("TITLE" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);
CREATE INDEX "TRAVEL"."IDX_GUIDES_USER_ID"
  ON "TRAVEL"."GUIDES" ("USERS_ID" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);
CREATE INDEX "TRAVEL"."IDX_GUIDES_USER_STATUS"
  ON "TRAVEL"."GUIDES" ("USERS_ID" ASC, "GUIDES_AUDIT_STATUS_ID" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);

-- ----------------------------
-- Triggers structure for table GUIDES
-- ----------------------------
CREATE TRIGGER "TRAVEL"."GUIDES_ID_TRIGGER" BEFORE INSERT ON "TRAVEL"."GUIDES" REFERENCING OLD AS "OLD" NEW AS "NEW" FOR EACH ROW 
BEGIN
    SELECT guides_id_seq.NEXTVAL INTO :NEW.id FROM DUAL;
END;
/
CREATE TRIGGER "TRAVEL"."TRG_CASCADE_DELETE_GUIDE" BEFORE DELETE ON "TRAVEL"."GUIDES" REFERENCING OLD AS "OLD" NEW AS "NEW" FOR EACH ROW 
BEGIN
    -- 删除该攻略的所有点赞记录
    DELETE FROM guides_likes 
    WHERE guides_id = :OLD.id;
    
    -- 删除该攻略的所有收藏记录
    DELETE FROM guides_favorites 
    WHERE guides_id = :OLD.id;
    
    -- 删除该攻略的所有评论记录
    DELETE FROM guides_comments 
    WHERE guides_id = :OLD.id;
    
    -- 注意：Oracle不需要显式提交，触发器中不允许COMMIT
    DBMS_OUTPUT.PUT_LINE('已级联删除攻略ID ' || :OLD.id || ' 的所有关联数据');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('删除攻略关联数据时出错: ' || SQLERRM);
        -- 抛出异常阻止攻略删除
        RAISE;
END trg_cascade_delete_guide;
/

-- ----------------------------
-- Primary Key structure for table GUIDES_AUDIT_STATUS
-- ----------------------------
ALTER TABLE "TRAVEL"."GUIDES_AUDIT_STATUS" ADD CONSTRAINT "SYS_C0011437" PRIMARY KEY ("ID");

-- ----------------------------
-- Uniques structure for table GUIDES_AUDIT_STATUS
-- ----------------------------
ALTER TABLE "TRAVEL"."GUIDES_AUDIT_STATUS" ADD CONSTRAINT "SYS_C0011438" UNIQUE ("NAME") NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table GUIDES_AUDIT_STATUS
-- ----------------------------
ALTER TABLE "TRAVEL"."GUIDES_AUDIT_STATUS" ADD CONSTRAINT "SYS_C0011436" CHECK ("NAME" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Triggers structure for table GUIDES_AUDIT_STATUS
-- ----------------------------
CREATE TRIGGER "TRAVEL"."GUIDES_AUDIT_STATUS_ID_TRIGGER" BEFORE INSERT ON "TRAVEL"."GUIDES_AUDIT_STATUS" REFERENCING OLD AS "OLD" NEW AS "NEW" FOR EACH ROW 
BEGIN
    SELECT guides_audit_status_id_seq.NEXTVAL INTO :NEW.id FROM DUAL;
END;
/

-- ----------------------------
-- Primary Key structure for table GUIDES_COMMENTS
-- ----------------------------
ALTER TABLE "TRAVEL"."GUIDES_COMMENTS" ADD CONSTRAINT "SYS_C0011466" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table GUIDES_COMMENTS
-- ----------------------------
ALTER TABLE "TRAVEL"."GUIDES_COMMENTS" ADD CONSTRAINT "SYS_C0011463" CHECK ("GUIDES_ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE "TRAVEL"."GUIDES_COMMENTS" ADD CONSTRAINT "SYS_C0011464" CHECK ("USERS_ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE "TRAVEL"."GUIDES_COMMENTS" ADD CONSTRAINT "SYS_C0011465" CHECK ("CONTENT" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Indexes structure for table GUIDES_COMMENTS
-- ----------------------------
CREATE INDEX "TRAVEL"."IDX_COMMENTS_CREATE_TIME"
  ON "TRAVEL"."GUIDES_COMMENTS" ("CREATE_TIME" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);
CREATE INDEX "TRAVEL"."IDX_COMMENTS_GUIDE_ID"
  ON "TRAVEL"."GUIDES_COMMENTS" ("GUIDES_ID" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);
CREATE INDEX "TRAVEL"."IDX_COMMENTS_USER_ID"
  ON "TRAVEL"."GUIDES_COMMENTS" ("USERS_ID" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);

-- ----------------------------
-- Triggers structure for table GUIDES_COMMENTS
-- ----------------------------
CREATE TRIGGER "TRAVEL"."GUIDES_COMMENTS_ID_TRIGGER" BEFORE INSERT ON "TRAVEL"."GUIDES_COMMENTS" REFERENCING OLD AS "OLD" NEW AS "NEW" FOR EACH ROW 
BEGIN
    SELECT guides_comments_id_seq.NEXTVAL INTO :NEW.id FROM DUAL;
END;
/

-- ----------------------------
-- Primary Key structure for table GUIDES_FAVORITES
-- ----------------------------
ALTER TABLE "TRAVEL"."GUIDES_FAVORITES" ADD CONSTRAINT "SYS_C0011460" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table GUIDES_FAVORITES
-- ----------------------------
ALTER TABLE "TRAVEL"."GUIDES_FAVORITES" ADD CONSTRAINT "SYS_C0011458" CHECK ("GUIDES_ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE "TRAVEL"."GUIDES_FAVORITES" ADD CONSTRAINT "SYS_C0011459" CHECK ("USERS_ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Indexes structure for table GUIDES_FAVORITES
-- ----------------------------
CREATE INDEX "TRAVEL"."IDX_FAVORITES_GUIDE_ID"
  ON "TRAVEL"."GUIDES_FAVORITES" ("GUIDES_ID" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);
CREATE INDEX "TRAVEL"."IDX_FAVORITES_USER_GUIDE"
  ON "TRAVEL"."GUIDES_FAVORITES" ("USERS_ID" ASC, "GUIDES_ID" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);
CREATE INDEX "TRAVEL"."IDX_FAVORITES_USER_ID"
  ON "TRAVEL"."GUIDES_FAVORITES" ("USERS_ID" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);

-- ----------------------------
-- Triggers structure for table GUIDES_FAVORITES
-- ----------------------------
CREATE TRIGGER "TRAVEL"."GUIDES_FAVORITES_ID_TRIGGER" BEFORE INSERT ON "TRAVEL"."GUIDES_FAVORITES" REFERENCING OLD AS "OLD" NEW AS "NEW" FOR EACH ROW 
BEGIN
    SELECT guides_favorites_id_seq.NEXTVAL INTO :NEW.id FROM DUAL;
END;
/

-- ----------------------------
-- Primary Key structure for table GUIDES_LIKES
-- ----------------------------
ALTER TABLE "TRAVEL"."GUIDES_LIKES" ADD CONSTRAINT "SYS_C0011455" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table GUIDES_LIKES
-- ----------------------------
ALTER TABLE "TRAVEL"."GUIDES_LIKES" ADD CONSTRAINT "SYS_C0011453" CHECK ("GUIDES_ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE "TRAVEL"."GUIDES_LIKES" ADD CONSTRAINT "SYS_C0011454" CHECK ("USERS_ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Indexes structure for table GUIDES_LIKES
-- ----------------------------
CREATE INDEX "TRAVEL"."IDX_LIKES_GUIDE_ID"
  ON "TRAVEL"."GUIDES_LIKES" ("GUIDES_ID" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);
CREATE INDEX "TRAVEL"."IDX_LIKES_USER_GUIDE"
  ON "TRAVEL"."GUIDES_LIKES" ("USERS_ID" ASC, "GUIDES_ID" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);
CREATE INDEX "TRAVEL"."IDX_LIKES_USER_ID"
  ON "TRAVEL"."GUIDES_LIKES" ("USERS_ID" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);

-- ----------------------------
-- Triggers structure for table GUIDES_LIKES
-- ----------------------------
CREATE TRIGGER "TRAVEL"."GUIDES_LIKES_ID_TRIGGER" BEFORE INSERT ON "TRAVEL"."GUIDES_LIKES" REFERENCING OLD AS "OLD" NEW AS "NEW" FOR EACH ROW 
BEGIN
    SELECT guides_likes_id_seq.NEXTVAL INTO :NEW.id FROM DUAL;
END;
/

-- ----------------------------
-- Primary Key structure for table ROLES
-- ----------------------------
ALTER TABLE "TRAVEL"."ROLES" ADD CONSTRAINT "SYS_C0011431" PRIMARY KEY ("ID");

-- ----------------------------
-- Uniques structure for table ROLES
-- ----------------------------
ALTER TABLE "TRAVEL"."ROLES" ADD CONSTRAINT "SYS_C0011432" UNIQUE ("NAME") NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table ROLES
-- ----------------------------
ALTER TABLE "TRAVEL"."ROLES" ADD CONSTRAINT "SYS_C0011430" CHECK ("NAME" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Triggers structure for table ROLES
-- ----------------------------
CREATE TRIGGER "TRAVEL"."ROLES_ID_TRIGGER" BEFORE INSERT ON "TRAVEL"."ROLES" REFERENCING OLD AS "OLD" NEW AS "NEW" FOR EACH ROW 
BEGIN
    SELECT roles_id_seq.NEXTVAL INTO :NEW.id FROM DUAL;
END;
/

-- ----------------------------
-- Primary Key structure for table USERS
-- ----------------------------
ALTER TABLE "TRAVEL"."USERS" ADD CONSTRAINT "SYS_C0011442" PRIMARY KEY ("ID");

-- ----------------------------
-- Uniques structure for table USERS
-- ----------------------------
ALTER TABLE "TRAVEL"."USERS" ADD CONSTRAINT "SYS_C0011443" UNIQUE ("ACCOUNT") NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table USERS
-- ----------------------------
ALTER TABLE "TRAVEL"."USERS" ADD CONSTRAINT "SYS_C0011439" CHECK ("ACCOUNT" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE "TRAVEL"."USERS" ADD CONSTRAINT "SYS_C0011440" CHECK ("PASSWORD" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE "TRAVEL"."USERS" ADD CONSTRAINT "SYS_C0011441" CHECK ("ROLES_ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Indexes structure for table USERS
-- ----------------------------
CREATE INDEX "TRAVEL"."IDX_USERS_NICKNAME"
  ON "TRAVEL"."USERS" ("NICKNAME" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);
CREATE INDEX "TRAVEL"."IDX_USERS_ROLES_ID"
  ON "TRAVEL"."USERS" ("ROLES_ID" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);
CREATE INDEX "TRAVEL"."IDX_USERS_STATUS_ID"
  ON "TRAVEL"."USERS" ("USERS_STATUS_ID" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);

-- ----------------------------
-- Triggers structure for table USERS
-- ----------------------------
CREATE TRIGGER "TRAVEL"."USERS_ID_TRIGGER" BEFORE INSERT ON "TRAVEL"."USERS" REFERENCING OLD AS "OLD" NEW AS "NEW" FOR EACH ROW 
BEGIN
    SELECT users_id_seq.NEXTVAL INTO :NEW.id FROM DUAL;
END;
/

-- ----------------------------
-- Primary Key structure for table USERS_STATUS
-- ----------------------------
ALTER TABLE "TRAVEL"."USERS_STATUS" ADD CONSTRAINT "SYS_C0011434" PRIMARY KEY ("ID");

-- ----------------------------
-- Uniques structure for table USERS_STATUS
-- ----------------------------
ALTER TABLE "TRAVEL"."USERS_STATUS" ADD CONSTRAINT "SYS_C0011435" UNIQUE ("NAME") NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table USERS_STATUS
-- ----------------------------
ALTER TABLE "TRAVEL"."USERS_STATUS" ADD CONSTRAINT "SYS_C0011433" CHECK ("NAME" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Triggers structure for table USERS_STATUS
-- ----------------------------
CREATE TRIGGER "TRAVEL"."USERS_STATUS_ID_TRIGGER" BEFORE INSERT ON "TRAVEL"."USERS_STATUS" REFERENCING OLD AS "OLD" NEW AS "NEW" FOR EACH ROW 
BEGIN
    SELECT users_status_id_seq.NEXTVAL INTO :NEW.id FROM DUAL;
END;
/

-- ----------------------------
-- Primary Key structure for table VISITS
-- ----------------------------
ALTER TABLE "TRAVEL"."VISITS" ADD CONSTRAINT "SYS_C0011470" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table VISITS
-- ----------------------------
ALTER TABLE "TRAVEL"."VISITS" ADD CONSTRAINT "SYS_C0011469" CHECK ("USERS_ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Indexes structure for table VISITS
-- ----------------------------
CREATE INDEX "TRAVEL"."IDX_VISITS_CREATE_TIME"
  ON "TRAVEL"."VISITS" ("CREATE_TIME" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);
CREATE INDEX "TRAVEL"."IDX_VISITS_USER_ID"
  ON "TRAVEL"."VISITS" ("USERS_ID" ASC)
  LOGGING
  VISIBLE
PCTFREE 10
INITRANS 2
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
);

-- ----------------------------
-- Triggers structure for table VISITS
-- ----------------------------
CREATE TRIGGER "TRAVEL"."VISITS_ID_TRIGGER" BEFORE INSERT ON "TRAVEL"."VISITS" REFERENCING OLD AS "OLD" NEW AS "NEW" FOR EACH ROW 
BEGIN
    SELECT visits_id_seq.NEXTVAL INTO :NEW.id FROM DUAL;
END;
/

-- ----------------------------
-- Foreign Keys structure for table GUIDES
-- ----------------------------

-- ----------------------------
-- Foreign Keys structure for table GUIDES_COMMENTS
-- ----------------------------

-- ----------------------------
-- Foreign Keys structure for table GUIDES_FAVORITES
-- ----------------------------

-- ----------------------------
-- Foreign Keys structure for table GUIDES_LIKES
-- ----------------------------

-- ----------------------------
-- Foreign Keys structure for table USERS
-- ----------------------------

-- ----------------------------
-- Foreign Keys structure for table VISITS
-- ----------------------------
