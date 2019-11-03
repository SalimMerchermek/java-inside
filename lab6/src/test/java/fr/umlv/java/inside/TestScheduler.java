package fr.umlv.java.inside;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestScheduler {

    @Test
    public void schedulerWithStackPolicy() {
        StringJoiner sjExpected = new StringJoiner("\n") ;
        StringJoiner sjOutPut = new StringJoiner("\n") ;

        sjExpected.add("start 1").add("start 2").add("middle 2").add("end 2").add("middle 1").add("end 1") ;
        var scheduler = new StackScheduler() ;
        var scope = new ContinuationScope("scope");
        var continuation1 = new Continuation(scope, () -> {
            sjOutPut.add("start 1");
            scheduler.enqueue(scope);
            sjOutPut.add("middle 1");
            scheduler.enqueue(scope);
            sjOutPut.add("end 1");
        });

        var continuation2 = new Continuation(scope, () -> {
            sjOutPut.add("start 2");
            scheduler.enqueue(scope);
            sjOutPut.add("middle 2");
            scheduler.enqueue(scope);
            sjOutPut.add("end 2");
        });
        var list = List.of(continuation1, continuation2);
        list.forEach(Continuation::run);
        scheduler.runLoop();

        assertEquals(sjExpected.toString(), sjOutPut.toString());

    }

    @Test
    public void schedulerWithFIFOPolicy() {
        StringJoiner sjExpected = new StringJoiner("\n") ;
        StringJoiner sjOutPut = new StringJoiner("\n") ;

        sjExpected.add("start 1").add("start 2").add("middle 1").add("middle 2").add("end 1").add("end 2") ;
        var scheduler = new FifoScheduler() ;
        var scope = new ContinuationScope("scope");
        var continuation1 = new Continuation(scope, () -> {
            sjOutPut.add("start 1");
            scheduler.enqueue(scope);
            sjOutPut.add("middle 1");
            scheduler.enqueue(scope);
            sjOutPut.add("end 1");
        });

        var continuation2 = new Continuation(scope, () -> {
            sjOutPut.add("start 2");
            scheduler.enqueue(scope);
            sjOutPut.add("middle 2");
            scheduler.enqueue(scope);
            sjOutPut.add("end 2");
        });
        var list = List.of(continuation1, continuation2);
        list.forEach(Continuation::run);
        scheduler.runLoop();

        assertEquals(sjExpected.toString(), sjOutPut.toString());

    }

}
