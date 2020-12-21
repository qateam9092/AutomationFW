package com.example.tests;

import com.example.util.CliUtils;
import com.example.util.TestUtils;
import java.io.File;
import java.util.Collections;
import org.apache.commons.io.FileUtils;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

/**
 * The main class which serves as entry point for execution via jar
 */
public class Main {

  private static final String UNIQUE_FOLDER = TestUtils.getUniqueString();

  public static void main(String[] args) {
    try {
      FileUtils.cleanDirectory(new File("./target"));
    } catch (Exception e) {
      if (!e.getMessage().contains("Unable to delete file: .\\target\\Log.log")) {
        throw new RuntimeException(
            "Failed to clean target directory. It appears that something is open from target. "
                + "Alternately you may manually delete target directory");
      }
    }

    XmlSuite xmlSuite = new XmlSuite();
    xmlSuite.setName("API Test Suite");

    XmlClass xmlClass = new XmlClass();
    xmlClass.setClass(com.example.tests.Runner.class);

    XmlTest xmlTest = new XmlTest(xmlSuite);
    xmlTest.setName("API Tests");
    xmlTest.setXmlClasses(Collections.singletonList(xmlClass));

    TestNG testNG = new TestNG();
    testNG.setXmlSuites(Collections.singletonList(xmlSuite));
    testNG.setOutputDirectory("./target/test-output/");
    testNG.run();

    TestUtils.copyFile("./resources/categories.json", "./target/allure-results/categories.json");
    TestUtils.copyFile("./resources/allure.properties", "./target/allure-results/allure.properties");
    TestUtils.copyFile("./resources/environment.properties", "./target/allure-results/environment.properties");
    TestUtils.copyDirectory("./back-up/LATEST/allure-report/history", "./target/allure-results/history");

    CliUtils.runCmd("cd target && allure generate --clean");

    TestUtils.copyDirectory("./target/allure-report", "./back-up/" + UNIQUE_FOLDER + "/allure-report");
    TestUtils.copyDirectory("./target/allure-report", "./back-up/LATEST/allure-report");
    TestUtils.copyFile("./target/Log.log", "./back-up/" + UNIQUE_FOLDER + "/Log.log");
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      //
    }
  }

}
