# 마켓컬렉션(Market-collection)

### 온라인 식품 쇼핑몰 '마켓컬리' 벤치마킹 프로젝트
마켓컬리의 주요 기능들(타임 세일, 상품 추천, 회원 등급제, 적립금, 최근 본 상품 등)을 스스로 고민하고 구현한   
1인 개발 프로젝트입니다.

## 개발 목표
> 유지 보수를 고려한 객체 지향적인 코드 구현   
> 대량의 데이터와 트래픽을 대비한 성능 최적화

## 기술 스택

### Backend
> Java
> Spring Boot, Spring MVC, Spring Security, Spring Data JPA, Junit   
> MySql

### Frontend
> HTML5, CSS3, JavaScript(JQuery), Thymeleaf

### DevOps
> AWS EC2, RDS, S3

### Tool
> Intellij, Git

## 아키텍처 설계
AWS에서 무료 제공하는 프리티어 환경에서 구축하였습니다.
![market-collection-architecture](https://github.com/dschoi30/market-collection/assets/97089961/04a11214-80ca-4853-95c2-0aa0c7b511e4)

### CI/CD 프로세스
1. Github에 커밋 시 Github Actions로 배포 요청
2. Github Actions는 배포 스크립트를 실행하여 프로젝트 빌드 및 jar 파일을 생성 후 S3에 전달
3. Github Actions는 동시에 CodeDeploy에 배포 요청
4. CodeDeploy는 EC2 내부 CodeDeployAgent에 배포 요청
5. CodeDeployAgent는 S3에서 파일을 받아 배포 스크립트 실행
6. SpringBoot WAS 실행(Nginx 리버스 프록시를 적용하여 포트 스위칭을 통한 무중단 배포)

## ERD 설계
![market-collection - ER diagram](https://user-images.githubusercontent.com/97089961/228749916-0db0ecae-7c7f-4545-8ab0-19d1b9c08f53.png)


## 기능 정의

### 회원
1. SNS 서비스를 통해 소셜 로그인(네이버, 카카오톡, 구글)을 할 수 있습니다.
2. 상품을 카테고리별, 판매순, 가격순으로 필터링 및 검색할 수 있습니다.
3. 추천 상품, 타임 세일 상품 목록을 확인할 수 있습니다.
4. 최근 본 상품 목록을 조회할 수 있습니다.
5. 상품 리뷰를 등록 및 조회할 수 있습니다.
6. 상품을 개인 장바구니에 담을 수 있습니다.
7. 장바구니에 담은 상품의 수량을 수정 및 삭제할 수 있습니다.
8. 주문 페이지를 통해 상품을 주문할 수 있습니다.
9. 기간별로 본인의 주문 이력을 조회할 수 있습니다.
10. 주문을 취소할 수 있습니다.
11. 주문 시 회원 등급별 적립률을 통해 포인트를 적립할 수 있습니다.
12. 주문 시 포인트를 사용하여 결제할 수 있습니다.

### 관리자 
1. 상품을 등록, 수정 및 삭제할 수 있습니다.
2. 상품을 판매순, 조회순, 등록일순으로 필터링 및 검색할 수 있습니다.
3. 전체 주문 내역을 기간별로 필터링 및 조회할 수 있습니다.
4. 특정 기간을 설정하여 상품 할인을 진행할 수 있습니다.

## 기술적 이슈 및 개선 사항
1. DB 100만건 데이터 삽입 및 실행 계획 분석 후 커버링 인덱스 적용 페이징 처리 속도 개선(4s -> 0.02s)
2. 상품 주문 시의 동시성 이슈 확인 후 비관적 락 적용을 통한 문제 개선
3. 정적 데이터 캐시 적용을 통한 조회 속도 개선
4. JPA를 이용한 기능 구현 중 무한 루프 빠짐, N+1 문제 해결
5. Exception Handler를 활용한 예외 전역 처리

## 기능 시연
### 메인 페이지
https://github.com/dschoi30/market-collection/assets/97089961/a31fed43-aee4-46de-b4ec-81e4b68c4259
##
### 주문 페이지
https://github.com/dschoi30/market-collection/assets/97089961/b9e5644f-0c3a-465e-be14-4a5505c72c0a
##
### 관리자 페이지
https://github.com/dschoi30/market-collection/assets/97089961/25e5f01d-00f5-4b90-a941-3c849fd10be4
