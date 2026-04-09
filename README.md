# CulinAIry 🍽️

**CulinAIry** is an AI-powered restaurant and dining location advisor. It helps users discover nearby restaurants, get personalised AI-driven recommendations, read and write reviews, and manage their dining history and favourites — all through a modern, responsive web interface.

---

## Features

- 🔍 **Nearby Search** — Find restaurants and dining spots near any location using Google Maps
- 🤖 **AI Recommendations** — Receive personalised restaurant suggestions powered by OpenAI
- ⭐ **Reviews** — Read aggregated reviews and submit your own
- 👤 **User Accounts** — Register/login with email or Google OAuth2
- 📋 **Favourites & History** — Save favourite places and track your visit history
- 🖼️ **Image Uploads** — Profile and place images managed via ImageKit
- 📧 **Email Verification** — Account verification emails via SMTP

---

## Tech Stack

### Frontend
| Technology | Purpose |
|---|---|
| React 19 + Vite | UI framework & build tool |
| Material UI (MUI) | Component library |
| React Router 7 | Client-side routing |
| TanStack React Query | Server state management |
| Axios | HTTP client |
| `@vis.gl/react-google-maps` | Google Maps integration |
| `@react-oauth/google` | Google OAuth2 login |
| Motion | Animations |
| Swiper | Carousels |

### Backend
| Technology | Purpose |
|---|---|
| Spring Boot 3.4 (Java 22) | REST API framework |
| MongoDB | Database |
| Spring AI + OpenAI | AI recommendations & classification |
| Spring Security + JWT | Authentication & authorisation |
| Google OAuth2 | Social login |
| ImageKit | Image storage & delivery |
| Spring Mail | Email notifications |

---

## Project Structure

```
CulinAIry/
├── Frontend/          # React + Vite application
│   ├── src/
│   │   ├── pages/     # Route-level page components
│   │   ├── components/# Shared UI components
│   │   ├── hooks/     # Custom React hooks
│   │   ├── config/    # Configuration files
│   │   └── assets/    # Static assets
│   ├── Dockerfile
│   └── .env.example
└── Backend/           # Spring Boot application
    ├── src/main/java/ # Application source code
    │   ├── Controllers/   # REST controllers
    │   ├── Services/      # Business logic
    │   ├── Models/        # Domain models
    │   ├── Repositories/  # MongoDB repositories
    │   └── Config/        # Security & app config
    ├── Dockerfile
    └── src/main/resources/.env.example
```

---

## Getting Started

### Prerequisites

- Node.js 20+
- Java 22+
- Maven 3.9+
- MongoDB Atlas cluster (or local MongoDB)
- OpenAI API key
- Google Cloud project with Maps Platform & OAuth2 credentials

### 1. Backend Setup

```bash
cd Backend
```

Copy the example environment file and fill in your values:

```bash
cp src/main/resources/.env.example src/main/resources/.env
```

| Variable | Description |
|---|---|
| `MONGO_DB` | MongoDB database name |
| `MONGO_USER` | MongoDB username |
| `MONGO_PASSWORD` | MongoDB password |
| `MONGO_CLUSTER` | MongoDB cluster hostname |
| `JWT_SECRET` | Secret key for JWT signing |
| `GOOGLE_CLIENT_ID` | Google OAuth2 client ID |
| `GOOGLE_CLIENT_SECRET` | Google OAuth2 client secret |
| `GOOGLE_REDIRECT_URI` | OAuth2 redirect URI |
| `OPENAI_API_KEY` | OpenAI API key |
| `MAPS_PLATFORM_KEY` | Google Maps Platform API key |
| `IMAGEKIT_PUBLIC` | ImageKit public key |
| `IMAGEKIT_PRIVATE` | ImageKit private key |
| `IMAGEKIT_URL` | ImageKit URL endpoint |
| `EMAIL_SENDER` | Gmail address for sending emails |
| `EMAIL_SENDER_PW` | Gmail app password |

Run the backend:

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:1010/v1/api`.

### 2. Frontend Setup

```bash
cd Frontend
```

Copy the example environment file and fill in your values:

```bash
cp .env.example .env
```

| Variable | Description |
|---|---|
| `VITE_GOOGLE_OAUTH2_CLIENT_ID` | Google OAuth2 client ID |
| `VITE_GOOGLE_MAP_ID` | Google Maps map ID |
| `VITE_DEFAULT_BACKEND_URL` | Backend base URL (e.g. `http://localhost:1010`) |
| `VITE_DEFAULT_BACKEND_API_PATH` | Backend API path (e.g. `/v1/api`) |

Install dependencies and start the development server:

```bash
npm install
npm run dev
```

The frontend will be available at `http://localhost:5173`.

---

## Docker Deployment

Both services include Dockerfiles for containerised deployment.

### Backend

```bash
cd Backend
./mvnw package -DskipTests
docker build -t culinairy-backend .
docker run -p 1010:1010 --env-file src/main/resources/.env culinairy-backend
```

### Frontend

```bash
cd Frontend
docker build -t culinairy-frontend .
docker run -p 80:80 culinairy-frontend
```

---

## API Overview

The backend exposes REST endpoints under `/v1/api`:

| Controller | Endpoints |
|---|---|
| `AuthController` | Register, login, logout |
| `OAuth2Controller` | Google OAuth2 flow |
| `VerificationController` | Email verification |
| `UserController` | Profile, settings, favourites, history |
| `SearchController` | Nearby place search |
| `PlaceController` | Place details and reviews |
| `RecommendationsController` | AI-powered recommendations |
| `AiController` | Direct AI query endpoints |

---

## License

This project is licensed under the terms in the [LICENSE](LICENSE) file.
