pipeline {
    agent {
        label 'linux'
    }

    environment {
        DOCKER_USER = "ivanmikhailenka"
        DOCKER_PROJECT = "currency-converter"
        SERVER_HOST = "ec2-44-193-117-151.compute-1.amazonaws.com"
        SERVER_USER = "ubuntu"
    }

    stages {
        stage('Test') {
            steps {
                sh 'chmod +x ./mvnw'
                sh './mvnw test'
            }
        }
        stage('Build') {
            steps {
                sh './mvnw package -DskipTests'

                // Archive the JAR file as an artifact
                archiveArtifacts artifacts: 'currency-converter-contract/target/*.jar', fingerprint: true
                archiveArtifacts artifacts: 'currency-converter-server/target/*.jar', fingerprint: true
            }
        }
        stage('Push to artifactory'){
            steps {
                script {
                    def new_version = sh( script: """
                        # Fetch tags from Docker Hub
                        JSON_RESPONSE=\$(curl -s https://hub.docker.com/v2/repositories/${DOCKER_USER}/${DOCKER_PROJECT}/tags)

                        # Extract the version numbers from the JSON response
                        VERSIONS=\$(echo \$JSON_RESPONSE | tr ',' '\\n' | grep -Eo '"[0-9]+\\.[0-9]+\\.[0-9]+"' | tr -d '"' | sort -V)

                        # Get the latest version
                        LATEST_TAG=\$(echo "\$VERSIONS" | tail -n 1)

                        # If there are no version tags yet, start from 0.0.0
                        if [ -z "\${LATEST_TAG}" ]; then
                          LATEST_TAG="0.0.0"
                        fi

                        # Split the version into parts
                        MAJOR_VERSION=\$(echo \${LATEST_TAG} | cut -d "." -f 1)
                        MINOR_VERSION=\$(echo \${LATEST_TAG} | cut -d "." -f 2)
                        PATCH_VERSION=\$(echo \${LATEST_TAG} | cut -d "." -f 3)

                        # Increment the patch version
                        NEW_PATCH_VERSION=\$((PATCH_VERSION + 1))

                        # Construct the new version
                        NEW_TAG="\${MAJOR_VERSION}.\${MINOR_VERSION}.\${NEW_PATCH_VERSION}"

                        # Print the new version
                        echo \${NEW_TAG}
                    """, returnStdout: true).trim().tokenize('\n').last()

                    sh 'echo "Build and push the Docker image to Docker Hub"'

                    docker.withRegistry('https://docker.io', 'docker.hub') {}
                    sh """
                        docker build -t ${env.DOCKER_USER}/${env.DOCKER_PROJECT}:${new_version} .
                        docker build -t ${env.DOCKER_USER}/${env.DOCKER_PROJECT}:latest .
                        docker push ${env.DOCKER_USER}/${env.DOCKER_PROJECT}:${new_version}
                        docker push ${env.DOCKER_USER}/${env.DOCKER_PROJECT}:latest
                    """
                }
            }
        }
        stage('Deploy') {
            steps {
                withCredentials([[$class: 'UsernamePasswordMultiBinding',
                                  credentialsId: 'docker.hub',
                                  usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                    sshagent(credentials: ['ec2-dev']) {
                        sh """ssh -o StrictHostKeyChecking=no -l $SERVER_USER $SERVER_HOST /bin/bash << EOF
                            docker login --username $USERNAME --password $PASSWORD
                            docker pull $DOCKER_USER/$DOCKER_PROJECT
                            if docker container inspect $DOCKER_PROJECT >/dev/null 2>&1; then
                                docker stop $DOCKER_PROJECT
                                docker rm $DOCKER_PROJECT
                            fi
                            docker run -d -p 8080:8080 --name $DOCKER_PROJECT $DOCKER_USER/$DOCKER_PROJECT
                        << EOF
                        """
                    }
                }
            }
        }
    }
}
