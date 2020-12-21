package com.example.base;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * The Constants
 */
public class Constant {

  public static final String CONTROLLER_SHEET_PATH = Paths.get("Controller.xlsx").toString();
  public static final String PARENT_DIR = System.getProperty("user.dir");
  public static final String FILES_DIR = Paths.get(PARENT_DIR, "files").toString();
  public static final String RESOURCES_DIR = Paths.get(PARENT_DIR, "src", "test", "resources").toString();
  public static final String TARGET_DIR = Paths.get(PARENT_DIR, "target").toString();
  public static final String SUITE_SHEET_NAME = "TestSuite";
  public static final File HIBERNATE_CONFIG_FILE = Paths.get(Constant.RESOURCES_DIR, "hibernate.cfg.xml").toFile();
  public static final String UNIQUE_TEST_REFERENCE = UUID.randomUUID().toString();
  public static boolean isSheetFound = true;
  public static boolean isAtLeastOneTestToRun = true;

}
