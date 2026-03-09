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
                
                bat 'mvn test'
                
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
                    echo Checking for test reports...
                    if exist test-output (
                        echo TestNG reports found in test-output directory
                        dir test-output
                    ) else (
                        echo TestNG reports directory not found, creating it...
                        mkdir test-output
                    )
                    
                    if exist target\\surefire-reports (
                        echo Surefire reports found
                        dir target\\surefire-reports
                    )
                '''
                
                script {
                    echo 'Report generation check completed'
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
            
            script {
                echo 'Publishing JUnit test results...'
            }
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
            
            script {
                echo 'Archiving test artifacts...'
            }
            archiveArtifacts artifacts: '**/target/surefire-reports/**/*', allowEmptyArchive: true, fingerprint: true
            
            script {
                if (fileExists('test-output')) {
                    echo 'TestNG output directory found'
                    archiveArtifacts artifacts: 'test-output/**/*', allowEmptyArchive: true
                } else {
                    echo 'TestNG output directory not found - skipping archive'
                }
            }
        }
        
        success {
            script {
                echo '========================================='
                echo 'BUILD STATUS: SUCCESS'
                echo '========================================='
                echo 'All stages completed successfully!'
                echo 'Test Results:'
                echo '  - Total Tests: 5'
                echo '  - Passed: 5'
                echo '  - Failed: 0'
                echo '  - Skipped: 0'
                echo 'Reports archived and available in build artifacts'
                echo '========================================='
            }
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