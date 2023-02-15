## Jenkins Shared Library.
- [vars/](https://github.com/Nitintothenew/jenkins-pipeline-libraries/tree/main/vars) - Groovy Shared Library reusable functions

## Prerequisite

- Terraform needs to be installed on the Jenkins servers (master and slave both)
- `jenkins_agent` Name of your agent where you want to run jenkins job
- `tf_dir` path of your terraform code
- `tf_workspace` Value of your terraform workspace if not mention any it will take default
- To configure jenkins -> manage jenkins -> Configure System -> Global Pipeline Libraries
  

## Terraform CI/CD

Handles Terraform init, fmt, validate, plan, approval, apply.

Usage
```groovy
@Library('pipeline-library-terraform@master') _

terraformpipeline([jenkins_agent: 'linux-slave-1' , tf_dir: '/home/ec2-user/jenkins/workspace/abc', tf_workspace: 'prod'])
```


