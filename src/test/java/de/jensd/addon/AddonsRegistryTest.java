/**
 * Copyright (c) 2017 Jens Deters http://www.jensd.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
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
public class AddonsRegistryTest {

    public AddonsRegistryTest() {
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
        String lookupPath = "build/resources/test";
        System.setProperty(AddOnRegistryServiceLoader.ADDON_LOOKUP_PATH_PROPERTY_NAME, lookupPath);
        AddOnRegistryServiceLoader extensionRegistry = new AddOnRegistryServiceLoader();
        List<URL> addOnUrls = extensionRegistry.lookupAddOnUrls();
    }
}
