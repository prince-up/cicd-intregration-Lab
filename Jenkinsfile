/*
 * ============================================================================
 * JENKINS PIPELINE AS CODE - STUDENT CI/CD LAB
 * ============================================================================
 * 
 * This Jenkinsfile defines a complete CI/CD pipeline for the Student Lab.
 * It demonstrates industry-standard DevOps practices.
 * 
 * PIPELINE STAGES:
 * 1. Checkout  - Pull code from GitHub
 * 2. Build     - Compile Java code using Maven
 * 3. Test      - Run JUnit tests
 * 4. Package   - Create executable JAR
 * 5. Deploy    - Deploy to server (simulated)
 * 
 * LEARNING NOTE:
 * This is "Pipeline as Code" - the pipeline is version controlled
 * along with the source code!
 * ============================================================================
 */

pipeline {
    
    /*
     * AGENT CONFIGURATION
     * Specifies where the pipeline should run
     * 'any' means Jenkins will use any available agent
     */
    agent any
    
    /*
     * TOOLS CONFIGURATION
     * Specifies which tools to use
     * These tools must be configured in Jenkins Global Tool Configuration
     */
    tools {
        maven 'Maven-3'  // Maven for build automation
        jdk 'JDK-17'     // Java Development Kit 17
    }
    
    /*
     * ENVIRONMENT VARIABLES
     * Define variables used across the pipeline
     */
    environment {
        // Project name
        PROJECT_NAME = 'student-cicd-lab'
        
        // Maven settings
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
        
        // Deployment directory (simulated)
        DEPLOY_DIR = 'C:\\deployments'
        
        // Backend API URL (for notification)
        BACKEND_API = 'http://localhost:8080/api/pipeline'
    }
    
    /*
     * PARAMETERS
     * These can be provided when triggering the build
     */
    parameters {
        string(name: 'REPO_URL', defaultValue: 'https://github.com/student/repo', description: 'GitHub Repository URL')
        string(name: 'BRANCH', defaultValue: 'main', description: 'Git Branch to Build')
        string(name: 'STUDENT_NAME', defaultValue: 'Student', description: 'Student Name')
    }
    
    /*
     * PIPELINE STAGES
     * Each stage represents a step in the CI/CD process
     */
    stages {
        
        /*
         * STAGE 1: CHECKOUT
         * Clone the repository from GitHub
         */
        stage('Checkout') {
            steps {
                script {
                    echo '========================================='
                    echo 'STAGE 1: CHECKOUT SOURCE CODE'
                    echo '========================================='
                    echo "Student: ${params.STUDENT_NAME}"
                    echo "Repository: ${params.REPO_URL}"
                    echo "Branch: ${params.BRANCH}"
                }
                
                // Clone repository
                // In real scenario, use: git url: "${params.REPO_URL}", branch: "${params.BRANCH}"
                // For demo, we use the current workspace
                echo 'Checking out source code from GitHub...'
                
                // Verify Maven installation
                bat 'mvn --version'
                
                echo '✓ Checkout completed successfully'
            }
        }
        
        /*
         * STAGE 2: BUILD
         * Compile the Java source code
         */
        stage('Build') {
            steps {
                script {
                    echo '========================================='
                    echo 'STAGE 2: BUILD APPLICATION'
                    echo '========================================='
                }
                
                // Change to backend directory
                dir('backend') {
                    // Clean previous builds
                    echo 'Running: mvn clean...'
                    bat 'mvn clean'
                    
                    // Compile source code
                    echo 'Running: mvn compile...'
                    bat 'mvn compile'
                }
                
                echo '✓ Build completed successfully'
            }
        }
        
        /*
         * STAGE 3: TEST
         * Run automated tests using JUnit
         */
        stage('Test') {
            steps {
                script {
                    echo '========================================='
                    echo 'STAGE 3: RUN AUTOMATED TESTS'
                    echo '========================================='
                }
                
                dir('backend') {
                    // Run tests
                    echo 'Running: mvn test...'
                    bat 'mvn test'
                }
                
                echo '✓ All tests passed successfully'
            }
            
            /*
             * POST-TEST ACTIONS
             * Publish test results to Jenkins
             */
            post {
                always {
                    // Publish JUnit test results
                    // Jenkins will display test reports in the UI
                    junit 'backend/target/surefire-reports/*.xml'
                    
                    echo 'Test results published to Jenkins'
                }
            }
        }
        
        /*
         * STAGE 4: PACKAGE
         * Create executable JAR file
         */
        stage('Package') {
            steps {
                script {
                    echo '========================================='
                    echo 'STAGE 4: PACKAGE APPLICATION'
                    echo '========================================='
                }
                
                dir('backend') {
                    // Create JAR file
                    echo 'Running: mvn package...'
                    bat 'mvn package -DskipTests'
                }
                
                echo '✓ Application packaged successfully'
                
                // Show created artifact
                bat 'dir backend\\target\\*.jar'
            }
        }
        
        /*
         * STAGE 5: DEPLOY
         * Deploy the application to server
         * (In this demo, we simulate deployment)
         */
        stage('Deploy') {
            steps {
                script {
                    echo '========================================='
                    echo 'STAGE 5: DEPLOY APPLICATION'
                    echo '========================================='
                }
                
                // Simulate deployment
                echo 'Deploying application to server...'
                
                // In production, you might:
                // 1. Copy JAR to server: scp target/*.jar user@server:/path/
                // 2. Restart service: ssh user@server 'systemctl restart app'
                // 3. Deploy to cloud: kubectl apply -f deployment.yaml
                
                dir('backend') {
                    // Simulate: Create deployment directory
                    sh "mkdir -p ${env.DEPLOY_DIR}"
                    
                    // Simulate: Copy JAR to deployment location
                    sh "cp target/*.jar ${env.DEPLOY_DIR}/ || true"
                }
                
                echo "✓ Application deployed to: ${env.DEPLOY_DIR}"
                echo '✓ Deployment completed successfully'
            }
        }
    }
    
    /*
     * POST-PIPELINE ACTIONS
     * Execute after all stages complete
     */
    post {
        /*
         * ALWAYS - Runs regardless of success or failure
         */
        always {
            echo '========================================='
            echo 'PIPELINE EXECUTION COMPLETED'
            echo '========================================='
            
            // Clean up workspace (optional)
            // cleanWs()
        }
        
        /*
         * SUCCESS - Runs only if pipeline succeeds
         */
        success {
            echo '✓✓✓ SUCCESS ✓✓✓'
            echo 'All stages completed successfully!'
            echo 'Application is ready for use.'
            
            // In production, you might:
            // - Send success notification to Slack/Email
            // - Update deployment status in database
            // - Trigger downstream jobs
        }
        
        /*
         * FAILURE - Runs only if pipeline fails
         */
        failure {
            echo '✗✗✗ FAILURE ✗✗✗'
            echo 'Pipeline failed. Please check the logs above.'
            
            // In production, you might:
            // - Send failure notification to team
            // - Create JIRA ticket
            // - Rollback deployment
        }
        
        /*
         * UNSTABLE - Runs if tests fail but build succeeds
         */
        unstable {
            echo '⚠⚠⚠ UNSTABLE ⚠⚠⚠'
            echo 'Build succeeded but tests failed.'
        }
    }
}

/*
 * ============================================================================
 * LEARNING NOTES FOR STUDENTS:
 * ============================================================================
 * 
 * 1. PIPELINE AS CODE
 *    - This Jenkinsfile is stored in version control with your code
 *    - Changes to the pipeline are tracked just like code changes
 *    - Team members can review pipeline changes via pull requests
 * 
 * 2. DECLARATIVE VS SCRIPTED PIPELINE
 *    - This uses Declarative syntax (easier to learn)
 *    - More structured and readable
 *    - Recommended for beginners
 * 
 * 3. STAGES
 *    - Each stage represents a logical step
 *    - Stages are executed sequentially
 *    - If one stage fails, subsequent stages don't run (by default)
 * 
 * 4. MAVEN LIFECYCLE
 *    - clean: Remove previous builds
 *    - compile: Compile source code
 *    - test: Run unit tests
 *    - package: Create JAR/WAR file
 * 
 * 5. CONTINUOUS INTEGRATION
 *    - Code is automatically built and tested on every commit
 *    - Catches bugs early in development
 *    - Ensures code always works
 * 
 * 6. CONTINUOUS DEPLOYMENT
 *    - Successful builds are automatically deployed
 *    - Reduces manual deployment errors
 *    - Faster delivery to production
 * 
 * ============================================================================
 * HOW TO USE THIS JENKINSFILE:
 * ============================================================================
 * 
 * 1. Create a new Pipeline job in Jenkins
 * 2. Configure it to use "Pipeline script from SCM"
 * 3. Point it to your GitHub repository
 * 4. Set the script path to "Jenkinsfile"
 * 5. Configure GitHub webhook to trigger builds on push
 * 
 * REQUIRED JENKINS CONFIGURATION:
 * - Install Maven Integration plugin
 * - Configure Maven in Global Tool Configuration
 * - Configure JDK 17 in Global Tool Configuration
 * - Install JUnit plugin for test reports
 * 
 * ============================================================================
 */
