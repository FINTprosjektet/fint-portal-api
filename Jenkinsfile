pipeline {
    agent none
    stages {
        stage('Build') {
            agent { docker 'gradle:4.2.1-jdk8-alpine' }
            steps {
                sh 'gradle --no-daemon clean build'
                sh 'find build/libs'
                archiveArtifacts 'build/libs/*.jar'
            }
        }
    }
}
