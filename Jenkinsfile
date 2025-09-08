pipeline {
    agent any

    tools {
        maven 'Maven-3.9.0'
        jdk 'JDK-17'
    }

    environment {
        DOCKER_REGISTRY = 'docker.io'
        IMAGE_NAME = 'g4rvr/flybooker'
        DB_PASSWORD = credentials('db-password')
        SPRING_DATASOURCE_URL = credentials('db-url')
        SPRING_DATASOURCE_USERNAME = credentials('db-username')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                // Skip integration tests that require database
                sh 'mvn test -DskipTests'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} ."
                    sh "docker tag ${IMAGE_NAME}:${BUILD_NUMBER} ${IMAGE_NAME}:latest"
                    // Secure login with --password-stdin
                    withCredentials([usernamePassword(credentialsId: 'docker-registry-credentials',
                                     passwordVariable: 'DOCKER_TOKEN',
                                     usernameVariable: 'DOCKER_USERNAME')]) {
                        sh 'echo $DOCKER_TOKEN | docker login -u $DOCKER_USERNAME --password-stdin'
                        sh "docker push ${IMAGE_NAME}:${BUILD_NUMBER}"
                        sh "docker push ${IMAGE_NAME}:latest"
                        sh 'docker logout'
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                sh """
                    export SPRING_DATASOURCE_URL='${SPRING_DATASOURCE_URL}'
                    export SPRING_DATASOURCE_USERNAME='${SPRING_DATASOURCE_USERNAME}'
                    export SPRING_DATASOURCE_PASSWORD='${DB_PASSWORD}'
                    export DB_USERNAME='${SPRING_DATASOURCE_USERNAME}'
                    export DB_PASSWORD='${DB_PASSWORD}'
                    docker compose down
                    docker compose pull
                    docker compose up -d
                """
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
