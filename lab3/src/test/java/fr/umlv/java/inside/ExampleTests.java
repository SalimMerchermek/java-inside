package fr.umlv.java.inside;

import org.junit.jupiter.api.Test;

import javax.crypto.ExemptionMechanism;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;


public class ExampleTests {
    @Test
    public void staticHelloReflect() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        var method = Example.class.getDeclaredMethod("aStaticHello",int.class) ;
        method.setAccessible(true);
        assertEquals("question 3", method.invoke(null, 3)) ;
    }

    @Test
    public void instanceHelloReflect() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        var method = Example.class.getDeclaredMethod("anInstanceHello",int.class) ;
        method.setAccessible(true);
        assertEquals("question 3", method.invoke(new Example(), 3)) ;
    }

    @Test
    public void staticHello() throws Throwable {
        Lookup lookupPrivate = privateLookupIn(Example.class, MethodHandles.lookup());
        var method = lookupPrivate.findStatic(Example.class ,"aStaticHello", methodType(String.class, int.class)) ;
        assertEquals("question 3", (String) method.invokeExact(3)) ;
    }

    @Test
    public void instanceHello() throws Throwable {
        Lookup lookupPrivate = privateLookupIn(Example.class, MethodHandles.lookup());
        var method = lookupPrivate.findVirtual(Example.class ,"anInstanceHello",methodType(String.class, int.class)) ;
        assertEquals("question 3", (String) method.invokeExact(new Example() ,3)) ;
    }

    @Test
    public void staticHelloWithInsertArgument() throws Throwable {
        Lookup lookupPrivate = privateLookupIn(Example.class, MethodHandles.lookup());
        var method = lookupPrivate.findStatic(Example.class ,"aStaticHello", methodType(String.class, int.class)) ;
        MethodHandle methodeHandleWithArguments = insertArguments(method,0,8) ;
        assertEquals("question 8", (String) methodeHandleWithArguments.invokeExact()) ;
    }

    @Test
    public void instanceHelloWithInsertArgument() throws Throwable {
        Lookup lookupPrivate = privateLookupIn(Example.class, MethodHandles.lookup());
        var method = lookupPrivate.findVirtual(Example.class ,"anInstanceHello",methodType(String.class, int.class)) ;
        MethodHandle methodeHandleWithArguments = insertArguments(method,0,new Example(), 8) ;
        assertEquals("question 8", (String) methodeHandleWithArguments.invokeExact()) ;
    }

    @Test
    public void staticHelloWithDropArgument() throws Throwable {
        Lookup lookupPrivate = privateLookupIn(Example.class, MethodHandles.lookup());
        var method = lookupPrivate.findStatic(Example.class ,"aStaticHello", methodType(String.class, int.class)) ;
        MethodHandle methodeHandleWithArguments = dropArguments(method,1,int.class) ;
        assertEquals("question 8", (String) methodeHandleWithArguments.invokeExact(8,1)) ;
    }

    @Test
    public void instanceHelloWithDropArgument() throws Throwable {
        Lookup lookupPrivate = privateLookupIn(Example.class, MethodHandles.lookup());
        var method = lookupPrivate.findVirtual(Example.class ,"anInstanceHello",methodType(String.class, int.class)) ;
        MethodHandle methodeHandleWithArguments = dropArguments(method,2,int.class) ;
        assertEquals("question 8", (String) methodeHandleWithArguments.invokeExact(new Example(), 8,1)) ;
    }

    @Test
    public void staticHelloAsType() throws Throwable {
        Lookup lookupPrivate = privateLookupIn(Example.class, MethodHandles.lookup());
        var method = lookupPrivate.findStatic(Example.class ,"aStaticHello", methodType(String.class, int.class)).asType(methodType(String.class, Integer.class)) ;
        assertEquals("question 8", (String) method.invokeExact(Integer.valueOf(8))) ;
    }

    @Test
    public void instanceHelloAsType() throws Throwable {
        Lookup lookupPrivate = privateLookupIn(Example.class, MethodHandles.lookup());
        var method = lookupPrivate.findVirtual(Example.class ,"anInstanceHello",methodType(String.class, int.class)).asType(methodType(String.class, Example.class,Integer.class));
        assertEquals("question 8", (String) method.invokeExact(new Example(), Integer.valueOf(8))) ;
    }


    @Test
    public void constantTest() throws Throwable {
        var method = constant(String.class, "question 8") ;
        assertEquals("question 8", (String) method.invokeExact()) ;
    }

    @Test
    public void guardTestOk() throws Throwable {
      var test = "foo" ;
      Lookup lookup = MethodHandles.publicLookup() ;
      var testMethodeHandle = lookup.findVirtual(String.class, "equals",  methodType(boolean.class, Object.class)) ;

      var methodeHandle = MethodHandles.guardWithTest(
              testMethodeHandle.asType(MethodType.methodType(boolean.class, String.class, String.class)),
              MethodHandles.dropArguments(MethodHandles.constant(int.class, 1), 0, String.class, String.class),
              MethodHandles.dropArguments(MethodHandles.constant(int.class, -1), 0, String.class, String.class)) ;
      assertEquals(1, (int)MethodHandles.insertArguments(methodeHandle, 1, test).invokeExact(test));

    }

    @Test
    public void guardTestNotOk() throws Throwable {
        var testOk = "foo" ;
        var testNotOk = "fooo" ;
        Lookup lookup = MethodHandles.publicLookup() ;
        var testMethodeHandle = lookup.findVirtual(String.class, "equals",  methodType(boolean.class, Object.class)) ;

        var methodeHandle = MethodHandles.guardWithTest(
                testMethodeHandle.asType(MethodType.methodType(boolean.class, String.class, String.class)),
                MethodHandles.dropArguments(MethodHandles.constant(int.class, 1), 0, String.class, String.class),
                MethodHandles.dropArguments(MethodHandles.constant(int.class, -1), 0, String.class, String.class)) ;
        assertEquals(-1, (int)MethodHandles.insertArguments(methodeHandle, 1, testOk).invokeExact(testNotOk));

    }

}
