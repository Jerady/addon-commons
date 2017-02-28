package de.jensd.addon.demo.converter;

import de.jensd.addon.AddOn;

public interface StringConverter extends AddOn{
    String convert(String value);
    String name();
}