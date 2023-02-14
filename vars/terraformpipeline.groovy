def call(String TERRAFORM_DIR) {
		
pipeline {
    agent any
	
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

        stage('Terraform-Approval') {
            steps {
	        dir("${TERRAFORM_DIR}") {		    
                script {
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
                sh 'terraform apply -input=false tfplan'
                
            }
            }

        }
    }	
}
