package com.example.util;

import com.example.base.Constant;
import com.example.excel.SchemaInfoModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;

/**
 * Common test utils class
 */
public class TestUtils {

  private static final Logger LOGGER = LogManager.getLogger(TestUtils.class);

  private TestUtils() {
  }

  /**
   * Reads file from 'src/test/resources folder' and converts to String
   */
  public static String getBodyFromAFile(String filePath) {
    try {
      return IOUtils.toString(TestUtils.class.getResourceAsStream(filePath), StandardCharsets.UTF_8);
    } catch (NullPointerException e) {
      throw new RuntimeException("Can not find file : " + filePath);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Generates unique string using current date & time stamp.
   */
  public static String getUniqueString() {
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
    Date date = new Date();
    return dateFormat.format(date);
  }

  /**
   * Copy any directory from source to destination
   */
  public static void copyDirectory(String sourceDirectory, String destinationDirectory) {
    File destinationFile = new File(destinationDirectory);
    try {
      File sourceFile = new File(Constant.PARENT_DIR + sourceDirectory);
      if (!sourceFile.exists()) {
        destinationFile.mkdirs();
      }
      FileUtils.copyDirectory(sourceFile, destinationFile);
      LOGGER.info("Copied directory [{}] to [{}]", sourceDirectory, destinationDirectory);
    } catch (FileNotFoundException e) {
      LOGGER.error(e);
    } catch (Exception e) {
      LOGGER.warn("Exception while copying results : ", e);
    }
  }

  /**
   * Copy any file from source to destination directory
   */
  public static void copyAllFilesByExtension(String sourceFile, String destinationFile, String fileExtension) {
    try {
      String[] fileNames = (new File(sourceFile)).list();
      assert fileNames != null;
      for (String fileName : fileNames) {
        if (fileName.contains(fileExtension)) {
          String newSourceFile = Paths.get(sourceFile, fileName).toString();
          String newDestFile = Paths.get(destinationFile, fileName).toString();
          FileUtils.copyFile(new File(newSourceFile), new File(newDestFile));
          LOGGER.info("Copied file [{}] to [{}]", newSourceFile, newDestFile);
        }
      }
    } catch (Exception e) {
      LOGGER.error(e);
    }
  }

  public static void copyFile(String sourceFile, String destinationFile) {
    try {
      FileUtils.copyFile(new File(sourceFile), new File(destinationFile));
      LOGGER.info("Copied file [{}] to [{}]", sourceFile, destinationFile);
    } catch (IOException e) {
      LOGGER.error(e);
    }
  }

  public static void jsonValidation(String expectedResponse, String actualResponse, SchemaInfoModel schemaInfoModel)
      throws JSONException {
    Customization[] jsonIgnores = Arrays.stream(schemaInfoModel.getJsonIgnore())
        .map(s -> new Customization(s, (o1, o2) -> true))
        .toArray(Customization[]::new);
    JSONAssert.assertEquals(expectedResponse, actualResponse, new CustomComparator(JSONCompareMode.LENIENT, jsonIgnores));
  }
}
