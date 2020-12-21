package com.example.excel;

/**
 * The sheet model
 */
public class SheetModel {

  private Integer srNo;

  private String testId;

  private Boolean testRunMode;

  private String testSeverity;

  private String testCaseName;

  private String testDescription;

  private String testApiType;

  private String testUrl;

  private String requestBody;

  private String requestHeaders;

  private String requestParameters;

  private String formData;

  private String testSchemaInfo;

  private Integer expectedStatusCode;

  private String expectedResponse;

  private Integer actualStatusCode;

  private String actualResponse;

  private String baseUrl;

  private String sheetName;

  private String testStatus;

  private String owner;

  public Integer getSrNo() {
    return srNo;
  }

  public void setSrNo(Integer srNo) {
    this.srNo = srNo;
  }

  public String getTestId() {
    return testId;
  }

  public void setTestId(String testId) {
    this.testId = testId;
  }

  public Boolean getTestRunMode() {
    return testRunMode;
  }

  public void setTestRunMode(Boolean testRunMode) {
    this.testRunMode = testRunMode;
  }

  public String getTestSeverity() {
    return testSeverity;
  }

  public void setTestSeverity(String testSeverity) {
    this.testSeverity = testSeverity;
  }

  public String getTestCaseName() {
    return testCaseName;
  }

  public void setTestCaseName(String testCaseName) {
    this.testCaseName = testCaseName;
  }

  public String getTestDescription() {
    return testDescription;
  }

  public void setTestDescription(String testDescription) {
    this.testDescription = testDescription;
  }

  public String getTestApiType() {
    return testApiType;
  }

  public void setTestApiType(String testApiType) {
    this.testApiType = testApiType;
  }

  public String getTestUrl() {
    return testUrl;
  }

  public void setTestUrl(String testUrl) {
    this.testUrl = testUrl;
  }

  public String getRequestBody() {
    return requestBody;
  }

  public void setRequestBody(String requestBody) {
    this.requestBody = requestBody;
  }

  public String getRequestHeaders() {
    return requestHeaders;
  }

  public void setRequestHeaders(String requestHeaders) {
    this.requestHeaders = requestHeaders;
  }

  public String getRequestParameters() {
    return requestParameters;
  }

  public void setRequestParameters(String requestParameters) {
    this.requestParameters = requestParameters;
  }

  public String getFormData() {
    return formData;
  }

  public void setFormData(String formData) {
    this.formData = formData;
  }

  public String getTestSchemaInfo() {
    return testSchemaInfo;
  }

  public void setTestSchemaInfo(String testSchemaInfo) {
    this.testSchemaInfo = testSchemaInfo;
  }

  public Integer getExpectedStatusCode() {
    return expectedStatusCode;
  }

  public void setExpectedStatusCode(Integer expectedStatusCode) {
    this.expectedStatusCode = expectedStatusCode;
  }

  public String getExpectedResponse() {
    return expectedResponse;
  }

  public void setExpectedResponse(String expectedResponse) {
    this.expectedResponse = expectedResponse;
  }

  public Integer getActualStatusCode() {
    return actualStatusCode;
  }

  public void setActualStatusCode(Integer actualStatusCode) {
    this.actualStatusCode = actualStatusCode;
  }

  public String getActualResponse() {
    return actualResponse;
  }

  public void setActualResponse(String actualResponse) {
    this.actualResponse = actualResponse;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getSheetName() {
    return sheetName;
  }

  public void setSheetName(String sheetName) {
    this.sheetName = sheetName;
  }

  public String getTestStatus() {
    return testStatus;
  }

  public void setTestStatus(String testStatus) {
    this.testStatus = testStatus;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  @Override
  public String toString() {
    return "[TC No=" + srNo + ", Sheet=" + sheetName + "]";
  }
}
