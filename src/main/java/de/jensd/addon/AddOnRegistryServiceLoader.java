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

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * An add-on service loader registry.
 * 
 * The Loader is looking for types of the given class on the classpath (must implement the interface @see AddOn.java).
 * 
 * @author Jens Deters
 */
public class AddOnRegistryServiceLoader implements AddOnRegistry {

    public static final String ADDON_LOOKUP_PATH_PROPERTY_NAME = "de.jensd.addon.lookupPath";
    public static final String ADDON_FILE_EXTENSION_PROPERTY_NAME = "de.jensd.addon.fileExtension";
    public static final String ADDON_LOOKUP_PATH_DEFAULT = "./addon/";
    public static final String ADDON_FILE_EXTENSION_DEFAULT = ".jar";

    @Override
    public <TAddOn extends AddOn> List<TAddOn> getAddOns(Class<TAddOn> addOnClass) {
        ServiceLoader<TAddOn> addOnLoader = ServiceLoader.load(addOnClass, createAddOnClassLoader(lookupAddOnUrls()));
        List<TAddOn> addOns = new ArrayList<>();
        addOnLoader.forEach(addOn -> {
            System.out.println(String.format("Found add-on of type %s at: %s",
                    addOnClass.getName(),
                    addOn.getClass().getProtectionDomain().getCodeSource().getLocation()));
            addOns.add(addOn);
        });
        if (addOns.isEmpty()) {
            System.out.println(String.format("No implementations of type %s found", addOnClass.getName()));
        }
        return addOns;
    }

    /**
     * 
     * @return The list of URL the registry is trying to lookup.
     */
    public List<URL> lookupAddOnUrls() {
        File addOnFolder = new File(getAddOnLookupPath());
        System.out.println(String.format("Add-on folder: %s", addOnFolder.getAbsolutePath()));
        List<URL> addOnUrlList = new ArrayList<>();
        File[] addOnJars = addOnFolder.listFiles(getAddOnJarFilter());
        if (addOnJars == null || addOnJars.length == 0) {
            System.out.println("No add-on jars found");
        } else {
            System.out.println(String.format("Found %s add-ons package(s)", addOnJars.length));
            for (File extensionJar : addOnJars) {
                try {
                    URL addOnJarUrl = extensionJar.toURI().toURL();
                    addOnUrlList.add(addOnJarUrl);
                    System.out.println(String.format("Added add-ons package: %s", addOnJarUrl));
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return addOnUrlList;
    }

    private URLClassLoader createAddOnClassLoader(List<URL> addOnJars) {
        return new URLClassLoader(addOnJars.toArray(new URL[addOnJars.size()]));
    }

    private FilenameFilter getAddOnJarFilter() {
        return (File dir, String name) -> name.endsWith(getAddOnFileExtension());
    }

    /**
     * 
     * @return The path the registry is looking up external jar files.
     */
    public String getAddOnLookupPath() {
        return System.getProperty(ADDON_LOOKUP_PATH_PROPERTY_NAME, ADDON_LOOKUP_PATH_DEFAULT);
    }

    /**
     * 
     * @return The file extension of add-on packages.
     */
    public String getAddOnFileExtension() {
        return System.getProperty(ADDON_FILE_EXTENSION_PROPERTY_NAME, ADDON_FILE_EXTENSION_DEFAULT);
    }

}
