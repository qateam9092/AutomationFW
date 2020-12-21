package com.example.database;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Test case details data access object Writes test case details to DB
 */
public class TestCaseDetailsDAO {

  private static final Logger LOGGER = LogManager.getLogger(TestCaseDetailsDAO.class);
  private final SessionFactory sessionFactory;

  public TestCaseDetailsDAO(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  /**
   * Saves test case details to DB
   */
  public boolean save(List<TestCasesDetailsModel> testCasesDetailsModelList) {
    LOGGER.info("Attempting to save test case details to DB | Test case count : {}", testCasesDetailsModelList.size());
    try {
      Session session = sessionFactory.openSession();
      testCasesDetailsModelList.forEach(testCasesDetailsModel -> {
        session.beginTransaction();
        session.save(testCasesDetailsModel);
        session.getTransaction().commit();
        LOGGER.info("Saved [{}] to DB", testCasesDetailsModel.getTestMethodName());
      });
      LOGGER.info("Successfully saved [{}] test case details to DB", testCasesDetailsModelList.size());
      return true;
    } catch (Exception e) {
      LOGGER.error(e);
      return false;
    }
  }
}