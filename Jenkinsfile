pipeline {
    agent {
        docker {
            image 'openjdk:alpine'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'ls -lag'
                sh './gradlew --version'
            }
        }
    }
}

