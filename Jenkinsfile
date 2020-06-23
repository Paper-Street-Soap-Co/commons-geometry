pipeline {
  agent any
  tools {
    maven 'm3'
  }
  stages {
    stage('Sonarqube') {
      environment {
        sonarqube = tool 'sonarqube'
        sonarqube_token = credentials('sonarqube')
      }
      steps {
        script {
          env.GIT_REPO_NAME = env.GIT_URL.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
        }
        withSonarQubeEnv('sonarqube') {
          sh """mvn sonar:sonar \
            -Dsonar.host.url="https://sonar.hauntedmansion.io" \
            -Dsonar.login=${sonarqube_token} \
            -Dsonar.projectKey=acg \
            -Dsonar.pullrequest.key=${env.CHANGE_ID} \
            -Dsonar.pullrequest.branch=${BRANCH_NAME} \
            -Dsonar.pullrequest.base=${CHANGE_TARGET} \
            -Dsonar.pullrequest.github.repository=${env.GIT_REPO_NAME} \
            -Dsonar.scm.provider=git \
            -Dsonar.java.binaries=/tmp
          """
        }
        sleep(10)
        timeout(time: 60, unit: 'SECONDS') {
          waitForQualityGate abortPipeline: true
        }
      }
    }
  }
}
