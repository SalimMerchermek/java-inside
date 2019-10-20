package fr.umlv.java.inside;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MutableCallSite;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

import static java.lang.invoke.MethodHandles.guardWithTest;
import static java.lang.invoke.MethodHandles.insertArguments;
import static java.lang.invoke.MethodType.methodType;

public interface Logger {
    public void log(String message);

    public static Logger of(Class<?> declaringClass, Consumer<? super String> consumer) {
        Objects.requireNonNull(declaringClass) ;
        Objects.requireNonNull(consumer) ;

        var mh = createLoggingMethodHandle(declaringClass, consumer);
        return new Logger() {
            @Override
            public void log(String message) {
                Objects.requireNonNull(message) ;
                try {
                    mh.invokeExact(message);
                } catch(Throwable t) {
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException)t;
                    }
                    if (t instanceof Error) {
                        throw (Error)t;
                    }
                    throw new UndeclaredThrowableException(t);
                }
            }
        };
    }

    public static Logger fastof(Class<?> declaringClass, Consumer<? super String> consumer) {
        Objects.requireNonNull(declaringClass) ;
        Objects.requireNonNull(consumer) ;

        var mh = createLoggingMethodHandle(declaringClass, consumer);
        return message -> {
            Objects.requireNonNull(message) ;
            try {
                mh.invokeExact(message);
            } catch(Throwable t) {
                if (t instanceof RuntimeException) {
                    throw (RuntimeException)t;
                }
                if (t instanceof Error) {
                    throw (Error)t;
                }
                throw new UndeclaredThrowableException(t);
            }
        };
    }

     static final ClassValue<MutableCallSite> ENABLE_CALLSITES = new ClassValue<>() {
        protected MutableCallSite computeValue(Class<?> type) {
            var mutableCallSite = new MutableCallSite(MethodHandles.constant(boolean.class, true));
            MutableCallSite.syncAll(new MutableCallSite [] {mutableCallSite});
            return mutableCallSite ;
        }
    };

    public static void enable(Class<?> declaringClass, boolean enable) {
        ENABLE_CALLSITES.get(declaringClass).setTarget(MethodHandles.constant(boolean.class, enable));
    }

    private static MethodHandle createLoggingMethodHandle(Class<?> declaringClass, Consumer<? super String> consumer) {
        // TODO
        Objects.requireNonNull(declaringClass) ;
        Objects.requireNonNull(consumer) ;
        var lookup = MethodHandles.publicLookup () ;
        MethodHandle methodHandle ;
        try {
             methodHandle = lookup.findVirtual(Consumer.class, "accept", methodType(void.class, Object.class)) ;
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new AssertionError(e) ;
        }
        methodHandle = insertArguments(methodHandle, 0, consumer) ;
        methodHandle = methodHandle.asType(methodType(void.class, String.class)) ;

        return guardWithTest(
                ENABLE_CALLSITES.get(declaringClass).dynamicInvoker(),
                methodHandle,
                MethodHandles.empty(methodType(void.class, String.class)));
    }

}
