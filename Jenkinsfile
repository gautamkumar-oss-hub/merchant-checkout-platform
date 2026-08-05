pipeline {
  agent any

  environment {
    IMAGE_NAME = 'checkout-platform'
    REGISTRY = 'registry.example.com'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }
    stage('Build') {
      steps {
        sh 'mvn -B clean package'
      }
    }
    stage('Test') {
      steps {
        sh 'mvn -B test'
      }
    }
    stage('Containerize') {
      steps {
        sh 'docker build -t ${REGISTRY}/${IMAGE_NAME}:latest .'
      }
    }
    stage('Deploy') {
      steps {
        echo 'Deploy stage placeholder - configure Kubernetes deploy steps here.'
      }
    }
  }

  post {
    always {
      junit 'target/surefire-reports/*.xml'
    }
  }
}
