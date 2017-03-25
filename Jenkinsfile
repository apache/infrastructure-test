pipeline {
        agent {
        label 'Windows'
    }
    stages {
        stage('JAVA') {
            steps { 
                bat 'echo %JAVA_HOME%'
            }
        }
        stage('ANT') {
            steps { 
                bat 'echo %ANT_HOME%'
            }
        }    
        stage('MAVEN') {
            steps { 
                bat 'echo %MAVEN_HOME%'
            }
        }
    }
}
