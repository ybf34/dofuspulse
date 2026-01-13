<div align="center">

  <img src="./docs/logo-light.svg#gh-light-mode-only" alt="DOFUS PULSE" width="120" height="120">
  <img src="./docs/logo-dark.svg#gh-dark-mode-only" alt="DOFUS PULSE" width="120" height="120">

  <h1>DOFUS PULSE</h1>

  **Advanced Economic Intelligence & Analytics for Dofus Touch**

  <p>
    <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=spring&logoColor=white" alt="Spring Boot">
    <img src="https://img.shields.io/badge/React-20232A?logo=react&logoColor=61DAFB" alt="React">
    <img src="https://img.shields.io/badge/TypeScript-007ACC?logo=typescript&logoColor=white" alt="TypeScript">
    <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License: MIT">

  </p>

<p align="center">
  <b>Dofus Pulse</b> is a data-driven web application for Dofus Touch, built to provide a comprehensive view of the in-game economy.
  <br />
  It combines market analytics, historical price trends, sales performance metrics, profit calculations, gearset planning, and portfolio monitoring,
  <br />
  giving players the tools to make informed trading and crafting decisions.
  <br />
  <br />
  <a href="#-local-development">Run locally</a>
  &middot;
  <a href="https://github.com/ybf34/dofuspulse/issues/new?labels=bug">Report Bug</a>
  &middot;
  <a href="https://github.com/ybf34/dofuspulse/issues/new?labels=enhancement">Request Feature</a>
</p>

  <br />
</div>



https://github.com/user-attachments/assets/103a628f-aafd-471b-9ecc-b1f5a3bb27b1

## 🛠 Tech Stack

* **Backend**: Spring Boot 3.x, Java 21, Maven
* **Frontend**: React, TypeScript, Vite, TailwindCSS
* **Database**: PostgreSQL (with TimescaleDB extension to efficiently store and query high-frequency historical market data)



---


<div id="-features">

## ✨ Features

| Feature | Description | Status |
| :--- | :--- | :--- |
| **Multi-Item Offer Analysis** | Evaluate trade offers containing multiple items by comparing their total market price or crafting cost. Helps quickly analyze trade profitability. | ✅ Available |
| **Gearset Management & Cost Planning** | Create and manage gear set plans, current item prices, and crafting trends to optimize set acquisition cost. | ✅ Available |
| **Market Analytics** | Track historical market prices of items, analyze trends, and monitor market activity metrics over time. | ✅ Available |
| **Sales Metrics** | Analyze item performance on the market: number of items sold, sales volume, listing counts, average sold duration. | ✅ Available |
| **Asset Portfolio** | Track owned items, acquisition cost, current market value, potential profit/loss, and realized profit/loss. | 🚧 Incoming |
| **Price Watcher / Alerts** | Create watchlists and alerts for items with specific user-required effects, notifying when items reach target prices or market changes occur. | 🚧 Incoming |

</div>


---

## 🛠 Prerequisites

* **Java 21** – For the Spring Boot backend.
* **Node.js & npm** – For the React/Vite frontend.
* **Docker** – Required for **Testcontainers** and the database.

---

## 📂 Project Structure

```text
.
├── apps
│   ├── api          # Springboot backend
│   └── web          # React Frontend           
├── env.docker       # Environment variables template
└── docker-compose.yml 
```

---

## 🚀 Local Development

### 1. Database Setup
The backend requires a running database. Start the db service via Docker:

```bash
docker compose up -d timescaledb
```

### 2. Environment Configuration
The services require environment variables defined in `env.docker`. Make sure to inject them into your IDE or terminal:

### 3. Backend (API)
The backend uses **Testcontainers** . **Docker must be running** for integrations tests to run correctly.

* **OpenAPI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

```bash
cd apps/api
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

### 4. Frontend 
```bash
cd apps/web
npm install
npm run dev
```

---

## 🐳 docker full setup

To run the entire stack (API + Web + DB) in a containerized environment:

```bash
# Start the full stack using the environment file
docker compose --env-file env.docker up --build -d

# Stop and remove containers
docker compose down
```

---
  
<div align="center">

  <img src="./docs/logo-dark.svg#gh-dark-mode-only" alt="DOFUS PULSE" width="60" height="60" />
  <img src="./docs/logo-light.svg#gh-light-mode-only" alt="DOFUS PULSE" width="60" height="60" />

  <p><sub>Made with ❤️ for the Dofus Touch community</sub></p>

  <p>
    <sub>
      Dofus Pulse is an independent project, not affiliated with Ankama Games. <br />
      DOFUS Touch and ANKAMA are trademarks of Ankama Games. Illustrations are the property of Ankama Studio and Dofus, all rights reserved.
    </sub>
  </p>


  <p><sub>© 2026 Dofus Pulse</sub></p>

</div>

