package com.example.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class represents 'TBL_QA_FINAL_TEST_RESULT' table
 */
@Entity
@Table(name = "TBL_QA_FINAL_TEST_RESULT")
public class FinalTestResultModel {

  @Id
  @Column(name = "test_reference")
  private String testReference;

  @Column(name = "test_suite_name")
  private String testSuitName;

  @Column(name = "final_result")
  private String finalResult;

  @Column(name = "execution_date_time")
  private String executionDateTime;

  public String getTestReference() {
    return testReference;
  }

  public void setTestReference(String testReference) {
    this.testReference = testReference;
  }

  public String getTestSuitName() {
    return testSuitName;
  }

  public void setTestSuitName(String testSuitName) {
    this.testSuitName = testSuitName;
  }

  public String getFinalResult() {
    return finalResult;
  }

  public void setFinalResult(String finalResult) {
    this.finalResult = finalResult;
  }

  public String getExecutionDateTime() {
    return executionDateTime;
  }

  public void setExecutionDateTime(String executionDateTime) {
    this.executionDateTime = executionDateTime;
  }
}
