pipeline {
    agent {
        docker {
            image 'gradle'
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

