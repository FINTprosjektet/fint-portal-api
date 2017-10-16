pipeline {
    agent none
    stages {
        stage('Build') {
            agent { dockerfile true }
            steps {
                archive includes: 'build/libs/*.jar'
            }
        }
    }
}

