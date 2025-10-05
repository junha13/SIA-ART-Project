-- 처음 실행때만 주석풀고 실행
-- CREATE EXTENSION postgis;

-- 원하는 스키마 접속
-- SET search_path TO public;

-- 테이블 드랍문
-- DROP TABLE IF EXISTS
-- tb_appreciation,
-- tb_artwork,
-- tb_board,
-- tb_class,
-- tb_class_category,
-- tb_comment,
-- tb_comment_list,
-- tb_detail_keyword,
-- tb_class_keyword,
-- tb_post_recommend,
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

-- 2. tb_class (클래스 메인 정보 - 모든 컬럼 추가 버전)
CREATE TABLE tb_class (
    -- 기본 정보
    class_number          SERIAL PRIMARY KEY,
    class_name            VARCHAR(100) NOT NULL,
    subtitle              VARCHAR(255),
    class_description     TEXT,
    class_image           TEXT,
    user_number           INTEGER, -- 강사(사용자)의 ID
    instructor            VARCHAR(50), -- 강사 이름
    instructor_avatar     TEXT, -- 강사 프로필 이미지 URL

    -- 상세 정보
    level                 VARCHAR(50), -- 예: 초급, 중급, 고급
    capacity              INTEGER DEFAULT 1, -- 수강 정원
    price                 INTEGER DEFAULT 0, -- 수강료
    materials_included    BOOLEAN DEFAULT FALSE, -- 재료비 포함 여부
    curriculum            TEXT, -- 커리큘럼 (줄바꿈 문자로 주차별 내용 구분)

    -- 일정/기간 정보
    start_date            DATE,
    end_date              DATE,
    class_time            VARCHAR(50), -- 예: 14:00 ~ 16:00
    days                  VARCHAR(50), -- 예: 토, 일
    duration_weeks        INTEGER DEFAULT 1,
    is_daily              BOOLEAN DEFAULT FALSE,

    -- 외래 키 및 공간 데이터
    class_category_number INTEGER REFERENCES tb_class_category(class_category_number),
    location              GEOGRAPHY(Point, 4326)
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
	 view_count   INTEGER ,
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

-- 게시글 추천 관련
CREATE TABLE tb_post_recommend (
    post_number   INTEGER NOT NULL,
    user_number   INTEGER NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),

    -- 복합 기본키 (중복 추천 방지)
    CONSTRAINT tb_post_recommend_pk
        PRIMARY KEY (post_number, user_number),

    -- FK: 게시글 삭제 시 해당 추천도 함께 삭제
    CONSTRAINT tb_post_recommend_post_fk
        FOREIGN KEY (post_number)
        REFERENCES tb_post(post_number)
        ON DELETE CASCADE,

    -- FK: 유저 삭제 시 해당 유저의 추천도 함께 삭제
    CONSTRAINT tb_post_recommend_user_fk
        FOREIGN KEY (user_number)
        REFERENCES tb_user(user_number)
        ON DELETE CASCADE
);

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

------------------------------------------------------------------------------------------------
-- 데이터 연결

-- tb_board에 (region_name × board_category_name × post_category_name) 데카르트 곱으로 삽입
INSERT INTO tb_board (region_name, board_category_name, post_category_name)
SELECT r.region_name, a.board_category_name, p.post_category_name
FROM
  unnest(ARRAY[
    '서울특별시','부산광역시','대구광역시','인천광역시','광주광역시',
    '대전광역시','울산광역시','세종특별자치시','경기도','강원특별자치도',
    '충청북도','충청남도','전라북도특별자치도','전라남도','경상북도','경상남도','제주특별자치도'
  ]) AS r(region_name)
, unnest(ARRAY[
    '순수 미술', '디자인 & 공예', '사진 & 영상', '디지털 아트', '공연 예술', '확장 예술'
  ]) AS a(board_category_name)
, unnest(ARRAY[
    '잡담','구인구직','정보'
  ]) AS p(post_category_name);

-- -- user 임시 1번
-- insert into tb_user (id)
-- values (1)

읽는
-- role 부분 insert
INSERT INTO tb_role (role_number, role_name)
VALUES
    (1, '일반 사용자'),
    (2, '예술가');


	
/* * --------------------------------------------------
 * class 부분 insert - 순서가 매우 중요!
 * --------------------------------------------------
*/

-- STEP 1: 부모 테이블인 카테고리 정보 먼저 삽입
INSERT INTO tb_class_category (class_category_name) VALUES
('순수 미술'), ('디자인 & 공예'), ('사진 & 영상'),
('디지털 아트'), ('공연 예술'), ('확장 예술');

-- STEP 2: 카테고리를 참조하는 상세 키워드 정보 삽입
INSERT INTO tb_detail_keyword (keyword_name, main_category_number) VALUES
('회화', 1), ('조각', 1), ('드로잉', 1), ('판화', 1), ('서예', 1),
('디자인', 2), ('공예', 2), ('건축', 2), ('메이크업', 2),
('사진', 3), ('영화', 3), ('애니메이션', 3), ('비디오 아트', 3),
('웹툰', 4), ('게임 아트', 4), ('인터랙티브 아트', 4),
('음악', 5), ('무용', 5), ('연극', 5), ('뮤지컬', 5),
('설치', 6), ('문학', 6), ('요리', 6), ('조향', 6);

-- STEP 3: 이제 카테고리(ID=1)가 존재하므로 클래스 정보 삽입 가능
INSERT INTO tb_class (
    class_name, subtitle, class_description, class_image, user_number, instructor, instructor_avatar,
    level, capacity, price, materials_included, curriculum,
    start_date, end_date, class_time, days, duration_weeks,
    class_category_number, location
) VALUES (
    '유화 마스터',
    '전문가를 위한 고급 유화 기법',
    '이 강좌는 유화의 깊이 있는 이해를 돕고, 자신만의 독창적인 스타일을 개발할 수 있도록 설계되었습니다. 색채 이론부터 복잡한 질감 표현까지, 전문적인 유화 기법을 마스터하게 됩니다.',
    'https://images.unsplash.com/photo-1579783902614-a3fb3927b6a5?q=80&w=1944&auto=format&fit=crop',
    1,
    '허지서',
    'https://images.unsplash.com/photo-1580489944761-15a19d654956?q=80&w=1961&auto=format&fit=crop',
    '고급', 5, 200000, TRUE,
    '1주차: 색채의 이해와 고급 혼합 기법\n2주차: 다양한 질감 표현과 나이프 페인팅\n3주차: 인물화와 구성의 원리\n4주차: 개인 프로젝트 및 작품 발표',
    '2025-11-01', '2025-11-22', '14:00 ~ 16:00', '매주 토요일', 4,
    1, -- '순수 미술' 카테고리 참조
    ST_SetSRID(ST_MakePoint(126.886369, 37.485294), 4326)
);

-- (선택) 방금 만든 클래스와 키워드를 연결
-- '유화 마스터' 클래스(class_number=1)와 '회화' 키워드(keyword_number=1) 연결
INSERT INTO tb_class_keyword (class_number, keyword_number) VALUES (1, 1);