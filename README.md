# Instrument Price

This project aims to provide a referente about how me (Romero) as an architect would solve a time series data processing. 

## Based sprint-boot 1.5.x - Instrument Price:
 
#### The application shows:
* The use of spring boot
* The use of spring data
* the use of spring integration
* Data ETL in a moderate scenario
* JUnit test cases
* Embedded launch of Spring-Boot 

#### TODO:
* Separate the Producer from the Consumer and add a Broker (such as RabbitMQ or Kafka) to improve scalability
* Add event driven sysout to avoid repeated prints on the log
* Docker support

## Getting Started

These instructions will teach you how to launch this application and simulate a instrument price processing.

### Prerequisites

Make sure you have both Maven and Java 1.8+ installed in your environment.

```
sudo apt install maven
```
Download the last version of the Java jdk's **.tar.gz** and follow the following instructions:
```
sudo tar vzxf /download/dir/jdk-package-name.tar.gz --directory=/usr/local/java/
sudo ln -s /usr/local/java/jdk-version /usr/local/java/current
export JAVA_HOME=/usr/local/java/current
export PATH=$PATH:$JAVA_HOME/bin
```

### Installing


Building only the jar/zip artifacts:

```
mvn clean install
```

Running the standalone:

```
#we're skipping the tests since you've already cleaned and installed the application.
mvn spring-boot:run -DskipTests
```
Alternatively you can add -Dspring.profiles.active=profileSuffix to determine a specific configuration of database, for example.
Alternatively you can add -Dfactor.influence.values=INSTRUMENTNAME1=40.0,INSTRUMENTNAME2=1.0 where INSTRUMENTNAME is the instrument name and its factor separated by comma, so you can inject the entries into the database on the startup.
Alternatively you can add -Dtsrd.input.path=/path/name to set the path where the applicationwill find your batch files (such as example_input.txt)

Once the application is running you can test it by doing:
```
1 - Add a file to the path that you set by using -Dtsrd.input.path=/path/name (if you don't set any it will be /opt/tsrd/inputs)
2 - Wait till the processing finishes (the file will be moved to the processed folder int he same /path/name tha you set up) and then check the results on the output log.
```

## Running the tests

By cleaning and installing you're already doing the tests, unless you use -DskipTests to skip the tests. Anyway, you still can run unit and integration tests separately.

### Unit tests

The unit tests will be ran without the Cucumber Tests by running:

```
mvn clean test
```
### Integration tests

The integration tests will be ran within the unit tests by running:

```
# this will clean test (unit) package and fire up Cucumber Tests.
mvn clean verify
```

### Why did I choose this solution?
By using spring-integration, the separation of the producer/consumer can be made very easy and introduce a broker to distribute the process and make scalability efficient.
Notice that even though we're talking about time series data processing, the thing can still be made parallel due to the many algorithims that can process separately despite the sequential nature of a time series data set.

## Deployment

This application is not intended to be deployed as an API, it's just a reference on what Romero can show up as a blueprint implementation of an ETL (despite we're not loading the data in any part apart from printing results on the terminal).

## Built With

* [Java 1.8+](http://docs.oracle.com/javase/8/docs/) - Java Platform Standard Edition 8 Documentation
* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring boot](https://docs.spring.io/spring-boot/docs/1.5.x/reference/htmlsingle/) - Spring Boot Reference Guide

## Versioning

Since this is just a testing application, we're not doing any management on the versioning.

## Authors

* **Rômero Ricardo de Sousa Pereira** - *Java Architect (Candidate)* - [JAVA PI LTDA]

## License

Check the appliable license.

## Acknowledgments

* This sentence is false.
