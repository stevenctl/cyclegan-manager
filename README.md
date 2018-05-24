# CycleGAN Manager

This project will be a web portal that will utilize:

- Ansible to deploy code to machines 
- Groovy/Spring to create a web api for managing jobs runs, machines, etc
- MongoDB for persistence of jobs, job status, users, machines, etc
- Vue.js for the single-page-app frontend

## Concept:

With this portal you will be able to register machines by providing an IP address to the machine. You will copy a provided ssh key to the machine that will allow the automation to take control of the machine, get information about available resources, install the agent and finally queue jobs to that machine. 

Through the web portal you will be able to design job templates, queue jobs and look at results of past or running jobs. These results could include images generated from CycleGAN, your provided training data, graphs of things like loss, learning rate, and other things you may need. 

You will also be able to adjust hyperparameters between jobs and compare the results of different parameter adjustments.


## Run Locally

Manually:
```bash
./build_frontend.sh # or './build_frontend.sh production' to disable Vue's dev mode
cp -r frontend/public src/main/resources/static
gradle bootRun # or run with your IDE of choice
```

With Gradle: 
```bash
gradle buildVueApp copyVueApp bootRun # or ./gradlew to use the wrapper
```