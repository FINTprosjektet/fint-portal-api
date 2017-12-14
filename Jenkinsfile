pipeline {
    agent none
    stages {
        stage('Prepare') {
            agent { label 'master' }
            steps {
                script {
                    VERSION = sh 'git log --oneline | nl -nln | perl -lne \'if (/^(\\d+).*Version (\\d+\\.\\d+\\.\\d+)/) { print "$2-$1"; exit; }\''
                }
            }
        }
        stage('Build') {
            agent { 
                docker {
                    label 'docker'
                    image 'gradle:4.2.1-jdk8-alpine' 
                }
            }
            steps {
                sh "gradle -Pversion=${VERSION} --no-daemon clean build"
                stash includes: 'build/libs/*.jar', name: 'libs'
            }
        }
        stage('Deploy') {
            agent { 
                docker {
                    label 'docker'
                    image 'gradle:4.2.1-jdk8-alpine' 
                }
            }
            environment {
                BINTRAY = credentials('fint-bintray')
            }
            when {
                branch 'master'
            }
            steps {
                unstash 'libs'
                archiveArtifacts 'build/libs/*.jar'
                sh 'gradle --no-daemon -PbintrayUser=${BINTRAY_USR} -PbintrayKey=${BINTRAY_PSW} bintrayUpload'
            }
        }
    }
}
