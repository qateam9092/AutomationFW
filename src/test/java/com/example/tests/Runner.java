package com.example.tests;

import com.example.allure.AllureUtils;
import com.example.base.Base;
import com.example.base.Constant;
import com.example.base.DataSource;
import com.example.database.DBUtil;
import com.example.database.FinalTestResultDAO;
import com.example.database.FinalTestResultModel;
import com.example.database.TestCaseDetailsDAO;
import com.example.database.TestCasesDetailsModel;
import com.example.excel.SchemaInfoModel;
import com.example.excel.SheetModel;
import com.example.steps.ApiSteps;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import io.restassured.response.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.internal.BaseTestMethod;

/**
 * Single Runner Class
 */
public class Runner extends Base {

  private static final Logger LOGGER = LogManager.getLogger(Runner.class);
  private static final List<TestCasesDetailsModel> TEST_CASES_DETAILS_TABLES_LIST = new ArrayList<>();

  /**
   * Before the test suite runs
   */
  @BeforeSuite(description = "Before Suite", alwaysRun = true)
  public void beforeSuite() {
    LOGGER.info("Start of test suite execution");
  }

  /**
   * Set Test name
   */
  @BeforeMethod(description = "Set test case name", alwaysRun = true)
  public void beforeMethod(Method method, ITestResult result, Object[] data) {
    SheetModel sheetModel = (SheetModel) data[0];
    try {
      BaseTestMethod baseTestMethod = (BaseTestMethod) result.getMethod();
      Field methodName = baseTestMethod.getClass().getSuperclass().getDeclaredField("m_methodName");
      methodName.setAccessible(true);
      methodName.set(baseTestMethod, sheetModel.getTestCaseName());
      AllureUtils.overrideAllureFields(method, sheetModel);
    } catch (Exception e) {
      LOGGER.error(e);
    }
  }

  /**
   * Single testNG Test to run all tests via excel Controller file
   */
  @Description()
  @Owner(value = "")
  @Feature(value = "")
  @TmsLink(value = "")
  @Test(dataProvider = "excel-data-provider", dataProviderClass = DataSource.class, alwaysRun = true)
  @Severity(value = SeverityLevel.NORMAL)
  public void test(SheetModel sheetModel, ITestContext context) {
    LOGGER.debug(sheetModel);
    ApiSteps apiSteps = new ApiSteps();
    apiSteps.setSheetModel(sheetModel);
    Response response = null;
    // Set base uri
    apiSteps.setBaseUri(sheetModel.getBaseUrl());
    try {
      if (sheetModel.getTestApiType().equalsIgnoreCase("GET")) {
        // Make GET request
        response = apiSteps.makeGETRequest();
      } else if (sheetModel.getTestApiType().equalsIgnoreCase("POST")) {
        // Make POST request
        response = apiSteps.makePOSTRequest();
      } else if (sheetModel.getTestApiType().equalsIgnoreCase("SOAP")) {
        // Make SOAP request
        response = apiSteps.makeSOAPRequest();
      }
      sheetModel.setActualStatusCode(response.statusCode());
      sheetModel.setActualResponse(response.asString());
      apiSteps.setResponse(response);
      context.setAttribute("excelTestSheetModel", sheetModel);

      // Validate response status code
      apiSteps.validateResponseCode(sheetModel.getExpectedStatusCode());

      // Validate response against schema as well as expected response
      if (StringUtils.isNotBlank(sheetModel.getTestSchemaInfo())) {
        SchemaInfoModel schemaInfoModel = new ObjectMapper().readValue(sheetModel.getTestSchemaInfo(), SchemaInfoModel.class);
        apiSteps.setSchemaInfoModel(schemaInfoModel);
        if (schemaInfoModel.getValidateOnlySchema()) {
          apiSteps.validateResponseAgainstSchema();
        }
        if (schemaInfoModel.getValidateFullResponse()) {
          apiSteps.validateActualResponseAgainstExpected();
        }
      }
    } catch (Exception e) {
      LOGGER.debug(e);
      throw new RuntimeException(e);
    } finally {
      if (response == null) {
        LOGGER.error("Response is null. Will skip further steps");
        sheetModel.setActualStatusCode(-1);
        sheetModel.setActualResponse("-1");
        context.setAttribute("excelTestSheetModel", sheetModel);
      }
    }
  }

  /**
   * Tear down to write test result back to Controller file
   */
  @AfterMethod(description = "Write test result to excel")
  public void writeResultToExcel(Method method, ITestContext context, ITestResult result) throws IOException {
    if (Constant.isSheetFound) {

      // Read Controller sheet to write test results
      SheetModel sheetModel = (SheetModel) context.getAttribute("excelTestSheetModel");

      LOGGER.info("Trying to write test result back to Controller sheet for test case [{}] from sheet [{}]",
          sheetModel.getTestCaseName(), sheetModel.getSheetName());

      FileInputStream fileInputStream = new FileInputStream(new File(Constant.CONTROLLER_SHEET_PATH));

      // Workbook
      Workbook workbook = new XSSFWorkbook(fileInputStream);

      // Sheet
      Sheet sheet = workbook.getSheet(sheetModel.getSheetName());

      // Populate headers map
      Map<String, Integer> headersMap = new LinkedHashMap<>();
      for (int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++) {
        headersMap.put(sheet.getRow(0).getCell(i).getStringCellValue(), i);
      }

      // Add actual status code, response & test status to sheet model
      for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
        if (sheet.getRow(i).getCell(headersMap.get("Sr No")).getNumericCellValue() == sheetModel
            .getSrNo()) {
          LOGGER.debug("Test id : {}", i);
          sheet.getRow(i).getCell(headersMap.get("Actual Status Code")).setCellValue(sheetModel.getActualStatusCode());
          sheet.getRow(i).getCell(headersMap.get("Actual Response")).setCellValue(sheetModel.getActualResponse());
          sheet.getRow(i).getCell(headersMap.get("Test Status")).setCellValue(result.isSuccess() ? "Passed" : "Failed");
        }
      }
      fileInputStream.close();

      // Writes test result back to Controller sheet
      FileOutputStream fileOutputStream = new FileOutputStream(new File(Constant.CONTROLLER_SHEET_PATH));
      workbook.write(fileOutputStream);
      fileOutputStream.close();
      LOGGER.info("Successfully wrote back test result in Controller sheet for test id : [{}] | sheet : [{}]", sheetModel.getSrNo(),
          sheetModel.getSheetName());

      // Populate test case details model
      try {
        LOGGER.debug("Trying to populate test case details model for {}", sheetModel.getTestCaseName());
        TestCasesDetailsModel testCasesDetailsModel = new TestCasesDetailsModel();
        testCasesDetailsModel.setTestReference(Constant.UNIQUE_TEST_REFERENCE);
        testCasesDetailsModel.setTestSuiteName(context.getSuite().getName());
        testCasesDetailsModel.setSheetName(sheetModel.getSheetName());
        testCasesDetailsModel.setTestClassName(getClass().getName());
        testCasesDetailsModel.setTestMethodName(sheetModel.getTestCaseName());
        testCasesDetailsModel.setTestStatus(result.isSuccess() ? "Passed" : "Failed");
        testCasesDetailsModel.setExecutionDateTime(ZonedDateTime.now(ZoneId.systemDefault()).toString());
        LOGGER.debug("Successfully populated test case details model {}", testCasesDetailsModel);

        // Save each test case details model to a list. This will then be written to db in after test hook
        TEST_CASES_DETAILS_TABLES_LIST.add(testCasesDetailsModel);
      } catch (Exception e) {
        LOGGER.error(e);
      }
    } else {
      LOGGER.warn("Sheet not found. Will skip writing result to excel");
      throw new SkipException("Sheet not found. Will skip writing result to excel");
    }
  }

  /**
   * Writes test case details & final test results to DB
   */
  @AfterTest(description = "Write test details & final result to DB", alwaysRun = true)
  public void afterTest(ITestContext context) {
    if (Constant.isSheetFound && Constant.isAtLeastOneTestToRun) {

      // Get hibernate config file
      Configuration configuration;
      try {
        configuration = DBUtil.getHibernateConfiguration();
      } catch (HibernateException e) {
        LOGGER.error(e);
        throw new HibernateException(e.getMessage());
      }

      // Get session factory
      SessionFactory sessionFactory = DBUtil.getSessionFactory(configuration);

      if (sessionFactory == null) {
        throw new RuntimeException("Session factory is null. Couldn't connect to DB");
      } else {

        // Test case details data access object
        TestCaseDetailsDAO testCaseDetailsDAO = new TestCaseDetailsDAO(sessionFactory);

        // write test case details saved in the list to db
        testCaseDetailsDAO.save(TEST_CASES_DETAILS_TABLES_LIST);

        // Populate final test result model
        FinalTestResultModel finalTestResultModel = new FinalTestResultModel();
        finalTestResultModel.setTestReference(Constant.UNIQUE_TEST_REFERENCE);
        finalTestResultModel.setTestSuitName(context.getSuite().getName());
        finalTestResultModel.setFinalResult(context.getFailedTests().size() > 0 ? "Failed" : "Passed");
        finalTestResultModel.setExecutionDateTime(ZonedDateTime.now(ZoneId.systemDefault()).toString());

        // Write final test result to db
        FinalTestResultDAO finalTestResultDAO = new FinalTestResultDAO(sessionFactory);
        finalTestResultDAO.save(finalTestResultModel);

        LOGGER.info("Final result | Failed test : [{}]", context.getFailedTests().size());
        LOGGER.info(
            "SQL query to get test details for current run : SELECT * FROM TBL_QA_TEST_CASES_DETAILS where TEST_REFERENCE = '{}'",
            Constant.UNIQUE_TEST_REFERENCE);
        LOGGER.info(
            "SQL query to get final test result for current run : SELECT * FROM TBL_QA_FINAL_TEST_RESULT where TEST_REFERENCE = '{}'",
            Constant.UNIQUE_TEST_REFERENCE);
      }
    } else {
      if (!Constant.isAtLeastOneTestToRun) {
        LOGGER.warn("No row found across any sheet with run mode = TRUE. Skipping writing to DB");
      } else {
        LOGGER.warn("Sheet not found. Will skip writing result to db");
      }
      throw new SkipException("Sheet not found. Will skip writing result to db");
    }
  }

  /**
   * Tear down. Closes db session
   */
  @AfterSuite(description = "Close DB connection", alwaysRun = true)
  public void tearDown() {
    DBUtil.closeSessionFactory();
    LOGGER.info("End of test suite execution");
  }
}
