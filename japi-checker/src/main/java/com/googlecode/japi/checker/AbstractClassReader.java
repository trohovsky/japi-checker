/*
 * Copyright 2012 William Bernardet
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

import com.googlecode.japi.checker.utils.AntPatternMatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This abstract class provide basic behavior for the BCChecker to extract ClassData out of a container e.g: a
 * directory, a zip file, a jar file... It must be extended to implement such functionalities.
 */
public abstract class AbstractClassReader<C> {
	private Map<String, C> classes = new Hashtable<String, C>();

	/**
	 * This method should implement the extraction of the classes out of its container.
	 *
	 * @throws IOException is thrown in case of reading error.
	 */
	abstract void read() throws IOException;

	/**
	 * Get all the discovered classes.
	 *
	 * @return a list of all the discovered classes.
	 */
	public List<C> getClasses() {
		return getClasses(Collections.<AntPatternMatcher>emptyList(), Collections.<AntPatternMatcher>emptyList());
	}

	/**
	 * Get a filtered list of classes. The patterns are filesystem based.
	 *
	 * @param includes the include patterns.
	 * @param excludes the exclude patterns.
	 * @return a filtered list.
	 */
	public List<C> getClasses(List<AntPatternMatcher> includes, List<AntPatternMatcher> excludes) {
		List<C> result = new ArrayList<C>();
		for (Entry<String, C> entry : this.classes.entrySet()) {
			if (shouldInclude(entry.getKey(), includes, excludes)) {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	/**
	 * Empty this class data cache.
	 */
	protected void clear() {
		this.classes.clear();
	}

	/**
	 * Add a new class definition to the cache.
	 *
	 * @param name    the filename
	 * @param classes the classes related to this filename.
	 */
	protected void put(String name, C clazz) {
		this.classes.put(name, clazz);
	}

	/**
	 * Should a path be included based on the patterns.
	 *
	 * @param subpath  the path to check
	 * @param includes the include patterns
	 * @param excludes the exclude patterns.
	 * @return Returns true if the if should be included into the list, false otherwise.
	 */
	protected boolean shouldInclude(String subpath, List<AntPatternMatcher> includes, List<AntPatternMatcher> excludes) {
		boolean included = includes.size() == 0 ? true : false;
		for (AntPatternMatcher inc : includes) {
			if (inc.matches(subpath)) {
				included = true;
				break;
			}
		}
		for (AntPatternMatcher exc : excludes) {
			if (exc.matches(subpath)) {
				return false;
			}
		}
		return included;
	}
}
