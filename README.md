# FlyBooker✈️
Modular flight booking backend service implementing core features like :
- ✈️Flight Search - Multi-criteria search with origin/destination, dates, passengers, and class selection
- 📊Dynamic Results - Flight cards with pricing, and flight details
- 💺Seat Selection - Interactive aircraft seat map with different seat types (economy, premium, occupied)
- 💵Dynamic Pricing - Real-time price based on the demand
- 🔄️Nested transactions to ensure consistency in payment + seat booking workflows
- 🔒Row-level locks / concurrency control for safe seat reservations under high load
- 🧩Modular monolith architecture 

# Tech Stack🚀
- Spring Boot backend with separation-of-concerns architecture
- PostgreSQL as the database 
- Jenkins pipeline for CI/CD
- React.js vibe code with GLM 4.5 and Claude
- Container-first setup with Docker & Docker Compose
