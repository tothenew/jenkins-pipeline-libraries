## Jenkins Shared Library.
- [vars/](https://github.com/Nitintothenew/jenkins-pipeline-libraries/tree/main/vars) - Groovy Shared Library reusable functions

## Prerequisite

- Terraform needs to be installed on the Jenkins servers (master and slave both)
- `$TERRAFROM_DIRECTORY` variable value where your terraform code
- To configure jenkins -> manage jenkins -> Configure System -> Global Pipeline Libraries
  

## Terraform CI/CD

Handles Terraform init, fmt, validate, plan, approval, apply.

Usage
```groovy
@Library('pipeline-library-terraform@master') _

terraformpipeline("$TERRAFORM_DIRECTORY")
```


