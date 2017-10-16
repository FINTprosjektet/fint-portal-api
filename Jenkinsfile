pipeline {
    agent {
        docker 'gradle:4.0'
    }
    stages {
        stage('Build') {
            steps {
                sh 'gradle clean build'
            }
        }
    }
}

