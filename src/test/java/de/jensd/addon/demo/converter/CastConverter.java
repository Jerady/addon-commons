package de.jensd.addon.demo.converter;

/**
 *
 * @author Jens Deters
 */
public class CastConverter implements StringConverter {

    @Override
    public String convert(String name) {
        if (name.contains("Luke")) {
            return "Mark Hamill";
        } else if (name.contains("Leia")) {
            return "Carrie Fisher";
        } else {
            return "Unknown Actor";
        }
    }

    @Override
    public String name() {
        return "CastConverter";
    }

    @Override
    public String toString() {
        return name();
    }

}
