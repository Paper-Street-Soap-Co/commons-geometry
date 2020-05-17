pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh "uname -a"
        echo sh(returnStdout: true, script: 'env')
      }
    }
    stage('Sonarqube') {
      environment {
       sonarqube = tool 'sonarqube'
       sonarqube_token = credentials('sonarqube') 
      }
      steps {
        withSonarQubeEnv('sonarqube') {
          sh """${sonarqube}/bin/sonar-scanner \
            -Dsonar.host.url="https://sonar.hauntedmansion.io" \
            -Dsonar.login=${sonarqube_token} \
            -Dsonar.projectKey=acg \
            -Dsonar.pullrequest.key=${env.CHANGE_ID} \
            -Dsonar.pullrequest.branch="jenkins" \
            -Dsonar.pullrequest.base="dev" \
            -Dsonar.pullrequest.github.repository="Paper-Street-Soap-Co/commons-geometry" \
            -Dsonar.scm.provider=git \
            -Dsonar.java.binaries=/tmp
          """
        }
        timeout(time: 10, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
      } 
    }
  }
}
