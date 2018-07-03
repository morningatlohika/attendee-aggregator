#!/usr/bin/env groovy Jenkinsfile

def server = Artifactory.server "Artifactory"
def rtMaven = Artifactory.newMavenBuild()
def buildInfo = Artifactory.newBuildInfo()
buildInfo.env.capture = true

pipeline() {

    agent any

    triggers {
        pollSCM('H/10 * * * *')
    }

    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    environment {
        SLACK_AUTOMATION_CHANNEL = "#automation"
        SLACK_AUTOMATION_TOKEN = credentials("jenkins-ci-integration-token")
        JENKINS_HOOKS = credentials("morning-at-lohika-jenkins-ci-hooks")
    }

    stages {

        stage('Configuration') {
            steps {
                script {
                    rtMaven.deployer server: server, snapshotRepo: 'morning-at-lohika-snapshots'
                }
            }
        }

        stage('Maven build and deploy') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'master') {
                        sh "./mvnw clean install"
                    } else {
                        sh "./mvnw clean install"
                    }
                    buildInfo.env.filter.addExclude("*TOKEN*")
                    buildInfo.env.filter.addExclude("*HOOK*")
                    buildInfo.env.collect()
                }
            }
        }
    }

    post {
        always {
            script {
                publishHTML(target: [
                    allowMissing         : true,
                    alwaysLinkToLastBuild: false,
                    keepAll              : true,
                    reportDir            : 'build/reports/tests/test',
                    reportFiles          : 'index.html',
                    reportName           : "Test Summary"
                ])
                junit testResults: 'build/test-results/test/*.xml', allowEmptyResults: true
                server.publishBuildInfo buildInfo
            }
        }

        success {
            script {
                dir("${env.WORKSPACE}") {
                    archiveArtifacts '*/target/*.jar'
                }

                slackSend(
                    baseUrl: "${env.JENKINS_HOOKS}",
                    token: "${env.SLACK_AUTOMATION_TOKEN}",
                    channel: "${env.SLACK_AUTOMATION_CHANNEL}",
                    botUser: true,
                    color: "good",
                    message: "BUILD SUCCESS: Job ${env.JOB_NAME} [${env.BUILD_NUMBER}]\nCheck console output at: ${env.BUILD_URL}"
                )
            }
        }

        failure {
            script {
                slackSend(
                    baseUrl: "${env.JENKINS_HOOKS}",
                    token: "${env.SLACK_AUTOMATION_TOKEN}",
                    channel: "${env.SLACK_AUTOMATION_CHANNEL}",
                    botUser: true,
                    color: "danger",
                    message: "BUILD FAILURE: Job ${env.JOB_NAME} [${env.BUILD_NUMBER}]\nCheck console output at: ${env.BUILD_URL}"
                )
            }
        }
    }
}