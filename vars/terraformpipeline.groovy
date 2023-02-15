def call(Map args =[ jenkins_agent: '', tf_dir: '', tf_workspace: ''] ){

    args.tf_dir = args.tf_dir ?: '.'
    args.jenkins_agent = args.jenkins_agent ?: 'master'
    args.tf_workspace = args.tf_workspace ?: 'default'	

pipeline {
    agent {
        label "${args.jenkins_agent}"
    }

    environment {
        TERRAFORM_DIR = "$args.tf_dir"
        TERRAFORM_WORKSPACE = "$args.tf_workspace"
    }	
    
    stages {
        
        
        stage("Clean Workspace") {
                steps {
                    cleanWs()
                }
            }
    
        stage("Terraform init") {
            steps {
                dir("${TERRAFORM_DIR}") {

                        sh "ls -la"
                    sh "terraform init -input=false"	
                }
                
            } 
        }
                
        stage('Terraform-Format') {
            steps {
        dir("${TERRAFORM_DIR}") {  
                sh "terraform fmt -list=true -diff=true"
        }
            }
            }

        stage('Terraform-Validate') {
            steps {
            dir("${TERRAFORM_DIR}") {    
                sh "terraform validate"
        }
            }
            }

        stage('Terraform-Plan') {
            steps {
        dir("${TERRAFORM_DIR}") {	    
                sh 'terraform plan -out tfplan'
        }
            }
            }

        stage('NonprodApproval') {
                when { 
                    expression {"${TERRAFORM_WORKSPACE}" != 'prod' }
        }
                steps {
                    
                    dir("${TERRAFORM_DIR}") {		    
                        script {
                             echo "${TERRAFORM_WORKSPACE} no approval needed here"
                            
                        }
                    }   
                    }    
            }

            stage('ProdApproval') {
                when {
                    expression { "${TERRAFORM_WORKSPACE}" == 'prod' }
                }
                steps {
                     
                       dir("${TERRAFORM_DIR}") {
                        script {

                             echo "${TERRAFORM_WORKSPACE}"
                        timeout(time: 10, unit: 'MINUTES') {
                            def userInput = input(id: 'Approve', message: 'Do You Want To Apply The Terraform Changes?', parameters: [
                            [$class: 'BooleanParameterDefinition', defaultValue: false, description: 'Apply Terraform Changes', name: 'Approve?']
                            ])
                        }
                        }   
                        }
                    }    
            }

            stage('Terraform-Apply') {
              steps {
                dir("${TERRAFORM_DIR}") { 
                  sh 'terraform apply -input=false tfplan'
                
            }
              }
            }    
            

            }
    }
}  