# 🎈 실시간 공연 예매 서비스


## 📝 소개
본 프로젝트는 **Spring Boot** 기반의 웹 애플리케이션으로, **실시간 공연 예매 기능**을 제공합니다.  
**Spring Security**를 활용하여 사용자와 관리자 권한을 분리하여 관리의 효율성과 보안을 강화하였습니다.  
**Java Mail Sender**를 활용하여 **이메일 전송 기능**을 구현했습니다.  
좌석에 대한 **동시성 처리**를 위해 **낙관적 락**을 사용했습니다.  

---
## 📝 프로젝트 기획서
![image](https://github.com/user-attachments/assets/02d115a0-9432-4d57-85bb-59f223f923cf)


### 📌 주요 기능

### 🔐 권한 분리 (Spring Security 기반)
- **관리자(Admin)**: 공연 및 예매 전체 관리
- **사용자(User)**: 공연 조회 및 공연 예매

---

### 🛒 공연

| 역할     | 기능                            |
|--------|---------------------------------|
| 관리자  | 공연 등록, 수정, 삭제, 조회 (CRUD) |
| 사용자  | 공연 목록 및 상세 조회, 좋아요 기능  |

---

### 📦 예매

| 역할     | 기능                                       |
|--------|--------------------------------------------|
| 관리자  | 전체 예매 내역 조회, 공연별 예매 현황 조회, 에매 내역 메일 전송 |
| 사용자  | 공연 목록 필터링 조회, 공연 검색, 좋아요, 좋아요 목록 조회, 예매, 예매 내역 조회, 예매 취소|

---

### ⏱️ 이메일 전송
- 회원가입 이메일 인증코드 전송
- 공연 정보 변경에 대한 알림메일 전송
- 예매 완료 메일 전송
- 예매 시작 알림 메일 전송
- 결제 완료 메일 전송

---

> ✅ 본 프로젝트는 **역할 기반 권한 제어**, **RESTful API 설계**, **스케줄링 처리** 등 실무 중심 기능을 반영하여 설계되었습니다.

## ⚙ 기술 스택
### 언어 
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Java.png?raw=true" width="80" alt="Java"/><br/>
      <sub><b>Java 21</b></sub>
    </td>
    <td align="center">
      <img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/HTMLCSS.png?raw=true" width="80" alt="HTMLCSS"/><br/>
      <sub><b></b></sub>
    </td>
    <td align="center">
      <img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/JavaScript.png?raw=true" width="80" alt="Javascript"/><br/>
      <sub><b></b></sub>
    </td>
  </tr>
</table>

### 프레임 워크 및 라이브러리 
<table>
  <tr>
     <td align="center">
      <img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringBoot.png?raw=true" width="80" alt="Spring Boot"/><br/>
      <sub><b>3.4.5</b></sub>
    </td>
    <td align="center">
      <img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringSecurity.png?raw=true" width="80" alt="Spring Security"/><br/>
      <sub><b></b></sub>
    </td>
    <td align="center">
      <img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringDataJPA.png?raw=true" width="80" alt="Spring Data JPA"/><br/>
      <sub><b></b></sub>
    </td>
    <td align="center">
      <img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Thymeleaf.png?raw=true" width="80" alt="Thymeleaf"/><br/>
      <sub><b></b></sub>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/b72cf4ef-1ba9-4525-a7f1-aaf9d28d4b99" width="80" alt="Java Mail Sender"/><br/>
      <sub><b>Java Mail Sender</b></sub>
    </td>
  </tr>
</table>

### 데이터 베이스 
<table>
  <tr>
    <td align="center">
       <img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Mysql.png?raw=true" width="80" alt="MySQL"/><br/>
      <sub><b></b></sub>
    </td>
    <td align="center">
       <img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Redis.png?raw=true" width="80" alt="Redis"/><br/>
      <sub><b></b></sub>
    </td>
  </tr>
</table>

### 인프라 
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/9d571dcc-75fb-4966-b8cb-e17278e78aeb" width="80" alt="GitHub Action"/><br/>
      <sub><b>Github Actions</b></sub>
    </td>
    <td align="center">
      <img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Docker.png?raw=true" width="80" alt="Docker"/><br/>
      <sub><b></b></sub>
    </td>
  </tr>
</table>

### 협업 도구 
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Github.png?raw=true" width="80" alt="GitHub"/><br/>
      <sub><b></b></sub>
    </td>
    <td align="center">
      <img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Notion.png?raw=true" width="80" alt="Notion"/><br/>
      <sub><b></b></sub>
    </td>
        <td align="center">
      <img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Swagger.png?raw=true" width="80" alt="Swagger"/><br/>
      <sub><b></b></sub>
    </td>
  </tr>
</table>

<br />

## 💁‍♂️ 프로젝트 팀원

<table>
  <thead>
    <tr>
      <th align="center">팀장</th>
      <th align="center">팀원</th>
      <th align="center">팀원</th>
      <th align="center">팀원</th>
      <th align="center">팀원</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center">
        <a href="https://github.com/hs986">
          <img src="https://github.com/hs986.png" width="120" height="120" alt="서희승"/><br/>
          <sub><b>서희승</b></sub>
        </a>
      </td>
      <td align="center">
        <a href="https://github.com/jiwon1217">
          <img src="https://github.com/jiwon1217.png" width="120" height="120" alt="곽지원"/><br/>
          <sub><b>곽지원</b></sub>
        </a>
      </td>
      <td align="center">
        <a href="https://github.com/ZELDA31777">
          <img src="https://github.com/ZELDA31777.png" width="120" height="120" alt="김성원"/><br/>
          <sub><b>김성원</b></sub>
        </a>
      </td>
      <td align="center">
        <a href="https://github.com/ense333">
          <img src="https://github.com/ense333.png" width="120" height="120" alt="나상연"/><br/>
          <sub><b>나상연</b></sub>
        </a>
      </td>
      <td align="center">
        <a href="https://github.com/chcch529">
          <img src="https://github.com/chcch529.png" width="120" height="120" alt="정채린"/><br/>
          <sub><b>정채린</b></sub>
        </a>
      </td>
    </tr>
  </tbody>
</table>

## 🛠️ 역할 분담

| 이름   | 담당 기능 |
|--------|-----------|
| **희승** | - 좋아요 기능 <br>- 좋아요 누른 공연에 대한 예매 시작 알림 메일 전송 <br>- 프론트엔드 UI 통합 작업 |
| **지원** | - 예외 처리 로직 구현 <br>- CI 설정 <br>- 사용자 공연 목록 필터링 조회 기능 <br>- 키워드 검색 기능 |
| **성원** | - 관리자 공연 CRUD 구현 <br>- 좌석에 대한 동시성 처리 <br>- 공연 정보 변경에 대한 이메일 전송 |
| **상연** | - Spring Security를 통한 인증/인가 적용 <br>- 로그인, 회원가입 <br>- Oauth 인증 방식의 로그인 <br>- 공연별 예매 현황 기능 <br>- 좋아요 누른 공연 목록 조회 |
| **채린** | - 공연 상세 조회 기능 구현<br>- 좌석 선택 및 예매 처리 <br>- 나의 예매 내역 조회 <br>- 예매 내역 상세 조회 |


## 🛠️ 프로젝트 아키텍쳐

## 시스템 구성도
![image](https://github.com/user-attachments/assets/13d6f9be-6189-4fd7-ab10-8d2e8c91cf0f)


## ERD

![image](https://github.com/user-attachments/assets/0b4f867c-3030-4597-8290-88be53f3859e)

## 협업 방식
### 🛠️ 브랜치 전략
![Image20250428163351](https://github.com/user-attachments/assets/71405653-385a-4bd0-95dd-bb0f58aed569)
1. **이슈 생성**
    - GitHub 이슈를 통해 작업 항목 정의

2. **브랜치 생성**
    - `dev` 브랜치에서 이슈별 작업 브랜치 생성
    - 브랜치 명명 규칙 예시: `feature/이슈번호/작업자 이름`

3. **PR 및 코드 리뷰**
    - 작업 완료 후 Pull Request(PR) 생성
    - 팀원 간 코드 리뷰 진행

4. **Merge 및 브랜치 정리**
    - 리뷰 완료 후 `dev` 브랜치로 Merge
    - Merge 후 이슈 브랜치 삭제
    - `dev` 브랜치 최신 상태 유지

---

### 🧑‍💻 코딩 컨벤션

### Git 컨벤션
1. **Commit 메시지 형식**
   - [이모지][타입] 커밋 메시지
   - 예: `♻️ feat:사용자 로그인 기능 구현`

2. **PR 제목 및 설명**
   - 제목: `[타입] 이슈번호 - 작업 내용 요약`
   - 본문: 작업 내용, 고려한 사항, 테스트 방법 등 기재

3. **Branch 명명 규칙**
   - `타입/#이슈번호/작업자 일름`
   - 예:`feat/#15/홍길동`

4. **Issue 제목 규칙**
   - `[타입] 작업 내용 요약`

---

### 코드 스타일

1. **스타일 가이드**
   - [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) 적용

2. **DTO 작성 기준**
   - 요청(Request) / 응답(Response) DTO 분리
   - `mapper`를 활용한 변환 로직 구성
  
3. **커스텀 예외 처리**
   - 서비스 로직에서 발생하는 예외는 커스텀 예외로 처리

4. **이름 규칙**
   - **클래스명**: 대문자로 시작, 명확한 의미 전달
       - 예: `UserService`, `OrderController`
   - **메서드명**: 동사 + 대상, camelCase 사용
       - 예: `createUser()`, `getOrderList()`
   - **변수명**: camelCase 사용, 명확하고 간결하게
       - 예: `userId`, `orderRequest`

## 🗂️ APIs

작성한 API는 아래에서 확인할 수 있습니다.

<details>
<summary> 👉🏻 API 바로보기 </summary>
<div markdown="1">
  
![image](https://github.com/user-attachments/assets/03e69da3-10ce-442c-b1ff-2ed8f50d4c2a)
![image](https://github.com/user-attachments/assets/4dde9255-5dbb-4628-a3ab-4c114e78da68)

  
</div>
</details>

## 🤔 기술적 이슈와 해결 과정
- 각 메서드의 파라미터에 대한 예외처리 과정을 개선하기 위해 Apache Commons의 util 메서드 활용
- Random의 성능 이슈를 고려하여 SecureRandom으로 대체
  - 회원가입 시, 이메일 인증을 위한 무작위 인증 코드 생성
- TaskScheduler를 이용한 결제 미완료 예매 자동 취소 스케줄링
  - TaskScheduler는 메모리 기반 으로 동작하여 서버 종료 시 스케줄 정보가 모두 사라짐
  - 스케줄 정보를 DB에 저장하여, 서버 재시작 시에도 스케줄링 정보를 복원할 수 있도록 해결 
<br />
