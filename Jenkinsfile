pipeline {
    agent none
    stages {
        stage('Build') {
            agent { docker 'gradle:4.2.1-jdk8-alpine' }
            steps {
                archive includes: 'build/libs/*.jar'
            }
        }
    }
}
