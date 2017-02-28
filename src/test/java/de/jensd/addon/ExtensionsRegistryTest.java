package de.jensd.addon;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;
import de.jensd.addon.demo.converter.StringConverter;
import java.net.URL;

/**
 *
 * @author Jens Deters
 */
public class ExtensionsRegistryTest {

    public ExtensionsRegistryTest() {
    }

    @Test
    public void testLoadExtensions() {
        AddOnRegistryServiceLoader extensionRegistry = new AddOnRegistryServiceLoader();
        List<StringConverter> converter = extensionRegistry.getAddOns(StringConverter.class);
        assertTrue(converter.size() == 3);
    }

    @Test
    public void testConverter() {
        AddOnRegistryServiceLoader extensionRegistry = new AddOnRegistryServiceLoader();
        List<StringConverter> converter = extensionRegistry.getAddOns(StringConverter.class);
        Map<String, StringConverter> converters = converter.stream().collect(
                Collectors.toMap(c -> c.name(), c -> c));
        String lord = converters.get("DarkSideConverter").convert("Anakin Skywalker");
        assertEquals(lord, "Darth Vader");
        String jedi = converters.get("LightSideConverter").convert("Darth Vader");
        assertEquals(jedi, "Anakin Skywalker");
        String cast = converters.get("CastConverter").convert("Luke");
        assertEquals(cast, "Mark Hamill");
    }
    
    @Test
    public void testLoadExtensionsFromJar() {
        String lookupPath = "/Users/jens/NetBeansProjects/extensions-commons/build/resources/test";
        System.setProperty(AddOnRegistryServiceLoader.ADDON_LOOKUP_PATH_PROPERTY_NAME, lookupPath);
        AddOnRegistryServiceLoader extensionRegistry = new AddOnRegistryServiceLoader();
        List<URL> addOnUrls = extensionRegistry.lookupAddOnUrls();
    }
}
