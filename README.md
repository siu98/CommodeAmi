# CommodeAmi

## 🤝TEAM

| <img src="https://github.com/user-attachments/assets/46fa9b3a-4359-4e59-be8e-dd33561056c1" width="200" height = "160"> |
|:---------------------------------------------------------------:| 
|               김시우              |           
|[![GitHub Link](https://img.shields.io/badge/GitHub-Link-black?style=for-the-badge&logo=github)](https://github.com/siu98) | 

## 🛠️기술스택
### 1. Backend
| JAVA |  SpringBoot | Spring Security | Hibernate | Redis | Postman |
| :-----------------------------------:| :-----------------------------------: | :--------------------: | :---------------------: | :---------------------: | :---------------------: |
| ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=flat-square&logo=openjdk&logoColor=white) |  ![springboot](https://img.shields.io/badge/springboot-6DB33F?style=flat-square&logo=springboot&logoColor=white) | ![SpringSecurity](https://img.shields.io/badge/SpringSecurity-6DB33F?style=flat-square&logo=SpringSecurity&logoColor=white) | ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=flat-square&logo=Hibernate&logoColor=white) | ![Redis](https://img.shields.io/badge/Redis-FF4438?style=flat-square&logo=Redis&logoColor=white) | ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=Postman&logoColor=white)

### 2. Frontend
| HTML 5 | CSS 3 | JavaScript | Node.js | React | PrimeReact |
| :-----------------------------------:| :-----------------------------------: | :-----------------------------------: | :-----------------------------------: | :-----------------------------------: | :-----------------------: |
| ![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=HTML5&logoColor=white) | ![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=flat-square&logo=CSS3&logoColor=white) | ![JAVASCRIPT](https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=JavaScript&logoColor=white) | ![Node.js](https://img.shields.io/badge/Node.js-5FA04E?style=flat-square&logo=Node.js&logoColor=white) | ![React](https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=React&logoColor=white) |  ![PrimeReact](https://img.shields.io/badge/PrimeReact-03C4E8?style=flat-square&logo=PrimeReact&logoColor=white) |


### 3. Database
| MariaDB |
| :-----------------------------------:|
| ![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=MariaDB&logoColor=white) |

## 📋전체 프로젝트 일정
**프로젝트 일정: 2024년 12월 29일 ~ 2025년 1월 31일**


### 목차

- [1. 프로젝트 개요](#1-프로젝트-개요)
- [2. 요구사항 명세서](#2-요구사항-명세서)
- [3. DB 모델링 ](#3-DB-모델링)
- [4. UI 설계 ](#4-UI-설계)
- [5. 백엔드 테스트 결과 ](#5-백엔드-테스트-결과)
- [6. 프론트엔드 테스트 결과 ](#6-프론트엔드-테스트-결과)
- [7. 느낀점 및 성과 ](#7-느낀점-및-성과)

---

## 🍀1. 프로젝트 개요

CommodeAmi는 프랑스어로 "Commode(편리한)"와 "Ami(친구)"의 결합으로, 영화에 관한 정보를 편리하게 검색하고 기록할 수 있는 시스템입니다. 

### 1.1. 프로젝트 소개

**영화에 관한 정보를 편리하게 얻어보세요.**
- ✔️ 궁금한 영화 정보를 검색해 보세요.
- ✔️ 관람한 영화에 대하여 별점 및 리뷰를 남겨보세요.
- ✔️ 다른 사람의 별점과 리뷰를 확인해보세요.
- ✔️ 날씨 추천 시스템을 활용해보세요.
- ✔️ 이미지를 업로드하여 커스텀 티켓을 만들어보세요.

### 1.2. 프로젝트 배경 

최근 OTT 및 영화를 시청하는 사람들이 늘어나고 있다. 하지만 영화의 정보를 한 번에 모아볼 수 없어서 불편하다고 생각하였습니다. 이를 통하여 영화의 정보를 한 눈에 확인하고 추가로 영화를 기록할 수 있는 사이트를 만들고자 하였습니다.

<img src="https://github.com/user-attachments/assets/7d161da6-a301-42c3-9cbd-8b28871c6b8e" alt="배경">

[출처] 배한님. (2024년 03월 13일). 국민 70% 이상이 OTT 본다…방송시장 성장세 둔화. 머니투데이. https://m.mt.co.kr/renew/view.html?no=2024031316393810339

     
<img src="https://github.com/user-attachments/assets/88ccaf3d-9c89-4064-ad40-9d98bec6b43f" alt="배경">

[출처] 김형호. (2024년 07월 01일). 상반기 영화관객 3년 연속 증가, 코로나19 이후 최다 관객. 이코노믹리뷰. https://www.econovill.com/news/articleView.html?idxno=659133


### 1.3. 국내외 유사 서비스와 차별성
#### 1.3.1 왓차피디아
영화, 책등의 정보를 얻을 수 있는 사이트로, 별점 및 리뷰 기능을 통하여 다른 회원들과 영화에 관하여 소통을 할 수 있습니다. 특히 별점을 0.5~5.0 사이로 매길 수 있어서 영화를 평가하는데 있어서 직관적이라고 할 수 있습니다.

#### 1.3.2 키노라이츠
영화 커뮤니티로, 별점 및 리뷰 기능이 있지만, 특히 별점의 경우 표시하는데 한계가 있어 이 영화가 회원 본인에게 잘 맞는 영화인지 알아가는데 어려움이 있습니다.

#### 1.3.3 commodeami
영화 사이트로, 영화의 정보를 얻고 별점 및 리뷰를 남길 수 있습니다. 영화에 관한 youtube 리뷰 영상을 제공함으로써 사이트 내부에서 해결할 수 있게 하였습니다. 또한, 회원이 사용하고 싶은 이미지를 이용해 티켓을 제작하여 소장할 수 있는 기능을 사용하였습니다.

<img width="471" alt="스크린샷 2025-01-07 오전 11 31 57" src="https://github.com/user-attachments/assets/6d824845-c8cd-46ff-8f6f-0b0b2dd0d840" />

## 🍀2. 요구사항 명세서

<details>
  <summary>회원</summary>
   <img alt="회원" src="https://github.com/user-attachments/assets/7cabf855-7fae-458d-99d8-82998411c34f" >
 </details>

<details>
  <summary>영화</summary>
     <img src="https://github.com/user-attachments/assets/d1be310d-931d-480f-b787-75c9c0b5f57f" alt="영화" >
 </details>

 <details>
  <summary>리뷰</summary>
      <img src="https://github.com/user-attachments/assets/b4369072-1a24-4c8a-ba05-b04f715188ce"  alt="리뷰" >
 </details>

  <details>
  <summary>커스텀 티켓</summary>
     <img src="https://github.com/user-attachments/assets/4b0c4e94-78a4-4af7-a6e2-f6dfc6c821e5"  alt="커스텀 티켓">
 </details>

## 🍀3. DB 모델링

<details>
  <summary>회원</summary>
   <img src="https://github.com/user-attachments/assets/ed291e2a-f017-4961-ad33-0350032a9ed8" alt="회원">
 </details>

<details>
  <summary>영화</summary>
   <img src="https://github.com/user-attachments/assets/f350ffe6-f939-4783-af86-fa8c26949588" alt="영화">
        <img src="https://github.com/user-attachments/assets/a1dc44ca-de06-4392-ae58-b6d01b1b7ce9" alt="베역">
        <img src="https://github.com/user-attachments/assets/34031acb-9c6d-43f1-bbfa-6971b9a1f554" alt="배우">
 </details>

 <details>
  <summary>리뷰</summary>
   <img src="https://github.com/user-attachments/assets/55a38dc8-e28c-4e3a-bdc6-c4192e2a1861" alt="리뷰">
 </details>

   <details>
  <summary>별점</summary>
   <img src="https://github.com/user-attachments/assets/6b552e66-29a1-45f9-af17-762554193596" alt="별점">
 </details>

<details>
  <summary>평균 별점</summary>
   <img src="https://github.com/user-attachments/assets/309f1a0c-a98e-492d-88f9-7a0264ff5770" alt="평균 별점">
 </details>

  <details>
  <summary>커스텀 티켓</summary>
   <img src="https://github.com/user-attachments/assets/55769ea8-a6db-4123-ab66-6813e8a066bd" alt="커스텀 티켓">
 </details>

 ## 🍀4. UI 설계
 
<details>
  <summary>메인화면</summary>
  
- <details>

  <summary>로그인 전</summary>
   <img src="https://github.com/user-attachments/assets/c9d95d12-3c59-4617-95fa-ac9ced8bf121" alt="로그인 전">

- <details>

  <summary>로그인 후</summary>
   <img src="https://github.com/user-attachments/assets/8b10174c-a499-428d-9d07-1c7fa3acfc2c" alt="로그인 후">
</details>

<details>
  <summary>회원가입</summary>
   <img src="https://github.com/user-attachments/assets/cb14afb0-689d-4c2a-b5ac-796e29855f7c" alt="회원가입">
 </details>

 <details>
  <summary>로그인</summary>
   <img src="https://github.com/user-attachments/assets/4e8f1a87-5dc8-4f51-88c9-ac6e3add8ddb" alt="로그인">
 </details>

 <details>
  <summary>별점</summary>
   <img src="https://github.com/user-attachments/assets/f4ee6d17-68cd-46ad-b2bb-6cbb8dee5427" alt="별점">
      <img src="https://github.com/user-attachments/assets/9af9c6bb-42a5-49d1-b0cb-3c457f7556ab" alt="별점">
 </details>

  <details>
  <summary>리뷰</summary>
   <img src="https://github.com/user-attachments/assets/3f236ba4-ac32-48db-b49d-872d4a19a2d3" alt="리뷰">
 </details>


  <details>
  <summary>영화 상세페이지</summary>
   <img src="https://github.com/user-attachments/assets/e3c4fb62-b2e5-4a0d-993f-2ecfbc543c4b" alt="영화 상세페이지1">
       <img src="https://github.com/user-attachments/assets/5828a641-7093-41c9-a8e4-b089280ea16d" alt="영화 상세페이지2">
 </details>

 <details>
  <summary>커스텀 티켓</summary>
   <img src="" alt="커스텀 티켓">
 </details>

  <details>
  <summary>마이페이지</summary>
   <img src="https://github.com/user-attachments/assets/c920633e-f6c2-4541-9470-741c043be875" alt="마이페이지">
 </details>

 ## 🍀5. 백엔드 테스트 결과 

 ### 5.1 회원
 
 <details>
  <summary>회원</summary>

  - <details>

    <summary>회원가입</summary>
       <img src="https://github.com/user-attachments/assets/c5e4cb7f-df10-4f4a-9967-6516eba7316b" alt="회원가입">

  - <details>

    <summary>로그인</summary>
    <img src="https://github.com/user-attachments/assets/8bb6430c-1e9a-465c-8af0-8e6b6f31e221" alt="로그인">

  - <details>

    <summary>비밀번호 수정</summary>
     <img src="https://github.com/user-attachments/assets/9bed72b5-410e-4cfd-b62a-15c461b92b74" alt="비밀번호 수정">

  - <details>

    <summary>비밀번호 변경</summary>
     <img src="https://github.com/user-attachments/assets/320d306b-7309-470b-b726-1dbe5627ef01" alt="비밀번호 변경">

  - <details>

    <summary>로그아웃</summary>
     <img src="https://github.com/user-attachments/assets/8b8350fc-9397-48e5-9894-6ead64111466" alt="로그아웃">

  </details>

 ### 5.2 영화
 
 <details>
  <summary>영화</summary>

  - <details>

    <summary>영화 정보 조회</summary>
     <img src="https://github.com/user-attachments/assets/cf1e9c67-0697-48e8-9c51-90294b9a1917" alt="영화 정보 조회">

  </details>


   ### 5.3 별점
 
 <details>
  <summary>별점</summary>

  - <details>

    <summary>별점 생성 및 수정</summary>
     <img src="https://github.com/user-attachments/assets/1f688607-6a5e-4424-acff-bfe0278af039" alt="별점 생성 및 수정">

  - <details>

    <summary>모든 별점 조회</summary>
    <img src="https://github.com/user-attachments/assets/c4f00ac2-b0d5-4f5b-bf58-62b20b1f355f" alt="모든 별점 조회">

  - <details>

    <summary>해당 유저의 별점 모두 조회</summary>
    <img src="https://github.com/user-attachments/assets/17c78b05-0088-456e-8c48-6c307bc2592c" alt="해당 유저의 별점 모두 조회">

  - <details>

    <summary>해당 유저의 득정 영화 별점 조회</summary>
    <img src="https://github.com/user-attachments/assets/2931c47c-7eea-4698-8488-ff472f1cca61" alt="해당 유저의 득정 영화 별점 조회">

  </details>

  ### 5.4 리뷰
 
 <details>
  <summary>리뷰</summary>

  - <details>

    <summary>리뷰 생성 및 수정</summary>
    <img src="https://github.com/user-attachments/assets/c171ce4f-5cb0-49cb-9623-68700f0b8717" alt="리뷰 생성 및 수정">

  - <details>

    <summary>리뷰 삭제</summary>
         <img src="https://github.com/user-attachments/assets/ad7e446a-6f77-4c6e-a4ad-a43f5a981d98" alt="리뷰 삭제">

  - <details>

    <summary>전체리뷰 조회</summary>
         <img src="https://github.com/user-attachments/assets/f66967d6-6451-4864-98f0-9b84a1b0ed75" alt="전체리뷰 조회">


  </details>

  ### 5.5 커스텀 티켓
 
 <details>
  <summary>커스텀 티켓</summary>

  - <details>

    <summary>커스텀 티켓 생성</summary>
         <img src="" alt="커스텀 티켓 생성">

  - <details>

    <summary>커스텀 티켓 삭제</summary>
         <img src="" alt="커스텀 티켓 삭제">

  - <details>

    <summary>커스텀 티켓 전체조회</summary>
         <img src="https://github.com/user-attachments/assets/f5d54d5e-3325-47ad-9064-b15fe5dcb6dd" alt="커스텀 티켓 전체조회">

  - <details>

    <summary>해당 유저의 커스텀 티켓 조회</summary>
         <img src="https://github.com/user-attachments/assets/1a649c74-49fb-4966-9f5b-2c8efdb3825e" alt="해당 유저의 커스텀 티켓 조회">

  </details>

  ### 5.6 평균별점
 
 <details>
  <summary>평균별점</summary>

  - <details>

    <summary>평균별점 조회</summary>
          <img src="https://github.com/user-attachments/assets/fea02a29-a7f7-4898-b485-038a56281ba7" alt="평균별점 조회">

  - <details>

    <summary>해당 영화의 평균별점 조회</summary>
          <img src="https://github.com/user-attachments/assets/202d8566-bf31-45d4-a9f4-fd22f6bd1586" alt="해당 영화의 평균별점 조회">

  </details>


  ### 5.7 검색
  <details>
  <summary>검색</summary>

  - <details>

    <summary>영화 검색</summary>
     <img src="https://github.com/user-attachments/assets/3491d006-477b-432c-a29b-017db7ec8bef" alt="영화 검색">
  </details>

## 🍀6. 프론트엔드 테스트 결과 
### 6.1 회원
 
 <details>
  <summary>회원</summary>

  - <details>

    <summary>회원가입</summary>
       <img src="https://github.com/user-attachments/assets/2fb48e0a-790d-4dbe-9932-ef14cd8a34b7" alt="회원가입">

  - <details>

    <summary>로그인</summary>
    <img src="https://github.com/user-attachments/assets/3713e40b-ef65-4dda-b190-2442e79ecbca" alt="로그인">

  - <details>

    <summary>비밀번호 찾기</summary>
     <img src="https://github.com/user-attachments/assets/220c77b7-b764-44c3-8aaf-1a44e4ce1465" alt="비밀번호 찾기">

  - <details>

    <summary>비밀번호 변경</summary>
     <img src="https://github.com/user-attachments/assets/68f20f13-b661-47bc-803f-4538cb9ed4ad" alt="비밀번호 변경">

  - <details>

    <summary>로그아웃</summary>
     <img src="https://github.com/user-attachments/assets/366f96bc-4a2f-44b3-832c-3df482ece892" alt="로그아웃">

  </details>

 ### 6.2 영화
 
 <details>
  <summary>영화</summary>

 - <details>

    <summary>박스오피스 순위(Top10)</summary>
     <img src="https://github.com/user-attachments/assets/1153490b-1e59-4a37-9b2b-96ddfb055e56" alt="박스오피스 순위(Top10)">



  - <details>

    <summary>영화 정보 조회(배우&스틸컷)</summary>
     <img src="https://github.com/user-attachments/assets/60e09b35-2f83-49b6-aa4f-01ef38e3f001" alt="영화 정보 조회(배우&스틸컷)">


- <details>

    <summary>영화 정보 조회(트레일러&리뷰영상)</summary>
     <img src="https://github.com/user-attachments/assets/c48cfa19-88ef-4b51-86e0-855699d3f700" alt="영화 정보 조회(트레일러&리뷰영상)">

</details>

  ### 6.3 별점
 
 <details>
  <summary>별점</summary>

  - <details>

    <summary>별점 생성</summary>
     <img src="https://github.com/user-attachments/assets/ccba92db-a16e-4d2d-887c-eb83d77e42f7" alt="별점 생성">

  - <details>

    <summary>별점 수정</summary>
    <img src="https://github.com/user-attachments/assets/a21f37f3-1667-4322-82ef-6dad400e1e76" alt="별점 수정">

  </details>

  ### 6.4 리뷰
 
 <details>
  <summary>리뷰</summary>

  - <details>

    <summary>리뷰 생성</summary>
    <img src="https://github.com/user-attachments/assets/e947816a-f374-4e23-bb1f-ede8ac1f19f0" alt="리뷰 생성">

  - <details>

    <summary>리뷰 수정</summary>
         <img src="https://github.com/user-attachments/assets/a39f0c5a-75e0-4f87-9a85-abb94f7262b6" alt="리뷰 수정">


  </details>

  ### 6.5 커스텀 티켓
 
 <details>
  <summary>커스텀 티켓</summary>

  - <details>

    <summary>커스텀 티켓 생성</summary>
         <img src="https://github.com/user-attachments/assets/49cf510b-1e4e-4df8-a52d-732a32e4cb03" alt="커스텀 티켓 생성">

  - <details>

    <summary>커스텀 티켓 삭제</summary>
         <img src="https://github.com/user-attachments/assets/066fa050-f96b-4262-88c1-ae7945734ade" alt="커스텀 티켓 삭제">

  </details>

  ### 6.6 평균별점
 
 <details>
  <summary>평균별점</summary>

  - <details>

    <summary>평균별점 높은영화</summary>
          <img src="https://github.com/user-attachments/assets/373dcd82-5032-448f-ad36-265c45ff4357" alt="평균별점 높은영화">

  </details>


  ### 6.7 검색
  <details>
  <summary>검색</summary>

  - <details>

    <summary>영화 검색</summary>
     <img src="https://github.com/user-attachments/assets/deb8ad7c-a893-496e-ae47-9167d32f07eb" alt="영화 검색">
  </details>

  ### 6.8 추천
  <details>
  <summary>추천</summary>

  - <details>

    <summary>날씨기반 랜덤 추천</summary>
     <img src="https://github.com/user-attachments/assets/86112819-09af-41b2-accf-2d3d61c0bc83" alt="날씨기반 랜덤 추천">
  </details>
  
## 🍀7. 느낀점 및 성과

> 캡스톤 디자인에서 진행했던 프로젝트를 혼자서 고도화를 진행하게 되었다. 개인 프로젝트로서 프론트 부분에서는 Redux등을 사용해서 고도화를 진행하고 Java를 사용하여 Spring Boot로 코드를 짜는 것이 재미있고 보람찼던 것 같다. 확실히 양쪽을 모두 하다 보니까 시간은 두 배로 들지만 Front-End와 Back-End를 같이 하는것도 재밌다는 생각이 들었고 이를 통해서 더욱 성장하게 된 것 같다.   
> 프로젝트를 진행하면서 추가할 요소들이 생겼던 것 같은데 이런 부분을 추후 천천히 고도화 할 예정이다.

