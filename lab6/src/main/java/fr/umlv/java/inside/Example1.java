package fr.umlv.java.inside;

import java.util.ArrayDeque;
import java.util.List;

public class Example1 {
    public static void main(String[] args) {

        var scope = new ContinuationScope("hello1") ;


        var continuation = new Continuation(scope, () -> {

            System.out.println("enter1");
            Continuation.yield(scope) ;
            System.out.println("middle1");
            Continuation.yield(scope) ;
            System.out.println("exit1");

        }) ;

        var continuation2 = new Continuation(scope, () -> {
            System.out.println("enter2");
            Continuation.yield(scope) ;
            System.out.println("exit2");
        }) ;

        var continuation3 = new Continuation(scope, () -> {
            System.out.println("enter3");
            Continuation.yield(scope) ;
            System.out.println("exit3");
        }) ;

        var list  = List.of(continuation, continuation2, continuation3) ;

        var arrayDequ = new ArrayDeque<>(list) ;

        while (!arrayDequ.isEmpty()) {
            var cont = arrayDequ.poll() ;
            cont.run();
            if (!cont.isDone()) arrayDequ.offer(cont);
        }

    }

}
