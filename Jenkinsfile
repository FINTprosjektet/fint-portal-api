pipeline {
    agent {
        docker {
            image 'gradle'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew --version'
            }
        }
    }
}

