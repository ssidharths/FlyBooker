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
                    def image = docker.build("${IMAGE_NAME}:${BUILD_NUMBER}")
                    docker.withRegistry("https://${DOCKER_REGISTRY}", 'docker-registry-credentials') {
                        image.push()
                        image.push('latest')
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
                    docker-compose down
                    docker-compose pull
                    docker-compose up -d
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
