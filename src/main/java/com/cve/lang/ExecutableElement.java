package com.cve.lang;

import java.lang.reflect.Member;

/**
 * An executable element of a stack trace.
 * In other words, a method or constructor.
 */
public interface ExecutableElement extends Member {

    Class getReturnType();

    Class[] getParameterTypes();

    Class[] getExceptionTypes();
}
