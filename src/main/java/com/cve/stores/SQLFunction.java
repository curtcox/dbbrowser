package com.cve.stores;

import java.sql.SQLException;

/**
 * A function that might throw a SQL exception.
 * If Function from Google Collection threw a SQLException, we would be
 * using it instead.
 * @author curt
 */
public interface SQLFunction<F, T> {

  /**
   */
  T apply(F from) throws SQLException;

}
