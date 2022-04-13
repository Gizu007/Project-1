# Project-1

## Project Description

The Expense Reimbursement System (ERS) will allow  all employees in the company to login and submit requests for reimbursement and view their past tickets and pending requests.Also, finance managers will be able to log in and view all reimbursement requests. Finance managers are authorized to approve and deny requests for expense reimbursement.

## Technologies Used

* Java
* Javalin
* JDBC
* HTML
* CSS
* JavaScript
* Postgresql
* JUnit
* Mockito
* GCP Cloud SQL
* GCP Cloud Storage
* Firebase

## Features

List of features:
* Employee Features:
  * Login and view current status of reimbursements.
  * Submit a new reimbursement request.

* Manager Features:
  * Login with employee functionality.
  * Navigate to Manager Page.
  * View All Reimbursements.
  * Approve/Deny any Pending Reimbursement.

## Getting Started
- Initial Setup:
```
git clone https://github.com/java-gcp-220228/CaitlinTalerico/new/main/project-1-reimbursement-system
gradle build
```
- Required Environmental Variables:
  - Database:
    - Username, Password, URL
  - GCP Cloud Storage
    - Google Generate Service Key


## Usage

* Build the project
* Create a database based Script-Tables.sql and Script-commands.sql in backend folder
* Run live server or use postman 
* Funtions include login in as finance manager or employee with respective permissions established on features
