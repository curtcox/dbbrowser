package com.cve.stores;

/**
 * A function that might throw an exception.
 * If Function from Google Collection threw an Exception, we would be
 * using it instead.
 * @author curt
 */
public interface UnpredictableFunction<F, T> {

  /**
   */
  T apply(F from) throws Exception;

}
