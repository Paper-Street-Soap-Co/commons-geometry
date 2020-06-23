pipeline {
  agent any
  stages {
    stage('Sonarqube') {
      environment {
        maven = tool "m3"
        sonarqube = tool 'sonarqube'
        sonarqube_token = credentials('sonarqube')
      }
      steps {
        script {
          env.GIT_REPO_NAME = env.GIT_URL.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
        }
        withEnv( ["PATH+MAVEN=${tool maven}/bin"] ) {
          withSonarQubeEnv('sonarqube') {
            sh """mvn sonar:soanr \
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
        }
        sleep(10)
        timeout(time: 60, unit: 'SECONDS') {
          waitForQualityGate abortPipeline: true
        }
      }
    }
  }
}
