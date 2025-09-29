#!/user/bin/env groovy

def call() {
  echo 'building the application...'
  sh 'mvn -f java-maven-app/pom.xml package'
}
