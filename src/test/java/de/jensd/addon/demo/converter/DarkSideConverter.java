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
