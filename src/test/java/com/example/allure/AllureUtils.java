package com.example.allure;

import com.example.excel.SheetModel;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class to intercept & override allure fields
 */
public class AllureUtils {

  private static final Logger LOGGER = LogManager.getLogger(AllureUtils.class);

  private AllureUtils() {
  }

  /**
   * Set allure fields to be overridden
   */
  public static void overrideAllureFields(Method method, SheetModel sheetModel) {

    if (StringUtils.isNotBlank(sheetModel.getTestDescription())) {
      Description description = method.getAnnotation(Description.class);
      changeAnnotationValue(description, "value", sheetModel.getTestDescription());
    }

    if (StringUtils.isNotBlank(sheetModel.getSheetName())) {
      Feature feature = method.getAnnotation(Feature.class);
      changeAnnotationValue(feature, "value", sheetModel.getSheetName());
    }

    if (StringUtils.isNotBlank(sheetModel.getOwner())) {
      Owner owner = method.getAnnotation(Owner.class);
      changeAnnotationValue(owner, "value", sheetModel.getOwner());
    }

    if (StringUtils.isNotBlank(sheetModel.getTestId())) {
      TmsLink tmsLink = method.getAnnotation(TmsLink.class);
      changeAnnotationValue(tmsLink, "value", sheetModel.getTestId());
    }

    Description description = method.getAnnotation(Description.class);
    Feature feature = method.getAnnotation(Feature.class);
    Owner owner = method.getAnnotation(Owner.class);
    TmsLink tmsLink = method.getAnnotation(TmsLink.class);
    Severity severity = method.getAnnotation(Severity.class);
    changeAnnotationValue(description, "value", sheetModel.getTestDescription());
    changeAnnotationValue(feature, "value", sheetModel.getSheetName());
    changeAnnotationValue(owner, "value", sheetModel.getOwner());
    changeAnnotationValue(tmsLink, "value", sheetModel.getTestId());
    changeAnnotationValue(severity, "value", SeverityLevel.valueOf(sheetModel.getTestSeverity()));
  }

  /**
   * Set allure fields values to be overridden
   */
  @SuppressWarnings("unchecked")
  private static void changeAnnotationValue(Annotation annotation, String key, Object newValue) {
    LOGGER.debug("Before annotation : " + annotation);
    Object handler = Proxy.getInvocationHandler(annotation);
    Field field;
    try {
      field = handler.getClass().getDeclaredField("memberValues");
    } catch (NoSuchFieldException | SecurityException e) {
      throw new IllegalStateException(e);
    }
    field.setAccessible(true);
    Map<String, Object> memberValues;
    try {
      memberValues = (Map<String, Object>) field.get(handler);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
    Object oldValue = memberValues.get(key);
    if (oldValue == null || oldValue.getClass() != newValue.getClass()) {
      throw new IllegalArgumentException();
    }
    memberValues.put(key, newValue);
    LOGGER.debug("After annotation : " + annotation);
  }

}
