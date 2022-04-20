# Store
음식점과 메뉴를 기록 할 수 있습니다.

store는 크게 3가지로 구분됩니다.  
store : 사용자가 web을 이용해서 보는 화면입니다.  
android_store : 사용자가 android에서 보는 화면입니다.  
restapi_store : rest API 서버입니다. store, android_store로 부터 요청을 받아 응답 합니다.  

# 시작하기
```
git clone https://github.com/hj3437/store.git
```

## 빌드 및 실행
mysql 설정이 필요합니다.  
```
https://github.com/hj3437/store/blob/main/db/storedb.sql
```

## restapi_store
- IntelliJ 설치 : https://www.jetbrains.com/ko-kr/idea/  
restapi_store > src > main > resources 폴더에서 application.properties 파일 생성합니다.  
```
server.port = 8989
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://<접속주소>:3306/<db이름>?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Seoul
spring.datasource.username=<아이디>
spring.datasource.password=<비밀번호>
```

IntelliJ IDE로 restapi_store 프로젝트 import 이후 StoreApplication 실행할 수 있습니다.  
또는 build>bootJar로 빌드 후 java -jar \build\libs\store-0.0.1-SNAPSHOT.jar 입력하여 실행합니다.  

## store
- Visual Studio Code 설치 : https://code.visualstudio.com  
프로젝트 open 및 Live Server Extension 설치후 실행

## android_store

- Android Studio 설치 : https://developer.android.com/studio  
안드드로이드 스튜디오 IDE에서 프로젝트 import 이후 빌드와 실행을 할 수 있습니다. 

# 영상
[스토어_웹 실행](https://youtu.be/Ih112A__cng)  
[스토어_안드로이드 실행](https://youtu.be/2OjkLKymoE4)  
