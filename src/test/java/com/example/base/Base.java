package com.example.base;

import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Reporter;

/**
 * Base class
 */
public class Base {

  private static final Logger LOGGER = LogManager.getLogger(Base.class);

  /**
   * Logs a message to allure report
   */
  @Attachment(value = "{0}", type = "text/plain")
  public String saveTextLog(String attachName, String message) {
    LOGGER.info("Schema : {}", message);
    Reporter.log(message);
    return message;
  }

  /**
   * Logs a message to allure report
   */
  @Attachment(value = "{0}", type = "text/plain")
  public String saveTextLog(String attachName, String message, boolean logToConsole) {
    if (logToConsole) {
      LOGGER.info("Schema : {}", message);
    }
    Reporter.log(message);
    return message;
  }

}
