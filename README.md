# spring-service(지자체 협약 지원 API 개발)

## 개발 프레임 워크
* Spring boot 1.5.19.RELEASE, JAVA 1.8


## 문제해결 방법

데이터베이스 : H2


### Entity
[REGIONS]
Master table
code : PK, GeneratedValue
name : 
[REGIONS_INF]
id : PK, GeneratedValue
regions : FK(REGIONS.code)
etc.


1. 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API
가정 : .csv 파일의 인코딩 방식 (EUC-KR)
POST 방식 - .csv 파일 전달받음

각 레코드의 컬럼값은 ',' 구분자로 되어있으나 한 컬럼이 "a,b" 인 경우도 존재하기 때문에,
split(",")을 사용하지 않고 별도로 파싱 진행

regionsRepository - REGIONS 테이블에 데이터 저장 (code,name = {1,"강릉시"})
regionsInfRepository - REGIONS_INF 테이블에 데이터 저장

- 링크 걸기

2. 지원하는 지자체 목록 검색 API
GET 방식

저장된 모든 지자체 목록 출력
RegionsInf의 정보를 모두 가져오고(selectAll)
Regions(FK)를 사용해서 Regions엔티티의 name값을 함게 출력

출력 : JSON(Jackson)

- 링크 걸기

3. 지원하는 지자체명을 입력받아 해당 지자체의 지원정보를 출력하는 API
GET 방식 - regions=지자체명 (regions=강릉시)

입력받은 지자체명으로 REGIONS 엔티티 내 매칭되는 code값 찾음(findByname)
찾은 code값으로 REGIONS_INF 엔티티의 정보 조회(findOne)

출력 : JSON(Jackson)

4. 지원하는 지자체 정보 수정 기능 API
POST 방식 - 수정하고자 하는 정보(JSON)
가정 : 지자체명은 PK로 무조껀 입력 값에 포함되어 있어야 하며, 이를 기준으로 변경된 정보만 수정

입력받은 지자체명으로 REGIONS 엔티티와 매칭되는 REGIONS_INF 정보 찾음
save 함수 사용하여 수정된 정보만 UPDATE

출력 : JSON(Jackson)

5. 지원한도 컬럼에서 지원금액으로 내림차순 정렬하여 특정 개수만 출력하는 API
가정 : 지원 한도 컬럼의 단위는 억, 백만 만 있음, 추천금액 이내는 무시
이자보전 컬럼에서 대출이자 전액은 무시
GET 방식 - num=특정 개수 (num=3)

Node 생성 : {지자체명, 단위 통일된 지원 한도 (억), 이차 보전의 평균}
Comparator를 사용한 Sort : 지원 한도 기준 정렬, 값이 같으면 이차 보전의 평균 사용하여 내림차순

특정 개수만큼 상위 지자체명 출력

출력 : JSON(Jackson)

6. 이차보전 컬럼에서 보전 비율이 가장 작은 추천 기관명을 출력하는 API
가정 : 이자보전 컬럼에서 대출이자 전액은 무시
이차 보전의 범위 중 가장 작은 값 사용 (0.1%~1.5% -> 0.1%)

Comparator를 사용한 Sort : 오름차순

0번째에 해당하는 *추천기관명* 출력

출력 : JSON(Jackson)

< 추가 제약사항 >
** signup과 login을 제외한 모든 API는 Header에 Token 값으로 유효성 검사 실시 **

JWT 이용 - Access token, Refresh token 생성

참고 사이트 : 
https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/
http://www.svlada.com/jwt-token-authentication-with-spring-boot/

7-1. signup 계정 생성 API
POST 방식 - ID,PW(JSON)

APP_USER 테이블에 정보 저장
패스워드 인코딩 : 스프링 시큐리티 라이브러리 bCryptPasswordEncoder 사용

7-2. signin 로그인 API
POST 방식 - ID,PW(JSON)

APP_USER 테이블에 저장된 정보와 일치하는지 확인

Refresh token, Access token 생성하여 출력 (bearer token)

Access token의 payload =
```json
{
  "sub": "admin",
  "scopes": [],
  "iss": "http://sunmin.com",
  "iat": 1554562020,
  "exp": 1554562920
}
```

Refresh token의 payload =
```json
{
  "sub": "admin",
  "scopes": [
    "ROLE_REFRESH_TOKEN"
  ],
  "iss": "http://sunmin.com",
  "jti": "9d03f73d-6d57-4a9c-95ae-b04ca34afb1e",
  "iat": 1554562020,
  "exp": 1554565620
}
```

출력 : JSON(Jackson)

7-3. refresh 토큰 재발급 API
GET 방식 - Header: Token 정보

Refresh token, Access token 생성하여 출력

출력 : JSON(Jackson)


## 빌드 및 실행 방법


1. jar 파일이 있는 폴더로 이동
2. 서비스 시작
``` java -jar spring-service-0.0.1-SNAPSHOT.jar ```

### API 테스트 시나리오


1. signup 

전송 방식 : POST
URL : http://localhost:8080/users/sign-up

입력 :
BODY - 
```json

{
   "username": "admin",
   "password": "password"
}
```


2. login

전송 방식 : POST
URL : http://localhost:8080/login

입력 :
BODY - 
```json
{
   "username": "admin",
   "password": "password"
}
```

3. refresh 토큰 재발급

전송 방식 : GET
URL : http://localhost:8080/api/auth/token

입력 :
HEADER - 
key:Authorization 
value: Bearer TOKEN (Login 결과로 생성된 TOKEN 값 입력)

전체 리

4. 지원하는 지자체 목록 검색 API

전송 방식 : GET
URL : http://localhost:8080/list

입력 :
HEADER - 
key:Authorization 
value: Bearer TOKEN (Login 결과로 생성된 TOKEN 값 입력)

5. 지원하는 지자체명을 입력받아 해당 지자체의 지원정보를 출력하는 API

전송 방식 : GET
URL : http://localhost:8080/select?region=강릉시

입력 :
HEADER - 
key:Authorization 
value: Bearer TOKEN (Login 결과로 생성된 TOKEN 값 입력)

6. 지원하는 지자체 정보 수정 기능 API

전송 방식 : POST
URL : http://localhost:8080/update

입력 :
HEADER - 
key:Authorization 
value: Bearer TOKEN (Login 결과로 생성된 TOKEN 값 입력)

BODY - 
```json
{
  "mgmt" : "강릉지점", 
  "rate" : "3%",
  "usage" : "운전",
  "limit" : "추천금액 이내",
  "institute" : "강릉시",
  "region" : "강릉시", <- KEY 값으로 작용. 수정하고자 하는 지자체 명
  "reception" : "강릉시 소재 영업점",
  "target" : "선민이" <- 수정하고자 하는 정보
}
```

7. 지원한도 컬럼에서 지원금액으로 내림차순 정렬하여 특정 개수만 출력하는 API

전송 방식 : GET
URL : http://localhost:8080/sort?num=3

입력 :
HEADER - 
key:Authorization 
value: Bearer TOKEN (Login 결과로 생성된 TOKEN 값 입력)

8. 이차보전 컬럼에서 보전 비율이 가장 작은 추천 기관명을 출력하는 API

전송 방식 : GET
URL : http://localhost:8080/minRate

입력 :
HEADER - 
key:Authorization 
value: Bearer TOKEN (Login 결과로 생성된 TOKEN 값 입력)


