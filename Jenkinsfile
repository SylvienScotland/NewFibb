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
                sh 'mvn clean package'
            }
        }
        stage('Staging Deployment') {
            steps {
                echo 'Deploying to staging...'
                sh 'kubectl apply -f staging-backend-deployment.yaml'
                sh 'kubectl apply -f staging-frontend-deployment.yaml'
            }
        }
        stage('Load Testing') {
            steps {
                echo 'Running load tests on staging...'
                sh 'jmeter -n -t fibonacci-load-test.jmx -l test-results.jtl'
            }
        }
        stage('Evaluate Load Test Results') {
            steps {
                script {
                    def errorRate = sh(script: "grep 'Err:' test-results.jtl | awk '{print $NF}'", returnStdout: true).trim()
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
                sh 'kubectl apply -f production-backend-deployment.yaml'
                sh 'kubectl apply -f production-frontend-deployment.yaml'
            }
        }
        stage('Rollback') {
            when {
                expression { currentBuild.result == 'FAILURE' }
            }
            steps {
                echo 'Rolling back deployment...'
                sh 'kubectl rollout undo deployment fibonacci-backend'
                sh 'kubectl rollout undo deployment fibonacci-frontend'
            }
        }
    }
}
