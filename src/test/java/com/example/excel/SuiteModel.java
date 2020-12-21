package com.example.excel;

/**
 * The suite model
 */
public class SuiteModel {

  private String testSheetName;

  private Boolean testRunMode;

  private String baseUrl;

  private String owner;

  public SuiteModel() {
  }

  public SuiteModel(String testSheetName, Boolean testRunMode, String baseUrl, String owner) {
    this.testSheetName = testSheetName;
    this.testRunMode = testRunMode;
    this.baseUrl = baseUrl;
    this.owner = owner;
  }

  public String getTestSheetName() {
    return testSheetName;
  }

  public void setTestSheetName(String testSheetName) {
    this.testSheetName = testSheetName;
  }

  public Boolean getTestRunMode() {
    return testRunMode;
  }

  public void setTestRunMode(Boolean testRunMode) {
    this.testRunMode = testRunMode;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  @Override
  public String toString() {
    return "SuiteModel{" +
        "testSheetName='" + testSheetName + '\'' +
        ", testRunMode=" + testRunMode +
        ", baseUrl='" + baseUrl + '\'' +
        ", owner='" + owner + '\'' +
        '}';
  }
}
