pipeline {
    agent any
    
    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }
    
    options {
        timestamps()
        timeout(time: 20, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    
    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Select browser for testing')
        booleanParam(name: 'HEADLESS', defaultValue: false, description: 'Run tests in headless mode')
    }
    
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo '========================================='
                    echo 'STAGE 1: CHECKOUT CODE FROM GITHUB'
                    echo '========================================='
                }
                
                checkout scm
                
                script {
                    echo "Repository URL: ${env.GIT_URL}"
                    echo "Branch: ${env.GIT_BRANCH}"
                    echo "Commit: ${env.GIT_COMMIT}"
                }
            }
        }
        
        stage('Environment Info') {
            steps {
                script {
                    echo '========================================='
                    echo 'STAGE 2: DISPLAY ENVIRONMENT INFORMATION'
                    echo '========================================='
                }
                
                bat '''
                    echo Workspace: %WORKSPACE%
                    echo Jenkins Home: %JENKINS_HOME%
                    echo Job Name: %JOB_NAME%
                    echo Build Number: %BUILD_NUMBER%
                    echo.
                    echo Java Version:
                    java -version
                    echo.
                    echo Maven Version:
                    mvn -version
                    echo.
                '''
            }
        }
        
        stage('Clean') {
            steps {
                script {
                    echo '========================================='
                    echo 'STAGE 3: CLEAN PREVIOUS BUILD ARTIFACTS'
                    echo '========================================='
                }
                
                bat 'mvn clean'
                
                script {
                    echo 'Previous build artifacts cleaned successfully'
                }
            }
        }
        
        stage('Compile') {
            steps {
                script {
                    echo '========================================='
                    echo 'STAGE 4: COMPILE SOURCE CODE'
                    echo '========================================='
                }
                
                bat 'mvn compile'
                
                script {
                    echo 'Source code compiled successfully'
                }
            }
        }
        
        stage('Run Tests') {
            steps {
                script {
                    echo '========================================='
                    echo 'STAGE 5: RUN SELENIUM AUTOMATION TESTS'
                    echo '========================================='
                    echo "Browser: ${params.BROWSER}"
                    echo "Headless Mode: ${params.HEADLESS}"
                    echo '========================================='
                }
                
                bat '''
                    echo Starting test execution...
                    mvn test -Dbrowser=%BROWSER% -Dheadless=%HEADLESS%
                '''
                
                script {
                    echo 'All tests executed successfully'
                }
            }
        }
        
        stage('Generate Reports') {
            steps {
                script {
                    echo '========================================='
                    echo 'STAGE 6: GENERATE TEST REPORTS'
                    echo '========================================='
                }
                
                bat '''
                    echo Generating TestNG reports...
                    if exist test-output echo TestNG reports generated in test-output directory
                    if exist target\\surefire-reports echo Surefire reports generated
                '''
                
                script {
                    echo 'Reports generated successfully'
                }
            }
        }
    }
    
    post {
        always {
            script {
                echo '========================================='
                echo 'POST-BUILD ACTIONS'
                echo '========================================='
            }
            
            // Publish TestNG Results
            script {
                echo 'Publishing TestNG test results...'
            }
            publishHTML([
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'test-output',
                reportFiles: 'index.html',
                reportName: 'TestNG HTML Report',
                reportTitles: 'ParaBank Automation Report'
            ])
            
            // Publish JUnit Results
            script {
                echo 'Publishing JUnit test results...'
            }
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
            
            // Archive Artifacts
            script {
                echo 'Archiving test artifacts...'
            }
            archiveArtifacts artifacts: 'test-output/**/*,target/surefire-reports/**/*', allowEmptyArchive: true
            
            // Clean Workspace
            script {
                echo 'Cleaning workspace...'
            }
            cleanWs(
                deleteDirs: true,
                patterns: [
                    [pattern: 'target/', type: 'INCLUDE']
                ]
            )
        }
        
        success {
            script {
                echo '========================================='
                echo 'BUILD STATUS: SUCCESS'
                echo '========================================='
                echo 'All stages completed successfully!'
                echo 'Test execution: PASSED'
                echo 'Reports generated and archived'
                echo '========================================='
            }
            
            // Send email notification (optional)
            // emailext(
            //     subject: "Jenkins Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            //     body: """
            //         Build Successful!
            //         
            //         Job: ${env.JOB_NAME}
            //         Build Number: ${env.BUILD_NUMBER}
            //         Build URL: ${env.BUILD_URL}
            //         
            //         Check console output at: ${env.BUILD_URL}console
            //     """,
            //     to: 'your.email@example.com'
            // )
        }
        
        failure {
            script {
                echo '========================================='
                echo 'BUILD STATUS: FAILURE'
                echo '========================================='
                echo 'Build failed! Check console output for details.'
                echo 'Build URL: ' + env.BUILD_URL
                echo '========================================='
            }
            
            // Send email notification (optional)
            // emailext(
            //     subject: "Jenkins Build FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            //     body: """
            //         Build Failed!
            //         
            //         Job: ${env.JOB_NAME}
            //         Build Number: ${env.BUILD_NUMBER}
            //         Build URL: ${env.BUILD_URL}
            //         
            //         Please check the console output: ${env.BUILD_URL}console
            //     """,
            //     to: 'your.email@example.com'
            // )
        }
        
        unstable {
            script {
                echo '========================================='
                echo 'BUILD STATUS: UNSTABLE'
                echo '========================================='
                echo 'Build completed but some tests may have failed'
                echo '========================================='
            }
        }
    }
}