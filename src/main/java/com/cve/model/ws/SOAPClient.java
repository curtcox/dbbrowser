package com.cve.model.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * This class creates SOAP clients from WDSL and a Java interface.
 * See http://java.sun.com/j2ee/1.4/docs/tutorial/doc/JAXRPC5.html
 * <p>
 * Consider the following sample usage:
 * <pre>
    URL            wsdl = new URL("http://localhost:8080/calc/calc?wsdl");
    String  serviceName = "CalculatorWSService";
    String nameSpaceURI = "http://calculator.me.org/";
    String     portName = "CalculatorWSPort";
    Calculator     calc = SOAPClient.newInstance(wsdl, nameSpaceURI, serviceName, portName, Calculator.class);
 * </pre>
 * @author Curt
 */
public final class SOAPClient {

    /**
     * Create a new SOAPClient, given the specified parameters.
     * @param url where the WSDL is
     * @param nameSpaceUri
     * @param serviceName
     * @param portName
     * @param face interface to use
     * @return an object that implements the interface and is connected to the server
     */
    public static <T> T newInstance(
        URL url, String nameSpaceUri, String serviceName,
        String portName, Class<T> face)
        throws RemoteException
    {
        try {
            QName portQname    = new QName(nameSpaceUri, portName);
            QName serviceQname = new QName(nameSpaceUri, serviceName);
            Service service = Service.create(url, serviceQname);
            T remote = service.getPort(portQname,face);
            T proxy = face.cast(remote);
            return proxy;
        } catch (Throwable t) {
            String message =
                "Connecting to URL=" + url +
                " name space URI= "+ nameSpaceUri +
                " service name=" + serviceName +
                " interface=" + face +
                " port=" + portName;
            throw new RemoteException(message,t);
        }
    }

    /**
     * Don't specify the portName and trust that the service will do it.
     */
    public static <T> T newInstance(
        URL url, String nameSpaceUri, String serviceName, Class<T> face)
        throws MalformedURLException, RemoteException
    {
        QName serviceQname = new QName(nameSpaceUri, serviceName);
        Service service = Service.create(url, serviceQname);
        T remote = service.getPort(face);
        T proxy = face.cast(remote);
        return proxy;
    }
}