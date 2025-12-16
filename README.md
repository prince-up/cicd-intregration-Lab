# 🚀 Student CI/CD Lab

## Complete DevOps Learning Platform

A production-ready web application demonstrating real-world CI/CD (Continuous Integration/Continuous Deployment) concepts using industry-standard tools and practices.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)
- [CI/CD Pipeline Explained](#cicd-pipeline-explained)
- [API Documentation](#api-documentation)
- [Jenkins Configuration](#jenkins-configuration)
- [GitHub Webhook Setup](#github-webhook-setup)
- [Project Structure](#project-structure)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)
- [Learning Resources](#learning-resources)
- [Contributing](#contributing)
- [License](#license)

---

## 🎯 Overview

**Student CI/CD Lab** is an educational platform built for college students and beginners to learn DevOps practices through hands-on experience. The application demonstrates:

- **Continuous Integration**: Automatic code building and testing
- **Continuous Deployment**: Automatic application deployment
- **Pipeline as Code**: Jenkins pipeline defined in Jenkinsfile
- **Automated Testing**: JUnit tests with detailed reporting
- **Full-Stack Development**: React frontend + Spring Boot backend

### What Students Will Learn

✅ How CI/CD pipelines work in real companies  
✅ Jenkins setup and configuration  
✅ Maven build automation  
✅ JUnit testing framework  
✅ REST API development  
✅ React.js frontend development  
✅ Git & GitHub workflows  
✅ DevOps best practices

---

## ✨ Features

### 🎨 Frontend (React)
- **Home Page**: Project explanation and learning guide
- **Dashboard**: View all pipeline executions with real-time status
- **Trigger Pipeline**: Form to start new CI/CD builds
- **Execution Details**: Detailed view of build stages, test results, and logs
- **Real-time Updates**: Auto-refresh to show live pipeline status

### ⚙️ Backend (Spring Boot)
- **REST APIs**: Complete API for pipeline management
- **Database**: H2 in-memory database (easy setup, no installation)
- **Jenkins Integration**: Trigger and monitor Jenkins builds
- **Test Reporting**: Store and retrieve JUnit test results
- **Error Handling**: Comprehensive error messages

### 🔄 CI/CD Pipeline
- **Stage 1**: Checkout - Clone code from GitHub
- **Stage 2**: Build - Compile Java code with Maven
- **Stage 3**: Test - Run automated JUnit tests
- **Stage 4**: Package - Create executable JAR file
- **Stage 5**: Deploy - Deploy application (simulated)

---

## 🛠️ Technology Stack

| Component | Technology | Purpose |
|-----------|-----------|---------|
| **Frontend** | React.js 18 | User interface |
| **Backend** | Java 17 + Spring Boot 3.1 | REST API server |
| **Build Tool** | Maven 3.9+ | Build automation |
| **CI/CD** | Jenkins 2.x | Pipeline automation |
| **Testing** | JUnit 5 | Automated testing |
| **Database** | H2 (in-memory) | Data storage |
| **Version Control** | Git + GitHub | Source code management |

---

## 🏗️ Architecture

```
┌─────────────────┐         ┌─────────────────┐         ┌─────────────────┐
│                 │         │                 │         │                 │
│  React Frontend │────────▶│  Spring Boot    │────────▶│    Jenkins      │
│  (Port 3000)    │  REST   │  Backend        │  API    │  (Port 8081)    │
│                 │  API    │  (Port 8080)    │         │                 │
└─────────────────┘         └─────────────────┘         └─────────────────┘
                                    │                            │
                                    │                            │
                                    ▼                            ▼
                            ┌─────────────────┐         ┌─────────────────┐
                            │   H2 Database   │         │  GitHub Repo    │
                            │   (In-Memory)   │         │   (Source Code) │
                            └─────────────────┘         └─────────────────┘
```

**Workflow:**
1. Student triggers pipeline from React UI
2. Backend receives request and calls Jenkins API
3. Jenkins clones code from GitHub
4. Jenkins executes pipeline: Build → Test → Package → Deploy
5. Test results saved to database
6. Frontend displays real-time status

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

### Required Software

1. **Java Development Kit (JDK) 17**
   - Download: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
   - Verify: `java -version`

2. **Apache Maven 3.9+**
   - Download: [Maven](https://maven.apache.org/download.cgi)
   - Verify: `mvn -version`

3. **Node.js 18+ and npm**
   - Download: [Node.js](https://nodejs.org/)
   - Verify: `node --version` and `npm --version`

4. **Jenkins 2.x**
   - Download: [Jenkins](https://www.jenkins.io/download/)
   - Or run with Docker: `docker run -p 8081:8080 jenkins/jenkins:lts`

5. **Git**
   - Download: [Git](https://git-scm.com/downloads)
   - Verify: `git --version`

### Optional
- **IDE**: IntelliJ IDEA, VS Code, or Eclipse
- **Postman**: For API testing

---

## 🚀 Installation & Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/student-cicd-lab.git
cd student-cicd-lab
```

### Step 2: Backend Setup

```bash
# Navigate to backend directory
cd backend

# Install dependencies and build
mvn clean install

# Run tests
mvn test

# Start the backend server
mvn spring-boot:run
```

Backend will start on: **http://localhost:8080**

### Step 3: Frontend Setup

```bash
# Navigate to frontend directory
cd ../frontend

# Install dependencies
npm install

# Start the React development server
npm start
```

Frontend will start on: **http://localhost:3000**

### Step 4: Verify Setup

Open your browser and visit:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api/pipeline/health
- H2 Database Console: http://localhost:8080/h2-console

---

## 🎮 Running the Application

### Development Mode

**Terminal 1 - Backend:**
```bash
cd backend
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd frontend
npm start
```

### Production Build

**Backend:**
```bash
cd backend
mvn clean package
java -jar target/student-cicd-lab-1.0.0.jar
```

**Frontend:**
```bash
cd frontend
npm run build
# Serve build folder using any static server
npx serve -s build
```

---

## 🔄 CI/CD Pipeline Explained

### Jenkinsfile Breakdown

The pipeline is defined in `Jenkinsfile` at the root of the project.

#### Stage 1: Checkout
```groovy
stage('Checkout') {
    steps {
        git url: "${params.REPO_URL}", branch: "${params.BRANCH}"
    }
}
```
**What it does**: Clones the GitHub repository

#### Stage 2: Build
```groovy
stage('Build') {
    steps {
        sh 'mvn clean compile'
    }
}
```
**What it does**: Compiles Java source code

#### Stage 3: Test
```groovy
stage('Test') {
    steps {
        sh 'mvn test'
    }
    post {
        always {
            junit 'backend/target/surefire-reports/*.xml'
        }
    }
}
```
**What it does**: Runs JUnit tests and publishes results

#### Stage 4: Package
```groovy
stage('Package') {
    steps {
        sh 'mvn package -DskipTests'
    }
}
```
**What it does**: Creates executable JAR file

#### Stage 5: Deploy
```groovy
stage('Deploy') {
    steps {
        sh 'cp target/*.jar /deployment/path/'
    }
}
```
**What it does**: Deploys the application

---

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api/pipeline
```

### Endpoints

#### 1. Health Check
```http
GET /api/pipeline/health
```
**Response:**
```json
{
  "status": "UP",
  "service": "Student CI/CD Lab",
  "version": "1.0.0"
}
```

#### 2. Trigger Pipeline
```http
POST /api/pipeline/trigger
Content-Type: application/json

{
  "studentName": "John Doe",
  "repositoryUrl": "https://github.com/user/repo",
  "branchName": "main"
}
```
**Response:**
```json
{
  "id": 1,
  "buildNumber": 123,
  "studentName": "John Doe",
  "status": "RUNNING",
  "currentStage": "CHECKOUT"
}
```

#### 3. Get All Executions
```http
GET /api/pipeline/executions
```

#### 4. Get Execution by ID
```http
GET /api/pipeline/executions/{id}
```

#### 5. Get Test Results
```http
GET /api/pipeline/executions/{id}/tests
```

---

## ⚙️ Jenkins Configuration

### 1. Install Jenkins

**Option A: Download & Install**
- Visit https://www.jenkins.io/download/
- Install Jenkins
- Access at http://localhost:8080

**Option B: Run with Docker**
```bash
docker run -d -p 8081:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  --name jenkins jenkins/jenkins:lts
```

### 2. Install Required Plugins

Go to: **Manage Jenkins → Manage Plugins**

Install:
- ✅ Maven Integration Plugin
- ✅ JUnit Plugin
- ✅ Pipeline Plugin
- ✅ Git Plugin
- ✅ GitHub Plugin

### 3. Configure Tools

Go to: **Manage Jenkins → Global Tool Configuration**

**Configure Maven:**
- Name: `Maven 3.9.0`
- Install automatically: ✅
- Version: 3.9.0

**Configure JDK:**
- Name: `JDK 17`
- JAVA_HOME: Path to your JDK installation

### 4. Create Pipeline Job

1. Click **New Item**
2. Enter name: `student-cicd-pipeline`
3. Select: **Pipeline**
4. Click **OK**

**Pipeline Configuration:**
- Definition: **Pipeline script from SCM**
- SCM: **Git**
- Repository URL: Your GitHub repo URL
- Branch: `*/main`
- Script Path: `Jenkinsfile`

### 5. Configure API Token

1. Click your username → **Configure**
2. **API Token** → **Add new Token**
3. Copy the token
4. Update `backend/src/main/resources/application.properties`:
```properties
jenkins.api.token=YOUR_TOKEN_HERE
```

---

## 🔗 GitHub Webhook Setup

### 1. Configure GitHub Webhook

1. Go to your GitHub repository
2. **Settings → Webhooks → Add webhook**
3. **Payload URL**: `http://your-jenkins-url:8080/github-webhook/`
4. **Content type**: `application/json`
5. **Events**: Select "Just the push event"
6. **Active**: ✅
7. Click **Add webhook**

### 2. Jenkins GitHub Configuration

**Jenkins → Configure System → GitHub**
- Add GitHub Server
- Credentials: Add GitHub personal access token

---

## 📁 Project Structure

```
student-cicd-lab/
│
├── backend/                          # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/cicd/
│   │   │   │   ├── CicdApplication.java       # Main application
│   │   │   │   ├── controller/                # REST controllers
│   │   │   │   │   └── PipelineController.java
│   │   │   │   ├── model/                     # Data models
│   │   │   │   │   ├── PipelineExecution.java
│   │   │   │   │   ├── TestResult.java
│   │   │   │   │   └── ...
│   │   │   │   ├── repository/                # Database repositories
│   │   │   │   │   └── PipelineExecutionRepository.java
│   │   │   │   ├── service/                   # Business logic
│   │   │   │   │   ├── PipelineService.java
│   │   │   │   │   └── JenkinsService.java
│   │   │   │   └── config/                    # Configuration
│   │   │   │       └── WebConfig.java
│   │   │   └── resources/
│   │   │       └── application.properties     # App configuration
│   │   └── test/                              # Unit tests
│   │       └── java/com/cicd/
│   │           ├── CicdApplicationTests.java
│   │           └── ...
│   └── pom.xml                                # Maven configuration
│
├── frontend/                         # React Frontend
│   ├── public/
│   │   └── index.html
│   ├── src/
│   │   ├── components/               # Reusable components
│   │   │   ├── Header.js
│   │   │   ├── StatusBadge.js
│   │   │   └── PipelineStage.js
│   │   ├── pages/                    # Page components
│   │   │   ├── Home.js
│   │   │   ├── Dashboard.js
│   │   │   ├── TriggerPipeline.js
│   │   │   └── ExecutionDetails.js
│   │   ├── services/                 # API services
│   │   │   └── api.js
│   │   ├── App.js                    # Main app component
│   │   ├── index.js                  # Entry point
│   │   └── index.css                 # Global styles
│   └── package.json                  # npm configuration
│
├── Jenkinsfile                       # Jenkins pipeline definition
└── README.md                         # This file
```

---

## 🧪 Testing

### Backend Tests

```bash
cd backend

# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=PipelineServiceTest

# Run with coverage
mvn test jacoco:report
```

### Frontend Tests

```bash
cd frontend

# Run tests
npm test

# Run with coverage
npm test -- --coverage
```

---

## 🐛 Troubleshooting

### Common Issues

#### 1. Backend won't start
**Error**: `Port 8080 already in use`

**Solution:**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

#### 2. Frontend can't connect to backend
**Error**: `Network Error` or `CORS Error`

**Solution:**
- Ensure backend is running on port 8080
- Check `application.properties` CORS settings
- Verify API base URL in `frontend/src/services/api.js`

#### 3. Jenkins can't connect
**Error**: `Failed to trigger Jenkins build`

**Solution:**
- Verify Jenkins is running: http://localhost:8081
- Check Jenkins URL in `application.properties`
- Verify API token is correct
- Ensure Jenkins job exists

#### 4. Maven build fails
**Error**: `Failed to execute goal`

**Solution:**
```bash
# Clean Maven cache
mvn clean

# Delete .m2 repository (Windows)
rmdir /s %USERPROFILE%\.m2\repository

# Rebuild
mvn clean install
```

#### 5. H2 Database issues
**Solution:**
- Delete database file (it's in-memory, so just restart app)
- Check `application.properties` datasource settings

---

## 📖 Learning Resources

### DevOps Concepts
- [What is CI/CD?](https://www.redhat.com/en/topics/devops/what-is-ci-cd)
- [Jenkins Documentation](https://www.jenkins.io/doc/)
- [Maven Tutorial](https://maven.apache.org/guides/getting-started/)

### Technologies Used
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

### Video Tutorials
- [Jenkins Tutorial for Beginners](https://www.youtube.com/watch?v=FX322RVNGj4)
- [Spring Boot Tutorial](https://www.youtube.com/watch?v=vtPkZShrvXQ)
- [React JS Tutorial](https://www.youtube.com/watch?v=SqcY0GlETPk)

---

## 🎓 Viva Questions & Answers

### 1. What is CI/CD?
**Answer**: CI/CD stands for Continuous Integration and Continuous Deployment. It's a DevOps practice where code changes are automatically built, tested, and deployed to production.

### 2. What is Jenkins?
**Answer**: Jenkins is an open-source automation server used to automate parts of software development including building, testing, and deploying.

### 3. What is Maven?
**Answer**: Maven is a build automation tool used primarily for Java projects. It manages dependencies and provides a standard project structure.

### 4. What happens in the Build stage?
**Answer**: Maven compiles the Java source code into bytecode (.class files).

### 5. What happens in the Test stage?
**Answer**: JUnit runs all automated test cases to verify the code works correctly.

### 6. What is a Jenkinsfile?
**Answer**: A Jenkinsfile is a text file that contains the definition of a Jenkins Pipeline. It's stored in version control along with the code.

### 7. What is Spring Boot?
**Answer**: Spring Boot is a Java framework that simplifies building production-ready applications with minimal configuration.

### 8. What is REST API?
**Answer**: REST (Representational State Transfer) API is an architectural style for web services that uses HTTP methods (GET, POST, PUT, DELETE).

### 9. Why use automated testing?
**Answer**: Automated testing catches bugs early, ensures code quality, and makes it safe to deploy frequently.

### 10. What is the benefit of CI/CD?
**Answer**: Faster delivery, fewer bugs, reduced manual work, and higher confidence in deployments.

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

---

## 📄 License

This project is created for educational purposes. Feel free to use it for learning DevOps concepts.

---

## 👨‍💻 Author

**Student CI/CD Lab Team**  
Created for final-year engineering students to learn DevOps

---

## 🌟 Acknowledgments

- Spring Boot Team
- React Team
- Jenkins Community
- Maven Community
- All contributors and learners

---

## 📞 Support

If you have questions or need help:

1. Check the [Troubleshooting](#troubleshooting) section
2. Review [Learning Resources](#learning-resources)
3. Open an issue on GitHub
4. Contact your instructor/mentor

---

**Happy Learning! 🚀**

Remember: DevOps is not just about tools, it's about culture, collaboration, and continuous improvement!
#   c i c d - i n t r e g r a t i o n - L a b  
 