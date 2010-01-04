package com.cve.web;

import com.cve.stores.ManagedFunction;
import com.cve.stores.StoreFactory;

/**
 *
 * @author Curt
 */
public interface WebApp {

    interface Context {
        ManagedFunction.Factory getManagedFunctionFactory();
        StoreFactory getStoreFactory();
    }
    
    RequestHandler getRequestHandler();

    ModelHtmlRenderer getModelHtmlRenderer();

}
