pipeline {
    agent none
    stages {
        stage('Build') {
            agent { docker 'gradle:4.2.1-jdk8-alpine' }
            steps {
                sh 'git log --oneline | nl -nln | perl -lne \'if (/^(\\d+).*Version (\\d+\\.\\d+\\.\\d+)/) { print "$2-$1"; exit; }\' > version.txt'
                sh 'gradle --no-daemon clean build'
                sh 'cat version.txt'
                archiveArtifacts 'build/libs/*.jar'
            }
        }
    }
}
