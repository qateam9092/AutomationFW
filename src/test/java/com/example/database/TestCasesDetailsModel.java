package com.example.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class represents 'TBL_QA_TEST_CASES_DETAILS' table
 */
@Entity
@Table(name = "TBL_QA_TEST_CASES_DETAILS")
public class TestCasesDetailsModel {

  @Id
  @Column(name = "sr_no")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer srNo;

  @Column(name = "test_reference")
  private String testReference;

  @Column(name = "test_suite_name")
  private String testSuiteName;

  @Column(name = "test_sheet_name")
  private String sheetName;

  @Column(name = "test_method_name")
  private String testMethodName;

  @Column(name = "test_class_name")
  private String testClassName;

  @Column(name = "test_status")
  private String testStatus;

  @Column(name = "execution_date_time")
  private String executionDateTime;

  public Integer getSrNo() {
    return srNo;
  }

  public void setSrNo(Integer srNo) {
    this.srNo = srNo;
  }

  public String getTestReference() {
    return testReference;
  }

  public void setTestReference(String testReference) {
    this.testReference = testReference;
  }

  public String getTestSuiteName() {
    return testSuiteName;
  }

  public void setTestSuiteName(String testSuiteName) {
    this.testSuiteName = testSuiteName;
  }

  public String getSheetName() {
    return sheetName;
  }

  public void setSheetName(String sheetName) {
    this.sheetName = sheetName;
  }

  public String getTestMethodName() {
    return testMethodName;
  }

  public void setTestMethodName(String testMethodName) {
    this.testMethodName = testMethodName;
  }

  public String getTestClassName() {
    return testClassName;
  }

  public void setTestClassName(String testClassName) {
    this.testClassName = testClassName;
  }

  public String getTestStatus() {
    return testStatus;
  }

  public void setTestStatus(String testStatus) {
    this.testStatus = testStatus;
  }

  public String getExecutionDateTime() {
    return executionDateTime;
  }

  public void setExecutionDateTime(String executionDateTime) {
    this.executionDateTime = executionDateTime;
  }

  @Override
  public String toString() {
    return "TesCasesDetails{" +
        "srNo=" + srNo +
        ", testReference='" + testReference + '\'' +
        ", testSuitName='" + testSuiteName + '\'' +
        ", testMethodName='" + testMethodName + '\'' +
        ", testClassName='" + testClassName + '\'' +
        ", testStatus='" + testStatus + '\'' +
        ", executionDateTime='" + executionDateTime + '\'' +
        '}';
  }
}
