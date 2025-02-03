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
**프로젝트 일정: 2025년 1월 8일 ~ 2025년 1월 24일**


### 목차

- [1. 프로젝트 개요](#1-프로젝트-개요)
- [2. 요구사항 명세서](#2-요구사항-명세서)
- [3. WBS ](#3-WBS)
- [4. DB 모델링 ](#4-DB-모델링)
- [5. UI 설계 ](#5-UI-설계)
- [6. 백엔드 테스트 결과 ](#6-백엔드-테스트-결과)
- [7. 프론트엔드 테스트 결과 ](#7-프론트엔드-테스트-결과)
- [8. 느낀점 및 성과 ](#8-느낀점-및-성과)

---

## 🍀1. 프로젝트 개요

CommodeAmi는 프랑스어로 "Commode(편리한)"와 "Ami(친구)"의 결합으로, 영화에 관한 정보를 편리하게 검색하고 기록할 수 있는 시스템입니다. 

### 1.1. 프로젝트 소개

**영화에 관한 정보를 편리하게 얻어보세요.**
- ✔️ 궁금한 영화 정보를 검색해 보세요.
- ✔️ 관람한 영화에 대하여 별점 및 리뷰를 남겨보세요.
- ✔️ 다른 사람의 별점과 리뷰를 확인해보세요.
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

## 🍀3. WBS

## 🍀4. DB 모델링

<details>
  <summary>회원</summary>
   <img src="https://github.com/user-attachments/assets/deed946a-9de4-4cd1-af08-7ddf6b729324" alt="회원">
 </details>

<details>
  <summary>영화</summary>
   <img src="https://github.com/user-attachments/assets/7bc41e2d-38a5-48b6-8e84-8fa748c65fb4" alt="영화">
 </details>

 <details>
  <summary>리뷰</summary>
   <img src="https://github.com/user-attachments/assets/64c35381-5c93-4764-8236-d9a123e90a4a" alt="리뷰">
 </details>

   <details>
  <summary>별점</summary>
   <img src="https://github.com/user-attachments/assets/9bd2f285-b607-4979-92ef-7f1943dcff4b" alt="별점">
 </details>

  <details>
  <summary>커스텀 티켓</summary>
   <img src="https://github.com/user-attachments/assets/efb7f131-2077-47f1-b2c1-078adcca8457" alt="커스텀 티켓">
 </details>

 ## 🍀5. UI 설계
 
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

 ## 🍀6. 백엔드 테스트 결과 

 ### 6.1 회원
 
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

 ### 6.2 영화
 
 <details>
  <summary>영화</summary>

  - <details>

    <summary>영화 정보 조회</summary>
     <img src="https://github.com/user-attachments/assets/cf1e9c67-0697-48e8-9c51-90294b9a1917" alt="영화 정보 조회">

  </details>


   ### 6.3 별점
 
 <details>
  <summary>별점</summary>

  - <details>

    <summary>별점 생성</summary>
     <img src="https://github.com/user-attachments/assets/243a039b-414c-4235-af93-c17ee02a006f" alt="별점 생성">

  - <details>

    <summary>별점 수정</summary>
    <img src="https://github.com/user-attachments/assets/5facfa3e-c519-45b6-9d28-06e43cb73c5a" alt="별점 수정">

  </details>

  ### 6.4 리뷰
 
 <details>
  <summary>리뷰</summary>

  - <details>

    <summary>리뷰 생성</summary>
    <img src="https://github.com/user-attachments/assets/ae3c0187-242d-45c1-91a1-15ba7be024ac" alt="별점 수정">

  - <details>

    <summary>리뷰 수정</summary>
         <img src="https://github.com/user-attachments/assets/80e9d576-6d96-4790-add3-5566ca309029" alt="리뷰 수정">

  </details>

  ### 6.5 커스텀 티켓
 
 <details>
  <summary>커스텀 티켓</summary>

  - <details>

    <summary>커스텀 티켓 생성</summary>
         <img src="https://github.com/user-attachments/assets/bbca2032-b4df-4dc5-8036-2e9d336bc65a" alt="커스텀 티켓 생성">

  - <details>

    <summary>커스텀 티켓 삭제</summary>
         <img src="https://github.com/user-attachments/assets/b46ff4b3-536b-4961-97bc-671ee00ee9f6" alt="커스텀 티켓 삭제">

  </details>

  ### 6.6 마이페이지
 
 <details>
  <summary>마이페이지</summary>

  - <details>

    <summary>마이페이지 조회</summary>
          <img src="https://github.com/user-attachments/assets/99ed0aa6-e5b8-417f-8a8d-966c370ae026" alt="마이페이지 조회">

  </details>


  ### 6.7 검색
  <details>
  <summary>검색</summary>

  - <details>

    <summary>영화 검색</summary>
     <img src="https://github.com/user-attachments/assets/3491d006-477b-432c-a29b-017db7ec8bef" alt="영화 검색">
  </details>

## 🍀7. 프론트엔드 테스트 결과 

## 🍀8. 느낀점 및 성과
#### 김시우
> 캡스톤 디자인이라는 과목 덕분에 처음으로 웹프로젝트를 진행하게 되었다. 이번에는 프론트를 맡아 진행하면서 흥미를 느끼고 뚜렸한 진로가 없었던 나에게 정할 수 있게 된 계기가 된 것같다. 이 후 현재 프로젝트의 문제점을 보완하고 백엔드의 경우에는 Java를 사용하여 고도화를 함으로써 풀스택을 도전해보고 싶다.

