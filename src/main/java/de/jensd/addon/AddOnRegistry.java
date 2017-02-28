package de.jensd.addon;

import java.util.List;

/**
 * 
 * @author Jens Deters
 */
public interface AddOnRegistry {
   
    /**
     * 
     * @param <TAddOn> The type of class to look for.
     * @param addOnClass The type of class to look for.
     * @return A List of the found implementations of the given type. 
     */
    <TAddOn extends AddOn> List<TAddOn> getAddOns(Class<TAddOn> addOnClass);
}
