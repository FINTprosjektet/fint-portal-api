pipeline {
    agent none
    stages {
        stage('Prepare') {
            agent { label 'master' }
            steps {
                script {
                    def VERSION=sh(returnStdout: true, script: "git log --oneline | nl -nln | perl -lne 'if (/^(\\d+).*Version (\\d+\\.\\d+\\.\\d+)/) { print \"\$2-\$1\"; exit; }'")
                    echo "Version is: ${VERSION}"
                }
            }
        }
        stage('Build') {
            agent { 
                dockerfile { 
                    label 'docker' 
                    additionalBuildArgs "--build-arg VERSION=${VERSION}"
                } 
            }
            steps {
                archive includes: 'build/libs/*.jar'
            }
        }
    }
}

