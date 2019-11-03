package fr.umlv.java.inside;

public interface Scheduler {
     void enqueue (ContinuationScope scope)  ;
     void runLoop () ;
}
