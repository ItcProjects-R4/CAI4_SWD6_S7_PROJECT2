# CAI4_SWD6_S7_PROJECT2

Software testing project combining manual testing, API testing, and Selenium test automation. Built against the [practice.expandtesting.com](https://practice.expandtesting.com) demo site.

## Repo Structure

```
├── Automation Final Project/     Selenium + TestNG automation suite
│   ├── pom.xml
│   ├── testng.xml
│   └── src/test/java/
│       ├── listeners/ExtentListener.java
│       └── tests/
│           ├── LoginTest.java
│           ├── UserRegistrationTest.java
│           ├── ForgotPasswordTest.java
│           ├── ContactUsTest.java
│           ├── DragAndDropTest.java
│           ├── FormValidationTest.java
│           ├── WebInputsTest.java
│           └── SecurePasswordCheckerTest.java
├── APITesting/                   Postman collection for API testing
│   ├── Notes API.postman_collection.json
│   └── local.postman_environment.json
├── Testing Final Project.xlsx    Manual test cases / test plan
├── Software_Testing_Project.pptx Project presentation
└── Testing Video and Excel sheet Demo recording and results
```

## Automation Suite

**Stack:** Java 21, Selenium 4.43, TestNG 7.10, WebDriverManager, ExtentReports, Maven, Edge browser.

**Test coverage:**
- `LoginTest` - login with valid/invalid credentials
- `UserRegistrationTest` - registration flow, success and failure paths
- `ForgotPasswordTest` - password reset flow
- `ContactUsTest` - contact form submission
- `DragAndDropTest` - drag and drop interaction
- `FormValidationTest` - form field validation rules
- `WebInputsTest` - input field handling
- `SecurePasswordCheckerTest` - password strength checker

Each test class targets a dedicated page on practice.expandtesting.com (`/login`, `/register`, `/forgot-password`, `/contact`, `/drag-and-drop`, `/form-validation`, `/inputs`, `/secure-password-checker`).

`ExtentListener` hooks into TestNG to generate HTML test reports after each run.

### Running the tests

Requires Java 21 and Maven. Microsoft Edge must be installed (driver is managed automatically by WebDriverManager).

```bash
cd "Automation Final Project"
mvn test
```

This runs all suites defined in `testng.xml`. Extent reports are generated after execution.

## API Testing

Postman collection covering the Notes API, with a matching local environment file. Import both into Postman to run the requests.

## Manual Testing

`Testing Final Project.xlsx` contains the manual test cases and results. The video/excel folder holds a recorded test run alongside the results sheet.
