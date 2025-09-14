# FlyBooker ✈️

A modular flight booking backend built with **Spring Boot** and **PostgreSQL**, designed for consistency, scalability, and real-world airline reservation workflows.

## Features
- **Flight Search (✈️)** – Multi-criteria query by origin, destination, dates, passengers, and class.  
- **Dynamic Results (📊)** – Flight cards with details and pricing.  
- **Seat Selection (💺)** – Seat map with availability (economy, premium, etc).  
- **Dynamic Pricing (💵)** – Demand-based real-time pricing.  
- **Nested Transactions (🔄)** – Atomic workflows for payment + seat booking.  
- **Concurrency Control (🔒)** – Row-level locks prevent double bookings.  
- **Modular Monolith (🧩)** – Separation-of-concerns within a single deployable unit.  

## Tech Stack
- **Backend:** Spring Boot (Java 17)  
- **Database:** PostgreSQL  
- **CI/CD:** Jenkins  
- **Frontend:** React.js (assisted with GLM 4.5 + Claude)  
- **Deployment:** Docker + Docker Compose  

## Getting Started

### Prerequisites
- Docker & Docker Compose  
- Java 17+  
- Node.js 18+ (optional frontend)  

### Run
```bash
git clone https://github.com/your-username/flybooker.git
cd flybooker
docker-compose up --build
```
