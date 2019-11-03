package fr.umlv.java.inside;

import java.util.ArrayDeque;
import java.util.Objects;

public class StackScheduler implements Scheduler {
    private final ArrayDeque<Continuation> continuations = new ArrayDeque<> () ;
    @Override
    public void enqueue(ContinuationScope scope) {
        Objects.requireNonNull(Continuation.getCurrentContinuation(scope)) ;
        continuations.offer(Continuation.getCurrentContinuation(scope)) ;
        Continuation.yield(scope) ;
    }
    @Override
    public void runLoop() {
        while (!this.continuations.isEmpty()) {
            this.continuations.pollLast().run();
        }
    }
}
