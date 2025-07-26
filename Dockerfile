# 베이스 이미지 (JDK 17 사용)
FROM eclipse-temurin:17-jdk

# 작업 디렉토리 생성
WORKDIR /app

# 빌드된 jar 파일 복사
COPY build/libs/app.jar app.jar

# 환경변수 설정 (예시, 실제는 docker run 시 주로 지정)
ENV SPRING_PROFILES_ACTIVE=prod

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]