### To run tests
- mvn clean verify
Profile : test

### To create jar
- mvn clean verify
Profile : jar

### Test controller guide lines
- Column header names must be static and changing them will break the project
- Adding a new column will have no effect until there is corresponding code change

### DB queries
- SELECT * FROM TBL_QA_TEST_CASES_DETAILS where TEST_REFERENCE = '20-09-2020-12-21-59'
- SELECT * FROM TBL_QA_FINAL_TEST_RESULT where TEST_REFERENCE = '20-09-2020-12-21-59'

### Json schema generator
- https://www.liquid-technologies.com/online-json-to-schema-converter

### XML schema generator
- https://www.liquid-technologies.com/online-xml-to-xsd-converter

### How set allure path
documents/How-to-set-allure-path.docx

### To ignore xml value form comparison
example : <logId>${xmlunit.ignore}</logId>

### To ignore json value from comparison
example : 

{
 "schemaFile" : "/schema/user/user-schema.json",
 "validateOnlySchema" : true,
 "validateFullResponse" : true,
 "jsonIgnore":["logId", "user.lastLoggedIn"]
}

### How to write a jsonPath expression
- https://github.com/json-path/JsonPath