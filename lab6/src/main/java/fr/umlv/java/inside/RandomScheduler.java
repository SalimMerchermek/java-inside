package fr.umlv.java.inside;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class RandomScheduler implements Scheduler {
    private final ArrayList<Continuation> continuations = new ArrayList<> () ;
    @Override
    public void enqueue(ContinuationScope scope) {
        Objects.requireNonNull(Continuation.getCurrentContinuation(scope)) ;
        continuations.add(Continuation.getCurrentContinuation(scope)) ;
        Continuation.yield(scope) ;
    }

    @Override
    public void runLoop() {
        this.continuations.get(ThreadLocalRandom.current().nextInt(this.continuations.size())).run();
    }
}
