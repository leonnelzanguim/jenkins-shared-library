#!/user/bin/env groovy

def call() {
  echo "building the application for $GIT_BRANCH"
  // sh 'mvn -f java-maven-app/pom.xml package'
  sh 'mvn clean package'
}
