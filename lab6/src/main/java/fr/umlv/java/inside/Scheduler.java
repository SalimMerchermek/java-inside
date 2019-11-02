package fr.umlv.java.inside;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Objects;

public class Scheduler {
    public enum State {
        STACK, FIFO, RANDOM
    }

    private final ArrayDeque<Continuation> continuations = new ArrayDeque<>() ;
    private final State state ;

    public Scheduler(State state) {
        this.state = state ;
    }

    public void enqueue (ContinuationScope scope) {
        Objects.requireNonNull(Continuation.getCurrentContinuation(scope)) ;
        continuations.offer(Continuation.getCurrentContinuation(scope)) ;
        Continuation.yield(scope) ;
    }

    public void runLoop () {
        while (!this.continuations.isEmpty()) {
            switch (this.state) {
                case FIFO :
                    this.continuations.pollFirst().run();
                    break;
                case STACK :
                    this.continuations.pollLast().run();
                    break;
                case RANDOM : {
                }
            }

        }
    }
}
