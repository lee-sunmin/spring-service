# spring-service(지자체 협약 지원 API 개발)

* * *

## 개발 프레임 워크
* Spring boot 1.5.19.RELEASE, JAVA 1.8


## 문제해결 방법

1. 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API
조건 : .csv 파일의 인코딩 방식 (EUC-KR)
POST 방식 - .csv 파일 전달받음

* File Parsing
.. 코드 볼 수 있게 링크 걸기





2. 지원하는 지자체 목록 검색 API

3. 지원하는 지자체명을 입력받아 해당 지자체의 지원정보를 출력하는 API

4. 지원하는 지자체 정보 수정 기능 API

5. 지원한도 컬럼에서 지원금액으로 내림차순 정렬하여 특정 개수만 출력하는 API

6. 이차보전 컬럼에서 보전 비율이 가장 작은 추천 기관명을 출력하는 API

7. 추가 제약사항

7-1. signup 계정 생성 API

7-2. signin 로그인 API

7-3. refresh 토큰 재발급 API



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


