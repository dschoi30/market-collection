select * from item_category;
-- ROOT 카테고리
insert into item_category(category_name, parent_id) values ('채소', 0);
insert into item_category(category_name, parent_id) values ('과일 · 견과 · 쌀', 0);
insert into item_category(category_name, parent_id) values ('수산 · 해산 · 건어물', 0);
insert into item_category(category_name, parent_id) values ('정육 · 계란', 0);
insert into item_category(category_name, parent_id) values ('국 · 반찬 · 메인요리', 0);
insert into item_category(category_name, parent_id) values ('샐러드 · 간편식', 0);
insert into item_category(category_name, parent_id) values ('면 · 양념 · 오일', 0);
insert into item_category(category_name, parent_id) values ('생수 · 음료 · 우유 · 커피', 0);
insert into item_category(category_name, parent_id) values ('간식 · 과자 · 떡', 0);
insert into item_category(category_name, parent_id) values ('배이커리 · 치즈 · 델리', 0);
insert into item_category(category_name, parent_id) values ('건강식품', 0);
insert into item_category(category_name, parent_id) values ('와인', 0);
insert into item_category(category_name, parent_id) values ('전통주', 0);
-- Level 2 카테고리
-- 채소
insert into item_category(category_name, parent_id) values ('친환경', 1);
insert into item_category(category_name, parent_id) values ('고구마 · 감자 · 당근', 1);
insert into item_category(category_name, parent_id) values ('시금치 · 쌈채소 · 나물', 1);
insert into item_category(category_name, parent_id) values ('브로콜리 · 파프리카 · 양배추', 1);
insert into item_category(category_name, parent_id) values ('양파 · 대파 · 마늘 · 배추', 1);
insert into item_category(category_name, parent_id) values ('오이 · 호박 · 고추', 1);
insert into item_category(category_name, parent_id) values ('냉동 · 이색 · 간편채소', 1);
insert into item_category(category_name, parent_id) values ('콩나물 · 버섯', 1);
-- 과일 · 견과 · 쌀
insert into item_category(category_name, parent_id) values ('친환경', 2);
insert into item_category(category_name, parent_id) values ('제철과일', 2);
insert into item_category(category_name, parent_id) values ('국산과일', 2);
insert into item_category(category_name, parent_id) values ('수입과일', 2);
insert into item_category(category_name, parent_id) values ('간편과일', 2);
insert into item_category(category_name, parent_id) values ('냉동 · 건과일', 2);
insert into item_category(category_name, parent_id) values ('견과류', 2);
insert into item_category(category_name, parent_id) values ('쌀 · 잡곡', 2);
-- 수산 · 해산 · 건어물
insert into item_category(category_name, parent_id) values ('제철수산', 3);
insert into item_category(category_name, parent_id) values ('생선류', 3);
insert into item_category(category_name, parent_id) values ('굴비 · 반건류', 3);
insert into item_category(category_name, parent_id) values ('오징어 · 낙지 · 문어', 3);
insert into item_category(category_name, parent_id) values ('새우 · 게 · 랍스터', 3);
insert into item_category(category_name, parent_id) values ('해산물 · 조개류', 3);
insert into item_category(category_name, parent_id) values ('수산가공품', 3);
insert into item_category(category_name, parent_id) values ('김 · 미역 · 해조류', 3);
insert into item_category(category_name, parent_id) values ('건어물 · 다시팩', 3);
-- 정육 · 계란
insert into item_category(category_name, parent_id) values ('국내산 소고기', 4);
insert into item_category(category_name, parent_id) values ('수입산 소고기', 4);
insert into item_category(category_name, parent_id) values ('돼지고기', 4);
insert into item_category(category_name, parent_id) values ('계란류', 4);
insert into item_category(category_name, parent_id) values ('닭 · 오리고기', 4);
insert into item_category(category_name, parent_id) values ('양념육 · 돈까스', 4);
insert into item_category(category_name, parent_id) values ('양고기', 4);
-- 국  · 반찬 · 메인요리
insert into item_category(category_name, parent_id) values ('국 · 탕 · 찌개', 5);
insert into item_category(category_name, parent_id) values ('밀키트 · 메인요리', 5);
insert into item_category(category_name, parent_id) values ('밑반찬', 5);
insert into item_category(category_name, parent_id) values ('김치 · 젓갈 · 장류', 5);
insert into item_category(category_name, parent_id) values ('두부 · 어묵 · 부침개', 5);
insert into item_category(category_name, parent_id) values ('베이컨 · 햄 · 통조림', 5);
-- 샐러드 · 간편식
insert into item_category(category_name, parent_id) values ('샐러드 · 닭가슴살', 6);
insert into item_category(category_name, parent_id) values ('도시락 · 밥류', 6);
insert into item_category(category_name, parent_id) values ('파스타 · 면류', 6);
insert into item_category(category_name, parent_id) values ('떡볶이 · 튀김 · 순대', 6);
insert into item_category(category_name, parent_id) values ('피자 · 핫도그 · 만두', 6);
insert into item_category(category_name, parent_id) values ('폭립 · 떡갈비 · 만주', 6);
insert into item_category(category_name, parent_id) values ('죽 · 스프 · 카레', 6);
insert into item_category(category_name, parent_id) values ('선식 · 시리얼', 6);
-- 면 · 양념 · 오일
insert into item_category(category_name, parent_id) values ('파스타 · 면류', 7);
insert into item_category(category_name, parent_id) values ('식초 · 소스 · 드레싱', 7);
insert into item_category(category_name, parent_id) values ('양념 · 액젓 · 장류', 7);
insert into item_category(category_name, parent_id) values ('식용유 · 참기름 · 오일', 7);
insert into item_category(category_name, parent_id) values ('소금 · 설탕 · 향신료', 7);
insert into item_category(category_name, parent_id) values ('밀가루 · 가루 · 믹스', 7);
-- 생수 · 음료 · 우유 · 커피
insert into item_category(category_name, parent_id) values ('생수 · 탄산수', 8);
insert into item_category(category_name, parent_id) values ('음료 · 주스', 8);
insert into item_category(category_name, parent_id) values ('우유 · 두유 · 요거트', 8);
insert into item_category(category_name, parent_id) values ('커피', 8);
insert into item_category(category_name, parent_id) values ('차', 8);
-- 간식 · 과자 · 떡
insert into item_category(category_name, parent_id) values ('과자 · 스낵 · 쿠키', 9);
insert into item_category(category_name, parent_id) values ('초콜릿 · 젤리 · 캔디', 9);
insert into item_category(category_name, parent_id) values ('떡 · 한과', 9);
insert into item_category(category_name, parent_id) values ('아이스크림', 9);
-- 베이커리 · 치즈 · 델리
insert into item_category(category_name, parent_id) values ('식빵 · 빵류', 10);
insert into item_category(category_name, parent_id) values ('잼 · 버터 · 스프레드', 10);
insert into item_category(category_name, parent_id) values ('케이크 · 파이 · 디저트', 10);
insert into item_category(category_name, parent_id) values ('치즈', 10);
insert into item_category(category_name, parent_id) values ('델리', 10);
insert into item_category(category_name, parent_id) values ('올리브 · 피클', 10);
