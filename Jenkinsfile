pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out the code...'
                checkout scm
            }
        }
        stage('Build') {
            steps {
                echo 'Building the project...'
                bat 'mvn clean package'
            }
        }
        stage('Staging Deployment') {
            steps {
                echo 'Deploying to staging...'
                bat 'kubectl apply -f staging-backend-deployment.yaml'
                bat 'kubectl apply -f staging-frontend-deployment.yaml'
            }
        }
        stage('Load Testing') {
            steps {
                echo 'Running load tests on staging...'
                bat 'jmeter -n -t fibonacci-load-test.jmx -l test-results.jtl'
            }
        }
        stage('Evaluate Load Test Results') {
            steps {
                script {
                    def errorRate = bat(script: 'findstr "Err:" test-results.jtl | find /c "Err:"', returnStdout: true).trim()
                    if (errorRate == "0") {
                        echo 'Load test passed. Proceeding to production deployment.'
                    } else {
                        error 'Load test failed. Stopping pipeline.'
                    }
                }
            }
        }
        stage('Production Deployment') {
            steps {
                echo 'Deploying to production...'
                bat 'kubectl apply -f production-backend-deployment.yaml'
                bat 'kubectl apply -f production-frontend-deployment.yaml'
            }
        }
        stage('Rollback') {
            when {
                expression { currentBuild.result == 'FAILURE' }
            }
            steps {
                echo 'Rolling back deployment...'
                bat 'kubectl rollout undo deployment fibonacci-backend'
                bat 'kubectl rollout undo deployment fibonacci-frontend'
            }
        }
    }
}
