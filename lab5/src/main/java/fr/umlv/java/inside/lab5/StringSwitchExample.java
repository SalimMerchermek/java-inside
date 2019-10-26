package fr.umlv.java.inside.lab5;

import java.lang.invoke.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Objects;

import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.methodType;

public class StringSwitchExample {

    private static final MethodHandle STRING_EQUALS ;

    static {
        var lookup = MethodHandles.lookup() ;
        try {
             STRING_EQUALS = lookup.findVirtual(String.class, "equals",  methodType(boolean.class, Object.class)) ;
        } catch (NoSuchMethodException  | IllegalAccessException e) {
           throw new AssertionError(e) ;
        }
    }

    public static int stringSwitch (String value) {
        Objects.requireNonNull(value) ;
        switch (value) {
            case "foo" : return 0;
            case "bar" : return 1;
            case "bazz" : return 2;
            default: return -1 ;
        }
    }

    public static int stringSwitch2 (String value)  {
        var mh = createMHFromStrings2("foo", "bar", "bazz");
        try {
            return (int) mh.invokeExact(value);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e2) {
            throw new UndeclaredThrowableException(e2);
        }
    }

    public static int stringSwitch3 (String value)  {
        var mh = createMHFromStrings3("foo", "bar", "bazz");
        try {
            return (int) mh.invokeExact(value);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e2) {
            throw new UndeclaredThrowableException(e2);
        }
    }


    public static MethodHandle createMHFromStrings2 (String ... args)  {
        var mh = dropArguments(MethodHandles.constant(int.class, -1), 0, String.class);

        for (var i = 0; i<args.length; i++) {
            mh = MethodHandles.guardWithTest(
                    insertArguments(STRING_EQUALS, 1, args[i]),
                    dropArguments(MethodHandles.constant(int.class, i), 0, String.class),
                    mh
            ) ;
        }
        return mh ;
    }

    public static MethodHandle createMHFromStrings3(String... matches) {
        return new InliningCache(matches).dynamicInvoker();
    }

    static class InliningCache extends MutableCallSite {
        private static final MethodHandle SLOW_PATH;
        static {
            try {
                SLOW_PATH = MethodHandles.lookup().findVirtual(
                        InliningCache.class, "slowPath",MethodType.methodType(int.class, String.class)
                ) ;
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new AssertionError(e) ;
            }

        }
        private final List<String> matches;
        public InliningCache(String... matches) {
            super(MethodType.methodType(int.class, String.class));
            this.matches = List.of(matches);
            setTarget(insertArguments(SLOW_PATH, 0, this));
        }

        private int slowPath(String value) {
            var index = this.matches.indexOf(value) ;
            var mh = guardWithTest(
                    insertArguments(STRING_EQUALS, 1, value),
                    dropArguments(MethodHandles.constant(int.class, index), 0, String.class),
                    getTarget()
            ) ;
            setTarget(mh);
            return index ;
        }

        
    }


}
