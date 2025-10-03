pipeline {
    agent any

    stages {
        stage('Test SSH') {
            steps {
                sshagent(credentials: ['ec2-ssh-key']) {
                    sh 'ssh -o StrictHostKeyChecking=no ec2-user@3.109.158.104 "echo Hello from EC2"'
                }
            }
        }
    }
}
