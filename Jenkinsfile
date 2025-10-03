pipeline {
    agent any

    environment {
        BACKEND_IMAGE = "my-backend:1.0"
        FRONTEND_IMAGE = "my-frontend:1.1"
        EC2_USER = "ec2-user"
        EC2_HOST = "3.109.158.104"
        FRONTEND_PORT = "3000"
        BACKEND_PORT = "8081"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/shaiksalmanmca1/Demo.git'
            }
        }

        stage('Deploy on EC2') {
            steps {
                sshagent(credentials: ['ec2-ssh-key']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} '
                            cd ~/Demo
                            docker rm -f backend frontend || true
                            cd backend
                            docker build -t ${BACKEND_IMAGE} .
                            docker run -d -p ${BACKEND_PORT}:8080 --name backend ${BACKEND_IMAGE}
                            cd ../frontend
                            if [ -f .env ]; then
                                sed -i "s|http://backend:8080|http://${EC2_HOST}:${BACKEND_PORT}|g" .env
                            fi
                            docker build -t ${FRONTEND_IMAGE} .
                            docker run -d -p ${FRONTEND_PORT}:80 --name frontend ${FRONTEND_IMAGE}
                        '
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ Deployment successful!"
            echo "Backend: http://${EC2_HOST}:${BACKEND_PORT}"
            echo "Frontend: http://${EC2_HOST}:${FRONTEND_PORT}"
        }
        failure {
            echo "❌ Build or deployment failed!"
        }
    }
}
