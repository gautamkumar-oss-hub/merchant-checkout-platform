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
        sh 'kubectl apply -f k8s/dependencies.yaml'
        sh 'kubectl apply -f k8s/deployment.yaml'
        sh 'kubectl apply -f k8s/monitoring.yaml'
      }
    }
  }

  post {
    always {
      junit 'target/surefire-reports/*.xml'
    }
  }
}
