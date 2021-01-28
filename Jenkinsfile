pipeline {
    agent {
        docker {
            label 'docker'
            image 'gradle:6.7.1-jdk11-openj9'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'gradle --no-daemon clean build'
                archiveArtifacts 'build/libs/*.jar'
            }
        }
        stage('Deploy') {
            environment {
                BINTRAY = credentials('fint-bintray')
            }
            when {
                tag pattern: "v\\d+\\.\\d+\\.\\d+(-\\w+-\\d+)?", comparator: "REGEXP"
            }
            steps {
                script {
                    VERSION = TAG_NAME[1..-1]
                }
                sh "echo Version is ${VERSION}"
                sh "gradle --no-daemon -Pversion=${VERSION} -PbintrayUser=${BINTRAY_USR} -PbintrayKey=${BINTRAY_PSW} bintrayUpload"
            }
        }
    }
}
