# spring-service(지자체 협약 지원 API 개발)



### 개발 프레임워크

- Spring boot 1.5.19.RELEASE, JAVA 1.8
- DataBase  : H2
- Security : JWT



### 문제해결 방법

------

#### Entity

**[REGIONS]** Master table

code : PK, GeneratedValue

name : 

**[REGIONS_INF]**

id : PK, GeneratedValue

regions : FK(REGIONS.code)

etc.



#### 1. 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API

> 가정 : .csv 파일의 인코딩 방식 (EUC-KR)

> 통신 방식 : POST  - .csv 파일 전달



각 레코드의 컬럼값은 ',' 구분자로 되어있으나 한 컬럼이 "a,b" 인 경우도 존재

split(",")을 사용하지 않고 별도로 파싱 진행



RegionsRepository - REGIONS 테이블에 데이터 저장 (code,name = {1,"강릉시"})

RegionsInfRepository - REGIONS_INF 테이블에 데이터 저장



#### 2. 지원하는 지자체 목록 검색 API

> 통신 방식 : GET



저장된 모든 지자체 목록 출력

RegionsInf의 정보 selectAll ->Regions(FK)를 사용해서 Regions엔티티의 name값을 함께 출력

> > 출력 : JSON(Jackson)



#### 3. 지원하는 지자체명을 입력받아 해당 지자체의 지원정보를 출력하는 API

> 통신 방식 : GET - regions=지자체명 (regions=강릉시)



입력받은 지자체명으로 REGIONS 엔티티 내 매칭되는 code값 찾고, REGIONS_INF 엔티티의 정보 조회

> > 출력 : JSON(Jackson)



#### 4. 지원하는 지자체 정보 수정 기능 API

> 가정 : 지자체명은 PK로 입력 값에 포함되어 있어야 하며, 이를 기준으로 변경된 정보만 수정

> 통신 방식 : POST 방식 - 수정하고자 하는 정보(JSON)



입력받은 지자체명으로 REGIONS 엔티티와 매칭되는 REGIONS_INF 정보 찾고,

save 함수 사용하여 수정된 정보만 UPDATE

> > 출력 : JSON(Jackson)



#### 5. 지원한도 컬럼에서 지원금액으로 내림차순 정렬하여 특정 개수만 출력하는 API

> 가정 : 지원 한도 컬럼의 단위는 억, 백만 만 있음, 추천금액 이내는 무시, 이자보전 컬럼에서 대출이자 전액은 무시

> 통신 방식 : GET - num=특정 개수 (num=3)



Node 생성 : {지자체명, 단위 통일된 지원 한도 (억), 이차 보전의 평균}

Comparator를 사용한 Sort : 지원 한도 기준 정렬, 값이 같으면 이차 보전의 평균 사용하여 내림차순

특정 개수만큼 상위 지자체명 출력

> > 출력 : JSON(Jackson)



#### 6. 이차보전 컬럼에서 보전 비율이 가장 작은 추천 기관명을 출력하는 API

> 가정 : 이자보전 컬럼에서 대출이자 전액은 무시, 이차 보전의 범위 중 가장 작은 값 사용 (0.1%~1.5% -> 0.1%)

> 통신 방식 : GET



Comparator를 사용한 Sort : 오름차순

0번째에 해당하는 *추천기관명* 출력

> > 출력 : JSON(Jackson)



##### < 추가 제약사항 >

**signup과 login을 제외한 모든 API는 Header에 Token 값으로 유효성 검사 실시**

*JWT* 이용 - Access token, Refresh token 생성



#### 7-1. signup 계정 생성 API

> 통신 방식 : POST  - ID,PW(JSON)



패스워드 인코딩하여 APP_USER 테이블에 정보 저장

인코딩 : 스프링 시큐리티 라이브러리 bCryptPasswordEncoder 사용



#### 7-2. signin 로그인 API

> 통신 방식 : POST - ID,PW(JSON)



APP_USER 테이블에 저장된 정보와 일치하는지 확인

Refresh token, Access token 생성하여 출력

> > 출력 : JSON(Jackson)



#### 7-3. refresh 토큰 재발급 API

> 통신 방식 : GET - Header: Token 정보



Refresh token, Access token 생성하여 출력

> > 출력 : JSON(Jackson)



## 빌드 및 실행 방법



##### 1. jar 파일이 있는 폴더로 이동

##### 2. 서비스 시작

``` java -jar spring-service-0.0.1-SNAPSHOT.jar ```



### [API 테스트 시나리오]

#### 1. signup 

> 통신 방식 : POST

> URL : http://localhost:8080/users/sign-up

[입력]

BODY - 

```json
{
   "username": "admin",
   "password": "password"
}
```



#### 2. login

> 통신 방식 : POST

> URL : http://localhost:8080/login

[입력]

HEADER -

X-Requested-With : XMLHttpRequest

BODY - 

```json
{
   "username": "admin",
   "password": "password"
}
```

[출력]

```JSON
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInNjb3BlcyI6W10sImlzcyI6Imh0dHA6Ly9zdW5taW4uY29tIiwiaWF0IjoxNTU0NjQxNDc0LCJleHAiOjE1NTQ2NDIzNzR9.YxHK-y8sujsMMYP8Qs6rGokSNrB8nnM5ve7u48CpBLhsNx_A72Pq0PQSaVYPtGdP4TistKgQW-t8J4ABPDL6LA",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInNjb3BlcyI6WyJST0xFX1JFRlJFU0hfVE9LRU4iXSwiaXNzIjoiaHR0cDovL3N1bm1pbi5jb20iLCJqdGkiOiIxNmE5ZDE5Zi0wNTY5LTRhMjYtYWQ4Ni0yM2FjZDhmOGM3MDkiLCJpYXQiOjE1NTQ2NDE0NzQsImV4cCI6MTU1NDY0NTA3NH0.czE38-rVbJOdIoxDtbwv0R2L9jXu2YhEWGFu6N4f2xYY7CPfYsCAhqpdJTNDJfOVHdQb9gAUKvr_X-v0xFHYwg"
}
```



#### 3. refresh 토큰 재발급

> 통신 방식 : GET

> URL : http://localhost:8080/api/auth/token

[입력]

| key           | value        |
| ------------- | ------------ |
| Authorization | Bearer token |



#### 4. 지원하는 지자체 목록 검색 API

> 통신 방식 : GET

> URL : http://localhost:8080/list

[입력]

| key           | value        |
| ------------- | ------------ |
| Authorization | Bearer token |



#### 5. 지원하는 지자체명을 입력받아 해당 지자체의 지원정보를 출력하는 API

> 통신 방식 : GET

> URL : http://localhost:8080/select?region=강릉시

[입력]

HEADER - key:Authorization  value: Bearer TOKEN (Login 결과로 생성된 TOKEN 값 입력)



#### 6. 지원하는 지자체 정보 수정 기능 API

> 통신 방식 : POST

> URL : http://localhost:8080/update

[입력]

HEADER - key:Authorization  value: Bearer TOKEN (Login 결과로 생성된 TOKEN 값 입력)

BODY - 

*region : KEY 값으로 작용. 수정하고자 하는 지자체 명*

*target : 아래 예제에서 수정하고자 하는 정보*

```json
{
  "mgmt" : "강릉지점", 
  "rate" : "3%",
  "usage" : "운전",
  "limit" : "추천금액 이내",
  "institute" : "강릉시",
  "region" : "강릉시",
  "reception" : "강릉시 소재 영업점",
  "target" : "선민이"
}
```



#### 7. 지원한도 컬럼에서 지원금액으로 내림차순 정렬하여 특정 개수만 출력하는 API

> 통신 방식 : GET

> URL : http://localhost:8080/sort?num=3

[입력]

HEADER - key:Authorization value: Bearer TOKEN (Login 결과로 생성된 TOKEN 값 입력)



#### 8. 이차보전 컬럼에서 보전 비율이 가장 작은 추천 기관명을 출력하는 API

> 통신 방식 : GET

> URL : http://localhost:8080/minRate

[입력]

HEADER - key:Authorization  value: Bearer TOKEN (Login 결과로 생성된 TOKEN 값 입력)

