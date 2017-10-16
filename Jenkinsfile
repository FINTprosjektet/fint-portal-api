pipeline {
    agent none
    stages {
        stage('Prepare') {
            agent { label 'master' }
            steps {
                sh "git log --oneline | nl -nln | perl -lne 'if (/^(\\d+).*Version (\\d+\\.\\d+\\.\\d+)/) { print \"\$2-\$1\"; exit; }'"
            }
        }
        stage('Build') {
            agent { dockerfile { label 'docker' } }
            steps {
                sh "find build/libs/"
                archive includes: 'build/libs/*.jar'
            }
        }
    }
}

