pipeline {
    agent none
    stages {
        stage('Prepare') {
            agent any
            steps {
                sh "git log --oneline | nl -nln | perl -lne 'if (/^(\\d+).*Version (\\d+\\.\\d+\\.\\d+)/) { print \"$2-$1\"; exit; }"
            }
        }
        stage('Build') {
            agent { dockerfile true }
            steps {
                sh "find build/libs/"
                archive includes: 'build/libs/*.jar'
            }
        }
    }
}

