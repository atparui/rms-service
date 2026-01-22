pipeline {
    agent any

    stages {
        stage('Setup') {
            steps {
                sh 'chmod +x ./mvnw'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Jib Build & Push') {
            steps {
                sh './mvnw -Pprod verify -DskipTests jib:build -Ddocker.username=shivain22 -Ddocker.password="Asd!@#123"'
            }
        }

        stage('Deploy to Platform') {
            steps {
                sh 'cd /platform && ./scripts/down.sh dev rms-service'
                sh 'cd /platform && RMS_SERVICE_IMAGE_OVERRIDE=docker.io/shivain22/rms-service RMS_SERVICE_IMAGE_TAG_OVERRIDE=latest ./scripts/up.sh dev rms-service'
                sh 'cd /platform && ./scripts/generate-configs.sh'
            }
        }

    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
    }
}
