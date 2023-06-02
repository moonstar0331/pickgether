# 👍 Pickgether
![Cover](https://github.com/Kim-Moon-Sung/capstone-pickgether/assets/103942182/d562261d-96d8-4944-83a4-6ded4f8635ae)

🔗 **Link**: https://pickgether.shop *(현재 서비스 중)*

---
## 💡 Topic
&nbsp;「장래가구추계: 2020~2050년」 조사에 따르면 현재 1인 가구 비율은 점차 증가하는 추세이며 2050년에는 전체 인구의 39.6%가 1인 가구일 것으로 예상하고 있다. 이런 1인 가구를 위해 __SNS__ 가 사회관계 결핍을 해소하고 부정적인 감정을 해소시켜 준다는 연구결과가 존재한다.<br>
&nbsp;또한, 최근 들어 MZ세대의 자아실현욕구가 높아지고 퍼스널 브랜딩이 트렌드로 부상함에 따라 퍼스널 브랜딩이 가능한 플랫폼에 대한 수요가 증가하고 있다. 이에 효과적인 퍼스널 브랜딩 을 위해 단순히 소비하는 콘텐츠보다는 __참여형 콘텐츠__ 가 효과적이라는 연구결과가 있다.
<details>
    <summary>참고자료</summary>

[1] David A. Cole, Elizabeth A. Nick, Rachel L. Zelkowitz, Kathryn M. Roeder and Tawny Spinelli, “Online social support for young people: Does it recapitulate in-person social support; can it help?”, Computers in Human Behavior, Vol. 68, NO. -, pp. 456-464, 03, 2017

[2] Byungwook Bae, “In the era of the 4th industrial revolution, create yourself through personal branding”, Korean Association of Converging Business Review, Vol. 9, NO. -, pp. 31-34, 03, 2020

[3] Mira Mayrhofer, Jörg Matthes, Sabine Einwiller and Brigitte Naderer, “User generated content presenting brands on social media increases young adults' purchase intention”, International Journal of Advertising,Vol. 39, No. 1, pp. 166-186, 01, 2020

[4] Sangmin Kim and Hyo Won Lee, “Establishing Digital Trust: The Analysis of SNS and Citizen Participation”, Korea Journal of Information Society, Vol. 23, No. 2, pp. 185-219, 08, 2022

[5] Xin Chen, Zhenfeng Cheng and Gyu-bae Kim, “서비스기업의 관계마케팅 활동이 고객시민행동에 미치는 영향 -고객에 대한 공감의 조절효과를 중심으로”, Journal of the Aviation Management Society of Korea, Vol. 2016, No. -, pp. 27, 2016

</details>


## 📝 Summary
&nbsp;앞선 사회적 이슈에 따라 __1인 가구의 사회관계 결핍을 해소__ 하고 __퍼스널 브랜딩__ 을 위해 __참여형 콘텐츠를 제공하는 투표기반의 참여형 sns 웹 어플리케이션__ 을 제작하고자 하였다.<br>
&nbsp;해당 서비스의 핵심기능은 크게 3가지로 **투표 생성, 참여, 분석**을 제공한다. 위 기능을 수행하기 위해 첫째, 투표 생성 시 사용자가 원하는 정보를 얻을 수 있도록 일부 사용자로 **참여를 제한**하는 기능을 제공한다. 둘째, 참여 과정에서 사용자와 의견을 주고 받는 **상호작용**을 지원한다. 셋째, **분석자료**를 시각적으로 제공할 뿐만 아니라 CSV 파일을 다운로드 받아 2차적으로 활용하도록 지원한다.


## ⭐️ Key Function
|**ID**|**서비스명**|**설명**|
|:------:|:---:|---|
|1|**회원 서비스**|- 회원가입/수정/삭제<br>- 타 플랫폼 계정인증 서비스|
|2|**투표 서비스**|-투표 생성<br>- 결과 분석<br>- 분석파일 제공|
|3|**댓글 서비스**|- 댓글 작성/수정/삭제<br>- 댓글 공감|
|4|**편의 기능**|- 북마크<br>- 투표 참여 제한<br>- 게시글 검색<br>- 투표 참여자 리스트|

## 💻 Development Environment
- IntelliJ IDEA Ultimate 2022.03
- Java 11
- Spring Boot 2.7.7
- Gradle 7.6
- AWS EC2 Ubuntu 22.04.2 LTS
- Docker 20.10.22
- MariaDB:10
- Redis:alpine


## 🛠 Tech Stack
|**종류**|**사용 도구**|
|:------:|---|
|**Backend**|Java, Spring Boot, Spring Web, Spring Security, Spring Data JPA<br>Junit5, Mockito, Gradle, Hadoop|
|**Frontend**|Thymeleaf, Html, Css, JavaScript|
|**DevOps & Tool**|Maria DB, Redis, AWS EC2, AWS S3, GitHub Actions, Docker, IntelliJ, GitKraken|


## 🔧 Architecture & development process
- **Architecture** : Spring MVC
- **development process** : Agile


## ⚙️ Structure of system
![structure](https://github.com/Kim-Moon-Sung/capstone-pickgether/assets/103942182/a5504d6e-4f05-4d67-a10d-6a9ef814fcc6)


## 🤚🏻 Part
**이지훈** <a href= "https://github.com/leejihoon0312"><img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/></a><br>
- [팀장] 백엔드, 보고서, 발표
   
**김문성** <a href= "https://github.com/Kim-Moon-Sung"><img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/></a><br>
- [개발 리더] 백엔드, 일정관리, 브랜치 관리

**정진주** <a href= "https://github.com/Ness731"><img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/></a><br>
- 프론트 / 백엔드, UI 디자인, PPT

**조희연** <a href= "https://github.com/chy0503"><img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/></a><br>
- 프론트 / 백엔드, 다이어그램 작성

**박시은** <a href= "https://github.com/sieunp06"><img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/></a><br>
- 프론트, UI 디자인


## 🤔 Learned
**1. 테스트 코드의 중요성**<br>
<p>테스트 코드를 작성함으로써 버그를 조기에 발견할 수 있었다. 또한, 코드 변경으로 인해 기존 기능이 영향이 있는지 없는지 확인할 수 있었다.

**2. 함수형 프로그래밍**<br>
<p>함수형 프로그래밍을 통해 코드의 길이가 짧아지고 가독성이 좋아짐을 느낄 수 있었다.</p>

**3. 협업의 중요성**<br>
<p>팀원의 도움을 받고 API를 구현하는 과정에서 내가 부족한 부분을 알고 이를 개선해 나갈 수 있었다. 또한, 다양한 아이디어를 공유하고 이를 결합함으로써 더 나은 결과물을 만들어 낼 수 있었다.</p>


## 🎥 Youtube
*추후 업로드 예정*
