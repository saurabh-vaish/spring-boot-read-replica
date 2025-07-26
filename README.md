````markdown path=READ_ME.md mode=EDIT
# Read Replica Demo

A Spring Boot application demonstrating PostgreSQL master-replica database architecture with automatic failover and load balancing capabilities.

## Features

- **Master-Replica Architecture**: PostgreSQL cluster with one master and multiple read replicas
- **Automatic Load Balancing**: Intelligent routing of read queries across replica nodes
- **Health Monitoring**: Continuous health checks for database nodes
- **Failover Support**: Automatic failover when replica nodes become unavailable
- **Docker Compose Setup**: Complete PostgreSQL cluster setup with Docker
- **Multiple Environment Profiles**: Development, CI, and production configurations

## Architecture

### Database Cluster
- **Master Node**: Handles all write operations (port 6000)
- **Replica Nodes**: Handle read operations with load balancing
  - Replica 1 (port 6001)
  - Replica 2 (port 6002) 
  - Replica 3 (port 6003)

### Application Components
- **Read/Write Routing**: Automatic routing based on operation type
- **Connection Pooling**: HikariCP for efficient connection management
- **Health Checks**: Real-time monitoring of database node status
- **RFC 7807 Error Handling**: Standardized error responses

## Quick Start

### Prerequisites
- Docker and Docker Compose
- Java 17+
- Maven 3.6+

### 1. Start PostgreSQL Cluster

```bash
cd postgres-cluster
docker-compose up -d
```

This will start:
- Master PostgreSQL instance on port 6000
- Three replica instances on ports 6001-6003
- Automatic replication setup between master and replicas

### 2. Run the Application

```bash
# Development profile (uses local PostgreSQL)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# CI profile (uses Docker cluster)
mvn spring-boot:run -Dspring-boot.run.profiles=ci

# Production profile
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## PostgreSQL Cluster Setup

The `postgres-cluster/` directory contains the complete Docker setup:

### Master Configuration (`postgres-cluster/master/`)
- **Dockerfile**: PostgreSQL 16 with replication setup
- **init-master.sh**: Configures WAL settings and creates replication user

### Replica Configuration (`postgres-cluster/replica/`)
- **Dockerfile**: PostgreSQL 16 with gosu for proper user management
- **docker-entrypoint.sh**: Handles base backup and replica initialization

### Docker Compose (`postgres-cluster/docker-compose.yml`)
- Complete cluster orchestration
- Network isolation with `pgnet`
- Persistent volumes for data storage
- Environment variable configuration

## Development

### Building
```bash
mvn clean compile
```

### Testing
```bash
mvn test
```

### Running with Docker
```bash
# Start the cluster
cd postgres-cluster && docker-compose up -d

# Run application
mvn spring-boot:run -Dspring-boot.run.profiles=ci
```

## Guides
* [Sprig-boot-read-replica](https://wisdom.gitbook.io/gyan/core/spring-boot-with-read-replica)
````
