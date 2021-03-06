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
