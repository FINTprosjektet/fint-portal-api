pipeline {
    agent { dockerfile true }
    stages {
        stage('Build') {
            steps {
                sh 'gradle clean build'
            }
        }
        stage('Publish') {
            steps {
                sh 'gradle bintrayUpload'
            }
        }
    }
}

