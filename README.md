# 1) 개요

- Auth/User/Follow: MySQL(JPA)
- Posts: MongoDB(문서형)
- Feed: Follow + Post 조합 조회
- Caching(옵션): 이후 Redis로 단건 조회 캐시(post:{id}) 도입 예정

# 2) 기술 스택
   - Java 17, Spring Boot 3
   - Spring Web, Spring Data JPA(MySQL), Spring Data MongoDB
   - (옵션) Spring Cache (+ Redis)
   - JUnit 5

# 3) 의존 서비스

MySQL, Mongo (옵션) 나중에 Redis

# 4) 빠른 실행

./mvnw clean install  

Swagger 실행 (run 이후)
http://localhost:8080/swagger-ui/index.html

# 5) APIs

## 5.1 Posts (Mongo)

- POST   /posts                # body: {authorId, title, content, tags[]}
- GET    /posts/{id}
- PUT    /posts/{id}           # body: {title?, content?, tags?}
- DELETE /posts/{id}
- GET    /posts?q=&tags=t1,t2&page=0&size=10

## 5.2 Follow / Users (MySQL)

- POST /users/{userId}/follow
- DELETE /users/{userId}/follow

- GET /users/{userId}/followers

## 5.3 Feed (조합)

- GET  /feed?userId=&page=0&size=10


# 6) Functional Requirements
- User (MySQL/JPA)
- Follow/Unfollow(MySQL/JPA)
- Post CURD
- Feeds (able to retrieve the latest posts from users who I follow)
- Tag or Title based search
- Cursor or pagination

# 7) Non-Functional Requirements

  - Latency < 200ms
  - Highly available
  - Consistency for user follow
  - Eventual Consistency for the latest post retrieval
  - Highly scalable - mySQL (read replicas) / NoSQL (sharding)

# 8) 간단 계산(DAU 10,000 기준)

- If a person does 30 requests (Read 8 : Write 2)
  - Daily: 30 * 10000 ‎ = 300,000 req/day
  - Avg: 300000 / 86400 ‎ = 3.472 RPS
  - Peak time (* 10) = 34.72 RPS

- If 1/3 create post (write)
  - Daily: 60000
  - Avg: 60000 / 86400 ‎ = 0.694 RPS
  - Peak time (*10) = 6.94

- Data Model
   - Title = ~1kb
   - Post Desc =  ~2kb
   - Tag = ~few mb
   - Total = ~3kb
   - Daily: 3kb * 60000 ‎ = 180000kB = 180 MB/day
   - Yearly: 180MB * 365 ‎ = 65700MB = 65.7GB

# 9) High-Level Architecture

<img width="806" height="541" alt="스크린샷 2025-09-08 오후 2 48 59" src="https://github.com/user-attachments/assets/12894444-50f3-4974-8112-53007c79d592" />

# 10) 테스트
    •	단위/통합 테스트: JUnit5 + Testcontainers(MySQL, Mongo)
    •	예: PostService 통합 테스트 → MongoContainer, User 존재 체크용 MySQLContainer

