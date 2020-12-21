package com.example.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Windows command utility
 */
public class CliUtils {

  private static final Logger LOGGER = LogManager.getLogger(CliUtils.class);

  /**
   * Runs windows command via java
   */
  public static void runCmd(String command) {
    LOGGER.info("Will run command {}", command);
    Runtime runtime = Runtime.getRuntime();
    try {
      final Process process = runtime.exec(new String[]{"cmd.exe", "/c", command});
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      LOGGER.debug("Here is the standard output of the command:\n");
      String string;
      while ((string = stdInput.readLine()) != null) {
        LOGGER.info(string);
      }
      // Read any errors from the attempted command
      LOGGER.debug("Here is the standard error of the command (if any):\n");
      while ((string = stdError.readLine()) != null) {
        LOGGER.info(string);
      }
    } catch (Exception e) {
      LOGGER.error(e);
    }
  }

}
