/*
 * Copyright 2011 William Bernardet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.japi.checker;

public final class Utils {
    
    private Utils() { }
    
    public static String fixNull(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }
    
    public static String fixEmpty(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        return str;
    }
    
    public static String toDottedClassName(final String name) {
        if (name == null) {
            return null;
        }
        return name.replaceAll("/", ".");
    }
    
    public static String[] toDottedClassNames(String[] names) {
    	if (names == null) {
    		return null;
    	}
        String[] ret = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            ret[i] = toDottedClassName(names[i]);
        }
        return ret;
    }
    
    public static String toSlashedClassName(final String name) {
    	if (name == null) {
    		return null;
    	}
    	return name.replaceAll("[.]", "/");
    }
}
