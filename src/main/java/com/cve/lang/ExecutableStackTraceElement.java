package com.cve.lang;

import java.lang.reflect.Member;

import static com.cve.util.Check.notNull;

final class ExecutableStackTraceElement
    extends ExecutableElement
    implements Member
{
    public final StackTraceElement inner;

    private ExecutableStackTraceElement(StackTraceElement stackTraceElement) {
        super(new StackTraceElementAsMember(stackTraceElement),listOf(new Class[0]),listOf(new Class[0]));
        inner = notNull(stackTraceElement);
    }

    static ExecutableElement of(StackTraceElement stackTraceElement) {
        return new ExecutableStackTraceElement(stackTraceElement);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ inner.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        ExecutableConstructor other = (ExecutableConstructor) o;
        return super.equals(other) && inner.equals(other.inner);
    }

    static final class StackTraceElementAsMember
        implements Member
    {
        public final StackTraceElement stackTraceElement;

        StackTraceElementAsMember(StackTraceElement stackTraceElement) {
            this.stackTraceElement = stackTraceElement;
        }

        @Override
        public Class<?> getDeclaringClass() {
            try {
                return Class.forName(stackTraceElement.getClassName());
            } catch (Exception e) {
                return Class.class;
            }
        }

        @Override
        public String getName() {
            return stackTraceElement.getMethodName();
        }

        @Override
        public int getModifiers() {
            return 0;
        }

        @Override
        public boolean isSynthetic() {
            return false;
        }
    }
}
