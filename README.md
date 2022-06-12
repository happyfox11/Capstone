
# 📋 Intro
AI기반 음식 분석 애플리케이션, 플랜밀

<p align="center">
  <img src = "https://user-images.githubusercontent.com/56522878/170629000-4a237356-9c32-414a-bf45-af9f039a36d7.jpg" width="700px">

</p>

## 🌞 Our Team
🌱 어니스트(Honest)

* 팀장 박주희([@juhee77](https://github.com/juhee77 "github link"))

* 팀원 변정원([@Byungul](https://github.com/Byungul "github link"))

* 팀원 이지수([@dlwltn0350](https://github.com/dlwltn0350 "github link"))

* 팀원 박한별([@happyfox11](https://github.com/happyfox11 "github link"))

* 팀원 박소영([@kdmsws](https://github.com/kdmsws "github link")) <br><br>

## 🗂 Content

1. [🔈 프로젝트 소개]
   <br>
   - [📑 개요 및 목표]
   - [📑 개발 환경]
   - [📑 기술 스택]
   <br><br>
2. [🔈 구현 결과]
   <br><br>

---

## 🥜 프로젝트 소개

### 🔔 개요 및 목표

#### 🧿 개요

    생활이 바쁘고 코로나19 사태로 인해 배달 음식 주문이 증가함
    영양분 불균형 섭취, 만성질환 유발 등으로 균형있는 영양소 섭취가 필요함

#### 🏃‍ 목표
    ✔ AI 머신러닝 기술을 활용해 사진으로 식단 기록
    ✔ 사용자 영양 균형에 도움
    ✔ 섭취 영양소를 바탕으로 음식 추천 기능 제공
    ✔ 기존 식단 애플리케이션 개선

### 🔨 개발 환경

- OS : Windows 10

- Server : Naver Cloud Platform

  - Ubuntu Server 18.04 (64-bit)

- Backend

  - Java : Java 1.11.0
  - Framework : SpringBoot 2.6.4
  - ORM : JPA(Hibernate)
  - Tomcat : Tomcat 9 (Ubuntu)
  - IDE : Intellij 2021.3.3
  - Dependency tool : gradle-7.4.1
  - Database : MySQL Ver 14.14 Distrib 5.7.37 (Ubuntu)

- Frontend 

    - Java : Java 1.15.0
    - Dependency tool : gradle-7.0.4
    - IDE : Android Studio

- AI

    - Python : 3.10
    - IDE : Pycharm 2021.3.2
    - Flask : 2.1.2
    
  <br><br>



## 🍴 주요 기능 설명
<br>
<br>
<p align="center">
  <img src = "https://user-images.githubusercontent.com/89199587/170630038-cb7b41eb-49b4-4df4-b5be-ce4623255c59.png" width="800px">
</p>
<br>
<br>

1. 회원 정보 관리
- 카카오 또는 구글 소셜 로그인 (개인 정보 수집 동의 후 회원가입 진행)
- 사용자의 기초 정보 수집 : 사용자 평균적인 기초대사량을 체크하기 위한 개인정보(나이, 신장, 몸무게), 목표칼로리(선택) 입력
- 개인 정보 확인 및 수정

<br>

2. 영양 관리
- 메인페이지에서 카메라, 갤러리로 음식 입력, 수기로 입력 선택 가능
- 카메라나 갤러리로 이미지를 입력하면, AI 분석을 통해 이미지에 대한 음식 결과 도출 후 영양 분석 페이지로 이동
- 영양 분석 페이지
  - 끼니별로 구분하여 저장할 수 있고 섭취량(인분) 조절이 가능
  - 이미지 인식이 잘 못 되었을 경우나 음식을 수정하고 싶을 경우를 대비하여 수정하기 버튼 존재
  - 음식 추가, 삭제 가능
- 상세 영양 분석 페이지 : 각 세부 음식에 대한 3대 영양소 이외의 영양 정보를 확인 가능

<br>

3. 식단 추천 관리
- 하나 이상의 음식을 섭취했을 경우, 사용자가 섭취한 데이터를 기반으로 음식을 추천
- 쇼핑하기 버튼을 누르면 온라인 쇼핑몰 선택창이 뜨며 해당 uri로 이동
- 레시피하기 버튼을 누르면 만개의 레시피 uri로 이동

<br>

4. 통계 관리
- 과거 기록보기 버튼으로 캘린더를 실행하고 과거 날짜에 대한 데이터를 불러옴
- 메인페이지에서 3대 영양소에 대한 비율 그래프를 확인 가능
- 주간 페이지 : 좌우 스크롤로 이동하며 페이지 단위로 표시
  - 1페이지에서 지난주 칼로리 소비량을 막대 그래프로 표시, 지지난주 대비 섭취       평균 칼로리를 확인 가능
  - 2페이지에서 지난주 3대 영양소 섭취 비율을 꺾은선 그래프로 표시, 평균 3대 영      양소 섭취량을 섭취 부족, 적정, 과잉으로 표시
  - 3페이지에서 지난주 섭취한 식단들을 그리드 형식으로 확인 가능
  - 4페이지에서 사용자의 주간 활동 기록을 확인 가능 

<br>

|                로그인                |             기본정보입력             |             개인정보수정            |
|:-------------------------------------:|:-------------------------------------------------:|:-----------------------------------------------:|
|![login](https://user-images.githubusercontent.com/56522878/173003261-bedb9b73-e897-4a18-93d0-202fdc90a5f6.png)|![init](https://user-images.githubusercontent.com/56522878/173002744-dada2409-242d-4a94-b260-519e21304e0a.png)|![modify](https://user-images.githubusercontent.com/56522878/173002766-e196cb91-e583-49af-acbb-4ffbe94cf961.png)|
