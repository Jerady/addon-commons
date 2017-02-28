package de.jensd.addon.demo.converter;

/**
 *
 * @author Jens Deters
 */
public class LightSideConverter implements StringConverter {

    @Override
    public String convert(String name) {
        switch (name) {
            case "Darth Vader":
                return "Anakin Skywalker";
            default:
                return "Unknown Jedi Master";
        }
    }

    @Override
    public String name() {
        return "LightSideConverter";
    }

    @Override
    public String toString() {
        return name();
    }

}
