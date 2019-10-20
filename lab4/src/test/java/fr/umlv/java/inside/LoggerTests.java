package fr.umlv.java.inside;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class LoggerTests {

    private static class TestA {
        private static final StringBuilder SB = new StringBuilder() ;
        private static final Logger LOGGER = Logger.of(TestA.class, SB::append) ;
    }

    @Test
    public void testLog() {
        TestA.LOGGER.log("toto");
        assertEquals("toto", TestA.SB.toString());
    }

    @Test
    public void ofNull () {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> Logger.of(null, __ -> {})) ,
                () -> assertThrows(NullPointerException.class, () -> Logger.of(LoggerTests.class, null))
        );
    }

    @Test
    public void logNull () {
        var logger = TestA.LOGGER ;
        assertThrows(NullPointerException.class, () -> logger.log(null)) ;
    }

    private static class TestB {
        private static final StringBuilder SB = new StringBuilder() ;
        private static final Logger LOGGER = Logger.of(TestB.class, SB::append) ;
    }

    @Test
    public void logDisabled () {
        Logger.enable(TestB.class, false);
        TestB.LOGGER.log("testing");
        assertEquals("", TestB.SB.toString()); ;
    }

    @Test
    public void logEnabled () {
        Logger.enable(TestB.class, true);
        TestB.LOGGER.log("testing");
        assertEquals("testing", TestB.SB.toString()); ;
    }
}
