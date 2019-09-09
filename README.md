# Ryanir challenge interconnection flights

## Tecnologies
* Java 8
* SpringBoot 2.1.7.RELEASE

## Run project
To run the the project just run the following command:
```bash
$ ./mvnw spring-boot:run
```

## Tests
To run the tests run the following command:
```bash
$ ./mvnw test
```
## Endpoints
There is only one end point in all the project

http://localhost:8080/flights/interconnections?departure={departure}&arrival={arrival}&departureDateTime={departureDateTime}&arrivalDateTime={arrivalDateTime}

* departure - a departure airport IATA code
* departureDateTime - a departure datetime in the departure airport timezone in
* ISO format
* arrival - an arrival airport IATA code
* arrivalDateTime - an arrival datetime in the arrival airport timezone in ISO format
