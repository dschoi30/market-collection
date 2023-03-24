# 마켓컬렉션(Market-collection)

### 온라인 식품 쇼핑몰 '마켓컬리' 벤치마킹 프로젝트
마켓컬리의 주요 기능들(소셜 로그인, 회원 등급제, 적립금, 최근 본 상품 등)을 스스로 고민하고 구현한 1인 개발 프로젝트입니다.

## 개발 목표
> 유지 보수를 고려한 객체 지향적인 코드 구현   
> 점진적인 트래픽 증가를 가정한 부하 분산 설계 및 성능 최적화

## 기술 스택

### Backend
> Java
> Spring Boot, Spring MVC, Spring Security, Spring Data JPA, Junit5   
> MySql

### Frontend
> HTML5, CSS3, JavaScript(JQuery), Thymeleaf

### DevOps
> AWS EC2, RDS, S3

### Tool
> Intellij, Git

## 아키텍처 설계
![image](https://user-images.githubusercontent.com/97089961/227414884-73f2507e-ca41-4081-b2df-7d0c1f2aaa73.png)

## ERD 설계
![market-collection - Database ER diagram (crow's foot)](https://user-images.githubusercontent.com/97089961/226277171-1de32a18-2665-49aa-b832-6e825ab8d6f8.png)

## 기능 정의

### 회원
1. SNS 서비스를 통해 소셜 로그인(네이버, 카카오톡, 구글)을 할 수 있습니다.
2. 상품을 카테고리별, 판매순, 가격순으로 필터링 및 검색할 수 있습니다.
3. 최근 본 상품 목록을 조회할 수 있습니다.
4. 상품 리뷰를 등록 및 조회할 수 있습니다.
5. 상품을 개인 장바구니에 담을 수 있습니다.
6. 장바구니에 담은 상품의 수량을 수정 및 삭제할 수 있습니다.
7. 주문 페이지를 통해 상품을 주문할 수 있습니다.
8. 기간별로 본인의 주문 이력을 조회할 수 있습니다.
9. 주문을 취소할 수 있습니다.
10. 주문 시 회원 등급별 적립률을 통해 포인트를 적립할 수 있습니다.
11. 주문 시 포인트를 사용하여 결제할 수 있습니다.

### 관리자 
1. 상품을 등록, 수정 및 삭제할 수 있습니다.
2. 상품을 판매순, 조회순, 등록일순으로 필터링 및 검색할 수 있습니다.
3. 전체 주문 내역을 기간별로 필터링 및 조회할 수 있습니다.
## 트러블 슈팅
1. 스프링 부트와 JPA를 활용한 기능 구현 중 무한 루프 빠짐, N+1 문제 등을 접하고 해결
2. 대용량 데이터 삽입 후 테이블 정규화 및 인덱싱을 통한 처리 속도 개선,
테스트 코드 작성 및 예외 처리를 통한 문제 관리 능력 향상
3. Enum, Stream, Lambda 적용 및 메소드 분리를 통한 코드 간결화

##
![image](https://user-images.githubusercontent.com/97089961/224226422-09f12046-e980-4e07-acfa-e366f2ae5157.png)   
##
![image](https://user-images.githubusercontent.com/97089961/224222870-ac587f80-72df-4328-8133-383dab5851e6.png)
##
![image](https://user-images.githubusercontent.com/97089961/224226437-c837db5f-00ef-4c88-8fd9-cfc5125d6cea.png)
