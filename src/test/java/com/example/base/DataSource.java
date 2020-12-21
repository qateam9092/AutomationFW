package com.example.base;

import com.example.excel.SheetModel;
import com.example.excel.SuiteModel;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.osgl.xls.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

/**
 * Class for data provider
 */
public class DataSource extends Base {

  private static final Logger LOGGER = LogManager.getLogger(DataSource.class);

  /**
   * Data provider method
   */
  @DataProvider(name = "excel-data-provider")
  public Object[] dataProviderMethod() {
    // Get all test suite models
    List<SuiteModel> suiteModels = getExcelTestSuiteModels();
    LOGGER.debug(suiteModels);
    LOGGER.info("Getting all test sheet models with run mode = TRUE");
    SheetModel[] sheetModels = suiteModels
        .parallelStream()
        .map(DataSource::getExcelTestSheetModels)
        .flatMap(Collection::parallelStream)
        .toArray(SheetModel[]::new);
    LOGGER.info("Found total [{}] rows (test cases) across all sheets with run mode = TRUE", sheetModels.length);
    if (sheetModels.length == 0) {
      Constant.isAtLeastOneTestToRun = false;
    }
    return sheetModels;
  }

  /**
   * Gets test suite models filtered by test run mode = TRUE
   */
  public static List<SuiteModel> getExcelTestSuiteModels() {
    LOGGER.info("Reading {} ", Constant.CONTROLLER_SHEET_PATH);
    LOGGER.info("Getting all suite models with test run mode = TRUE");

    List<SuiteModel> filteredSuiteModelList = ExcelReader.builder()
        .sheets(Constant.SUITE_SHEET_NAME)
        .file(new File(Constant.CONTROLLER_SHEET_PATH))
        .build()
        .read(SuiteModel.class)
        .parallelStream()
        .filter(SuiteModel::getTestRunMode)
        .collect(Collectors.toList());

    List<String> sheetListWithRunModeTrue = filteredSuiteModelList
        .parallelStream()
        .map(SuiteModel::getTestSheetName)
        .collect(Collectors.toList());

    LOGGER.info("Found [{}] suite models(rows) ==> {} with run mode = TRUE", filteredSuiteModelList.size(),
        Collections.singletonList(sheetListWithRunModeTrue));
    return filteredSuiteModelList;
  }

  /**
   * Gets all test sheets models from all the sheets filtered by test run mode = TRUE
   */
  public static List<SheetModel> getExcelTestSheetModels(SuiteModel suiteModel) {
    LOGGER.debug("Getting all test sheet models with run mode = TRUE");
    List<SheetModel> filteredSheetModelList = ExcelReader.builder()
        .sheets(suiteModel.getTestSheetName())
        .file(new File(Constant.CONTROLLER_SHEET_PATH))
        .build()
        .read(SheetModel.class)
        .parallelStream()
        .filter(SheetModel::getTestRunMode)
        .collect(Collectors.toList());

    // Update excelTestSheetModel with base url & sheet name
    filteredSheetModelList.forEach(sheetModel -> {
      sheetModel.setBaseUrl(suiteModel.getBaseUrl());
      sheetModel.setSheetName(suiteModel.getTestSheetName());
      sheetModel.setOwner(suiteModel.getOwner());
    });

    // Abort the execution if sheet is declared in suite but not found in the workbook
    Constant.isSheetFound = filteredSheetModelList.size() > 0;
    Assert.assertTrue(Constant.isSheetFound,
        String.format("[%s] is defined in suite sheet, but could not find any such test sheet | ", suiteModel.getTestSheetName()));
    LOGGER.info("Found [{}] rows with run mode = TRUE in sheet [{}]", filteredSheetModelList.size(), suiteModel.getTestSheetName());
    return filteredSheetModelList;
  }
}
