package com.cve.io.db;

import com.cve.stores.UnpredictableFunction;
import java.sql.SQLException;

/**
 * A function that might throw a SQL exception.
 * If Function from Google Collection threw a SQLException, we would be
 * using it instead.
 * @author curt
 */
public interface SQLFunction<F, T> extends UnpredictableFunction {

  /**
   */
  T apply(F from) throws SQLException;

}
