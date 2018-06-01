# CycleGAN Manager

http://gradman.herokuapp.com/

This project will be a web portal that will utilize:

- [Paperspace API](https://www.paperspace.com/api) (and maybe AWS, eventually) to access GPU powered machines
- Groovy/Spring to create a web api for managing jobs runs, machines, etc
- MongoDB for persistence of jobs, job status, users, machines, etc
- Vue.js for the single-page-app frontend

## Concept:

Through the web portal you will be able to design job templates, queue jobs and look at results of past or running jobs. These results could include images generated from CycleGAN, your provided training data, graphs of things like loss, learning rate, and other things you may need. 

You will also be able to adjust hyperparameters between jobs and compare the results of different parameter adjustments.


## Run Locally

```bash
./build_frontend.sh # or './build_frontend.sh production' to disable Vue's dev mode
cp -r frontend/public src/main/resources/static
gradle bootRun # or run with your IDE of choice
```
