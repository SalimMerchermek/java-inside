package fr.umlv.java.inside;

import java.util.ArrayDeque;
import java.util.List;

public class Example1 {
    public static void main(String[] args) {

        var scope = new ContinuationScope("scope");
        var scheduler = new Scheduler(Scheduler.State.STACK);
        var continuation1 = new Continuation(scope, () -> {
            System.out.println("start 1");
            scheduler.enqueue(scope);
            System.out.println("middle 1");
            scheduler.enqueue(scope);
            System.out.println("end 1");
        });
        var continuation2 = new Continuation(scope, () -> {
            System.out.println("start 2");
            scheduler.enqueue(scope);
            System.out.println("middle 2");
            scheduler.enqueue(scope);
            System.out.println("end 2");
        });

        var continuation3 = new Continuation(scope, () -> {
            System.out.println("start 3");
            scheduler.enqueue(scope);
            System.out.println("middle 3");
            scheduler.enqueue(scope);
            System.out.println("end 3");
        });
        var list = List.of(continuation1, continuation2, continuation3);
        list.forEach(Continuation::run);
        scheduler.runLoop();
    }

}
