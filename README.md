# Document AI POC

A proof of concept application that demonstrates document processing capabilities using AI technologies. The system combines OCR (Optical Character Recognition) for text extraction from documents with vector storage and AI-powered question answering capabilities.

## Features

- **Document Upload & OCR**: Upload PDF and image files for text extraction using Tesseract OCR
- **AI-Powered Q&A**: Ask questions about your uploaded documents using Spring AI
- **Vector Storage**: Store and retrieve document embeddings using PGVector
- **Multi-tenant Support**: Isolate documents and queries by tenant ID
- **REST API**: RESTful endpoints for document management and querying
- **React Frontend**: User-friendly web interface for document interaction

## Architecture

The application consists of:

- **Backend**: Spring Boot application with Spring AI integration
- **Frontend**: React.js web application
- **Database**: PostgreSQL with PGVector extension for vector storage
- **OCR**: Tesseract for optical character recognition
- **AI Models**: Support for both OpenAI and Ollama models

## Technology Stack

### Backend
- **Java 21** - Programming language
- **Spring Boot 3.5.5** - Application framework
- **Spring AI 1.0.1** - AI integration framework
- **Spring WebFlux** - Reactive web framework
- **Spring Data JPA** - Data persistence
- **PostgreSQL** - Primary database
- **PGVector** - Vector storage for embeddings
- **Tesseract 5.16.0** - OCR engine
- **Liquibase** - Database migration management
- **Lombok** - Code generation

### Frontend
- **React 19.1.1** - JavaScript framework
- **React Scripts 5.0.1** - Build tools

### Infrastructure
- **Docker & Docker Compose** - Containerization
- **Maven** - Build automation
- **Heroku** - Deployment platform support

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- Docker and Docker Compose
- Node.js 16+ (for frontend development)
- PostgreSQL with PGVector extension

## Quick Start

### Using Docker Compose (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd document-poc
   ```

2. **Start the services**
   ```bash
   docker-compose up -d
   ```
   This will start:
   - PostgreSQL with PGVector extension on port 55559
   - The application (when uncommented in compose.yaml)

3. **Build and run the application**
   ```bash
   make docker-build
   make docker-start
   ```

### Local Development

1. **Start the database**
   ```bash
   docker-compose up -d pgvector
   ```

2. **Configure environment variables**
   Create a `.env` file with:
   ```env
   SPRING_AI_OPENAI_API_KEY=your-openai-api-key
   # OR for Ollama
   SPRING_AI_OLLAMA_BASE_URL=http://localhost:11434
   ```

3. **Run the backend**
   ```bash
   make start
   # or
   mvn spring-boot:run
   ```

4. **Run the frontend** (in a separate terminal)
   ```bash
   cd document-ui
   npm install
   npm start
   ```

## API Endpoints

### Upload Document
```http
POST /api/documents
Content-Type: multipart/form-data

Parameters:
- file: PDF or image file
- tenantId: Tenant identifier
```

### Ask Question
```http
GET /api/documents?question=<your-question>&tenantId=<tenant-id>
```

## Configuration

### Application Properties

The application supports multiple profiles:
- `dev` - Development environment
- `prod` - Production environment

Key configuration options:

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:55559/mydatabase
spring.datasource.username=myuser
spring.datasource.password=secret

# Vector Store
spring.ai.vectorstore.pgvector.index-type=HNSW
spring.ai.vectorstore.pgvector.distance-type=COSINE_DISTANCE
spring.ai.vectorstore.pgvector.dimensions=1024

# AI Models
spring.ai.openai.api-key=${SPRING_AI_OPENAI_API_KEY}
```

## Development

### Available Make Commands

- `make build` - Build the application
- `make package` - Build JAR (skip tests)
- `make start` - Run the application
- `make clean` - Clean build artifacts
- `make docker-build` - Build Docker image
- `make docker-start` - Start with Docker
- `make db-update` - Run database migrations
- `make db-rollback` - Rollback database changes

### Database Management

The project uses Liquibase for database schema management. Migration files are located in:
```
src/main/resources/db/changelog/
```

To update the database:
```bash
make db-update
```

### Testing

Run tests with:
```bash
mvn test
```

The application includes Testcontainers for integration testing with:
- PostgreSQL
- Ollama
- Various Spring Boot test utilities

## Deployment

### Heroku

The project includes `heroku.yml` for Heroku deployment:

1. Create a Heroku app
2. Add PostgreSQL add-on
3. Set required environment variables
4. Deploy using Git

### Docker

Build and run with Docker:
```bash
make docker-start
```

## Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `SPRING_AI_OPENAI_API_KEY` | OpenAI API key | Yes (if using OpenAI) |
| `SPRING_AI_OLLAMA_BASE_URL` | Ollama service URL | Yes (if using Ollama) |
| `SPRING_DATASOURCE_URL` | Database connection URL | Yes |
| `SPRING_DATASOURCE_USERNAME` | Database username | Yes |
| `SPRING_DATASOURCE_PASSWORD` | Database password | Yes |
| `PORT` | Application port | No (default: 8080) |
| `TESSDATA_PREFIX` | Tesseract data directory | No |

## Architecture Decisions

### Reactive Programming
The application uses Spring WebFlux for reactive, non-blocking I/O operations, making it suitable for handling file uploads and AI processing efficiently.

### Vector Storage
PGVector is used for storing document embeddings, enabling semantic search capabilities and efficient similarity matching.

### Multi-tenancy
Documents are isolated by tenant ID, allowing multiple organizations or users to use the same instance securely.

### OCR Integration
Tesseract OCR is integrated for extracting text from images and PDFs, with native library support for optimal performance.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is a proof of concept for document AI capabilities.

## Support

For questions and support, please refer to the project documentation or create an issue in the repository.