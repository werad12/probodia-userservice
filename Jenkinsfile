pipeline {
    agent any
    environment {
        BUILD_NUMBER = '1.0.1'
    }
    stages {
        stage('========== Clone repository ==========') {
            steps {
                script {
                    echo "VERSION : ${env.BUILD_NUMBER}"
                    checkout scm
                }
            }
        }
        stage('========== Build image ==========') {
            steps{
                script {
                    app = docker.build("werad12/probodia-user")
                }
            }
        }
        stage('========== Push image ==========') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'dockerCred') {
                        app.push("${env.BUILD_NUMBER}")
                        app.push("latest")
                    }
                }
            }
        }
        stage('SSH transfer') {
            steps {
                script {
                    sshPublisher(
                            continueOnError: false, failOnError: true,
                            publishers: [
                                    sshPublisherDesc(
                                            configName: "discovery",
                                            verbose: true,
                                            transfers: [
                                                    sshTransfer(
                                                            sourceFiles: "",
                                                            removePrefix: "",
                                                            remoteDirectory: "",
                                                            execCommand: "sh deploy-user.sh"
                                                    )
                                            ]
                                    )
                            ]
                    )
                }
            }
        }
    }
}

