
# LinkedIn Application - Backend

LinkedIn Clone application is a social media platform built using Spring Boot microservices to manage user profiles, posts, and connections. The platform incorporates robust Spring Security for secure authentication and authorization. It uses Neo4j for efficient management of user connections, allowing seamless exploration of first, second, and third-degree relationships. The application also leverages Kafka for real-time notifications and inter-service communication.

## Key Features🚀
1.**Microservices-Based LinkedIn Clone**
- Developed using Spring Boot microservices architecture with distinct services: Connection Service, Post Service, User Service, API Gateway, Notification Service, Discovery Server, and Uploader Service

2.**Connection Service**
- Handles user connections with features to send, accept, and reject connection requests.
- Retrieves 1st, 2nd, and 3rd-degree connections using Neo4j for efficient graph-based relationships.

3.**Custom Spring Security**
- Implemented JWT-based authentication and authorization in the API Gateway to ensure secure API calls and user data protection.

4.**Kafka Messaging Queue**
- Integrated Kafka for real-time, asynchronous notifications, enabling smooth communication between microservices and instant updates.

5.**User Management**
- User Service allows users to sign up, log in, and securely store passwords using hashed passwords for better security.

6.**Post Management**
- Post Service enables users to create posts, like/dislike posts, and view user posts, enhancing engagement and interaction.

7.**Media Uploading**
- Uploader Service facilitates media file uploads using Cloudinary for secure and efficient media management.

8.**Service Discovery with Eureka**
- Implemented Eureka in the Discovery Server to manage service registration and discovery, ensuring high availability and fault tolerance.

9.**Dockerized Architecture**
- Containerized the entire microservices architecture using Docker, ensuring ease of deployment, scalability, and efficient resource management.
## Tech Stack 👨🏻‍💻

**Backend Framework**: Spring boot

**Build Tool**: Maven

**Security**: Jwt

**Database**: PostgreSQL, Neo4j

**API Documentation**: Postman

**Object Mapping**: ModelMapper

**Event Streaming**: Apache Kafka

**Containerization**: Docker


## API Reference 🔗
**Authentication 🔐** 

#### Sign up

```http
  POST /users/auth/signup
```

#### log in

```http
  POST /users/auth/login
```
****
****

**Connection Management 🤝**
#### Get first degree connections

```http
  GET /connections/core/firstConnections
```

#### Send connection request

```http
  POST /connections/core/request/{userId}
```

#### Acept connection request

```http
  POST /connections/core/accept/{userId}
```

#### Reject connection request

```http
  POST /connections/core/reject/{userId}
```
****
****
**Post Management 📝**

#### Create Post

```http
  POST /posts/core
```

#### Get post of user

```http
  GET /posts/core/{postId}
```

#### Get all posts of user

```http
  GET /posts/core/usres/{userId}/allPosts
```

#### Like post

```http
  POST /posts/likes/{postId}
```

#### Dislike post

```http
  DELETE /posts/likes/{postId}
```
****
****
**File Management 📤**
#### Upload file

```http
  POST /uploads/file
```



