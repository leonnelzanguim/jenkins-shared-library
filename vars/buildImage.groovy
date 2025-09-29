#!/user/bin/env groovy

def call(String imageName) {
    echo 'testing the application...'
  withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
    sh "docker build -t $imageName -f java-maven-app/Dockerfile java-maven-app/"
    sh 'echo $PASS | docker login -u $USER --password-stdin'
    sh "docker push $imageName"
  }
}
