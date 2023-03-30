# 마켓컬렉션(Market-collection)

### 온라인 식품 쇼핑몰 '마켓컬리' 벤치마킹 프로젝트
마켓컬리의 주요 기능들(소셜 로그인, 회원 등급제, 적립금, 최근 본 상품 등)을 스스로 고민하고 구현한 1인 개발 프로젝트입니다.

## 개발 목표
> 유지 보수를 고려한 객체 지향적인 코드 구현   
> 대량의 데이터와 트래픽을 대비한 성능 최적화

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
![image](https://user-images.githubusercontent.com/97089961/228763378-e65d33f7-11fd-4905-82cc-e26f95cbe33f.png)

## ERD 설계
![market-collection - ER diagram](https://user-images.githubusercontent.com/97089961/228749916-0db0ecae-7c7f-4545-8ab0-19d1b9c08f53.png)


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

## 기술적 이슈 및 개선 사항
1. JPA를 이용한 기능 구현 중 무한 루프 빠짐, N+1 문제 등을 접하고 해결
2. 대용량 데이터 삽입 후 테이블 정규화 및 인덱싱을 통한 조회 속도 개선
3. Enum, Stream, Lambda 적용 및 메소드 분리를 통한 코드 간결화
4. Exception Handler 적용을 통한 예외 전역 처리

## 기능 시연
### 메인 페이지
![2023-03-30-01-48-23](https://user-images.githubusercontent.com/97089961/228750026-6b21019a-abda-415b-bec2-97469b0b1de1.gif)
##
### 주문 페이지
![2023-03-30-15-28-26](https://user-images.githubusercontent.com/97089961/228750075-b5e09d9a-1afb-45f9-bcdf-8d20b22656ef.gif)
##
### 관리자 페이지
![2023-03-30-02-02-15](https://user-images.githubusercontent.com/97089961/228750055-b9be069f-c41e-4e9c-b61e-a38271c0fc82.gif)
