1. 기본적인 Security 설정을 위한 config.SecurityConfig 클래스
2. yaml 변경
3. member.UserEntity, Auth.AutorityEntity 작성 (사용자, 권한 에 대한 Entity)
   -> 두 엔티티는 다대다 관계로 조인테이블을 새로 생성해서 어떤 유저의 아이디로 해당 유저의 권한이름을 가져올 수 있게 하기 위함이다.

4. DB 연결하고 data.sql 에 있는 SQL script 실행해서 샘플 데이터 삽입
5. yaml 파일에 jwt 관련 설정 값 추가 (헤더 종류, 알고리즘, 키)
6. gradle 파일에 JWT 의존성 추가
7. 토큰의 생성과 토큰의 유효성 검사를 담당하는 jwt.TokenProvider 클래스 작성
8. JWT 를 위한 Custom Filter 를 위해서 jwt.JwtFilter 작성
9. JwtFilter 를 이용해서 TokenProvider 를 SecurityConfig 에 적용할  때 사용할 jwt.JwtSecurityConfig 클래스 작성
10. 유요한 자격증명을 제공하지 않고 접근할 때 401 에러를 반환하는 jwt.JwtAuthenticationEntryPoint 클래스 작성
11. 필요한 권한이 존재하지 않는 경우에 403 Forbidden 에러를 반환하는 jwt.JwtAccessDeniedHandler 클래스 작성
12. TokenProvider, JwtSecurityConfig, JwtFilter, JwtAuthenticationEntryPoint, JwtAccessDeniedHandler 를 config.SecurityConfig 에 적용
13. 로그인 정보를 외부에서 가지고오는 데 사용할 member.LoginDto 클래스 작성
14. 토큰 정보를 클라이언트에게 응답으로 주는 jwt.TokenDto 클래스 작성
15. 클라이언트에서 회원가입에 접근했을 때 데이터를 가져올 member.UserDto 클래스 작성
16. UserEntity를 이용해 DB에 실제 작업을 하는 메서드가 포함된 member.UserRepository 인터페이스 작성
17. UserService 를 구현한 jwt.service.CustomUserDetailsService 클래스 작성
18. 로그인 API를 위한 Auth.AuthConroller 작성
19. Postman 으로 AuthConroller 에 Post 요청(Body-raw-json)
20. SecurityContext 에 저장된 인증 정보중 username 을 가져오는  jwt.util.SecurityUtil 클래스 작성
21. 회원가입, 유저정보 조회 등의 메서드를 가진 member.UserSerivice 클래스 작성
22. PS Auth 패키지 member 패키지 합치기
23. UserService 를 주입받아서 사용할 member.UserController 작성
24. /api/signup 으로 POST 요청 (회원가입)
25. /api/user, /user/{username} 로 권한이 다른 사용자 계정으로 요청을 보내서 권한에 따라 요청에 대한 응답이 적절한지 확인
   - USER 권한을 가진 계정으로 Token을 발급받고 이를 Authriaztion 에 Bearer Token 에 넣어서 두 URL 에 GET 요청 후 확인
   - ADMIN 권한을 가진 계정으로 Token을 발급받고 이를 Authriaztion 에 Bearer Token 에 넣어서 두 URL 에 GET 요청 후 확인
