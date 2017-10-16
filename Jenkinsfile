pipeline {
    agent none
    parameters {
        gitParam('MY_GIT_TAG')
    }
    stages {
        stage('Build') {
            agent { dockerfile true }
            steps {
                echo "The Git tag is ${param.MY_GIT_TAG}"
            }
        }
    }
}

