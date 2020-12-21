package com.example.database;

import com.example.base.Constant;
import java.io.File;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Util class to get Session factory
 */
public class DBUtil {

  private static final Logger LOGGER = LogManager.getLogger(DBUtil.class);
  private static SessionFactory sessionFactory;

  private DBUtil() {
  }

  /**
   * Gets Hibernate configuration from config file
   */
  public static Configuration getHibernateConfiguration() throws HibernateException {
    Configuration configuration = new Configuration();
    File externalHibernateConfigFile = Paths.get(Constant.PARENT_DIR, "resources", "hibernate.cfg.xml").toFile();
    if (externalHibernateConfigFile.exists()) {
      LOGGER.info("Using external hibernate config file : {}", externalHibernateConfigFile.toString());
      return configuration.configure(externalHibernateConfigFile);
    } else {
      LOGGER.info("No external hibernate config file found. Looking for internal hibernate file");
      if (Constant.HIBERNATE_CONFIG_FILE.exists()) {
        LOGGER.info("Using internal hibernate configuration file : {}", Constant.HIBERNATE_CONFIG_FILE);
        return configuration.configure(Constant.HIBERNATE_CONFIG_FILE);
      } else {
        throw new HibernateException("Could not find hibernate config file : " + Constant.HIBERNATE_CONFIG_FILE.toString());
      }
    }
  }

  /**
   * Gets Session factory
   */
  public static SessionFactory getSessionFactory(Configuration configuration) {
    if (sessionFactory == null) {
      try {
        sessionFactory = configuration.configure().buildSessionFactory();
      } catch (Exception e) {
        LOGGER.error("SessionFactory creation failed. Will skip writing to DB");
        LOGGER.debug(e);
      }
    }
    return sessionFactory;
  }

  /**
   * Closes session factory
   */
  public static void closeSessionFactory() {
    try {
      if (sessionFactory != null) {
        sessionFactory.close();
        LOGGER.info("Successfully closed database connection");
      }
    } catch (Exception e) {
      LOGGER.error("Could not close session factory : ", e);
    }
  }
}