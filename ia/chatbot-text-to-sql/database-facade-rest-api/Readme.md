# Database Facade REST API

A REST API that provides a facade for relational databases, allowing users to execute SQL queries through HTTP endpoints. Built with Spring Boot and Java 21.

## Prerequisites

- JDK 21
- Maven 3.9.6+
- Docker & Docker Compose
- VS Code with Remote Development Extension Pack (for development)

## Technology Stack

- Java 21
- Spring Boot 3.4.0
- JDBC
- Docker
- Maven

## Getting Started

### Development Environment Setup

1. Clone the repository:

  ```bash
  git clone https://github.com/yourusername/database-facade-rest-api.git
  cd database-facade-rest-api
  ```

2. Setup environment variables:

  ```bash
  DB_URL=jdbc:oracle:thin:@//host:port/service
  DB_USERNAME=your_username
  DB_PASSWORD=your_password
  ```

3. Using VS Code with devcontainers:
   - Open the project in VS Code
   - Click "Reopen in Container" when prompted
   - Wait for the container to build and initialize

### Building the Project

Using Maven:

```bash
mvn clean install
```

### Testing

Run tests:

```bash
mvn test
```

### Running the Application

1. Using Maven:

  ```bash
  mvn spring-boot:run
  ```

2. Using Java:

  ```bash
  java --enable-preview -jar target/database-facade-rest-api-1.0.0.jar
  ```

## API Documentation

Once the application is running, access the Swagger UI at:

```url
http://localhost:8080/swagger-ui.html
```

### API Endpoints

#### Query Execution

```post
POST /api/v1/query
```

Request body example:

```json
{
  "query": "SELECT * FROM EMPLOYEES WHERE DEPARTMENT_ID = 10",
  "explanation": "Query to get all employees in department 10",
  "tables": ["EMPLOYEES"]
}
```

#### Health Check

```get
GET /api/v1/health
```

## Configuration

### Application Properties

Main configuration properties in `application.yml`:

```yaml
spring:
  application:
    name: database-facade-rest-api
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: oracle.jdbc.OracleDriver
```

### Security Considerations

- Only SELECT queries are allowed
- Key missing security features:
  - Query validation mechanisms to protect against SQL injection attacks
  - Request rate limiting to prevent system overload and potential abuse


### Database Configuration Guide

The Database Facade REST API is database-agnostic and can work with various relational databases. Below you'll find the configuration details for different database systems.

#### Current Configuration (Oracle)

Current configuration in `application.yml`:

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: oracle.jdbc.OracleDriver
```

Maven dependency (in `pom.xml`):

```xml
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc11</artifactId>
    <version>23.3.0.23.09</version>
    <scope>runtime</scope>
</dependency>
```

#### Alternative Database Configurations

##### PostgreSQL configuration

1. Replace the Oracle dependency with PostgreSQL in `pom.xml`:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.1</version>
</dependency>
```

2. Update `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_database
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver
```

##### IBM DB2 configuration

1. Add DB2 dependency in `pom.xml`:

  ```xml
  <dependency>
      <groupId>com.ibm.db2</groupId>
      <artifactId>jcc</artifactId>
      <version>11.5.9.0</version>
  </dependency>
  ```

2. Update `application.yml`:

  ```yaml
  spring:
    datasource:
      url: jdbc:db2://localhost:50000/your_database
      username: ${DB_USERNAME}
      password: ${DB_PASSWORD}
      driver-class-name: com.ibm.db2.jcc.DB2Driver
  ```

##### SQL Server configuration

1. Add SQL Server dependency in `pom.xml`:

```xml
<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <version>12.4.2.jre11</version>
</dependency>
```

2. Update `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=your_database;encrypt=true;trustServerCertificate=true
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
```

#### URL Formats by Database

| Database    | URL Format                                                          |
|-------------|---------------------------------------------------------------------|
| Oracle      | `jdbc:oracle:thin:@//host:port/service`                              |
| PostgreSQL  | `jdbc:postgresql://host:5432/database`                               |
| DB2         | `jdbc:db2://host:50000/database`                                     |
| SQL Server  | `jdbc:sqlserver://host:1433;databaseName=database;property=value`    |

#### Environment Variables

For any database, you'll need to set these environment variables:

```bash
# Oracle
export DB_URL=jdbc:oracle:thin:@//localhost:1521/XEPDB1
export DB_USERNAME=your_username
export DB_PASSWORD=your_password

# PostgreSQL
export DB_URL=jdbc:postgresql://localhost:5432/your_database
export DB_USERNAME=your_username
export DB_PASSWORD=your_password

# DB2
export DB_URL=jdbc:db2://localhost:50000/your_database
export DB_USERNAME=your_username
export DB_PASSWORD=your_password

# SQL Server
export DB_URL="jdbc:sqlserver://localhost:1433;databaseName=your_database;encrypt=true;trustServerCertificate=true"
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

#### Database-Specific Considerations

##### Oracle

- Supports both service name and SID connections
- Requires Oracle Client libraries in the runtime environment
- Best performance with connection pooling

##### PostgreSQL

- Native JSON support
- Case-sensitive object names by default
- Schema support with search_path

##### DB2

- Schema handling different from other databases
- Special catalog queries may be needed
- Specific transaction isolation considerations

##### SQL Server

- Windows authentication possible with integrated security
- Schema handling with dbo as default
- Special considerations for encrypted connections

#### Docker Configuration

Update the Dockerfile to include necessary database drivers:

```dockerfile
# For Oracle
RUN curl -O https://download.oracle.com/otn_software/linux/instantclient/instantclient-basiclite-linuxx64.zip && \
    unzip instantclient-basiclite-linuxx64.zip && \
    rm instantclient-basiclite-linuxx64.zip

# For PostgreSQL - usually included in base image
# For DB2
RUN curl -O https://public.dhe.ibm.com/ibmdl/export/pub/software/data/db2/drivers/db2_linuxx64_ds.tar.gz && \
    tar -xvf db2_linuxx64_ds.tar.gz

# For SQL Server - usually included in base image
```

#### Query Syntax Differences

Remember to adjust your queries according to the database-specific syntax:

```sql
-- Oracle
SELECT * FROM dual;
SELECT SYSDATE FROM dual;

-- PostgreSQL
SELECT NOW();
SELECT CURRENT_TIMESTAMP;

-- DB2
SELECT CURRENT TIMESTAMP FROM SYSIBM.SYSDUMMY1;

-- SQL Server
SELECT GETDATE();
```

#### Testing Database Connections

You can test your database connection using the health endpoint:

```bash
curl http://localhost:8080/actuator/health
```

Or by executing a simple query:

```bash
curl -X POST http://localhost:8080/api/v1/query \
     -H "Content-Type: application/json" \
     -d '{
           "query": "SELECT 1",
           "explanation": "Connection test",
           "tables": []
         }'
```

## Monitoring

Access actuator endpoints for monitoring:

```url
http://localhost:8080/actuator/health
http://localhost:8080/actuator/metrics
http://localhost:8080/actuator/info
```

## Troubleshooting

Common issues and solutions:

1. **Connection Issues**
   - Verify database credentials
   - Check network connectivity
   - Ensure Oracle client libraries are available

2. **Build Failures**
   - Clear Maven cache: `mvn clean`
   - Update dependencies: `mvn clean install -U`
   - Check Java version compatibility

## Docker Support

Build the image:

  ```bash
  docker build -t database-facade-rest-api .
  ```

Run the container:

  ```bash
  docker run -d \
    --name database-facade-api \
    -p 8080:8080 \
    -e DB_URL=jdbc:oracle:thin:@//host:port/service \
    -e DB_USERNAME=your_username \
    -e DB_PASSWORD=your_password \
    database-facade-rest-api
  ```

## License

This project is licensed under the MIT License - see the [LICENSE](../../../LICENSE.md) file for details.


## Contact

Diego Mendoza - [diego_mendoza@microgestion.com](mailto:diego_mendoza@microgestion.com)
