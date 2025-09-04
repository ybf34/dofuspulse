
# 🎮 Dofus Pulse

<div align="center">
  <img src="./apps/web/src/assets/dofus-pulse.svg" alt="Dofus Pulse Logo" width="200"/>
  
  **A comprehensive market analysis tool for Dofus Touch**
  
  [![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
  [![Java](https://img.shields.io/badge/java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
  [![React](https://img.shields.io/badge/react-19-blue.svg)](https://reactjs.org/)
  [![Spring Boot](https://img.shields.io/badge/spring--boot-3.4.0-green.svg)](https://spring.io/projects/spring-boot)
</div>

## 📖 Overview

Dofus Pulse is a powerful web application designed to provide players of Dofus Touch with comprehensive insights into the game's dynamic economy. Whether you're a casual player looking to make smart purchases or a serious trader seeking profit opportunities, Dofus Pulse delivers actionable market intelligence through intuitive analytics and real-time data.

### ✨ Key Features

- 📊 **Market Analytics**: Track pricing trends and sales history for all items
- 💰 **Profit Margin Analysis**: Identify lucrative trading opportunities
- 🏷️ **Item Type Analytics**: Analyze market patterns by item categories
- 📈 **Historical Data**: Access comprehensive historical market data
- 🔐 **Secure Authentication**: Multiple login options including Google and Discord OAuth
- 🎯 **User Profiles**: Personalized dashboards and saved preferences
- 📱 **Responsive Design**: Optimized for both desktop and mobile devices

## 🛠️ Technology Stack

### Backend
- **Java 21** - Modern JVM runtime
- **Spring Boot 3.4.0** - Enterprise-grade application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database abstraction layer
- **TimescaleDB** - Time-series database for market data
- **Maven** - Dependency management and build tool

### Frontend
- **React 19** - Modern UI framework
- **TypeScript** - Type-safe JavaScript
- **Vite** - Lightning-fast build tool
- **TanStack Router** - Type-safe routing
- **TanStack Query** - Data fetching and caching
- **Tailwind CSS** - Utility-first CSS framework
- **Radix UI** - Accessible component primitives

### Infrastructure
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration
- **PostgreSQL/TimescaleDB** - Primary database

## 🚀 Quick Start

### Prerequisites

- **Java 21** or higher (for backend development)
- **Node.js 18** or higher  
- **Docker** and **Docker Compose** (recommended for easy setup)
- **Git**

> **Note**: If you don't have Java 21, you can use Docker to run the entire application without installing Java locally.

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/ybf34/dofuspulse.git
   cd dofuspulse
   ```

2. **Set up environment variables**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

3. **Start the application with Docker**
   ```bash
   docker-compose up -d
   ```

   The application will be available at:
   - **API**: http://localhost:8080
   - **Web App**: http://localhost:3000

### Manual Setup (Development)

#### Backend Setup
```bash
cd apps/api
./mvnw spring-boot:run
```

#### Frontend Setup
```bash
cd apps/web
npm install
npm run dev
```

#### Database Setup
```bash
# Start TimescaleDB
docker run -d \
  --name dofuspulse-db \
  -p 5432:5432 \
  -e POSTGRES_DB=dofuspulse \
## 📚 API Documentation

### Authentication Endpoints

#### User Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "your_password"
}
```

#### User Registration
```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "your_password"
}
```

### Analytics Endpoints

#### Get Item Sales History
```http
GET /api/v1/items/{itemId}/saleshistory?startDate=2024-01-01&endDate=2024-01-31
```

#### Get Item Profit Margins
```http
GET /api/v1/items/{itemId}/profitmargin?startDate=2024-01-01&endDate=2024-01-31
```

#### Get Type-based Analytics
```http
GET /api/v1/type/{typeId}/saleshistory?startDate=2024-01-01&endDate=2024-01-31
GET /api/v1/type/{typeId}/profitmargin?startDate=2024-01-01&endDate=2024-01-31
```

For detailed API documentation, see the [API README](./apps/api/README.md).

## 🏗️ Project Structure

```
dofuspulse/
├── apps/
│   ├── api/                 # Spring Boot backend application
│   │   ├── src/
│   │   ├── pom.xml
│   │   └── README.md
│   └── web/                 # React frontend application
│       ├── src/
│       ├── package.json
│       └── README.md
├── docker-compose.yml       # Multi-container setup
├── .env.example            # Environment variables template
└── README.md              # This file
```

## 🧪 Development

### Running Tests

#### Backend Tests
```bash
cd apps/api
./mvnw test
```

#### Frontend Tests
```bash
cd apps/web
npm test
```

### Code Quality

#### Backend Linting
```bash
cd apps/api
./mvnw checkstyle:check
```

#### Frontend Linting
```bash
cd apps/web
npm run lint
```

### Building for Production

#### Backend Build
```bash
cd apps/api
./mvnw clean package
```

#### Frontend Build
```bash
cd apps/web
npm run build
```

## 🤝 Contributing

We welcome contributions! Please see our contributing guidelines:

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Make your changes** and ensure tests pass
4. **Commit your changes**: `git commit -m 'Add amazing feature'`
5. **Push to the branch**: `git push origin feature/amazing-feature`
6. **Open a Pull Request**

### Development Guidelines

- Follow existing code style and conventions
- Write tests for new features
- Update documentation when necessary
- Ensure all tests pass before submitting PR
- Use meaningful commit messages

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Ankama Games** - For creating the amazing Dofus universe
- **Dofus Touch Community** - For inspiration and feedback
- **Open Source Libraries** - For making this project possible

## 📞 Support

- 🐛 **Bug Reports**: [GitHub Issues](https://github.com/ybf34/dofuspulse/issues)
- 💡 **Feature Requests**: [GitHub Discussions](https://github.com/ybf34/dofuspulse/discussions)
- 📧 **Contact**: [Create an issue](https://github.com/ybf34/dofuspulse/issues/new) for support

---

<div align="center">
  Made with ❤️ for the Dofus Touch community
</div>
  -e POSTGRES_USER=dofuspulse \
  -e POSTGRES_PASSWORD=password \
  timescale/timescaledb:latest-pg17
```

## 📝 Environment Configuration

Create a `.env` file in the root directory with the following variables:

```env
# Database Configuration
DB_HOST=localhost
DB_NAME=dofuspulse
DB_USER=dofuspulse
DB_PASSWORD=password

# Application Configuration
APP_HOST=localhost:3000

# OAuth Configuration
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
DISCORD_CLIENT_ID=your_discord_client_id
DISCORD_CLIENT_SECRET=your_discord_client_secret
```

### ✨ Key Features

- 📊 **Market Analytics**: Track pricing trends and sales history for all items
- 💰 **Profit Margin Analysis**: Identify lucrative trading opportunities
- 🏷️ **Item Type Analytics**: Analyze market patterns by item categories
- 📈 **Historical Data**: Access comprehensive historical market data
- 🔐 **Secure Authentication**: Multiple login options including Google and Discord OAuth
- 🎯 **User Profiles**: Personalized dashboards and saved preferences
- 📱 **Responsive Design**: Optimized for both desktop and mobile devices

