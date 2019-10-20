package fr.umlv.java.inside;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;


@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class LoggerBenchMark {

    private static class Test {
        private static final Logger LOGGER = Logger.of(Test.class, __ -> {}) ;
        private static final Logger LOGGER_FAST = Logger.fastof(Test.class, __ -> {}) ;

    }

    private static class TestB {
        private static final Logger SIMPLE_LOGGER_DISABLED = Logger.of(Test.class, __ -> {}) ;
        static {
            Logger.enable(TestB.class, false);
        }
    }

    private static class TestC {
        private static final Logger FAST_LOGGER_DISABLED = Logger.fastof(Test.class, __ -> {}) ;
        static {
            Logger.enable(TestC.class, false) ;
        }
    }

    @Benchmark
    public void no_op() {
        // empty
    }

    @Benchmark
    public void simple_logger() {
      Test.LOGGER.log("Bonjour");
    }

    @Benchmark
    public void fast_logger() {
        Test.LOGGER_FAST.log("Bonjour");
    }

    @Benchmark
    public void simple_logger_disabled() {
        TestB.SIMPLE_LOGGER_DISABLED.log("Bonjour");
    }

    @Benchmark
    public void fast_logger_disabled() {
        TestC.FAST_LOGGER_DISABLED.log("Bonjour");
    }
}
