package com.cve.lang;

import com.google.common.collect.ImmutableList;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
/**
 * An executable element of a stack trace.
 * In other words, a method or constructor.
 */
@Immutable
public abstract class ExecutableElement implements Member {

    public final Class returnType;

    public final ImmutableList<Class> parameterTypes;

    public final ImmutableList<Class> exceptionTypes;

    public final Class declaringClass;

    public final String name;

    public final int modifiers;

    public final boolean synthetic;

    ExecutableElement(Method method) {
        this.returnType = notNull(method.getReturnType());
        this.parameterTypes = listOf(method.getParameterTypes());
        this.exceptionTypes = listOf(method.getExceptionTypes());
        this.declaringClass = method.getDeclaringClass();
        this.name = method.getName();
        this.modifiers = method.getModifiers();
        this.synthetic = method.isSynthetic();
    }

    ExecutableElement(Constructor method) {
        this.returnType = notNull(method.getDeclaringClass());
        this.parameterTypes = listOf(method.getParameterTypes());
        this.exceptionTypes = listOf(method.getExceptionTypes());
        this.declaringClass = method.getDeclaringClass();
        this.name = method.getName();
        this.modifiers = method.getModifiers();
        this.synthetic = method.isSynthetic();
    }


    static ImmutableList<Class> listOf(Class[] classes) {
        return ImmutableList.of(classes);
    }

    @Override
    final public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    @Override
    final public String getName() {
        return name;
    }

    @Override
    final public int getModifiers() {
        return modifiers;
    }

    @Override
    final public boolean isSynthetic() {
        return synthetic;
    }

    @Override
    public int hashCode() {
        return returnType.hashCode() ^
                parameterTypes.hashCode() ^
                exceptionTypes.hashCode() ^
                declaringClass.hashCode() ^
                name.hashCode() ^
                modifiers;
    }
    
    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        ExecutableElement other = (ExecutableElement) o;
        return returnType.equals(other.returnType) &&
               parameterTypes.equals(other.parameterTypes) &&
               exceptionTypes.equals(other.exceptionTypes) &&
               declaringClass.equals(other.declaringClass) &&
               name.equals(other.name) &&
               modifiers == other.modifiers &&
               synthetic == other.synthetic;
    }

}
