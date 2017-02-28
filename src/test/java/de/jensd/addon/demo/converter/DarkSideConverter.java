package de.jensd.addon.demo.converter;

/**
 *
 * @author Jens Deters
 */
public class DarkSideConverter implements StringConverter{
    @Override
    public String convert(String name) {
        switch (name) {
            case "Anakin Skywalker":
                return "Darth Vader";
            default:
                return "Unknown Sith Lord";
        }
    }

    @Override
    public String name() {
        return "DarkSideConverter";
    }

    @Override
    public String toString() {
        return name();
    }
    
    
}
