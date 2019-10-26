package fr.umlv.java.inside.lab5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class StringSwitchExampleTests {

    @ParameterizedTest
    @MethodSource("methods")
    public void teststringSwitch (ToIntFunction<String> method) {
        assertAll(
                () -> assertEquals(0,method.applyAsInt("foo") ) ,
                () -> assertEquals(1, method.applyAsInt("bar")) ,
                () -> assertEquals(2, method.applyAsInt("bazz")) ,
                () -> assertEquals(-1,method.applyAsInt("value") )
        );
    }

    static Stream<ToIntFunction<String>> methods() {
        return Stream.of(
                StringSwitchExample::stringSwitch,
                StringSwitchExample::stringSwitch2,
                StringSwitchExample::stringSwitch3
        );
    }
}
