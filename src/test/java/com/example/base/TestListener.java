package com.example.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Listener class to log current test details
 */
public class TestListener implements ITestListener {

  private static final Logger LOGGER = LogManager.getLogger(TestListener.class);

  @Override
  public void onTestStart(ITestResult result) {
    LOGGER.info("Executing test method : [{}] in class [{}]", result.getMethod().getMethodName(), result.getTestClass().getName());
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    LOGGER.info("Passed test method : [{}] in class [{}]", result.getMethod().getMethodName(), result.getTestClass().getName());
  }

  @Override
  public void onTestFailure(ITestResult result) {
    LOGGER.info("Failed test method : [{}] in class [{}]", result.getMethod().getMethodName(), result.getTestClass().getName());
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    LOGGER.info("Skipped test method : [{}] in class [{}]", result.getMethod().getMethodName(), result.getTestClass().getName());
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
  }

  @Override
  public void onStart(ITestContext context) {
  }

  @Override
  public void onFinish(ITestContext context) {
  }
}
