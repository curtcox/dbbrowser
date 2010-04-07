package com.cve.model.corba;

import org.omg.CORBA.Any;
import org.omg.CORBA.Context;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.Request;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

/**
 * See Enterprise Java in a Nutshell.
 * @author curt
 */
final class DIISimpleClient {

public static void main(String argv[])
    throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName
{
    ORB myORB = ORB.init(argv, null);
    ORB singleORB = ORB.init();
    // Get a reference to the object
    org.omg.CORBA.Object ncRef = myORB.resolve_initial_references("NameService");
    NamingContext nc = NamingContextHelper.narrow(ncRef);
    NameComponent nComp = new NameComponent("ThisOrThatServer", "");
    NameComponent[] path = {nComp};
    org.omg.CORBA.Object objRef = nc.resolve(path);

    // Now make a dynamic call to the doThis method.  The first step is
    // to build the argument list. In this case, there's a single String
    // argument to the method, so create an NVList of length 1.  Next
    // create an Any object to hold the value of the argument and insert
    // the desired value.  Finally, wrap the Any object with a NamedValue
    // and insert it into the NVList, specifying that it is an input
    // parameter.
    NVList argList = myORB.create_list(1);
    Any arg1 = myORB.create_any();
    arg1.insert_string("something");
    NamedValue nvArg = argList.add_value("what", arg1, org.omg.CORBA.ARG_IN.value);

    // Create an Any object to hold the return value of the method and
    // wrap it in a NamedValue
    Any result = myORB.create_any();
    result.insert_string("dummy");
    NamedValue resultVal = myORB.create_named_value("result", result, org.omg.CORBA.ARG_OUT.value);

    // Get the local context from the ORB.
    // NOTE: This call does not work in Java 1.2, and returns a
    //   NOT_IMPLEMENTED exception.  To make this work in Java 1.2, simply
    //   remove this call to get_default_context(), and pass a null pointer
    //   into the _create_request() call below.  This example should work
    //   as is with any compliant Java CORBA environment, however.
    Context ctx = myORB.get_default_context();

    // Create the method request using the default context, the name of
    // the method, the NVList argument list, and the NamedValue for the
    // result.  Then invoke the method by calling invoke() on the Request.
    Request thisReq = objRef._create_request(ctx, "doThis", argList, resultVal);
    thisReq.invoke();

    // Get the return value from the Request object and output results.
    result = thisReq.result().value();
    System.out.println("doThis() returned: " + result.extract_string());
}

}