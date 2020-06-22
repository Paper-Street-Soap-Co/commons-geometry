pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh "uname -a"
        echo sh(returnStdout: true, script: 'env')
      }
    }
  }
}
