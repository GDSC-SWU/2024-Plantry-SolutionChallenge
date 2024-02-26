![2024 Google Solution Challenge - Plantry Banner](https://github.com/GDSC-SWU/2024-Plantry-SolutionChallenge/assets/68212300/b2f36158-5221-465b-afdf-a10551e69a63)

## 💡 Contents

1. [Problem](#-problem)
2. [About Plantry](#-about-plantry)
3. [Our SDGs Goal](#-our-sdgs-goal)
4. [Technology Stack](#-technology-stack)
5. [Demo Video](#-demo-video)
6. [Design](https://github.com/GDSC-SWU/2024-Plantry-SolutionChallenge?tab=readme-ov-file#%EF%B8%8F-design)
7. [How to Start](#-how-to-start)
8. [How to Run on Local](#-how-to-run-on-local)
9. [Contributors](#-contributors)

<br>

# 📌 Problem

According to the **United Nations World Food Programme(WFP)**, around 1.3 billion tons of **food is wasted** globally every year without being used as food. That's enough to feed nearly 800 million people who suffer from hunger.
<br>
<br>
Wasted food is a major contributor to **greenhouse gases**, with methane emitted as it decays in landfills having 80 times the greenhouse effect of carbon dioxide. According to the **Food and Agriculture Organization of the United Nations(FAO)**, greenhouse gases from waste in the production and consumption of food account for 8-10% of global greenhouse gas emissions. The life cycle of food already emits a lot of carbon when it's produced, but when it's disposed of, additional carbon is released.
<br>
<br>
Consumers buy more food than they can actually eat, or store food incorrectly, resulting in food being thrown away before it is consumed. Not only does consumer overbuying create waste, it also contributes to producer overproduction.
<br>
<br>
Food is an essential part of human life, so it is essential that consumers consume it responsibly.

<br>

# ✨ About Plantry

To solve this problem, Plantry **focuses on the consumer** as one of the many contributors to the problem.
<br>
<br>
With Plantry, consumers can **manage food inventory** and **record how the food is consumed (discarded, ingested, shared)**.
<br>
<br>
**The data is visualized in graphs** to help consumers be more aware of food waste, and to help them buy the right amount of food when they make future purchases, taking into account their previous consumption. It also helps consumers learn how to store food properly to avoid throwing it away when it's still edible, and encourages them to **use use-by dates instead of sell-by dates** to ensure that food is safe to consume for a longer period of time.
<br>
<br>
**Reminder notifications** are provided for food items that are nearing their expiration date to help consumers eat food within the expiration date.

<br>

# 🚀 Our SDGs Goal
![Plantry SDGs_Goal](https://github.com/GDSC-SWU/2024-Plantry-SolutionChallenge/assets/68212300/37771e9f-6741-47ea-821f-cc2f8e4d0c4d)

Improving food consumption habits among consumers can reduce greenhouse gas emissions from food waste by reducing unnecessary food waste and preventing overproduction by producers.<br>
Proper storage and utilization of food can also help maintain a healthy lifestyle.

<br>

# 🛠 Technology Stack
![Plantry Tech Stack](https://github.com/GDSC-SWU/2024-Plantry-SolutionChallenge/assets/68212300/7f2cc7a8-6584-4298-a9b4-b3999b117386)

<br>

# 📹 Demo Video

[![Plantry Demo Video Link](https://github.com/GDSC-SWU/2024-Plantry-SolutionChallenge/assets/68212300/47598a0a-4399-43c5-9d57-44ec57dd425e)](https://youtu.be/fspKYlHi38A?si=x3TIBsasfWEiBI04)

<br>

# ✍️ Design

![readme-mockup](https://github.com/GDSC-SWU/2024-Plantry-SolutionChallenge/assets/68212300/5108c44b-4ce8-4813-b46e-43d255e5ee51)

<br>

# 📱 How to Start

#### 1. Download APK file

[APK Download Link](https://drive.google.com/file/d/1SL2hSk1qXrK29oom35dne_aCEHyjMbyX/view?usp=sharing)

#### 2. Install APK and Run the app

<br>

# 📲 How to Run on Local

#### 1. Clone this Repository

```bash
git clone https://github.com/GDSC-SWU/2024-Plantry-SolutionChallenge.git
```

#### 2. Set your Environment variables

Use this as a reference to set environment variables

[https://developer.android.com/tools/variables](https://developer.android.com/tools/variables)

#### 3. Open Android Source Code on Android Studio

Giraffe Version or higher recommended
<br>
[Android Studio Giraffe Download Link](https://teamandroid.com/android-studio-giraffe-download/)

#### 4. Download Emulator

Pixel XL API 34 recommended

#### 5. Set Api Base url in local properties file
```properties
base.url="http://34.47.86.239:8080"
ai_base.url="http://34.47.86.239:8000"
```

#### 6. Run the app

press the green start button
<br>
![Android Studio start button image](https://github.com/GDSC-SWU/2024-Plantry-SolutionChallenge/assets/97941141/e2d00e03-7ff0-46db-b299-15fd4f4c7fd2)


#### 7. Send me your sha 1 key

If you can't log in, please do the process below:)

```
1. Enter `gradlew signalingReport` in the terminal window.
2. Press `ctrl + enter` or `command + enter` to execute the command.
3. please send your sha1 key `yearim1226@naver.com`.
4. After receiving a reply, please try logging in again.
```

<br>

## +) How to Run Server on Local

The server is deployed, but if you want to run it on local, do the following:

#### 1. Create `application.properties` file on `server/plantry/src/main/resources`

```properties
# DATABASE

SPRING_DATASOURCE_URL={YOUR_MYSQL_DATABASE_URL}
SPRING_DATASOURCE_USERNAME={YOUR_MYSQL_DATABASE_USERNAME}
SPRING_DATASOURCE_PASSWORD={YOUR_MYSQL_DATABASE_PASSWORD}


# Redis

SPRING_DATA_REDIS_HOST={YOUR_REDIS_DATABASE_HOST}
SPRING_DATA_REDIS_PORT={YOUR_REDIS_DATABASE_PORT}
SPRING_DATA_REDIS_PASSWORD={YOUR_REDIS_DATABASE_PASSWORD}


# Google OAuth

GOOGLE_OAUTH_CLIENT_ID={YOUR_GOOGLE_OAUTH_CLIENT_ID}


# JWT

JWT_SECRET_KEY=some-random-secret-key-plantry-9876
JWT_ISSUER=gdsc


# Server profile

PROFILE=local
```

#### 2. Store the service account JSON file `firebase_account_file.json` containing the key on `server/plantry/src/main/resources/firebase`

Refer to the docs<br>
[Firebase DOCS](https://firebase.google.com/docs/admin/setup?hl=ko#set-up-project-and-service-account)

#### 3. Run the server

<br>

# 👩🏻‍💻 Contributors

| <img alt="Plantry Yusun Choi" src="https://github.com/GDSC-SWU/2024-Plantry-SolutionChallenge/assets/68212300/9aca10a1-06d4-42ea-b8f6-5585a582468e"> | <img alt="Plantry Yerim Lee" src="https://github.com/GDSC-SWU/2024-Plantry-SolutionChallenge/assets/68212300/37ba797a-658d-4d27-8a38-2b613abf418c"> | <img alt="Plantry Yehan Shin" src="https://github.com/GDSC-SWU/2024-Plantry-SolutionChallenge/assets/68212300/2d052916-7fac-40a8-a2d9-b91f572690f0"> | <img alt="Plantry Eunji Jung" src="https://github.com/GDSC-SWU/2024-Plantry-SolutionChallenge/assets/68212300/4e8b46ea-4503-4432-9861-5e7d14ce79b5"> |
| :--------------------: | :--------------: | :------------: | :-------------: |
|     **Yusun Choi**     |  **Yerim Lee**   | **Yehan Shin** | **Eunji Jung**  |
| PM<br>Server Developer | Client Developer | UX/UI Designer | AI/ML Developer |
