pipeline {
    agent any

    environment {
        BACKEND_IMAGE = "my-backend:1.0"
        FRONTEND_IMAGE = "my-frontend:1.1"
        EC2_USER = "ec2-user"
        EC2_HOST = "3.109.158.104"
        FRONTEND_PORT = "3000"
        BACKEND_PORT = "8081"
        GIT_REPO = "https://github.com/shaiksalmanmca1/Demo.git"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: "${GIT_REPO}"
            }
        }

        stage('Deploy on EC2') {
            steps {
                sshagent(credentials: ['ec2-ssh-key']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} "
                            echo '>>> Removing old containers and images'
                            docker rm -f backend frontend || true
                            docker rmi -f ${BACKEND_IMAGE} ${FRONTEND_IMAGE} || true

                            echo '>>> Checking existing Demo folder before deletion'
                            ls -ld /home/${EC2_USER}/Demo || echo 'No Demo folder found'

                            echo '>>> Deleting existing Demo folder'
                            rm -rf /home/${EC2_USER}/Demo

                            echo '>>> Verifying Demo folder deletion'
                            ls -ld /home/${EC2_USER}/Demo || echo 'Demo folder successfully deleted'

                            echo '>>> Cloning fresh repo'
                            git clone ${GIT_REPO} /home/${EC2_USER}/Demo

                            echo '>>> Listing cloned files'
                            ls -l /home/${EC2_USER}/Demo

                            cd /home/${EC2_USER}/Demo/backend
                            echo '>>> Building backend image'
                            docker build --no-cache -t ${BACKEND_IMAGE} .
                            echo '>>> Running backend container'
                            docker run -d -p ${BACKEND_PORT}:8080 --name backend ${BACKEND_IMAGE}

                            cd ../frontend
                            if [ -f .env ]; then
                                chmod +w .env
                                sed -i 's|http://backend:8080|http://${EC2_HOST}:${BACKEND_PORT}|g' .env
                            fi
                            echo '>>> Building frontend image'
                            docker build --no-cache -t ${FRONTEND_IMAGE} .
                            echo '>>> Running frontend container'
                            docker run -d -p ${FRONTEND_PORT}:80 --name frontend ${FRONTEND_IMAGE}

                            echo '>>> Deployment finished'
                        "
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
