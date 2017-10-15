pipeline {
    agent {
        docker {
            image 'gradle'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'gradle clean build'
            }
        }
    }
}

