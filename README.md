1. 기본적인 Security 설정을 위한 config.SecurityConfig 클래스
2. yaml 변경
3. member.UserEntity, Auth.AutorityEntity 작성 (사용자, 권한 에 대한 Entity)
   -> 두 엔티티는 다대다 관계로 조인테이블을 새로 생성해서 어떤 유저의 아이디로 해당 유저의 권한이름을 가져올 수 있게 하기 위함이다.

4. DB 연결하고 data.sql 에 있는 SQL script 실행해서 샘플 데이터 삽입
5. yaml 파일에 jwt 관련 설정 값 추가 (헤더 종류, 알고리즘, 키)
6. gradle 파일에 JWT 의존성 추가
7. 토큰의 생성과 토큰의 유효성 검사를 담당하는 jwt.TokenProvider 클래스 작성
8. JWT 를 위한 Custom Filter 를 위해서 jwt.JwtFilter 작성
9. TokenProvider, JwtFilter 를 SecurityConfig 에 적용할 때 사용할 jwt.JwtSecurityConfig 클래스 작성