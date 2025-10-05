-- 처음 실행때만 주석풀고 실행
CREATE EXTENSION postgis;

-- 원하는 스키마 접속
-- SET search_path TO public;

1
-- 테이블 드랍문
-- DROP TABLE IF EXISTS
-- tb_appreciation,
-- tb_artwork,
-- tb_board,
-- tb_class,
-- tb_class_category,
-- tb_comment,
-- tb_comment_list,
-- tb_follow,
-- tb_interests,
-- tb_local_art,
-- tb_post,
-- tb_product,
-- tb_regional_art_category,
-- tb_role,
-- tb_shipping_address,
-- tb_user,
-- tb_work_category
-- CASCADE;

CREATE TABLE tb_appreciation 
    ( 
     appreciation_number      SERIAL PRIMARY KEY, 
     appreciation_title    	  VARCHAR (50) , 
     appreciation_description DATE , 
     image                    TEXT , 
     local_art_number         INTEGER , 
     user_number              INTEGER
    ) 
;

CREATE TABLE tb_artwork 
    ( 
     artwork_number         SERIAL PRIMARY KEY , 
     artwork_title          VARCHAR (50) , 
     artwork_description    TEXT , 
     artwork_image          TEXT , 
     year_of_artwork        INTEGER , 
     location               geography(Point, 4326) , 
     user_number            INTEGER , 
     work_category_number   INTEGER 
    ) 
;

CREATE TABLE tb_board 
    ( 
     board_number        SERIAL PRIMARY KEY , 
     region_name         VARCHAR (50) , 
     board_category_name VARCHAR (50) , 
     post_category_name  VARCHAR (50) 
    ) 
;

-- 1. tb_class_category (메인 카테고리)
CREATE TABLE tb_class_category (
    class_category_number     SERIAL PRIMARY KEY,
    class_category_name       VARCHAR (50) NOT NULL UNIQUE
);

-- 2. tb_class (클래스 메인 정보)
CREATE TABLE tb_class (
     -- 식별자 (PRIMARY KEY)
    class_number           SERIAL PRIMARY KEY,

    -- 핵심 정보 (이름, 설명, 이미지)
	
    class_name             VARCHAR (50),
    class_description      TEXT,
    class_image            TEXT,

    -- 일정/기간 상세 정보 (새로 추가된 필드)
    start_date             DATE NOT NULL DEFAULT '2000-01-01',
    end_date               DATE NOT NULL DEFAULT '2000-01-01',
    class_time             VARCHAR (50),
    duration_weeks         INTEGER DEFAULT 1,
    is_daily               BOOLEAN DEFAULT FALSE,

    -- 외래 키 및 공간 데이터 (참조/특수 데이터 타입)
    user_number            INTEGER,
    class_category_number  INTEGER,
    location               geography(Point, 4326)
);

-- 3. tb_detail_keyword (상세 키워드: 회화, 조각 등)
CREATE TABLE tb_detail_keyword (
    keyword_number           SERIAL PRIMARY KEY,
    keyword_name             VARCHAR (50) NOT NULL,
    main_category_number     INTEGER      NOT NULL,
    
    FOREIGN KEY (main_category_number) REFERENCES tb_class_category (class_category_number)
);

-- 4. tb_class_keyword (클래스-키워드 연결 테이블 - N:M)
CREATE TABLE tb_class_keyword (
    class_number             INTEGER NOT NULL REFERENCES tb_class (class_number),
    keyword_number           INTEGER NOT NULL REFERENCES tb_detail_keyword (keyword_number),
    PRIMARY KEY (class_number, keyword_number)
);


INSERT INTO tb_class_category (class_category_name) VALUES
('순수 미술'),
('디자인 & 공예'),
('사진 & 영상'),
('디지털 아트'),
('공연 예술'),
('확장 예술');

INSERT INTO tb_detail_keyword (keyword_name, main_category_number)
VALUES
-- 1. 순수 미술
('회화', 1), ('조각', 1), ('드로잉', 1), ('판화', 1), ('서예', 1),

-- 2. 디자인 & 공예
('디자인', 2), ('공예', 2), ('건축', 2), ('메이크업', 2),

-- 3. 사진 & 영상
('사진', 3), ('영화', 3), ('애니메이션', 3), ('비디오 아트', 3),

-- 4. 디지털 아트
('웹툰', 4), ('게임 아트', 4), ('인터랙티브 아트', 4),

-- 5. 공연 예술
('음악', 5), ('무용', 5), ('연극', 5), ('뮤지컬', 5),

-- 6. 확장 예술
('설치', 6), ('문학', 6), ('요리', 6), ('조향', 6);



CREATE TABLE tb_comment 
    ( 
     comment_number     SERIAL PRIMARY KEY , 
     comment_content    VARCHAR (250) , 
     create_at          TIMESTAMP , 
     user_number        INTEGER 
    ) 
;


CREATE TABLE tb_comment_list 
    ( 
     comment_list_category_number    SERIAL PRIMARY KEY , 
     post_number                     INTEGER , 
     comment_number                  INTEGER 
    ) 
;

CREATE TABLE tb_follow 
    ( 
     user_number  INTEGER NOT NULL, 
     user_number1 INTEGER NOT NULL,
	 PRIMARY KEY (user_number, user_number1)
    ) 
;

CREATE TABLE tb_interests 
    ( 
     product_number INTEGER NOT NULL, 
     user_number    INTEGER NOT NULL,
	 PRIMARY KEY (product_number, user_number)
    ) 
;

CREATE TABLE tb_local_art 
    ( 
     local_art_number             SERIAL PRIMARY KEY , 
     local_art_name               VARCHAR (50) , 
     local_art_description        TEXT , 
     local_art_image              TEXT , 
     regional_art_category_number INTEGER , 
     location                     geography(Point, 4326) 
    ) 
;

CREATE TABLE tb_post 
    ( 
     post_number  SERIAL PRIMARY KEY , 
     post_title   VARCHAR (50) , 
     post_content TEXT , 
     create_at    TIMESTAMP , 
     user_number  INTEGER , 
     board_number INTEGER 
    ) 
;

CREATE TABLE tb_product 
    ( 
     product_number         SERIAL PRIMARY KEY , 
     product_name           VARCHAR (50) , 
     product_description    TEXT , 
     product_image          TEXT , 
     product_price          NUMERIC (10,0) , 
     view_count             INTEGER , 
     create_at              TIMESTAMP , 
     location               geography(Point, 4326) , 
     user_number            INTEGER , 
     work_category_number   INTEGER 
    ) 
;

CREATE TABLE tb_regional_art_category 
    ( 
      regional_art_category_number  SERIAL PRIMARY KEY , 
      regional_art_category_name    VARCHAR (50) 
    ) 
;

CREATE TABLE tb_role 
    ( 
      role_number SERIAL PRIMARY KEY , 
      role_name   VARCHAR (50) 
    ) 
;

CREATE TABLE tb_shipping_address 
    ( 
     shipping_address_number SERIAL PRIMARY KEY , 
     recipient_name             INTEGER , 
     postal_code                VARCHAR (50) , 
     mobile                     VARCHAR (50) , 
     address_detail             VARCHAR (200) , 
     is_default_address_YN      CHAR (1) , 
     road_address               VARCHAR (50) , 
     location                   geography(Point, 4326) , 
     user_number                INTEGER 
    ) 
;

CREATE TABLE tb_user
    ( 
     user_number       SERIAL PRIMARY KEY , 
     id                VARCHAR (50) , 
     password          VARCHAR (250) , 
     user_name         VARCHAR (50) , 
     activity_name     VARCHAR (50) , 
     mobile            VARCHAR (50) , 
     email             VARCHAR (100) , 
     birthday          DATE , 
     role_number       INTEGER , 
     location          geography(Point, 4326) , 
     interests_list    VARCHAR (100) 
    ) 
;

CREATE TABLE tb_work_category
    ( 
      work_category_number SERIAL PRIMARY KEY , 
      work_category_name   VARCHAR (50) 
    ) 
;

ALTER TABLE tb_post 
    ADD CONSTRAINT tb_board_FK FOREIGN KEY 
    ( 
     board_number
    ) 
    REFERENCES tb_board 
    ( 
     board_number 
    ) 
;

ALTER TABLE tb_class 
    ADD CONSTRAINT tb_class_category_FK FOREIGN KEY 
    ( 
     class_category_number
    ) 
    REFERENCES tb_class_category 
    ( 
     class_category_number
    ) 
;

ALTER TABLE tb_comment_list 
    ADD CONSTRAINT tb_comment_FK FOREIGN KEY 
    ( 
     comment_number
    ) 
    REFERENCES tb_comment 
    ( 
     comment_number
    ) 
;

ALTER TABLE tb_appreciation 
    ADD CONSTRAINT tb_local_art_FK FOREIGN KEY 
    ( 
     local_art_number
    ) 
    REFERENCES tb_local_art 
    ( 
     local_art_number
    ) 
;

ALTER TABLE tb_comment_list 
    ADD CONSTRAINT tb_post_FK FOREIGN KEY 
    ( 
     post_number
    ) 
    REFERENCES tb_post 
    ( 
     post_number
    ) 
;

ALTER TABLE tb_interests 
    ADD CONSTRAINT tb_product_FK FOREIGN KEY 
    ( 
     product_number
    ) 
    REFERENCES tb_product 
    ( 
     product_number
    ) 
;

ALTER TABLE tb_local_art 
    ADD CONSTRAINT tb_regional_art_category_FK FOREIGN KEY 
    ( 
     regional_art_category_number
    ) 
    REFERENCES tb_regional_art_category 
    ( 
     regional_art_category_number
    ) 
;

ALTER TABLE tb_user 
    ADD CONSTRAINT tb_role_FK FOREIGN KEY 
    ( 
     role_number
    ) 
    REFERENCES tb_role 
    ( 
     role_number
    ) 
;

ALTER TABLE tb_artwork 
    ADD CONSTRAINT tb_user_FK FOREIGN KEY 
    ( 
     user_number
    ) 
    REFERENCES tb_user
    ( 
     user_number
    ) 
;

ALTER TABLE tb_post 
    ADD CONSTRAINT tb_user_FKv1 FOREIGN KEY 
    ( 
     user_number
    ) 
    REFERENCES tb_user 
    ( 
     user_number
    ) 
;

ALTER TABLE tb_appreciation 
    ADD CONSTRAINT tb_user_FKv10 FOREIGN KEY 
    ( 
     user_number
    ) 
    REFERENCES tb_user 
    ( 
     user_number
    ) 
;

ALTER TABLE tb_product 
    ADD CONSTRAINT user_FKv2 FOREIGN KEY 
    ( 
     user_number
    ) 
    REFERENCES tb_user
    ( 
     user_number
    ) 
;

ALTER TABLE tb_follow 
    ADD CONSTRAINT tb_user_FKv3 FOREIGN KEY 
    ( 
     user_number1
    ) 
    REFERENCES tb_user
    ( 
     user_number
    ) 
;

ALTER TABLE tb_shipping_address 
    ADD CONSTRAINT tb_user_FKv4 FOREIGN KEY 
    ( 
     user_number
    ) 
    REFERENCES tb_user
    ( 
     user_number
    ) 
;

ALTER TABLE tb_follow 
    ADD CONSTRAINT tb_user_FKv5 FOREIGN KEY 
    ( 
     user_number
    ) 
    REFERENCES tb_user
    ( 
     user_number
    ) 
;

ALTER TABLE tb_comment
    ADD CONSTRAINT tb_user_FKv6 FOREIGN KEY 
    ( 
     user_number
    ) 
    REFERENCES tb_user 
    ( 
     user_number
    ) 
;

ALTER TABLE tb_interests 
    ADD CONSTRAINT tb_user_FKv7 FOREIGN KEY 
    ( 
     user_number
    ) 
    REFERENCES tb_user 
    ( 
     user_number
    ) 
;

ALTER TABLE tb_class 
    ADD CONSTRAINT tb_user_FKv8 FOREIGN KEY 
    ( 
     user_number
    ) 
    REFERENCES tb_user
    ( 
     user_number
    ) 
;

ALTER TABLE tb_artwork 
    ADD CONSTRAINT tb_work_category__FK FOREIGN KEY 
    ( 
     work_category_number
    ) 
    REFERENCES tb_work_category 
    ( 
     work_category_number
    ) 
;

ALTER TABLE tb_product 
    ADD CONSTRAINT tb_work_category__FKv2 FOREIGN KEY 
    ( 
     work_category_number
    ) 
    REFERENCES tb_work_category
    ( 
     work_category_number
    ) 
;



-- Oracle SQL Developer Data Modeler 요약 보고서: 
-- 
-- CREATE TABLE                            17
-- CREATE INDEX                             0
-- ALTER TABLE                             36
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
