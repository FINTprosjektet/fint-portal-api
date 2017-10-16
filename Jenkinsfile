pipeline {
    agent none
    environment {
        VERSION=sh(returnStdout: true, script: "git log --oneline | nl -nln | perl -lne 'if (/^(\\d+).*Version (\\d+\\.\\d+\\.\\d+)/) { print \"\$2-\$1\"; exit; }'")
    }
    stages {
        stage('Prepare') {
            agent { label 'master' }
            steps {
                echo "Version is: ${env.VERSION}"
            }
        }
        stage('Build') {
            agent { 
                dockerfile { 
                    label 'docker' 
                    additionalBuildArgs '--build-arg VERSION=${env.VERSION}'
                } 
            }
            steps {
                archive includes: 'build/libs/*.jar'
            }
        }
    }
}

