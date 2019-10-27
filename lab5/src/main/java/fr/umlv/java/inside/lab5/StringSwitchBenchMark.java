package fr.umlv.java.inside.lab5;


import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class StringSwitchBenchMark {

    private static final String [] strings = {"foo", "bar", "bazz", "tata", "titi", "toto"} ;
    private static final  ArrayList<String> stringsList = new ArrayList<>() ;
    static {
        Random r = new Random() ;
        for (var i =0; i<1_000_000; i++) {
            stringsList.add(strings[r.nextInt(strings.length)]) ;
        }
    }
    @Benchmark
    public void stringSwitch() {
        stringsList.forEach(StringSwitchExample::stringSwitch);
    }

    @Benchmark
    public void stringSwitch2() {
        stringsList.forEach(StringSwitchExample::stringSwitch2);    }

    @Benchmark
    public void stringSwitch3() {
        stringsList.forEach(StringSwitchExample::stringSwitch3);    }

}
