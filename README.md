# HelloFresh Automation Challenge

Extensible Web and API automation framework with improved web test cases and implementation of API test cases.

## Overview

Hybrid framework which is

* Extensible
* Maintainable
* Reusable
* Debuggable

## How to run

* Download or Clone the project
* Navigate to home directory

### Run Web test cases using following command

```shell
mvn  test -Dsurefire.suiteXmlFiles=web-testng.xml
```
or

```shell
mvn test -Dis_parallel="true" -Dbrowser="chrome" -Denvironment="prod" -Dplatform="mac" -Dsurefire.suiteXmlFiles=web-testng.xml
```

### Run API test cases using following command

```shell
mvn  test -Dsurefire.suiteXmlFiles=api-testng.xml
```
or

```shell
mvn test -Dis_parallel="false" -Denvironment="prod" -Dplatform="mac" -Dsurefire.suiteXmlFiles=api-testng.xml
```


This project makes it easy to configure and run tests using the following command line parameters:
* **`-Dis_parallel`** - Runs tests in parallel mode (Possible Values: `true`, `false`, Default Value: `true`)
* **`-Dbrowser`** - Possible Values: `chrome`, `firefox`, Default Value: `chrome` (Can be extended to `edge`)
* **`-Denvironment`** - Possible Values: `prod`,Default Value: `prod` (Can be extended to `qa` and `dev`)
* **`-Dplatform`** - Possible Values: `mac`,Default Value: `mac` (Can be extended to `windows` and `linux`)

## How to open the report file

Open `src/test/resources/testReports.html` in any browser

## Features

***Reporting***
* Real time, easy to understand reports
* Screenshots for failed screens

***Test Data Generator***
* Generates random string, integer for insignificant test data

***Logger***
* Custom readable Logger that extends testNG’s Reporter class 

***Error Handler***
* Generic and Maintainable error handler.

***Test Data File***
* Runs a test for multiple inputs using testNG’s data provider
* Maintainable test data file separated from test scripts

***Action Libraries***
* Wrapper over Selenium’s action methods: uses fluent wait, takes care of error handling, involves retries (in certain cases) 

***WebDriver Factory***
* Driver Factory returns driver based on the browser defined by the user.

## WIP
Notification Service - Email automation test report to users.
Capture error logs from browser console


