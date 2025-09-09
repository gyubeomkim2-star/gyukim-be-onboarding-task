# 1) 개요

•	Auth/User/Follow: MySQL(JPA)
•	Posts: MongoDB(문서형)
•	Feed: Follow + Post 조합 조회
•	Caching(옵션): 이후 Redis로 단건 조회 캐시(post:{id}) 도입 예정

# 2) 아키텍처 (서비스 단)

   •	Client → API Gateway/LB → User / Follow / Post / Feed Service
   •	User/Follow → MySQL, Post → MongoDB
   •	(옵션) Redis는 서비스 ↔ DB 사이의 읽기 캐시 레이어로 배치

# 3) 기술 스택
   •	Java 17, Spring Boot 3
   •	Spring Web, Spring Data JPA(MySQL), Spring Data MongoDB
   •	(옵션) Spring Cache (+ Redis)
   •	Testcontainers, JUnit 5, Docker / docker-compose

# 4) 의존 서비스

MySQL, Mongo (옵션) 나중에 Redis

# 5) 빠른 실행

mvn spring-boot:run

OpenAPI (run 이후)
http://localhost:8080/swagger-ui/index.html

# 6) APIs

## 6.1 Posts (Mongo)

POST   /posts                # body: {authorId, title, content, tags[]}
GET    /posts/{id}
PUT    /posts/{id}           # body: {title?, content?, tags?}
DELETE /posts/{id}
GET    /posts?q=&tags=t1,t2&page=0&size=10

## 6.2 Follow / Users (MySQL)

POST /users/{userId}/follow
DELETE /users/{userId}/follow

GET /users/{userId}/followers

## 6.3 Feed (조합)

GET  /feed?userId=&page=0&size=10


# 7) 비기능 요구사항 (NFR)
   •	Latency 목표: 단건 조회 p95 < 100ms, 피드 p95 < 200ms
   •	가용성: 토이 단계 단일 인스턴스 OK (후에 롤링 배포)
   •	일관성: MySQL(강한), Mongo/피드(최종)
   •	보안: JWT, BCrypt, Secret/환경변수 관리
   •	관측성: Actuator /health, /metrics, 요청 로그 traceId

# 8) Requirements

Functional Requirements
- User (MySQL/JPA)
- Follow/Unfollow(MySQL/JPA)
- Post CURD
- Feeds (able to retrieve the latest posts from users who I follow)
- Tag or Title based search
- Cursor or pagination

Non-Functional Requirements

Latency < 200ms
Highly available
Consistency  - for user follow
Eventual Consistency - for the latest post retrieval
Highly scalable - mySQL (read replicas) / NoSQL (sharing)

# 9) 간단 계산(DAU 10,000 기준)

If a person does 30 requests
If Read 8 : Write 2

Daily: 30 * 10000 ‎ = 300,000 req/day
Avg: 300000 / 86400 ‎ = 3.472 RPS
Peak time (* 10) = 34.72 RPS

If 1/3 create post (write)
Daily: 20000
Avg: 20000 / 86400 ‎ = 0.231 RPS
Peak time (*10) = 2.31

Title = ~1kb
Post Desc =  ~2kb
Tag = ~few mb
Total = ~3kb

Daily: 3kb * 20000 ‎ = 60,000kB = 60 MB/day
Yearly: 60MB * 365 ‎ = 21,900MB = 21.9GB

# 10) High-Level Architecture

<img width="806" height="541" alt="스크린샷 2025-09-08 오후 2 48 59" src="https://github.com/user-attachments/assets/12894444-50f3-4974-8112-53007c79d592" />

# 11) 테스트
    •	단위/통합 테스트: JUnit5 + Testcontainers(MySQL, Mongo)
    •	예: PostService 통합 테스트 → MongoContainer, User 존재 체크용 MySQLContainer

