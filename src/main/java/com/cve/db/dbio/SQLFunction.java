package com.cve.db.dbio;

import com.cve.stores.ExceptionalFunction;
import java.sql.SQLException;

/**
 * A function that might throw a SQL exception.
 * If Function from Google Collection threw a SQLException, we would be
 * using it instead.
 * @author curt
 */
public interface SQLFunction<F, T> extends ExceptionalFunction {

  /**
   */
  T apply(F from) throws SQLException;

}
