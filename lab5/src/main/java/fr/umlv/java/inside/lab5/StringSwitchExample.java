package fr.umlv.java.inside.lab5;

import java.util.Objects;

public class StringSwitchExample {

    public static int stringSwitch (String value) {
        Objects.requireNonNull(value) ;
        switch (value) {
            case "foo" : return 0;
            case "bar" : return 1;
            case "bazz" : return 2;
            default: return -1 ;
        }
    }
}
