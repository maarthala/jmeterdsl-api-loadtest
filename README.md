# jmeterdsl-api-loadtest
Starter Kit for API load testing using JMeterDSL

## Setup

install maven https://maven.apache.org/install.html


## Application Under Test 

https://restful-api.dev/

Respect the server provider. Do not run too much load against the server. Use it for POC purpose only.


## How to run
Go to the repo root folder after installing maven and run below command

````
mvn clean test
`````


## Folder Structure

    Below is an example of project folder structure

 
    jmeterdsl-api-loadtest/
        ├── README.md
        ├── src/
        │   └── test/
        │       └── java/
        │            └── perftests/
        │                   └── config/
        │                   └── scenarios/
        │                   └── transactions/
        │                   └── utils/
        │           └── PerformanceTest.java
        ├── resources/
        │   │   └── payloads/
        │   └── entities.csv
        ├── start.sh
        └── results/


