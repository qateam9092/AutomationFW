package com.example.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Final test result data access object Writes final test result to DB
 */
public class FinalTestResultDAO {

  private static final Logger LOGGER = LogManager.getLogger(FinalTestResultDAO.class);
  private final SessionFactory sessionFactory;

  public FinalTestResultDAO(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  /**
   * Saves final test result to DB
   */
  public boolean save(FinalTestResultModel finalTestResultModel) {
    LOGGER.info("Attempting to save final test result to DB");
    try {
      Session session = sessionFactory.openSession();
      session.beginTransaction();
      session.save(finalTestResultModel);
      session.getTransaction().commit();
      LOGGER.info("Successfully Saved final test result to DB. Unique test reference [{}]", finalTestResultModel.getTestReference());
      return true;
    } catch (Exception e) {
      LOGGER.error(e);
      return false;
    }
  }
}