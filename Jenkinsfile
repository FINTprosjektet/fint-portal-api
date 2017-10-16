pipeline {
    agent none
    stages {
        stage('Build') {
            agent { dockerfile true }
            steps {
                sh "find build/libs/"
                archive includes: 'build/libs/*.jar'
            }
        }
    }
}

