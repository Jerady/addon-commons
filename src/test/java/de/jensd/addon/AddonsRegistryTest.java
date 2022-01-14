/*
  Copyright (c) 2017 Jens Deters http://www.jensd.de

  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy of
  the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations under
  the License.
 */
package de.jensd.addon;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import de.jensd.addon.demo.converter.StringConverter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.net.URL;

/**
 *
 * @author Jens Deters
 */
class AddonsRegistryTest {

    public AddonsRegistryTest() {
    }

    @Test
    void testLoadExtensions() {
        AddOnRegistryServiceLoader extensionRegistry = new AddOnRegistryServiceLoader();
        List<StringConverter> converter = extensionRegistry.getAddOns(StringConverter.class);
        assertEquals(3, converter.size());
    }

    @Test
    void testConverter() {
        AddOnRegistryServiceLoader extensionRegistry = new AddOnRegistryServiceLoader();
        List<StringConverter> converter = extensionRegistry.getAddOns(StringConverter.class);
        Map<String, StringConverter> converters = converter.stream().collect(
                Collectors.toMap(StringConverter::name, c -> c));
        String lord = converters.get("DarkSideConverter").convert("Anakin Skywalker");
        assertEquals("Darth Vader", lord);
        String jedi = converters.get("LightSideConverter").convert("Darth Vader");
        assertEquals("Anakin Skywalker", jedi);
        String cast = converters.get("CastConverter").convert("Luke");
        assertEquals("Mark Hamill", cast);
    }
    
    @Test
    void testLoadExtensionsFromJar() {
        String lookupPath = "build/resources/test";
        System.setProperty(AddOnRegistryServiceLoader.ADDON_LOOKUP_PATH_PROPERTY_NAME, lookupPath);
        AddOnRegistryServiceLoader extensionRegistry = new AddOnRegistryServiceLoader();
        List<URL> addOnUrls = extensionRegistry.lookupAddOnUrls();
        assertEquals(1, addOnUrls.size());
    }
}
