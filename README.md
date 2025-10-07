# 📄 README – Jenkins Shared Library for Docker & Maven Pipelines

## 🚀 Overview
This repository contains a **Jenkins Shared Library** that provides reusable pipeline functions for building, pushing, and managing Docker images, as well as building Java Maven applications.

It includes:
- A **Groovy helper class** `Docker.groovy` under the `com.example` package.
- Multiple reusable pipeline steps defined under the `vars/` directory.

This library is intended to simplify CI/CD pipelines for projects using **Maven** and **Docker**, particularly when deploying to environments such as **AWS EC2** or **ECR**.

---

## 🧱 Folder Structure
```
jenkins-shared-library/
│
├── src/
│   └── com/
│       └── example/
│           └── Docker.groovy          # Groovy class for Docker operations
│
└── vars/
    ├── buildImage.groovy              # Wrapper for Docker image build
    ├── buildJar.groovy                # Wrapper for Maven build
    ├── dockerLogin.groovy             # Wrapper for Docker Hub login
    └── dockerPush.groovy              # Wrapper for Docker push
```

---

## 💡 Purpose
The shared library is designed to **centralize common Jenkins pipeline operations** such as building Java artifacts, creating Docker images, logging into Docker Hub, and pushing images.

By abstracting these actions into shared functions, pipelines become **cleaner**, **shorter**, and **easier to maintain**.

---

## ⚙️ Components

### 🧩 1. `src/com/example/Docker.groovy`
A serializable Groovy class that defines reusable Docker-related methods.

#### Methods
| Method | Description |
|---------|--------------|
| `buildDockerImage(String imageName)` | Builds a Docker image from the current workspace |
| `dockerLogin()` | Logs in to Docker Hub using Jenkins credentials (`docker-hub-repo`) |
| `dockerPush(String imageName)` | Pushes the Docker image to the repository |

#### Example Usage
```groovy
def docker = new com.example.Docker(this)
docker.buildDockerImage('my-app:latest')
docker.dockerLogin()
docker.dockerPush('my-app:latest')
```

---

### 🧩 2. `vars/buildJar.groovy`
A shared step for compiling a Java Maven application.

```groovy
#!/usr/bin/env groovy
def call() {
  echo "building the application for $GIT_BRANCH"
  sh 'mvn clean package'
}
```

#### Purpose
- Cleans and builds the Maven project.
- Can be reused across pipelines without redefining Maven commands.

---

### 🧩 3. `vars/buildImage.groovy`
Wraps the `Docker.buildDockerImage()` method from the `com.example.Docker` class.

```groovy
#!/usr/bin/env groovy
import com.example.Docker

def call(String imageName) {
    return new Docker(this).buildDockerImage(imageName)
}
```

#### Purpose
Simplifies image building by exposing it as a one-line step in Jenkins pipelines.

---

### 🧩 4. `vars/dockerLogin.groovy`
Wrapper around the `Docker.dockerLogin()` method.

```groovy
#!/usr/bin/env groovy
import com.example.Docker

def call() {
    return new Docker(this).dockerLogin()
}
```

#### Purpose
- Logs into Docker Hub securely using Jenkins credentials (ID: `docker-hub-repo`).
- Prevents credentials from being hardcoded in pipelines.

---

### 🧩 5. `vars/dockerPush.groovy`
Wrapper around the `Docker.dockerPush()` method.

```groovy
#!/usr/bin/env groovy
import com.example.Docker

def call(String imageName) {
    return new Docker(this).dockerPush(imageName)
}
```

#### Purpose
- Pushes built images to Docker Hub (or any configured registry).
- Uses Jenkins environment context for clean and secure pushes.

---

## 🧪 Example Jenkinsfile Using the Library

```groovy
@Library('jenkins-shared-library@main') _

pipeline {
    agent any
    tools {
        maven 'maven-3.9'
    }
    stages {
        stage('Build Jar') {
            steps {
                buildJar()
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    def imageName = "myuser/demo-app:1.0"
                    buildImage(imageName)
                    dockerLogin()
                    dockerPush(imageName)
                }
            }
        }
    }
}
```

✅ This example demonstrates how the shared library simplifies pipeline code by exposing simple functions instead of long shell commands.

---

## 🔐 Credentials
The following Jenkins credentials are required:
| ID | Type | Purpose |
|----|------|----------|
| `docker-hub-repo` | Username/Password | Used for `docker login` authentication |

---

## 📚 Requirements
- Jenkins with **Pipeline** and **Shared Library** support.  
- Docker installed on the Jenkins agent.  
- Maven installed and configured in Jenkins.  
- Valid credentials for Docker Hub or your private registry.

---

## ✅ Summary
This Jenkins Shared Library provides modular, reusable functions for:
- Building Java applications with Maven.  
- Creating and pushing Docker images.  
- Managing Docker authentication securely.  

By using this library, Jenkins pipelines become more maintainable, secure, and consistent across multiple projects.
