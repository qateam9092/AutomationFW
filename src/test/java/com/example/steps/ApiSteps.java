package com.example.steps;

import static io.restassured.RestAssured.given;
import static io.restassured.matcher.RestAssuredMatchers.matchesXsd;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import com.example.base.Base;
import com.example.base.Constant;
import com.example.excel.SchemaInfoModel;
import com.example.excel.SheetModel;
import com.example.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.Assert;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.placeholder.PlaceholderDifferenceEvaluator;

/**
 * Api Steps
 */
public class ApiSteps extends Base {

  private static final Logger LOGGER = LogManager.getLogger(ApiSteps.class);
  private static Response response;
  private static SchemaInfoModel schemaInfoModel;
  private static SheetModel sheetModel;

  /**
   * Sets Base URI
   */
  @Step("Set base uri")
  public void setBaseUri(String baseUri) {
    RestAssured.baseURI = baseUri;
    LOGGER.info("Base uri : {}", RestAssured.baseURI);
  }

  /**
   * Makes GET Request
   */
  @Step("Make GET Request")
  public Response makeGETRequest() throws IOException {
    Response response = given(getRequestSpecification(sheetModel)).log().all(true)
        .urlEncodingEnabled(false)
        .when()
        .get(getResolvedUrlSuffix(sheetModel))
        .then().log().all(true)
        .and().extract().response();
    LOGGER.debug("Response {} ", response.asString());
    return response;
  }

  /**
   * Make POST Request
   */
  @Step("Make POST Request")
  public Response makePOSTRequest() throws IOException {
    Response response = given(getRequestSpecification(sheetModel))
        .urlEncodingEnabled(false)
        .log().all(true)
        .post(getResolvedUrlSuffix(sheetModel))
        .then().log().all(true)
        .and().extract().response();
    LOGGER.debug("Response {} ", response.asString());
    return response;
  }

  /**
   * Make SOAP Request
   */
  @Step("Make SOAP Request")
  public Response makeSOAPRequest() throws IOException {
    if (StringUtils.isBlank(sheetModel.getRequestBody())) {
      saveTextLog("Soap request body validation", "Soap request body missing");
      Assert.fail("Soap request body missing");
    }
    Response response = given(getRequestSpecification(sheetModel))
        .log().all(true)
        .post(getResolvedUrlSuffix(sheetModel))
        .then().log().all(true)
        .and().extract().response();
    LOGGER.debug("Response {} ", response.asString());
    return response;
  }

  /**
   * Validates response HTTP code
   */
  @Step("Validate response http code")
  public void validateResponseCode(int expectedCode) {
    saveTextLog("Http code validation status", String.format("Expected : [%d] | Actual : [%d]", response.statusCode(), expectedCode));
    Assert.assertEquals(response.statusCode(), expectedCode, "HTTP status code doesn't match | ");
  }

  /**
   * Validates response body against the expected schema
   */
  @Step("Validate actual response body against expected schema")
  public void validateResponseAgainstSchema() throws IOException {
    if (StringUtils.isNotBlank(sheetModel.getTestSchemaInfo())) {
      File schemaFile = Paths.get(Constant.PARENT_DIR, "resources", schemaInfoModel.getSchemaFile()).toFile();
      if (!schemaFile.exists()) {
        schemaFile = new File(Paths.get(Constant.RESOURCES_DIR, schemaInfoModel.getSchemaFile()).toString());
        LOGGER.info("Using schema file in the jar");
      } else {
        LOGGER.info("Using schema file outside the jar");
      }
      String schemaString = FileUtils.readFileToString(schemaFile, StandardCharsets.UTF_8);
      saveTextLog("Expected response", sheetModel.getExpectedResponse(), false);
      saveTextLog("Actual response", sheetModel.getActualResponse(), false);
      saveTextLog("Expected Schema", schemaString, false);
      if (sheetModel.getTestApiType().equals("SOAP")) {
        response.then().assertThat().body(matchesXsd(schemaFile));
      } else {
        response.then().assertThat().body(matchesJsonSchema(schemaString));
      }
      saveTextLog("Schema validation status", "Schema validation successful");
    }
  }

  /**
   * Validates actual response body against expected response body
   */
  @Step("Validate actual response body against expected response body")
  public void validateActualResponseAgainstExpected() throws JSONException {
    saveTextLog("Expected response", sheetModel.getExpectedResponse(), false);
    saveTextLog("Actual response", sheetModel.getActualResponse(), false);
    if (sheetModel.getTestApiType().equals("SOAP")) {
      Diff diff = DiffBuilder
          .compare(sheetModel.getExpectedResponse())
          .withTest(response.asString())
          .ignoreWhitespace()
          .withDifferenceEvaluator(new PlaceholderDifferenceEvaluator())
          .build();
      List<?> allDifferences = (List<?>) diff.getDifferences();
      for (int i = 0; i < allDifferences.size(); i++) {
        saveTextLog("Difference-" + i, allDifferences.get(i).toString(), false);
      }
      Assert.assertEquals(allDifferences.size(), 0, "Actual response doesn't match with expected. | Differences");
    } else {
      saveTextLog("Ignored JSON fields", Arrays.toString(schemaInfoModel.getJsonIgnore()));
      if (schemaInfoModel.getJsonIgnore().length > 0) {
        TestUtils.jsonValidation(sheetModel.getExpectedResponse(), response.asString(), schemaInfoModel);
      } else {
        JSONAssert.assertEquals(sheetModel.getExpectedResponse(), response.asString(), true);
      }
    }
    saveTextLog("Response validation status", "Success. Actual response matches the expected");
  }

  /**
   * Builds & returns RequestSpecification
   */
  @SuppressWarnings("unchecked")
  public RequestSpecification getRequestSpecification(SheetModel sheetModel) throws IOException {
    RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
    requestSpecBuilder.addFilter(new AllureRestAssured());
    if (StringUtils.isNotBlank(sheetModel.getRequestHeaders())) {
      requestSpecBuilder.addHeaders(new ObjectMapper().readValue(sheetModel.getRequestHeaders(), Map.class));
    }
    if (StringUtils.isNotBlank(sheetModel.getRequestBody())) {
      requestSpecBuilder.setBody(sheetModel.getRequestBody());
    }
    if (StringUtils.isNotBlank(sheetModel.getRequestParameters())) {
      requestSpecBuilder.addParams(new ObjectMapper().readValue(sheetModel.getRequestParameters(), Map.class));
    }
    if (sheetModel.getTestApiType().equals("POST") && StringUtils.isNotBlank(sheetModel.getFormData())) {
      requestSpecBuilder.addMultiPart(getMultiPartSpecification(sheetModel));
    }
    return requestSpecBuilder.build();
  }

  private String getResolvedUrlSuffix(SheetModel excelTestSheetModel) {
    return StringUtils.isNotBlank(excelTestSheetModel.getTestUrl()) ? excelTestSheetModel.getTestUrl() : "";
  }

  /**
   * Builds & returns MultiPartSpecification
   */
  @SuppressWarnings("unchecked")
  private MultiPartSpecification getMultiPartSpecification(SheetModel sheetModel) throws IOException {
    Map<String, String> formDataMap = new ObjectMapper().readValue(sheetModel.getFormData(), Map.class);
    File fileToUpload = Paths.get(Constant.PARENT_DIR, "resources", "files", formDataMap.get("fileName")).toFile();
    if (fileToUpload.exists()) {
      LOGGER.info("Found file outside the jar : {}", fileToUpload.toString());
    } else {
      LOGGER.info("Looking for file to upload inside the jar");
      fileToUpload = Paths.get(Constant.FILES_DIR, formDataMap.get("fileName")).toFile();
    }
    MultiPartSpecBuilder multiPartSpecBuilder = new MultiPartSpecBuilder(fileToUpload);
    multiPartSpecBuilder.fileName(formDataMap.get("fileName"));
    multiPartSpecBuilder.mimeType(formDataMap.get("mimeType"));
    return multiPartSpecBuilder.build();
  }

  public void setResponse(Response response) {
    ApiSteps.response = response;
  }

  public void setSchemaInfoModel(SchemaInfoModel schemaInfoModel) {
    ApiSteps.schemaInfoModel = schemaInfoModel;
  }

  public void setSheetModel(SheetModel sheetModel) {
    ApiSteps.sheetModel = sheetModel;
  }
}
