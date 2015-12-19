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

import com.googlecode.japi.checker.model.Scope;

import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.ZipFile;

public final class Utils {

	private Utils() {
	}

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

	/**
	 * Joins the elements of the provided <code>Iterable</code> into a single String containing the provided elements.
	 *
	 * @return the String containing provided elements
	 */
	public static String join(Iterable<?> iterable, String separator) {
		StringBuffer buffer = new StringBuffer();
		Iterator<?> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			buffer.append(iterator.next());
			if (iterator.hasNext()) {
				buffer.append(separator);
			}
		}
		return buffer.toString();
	}

	public static boolean isArchive(File file) {
		ZipFile zf = null;
		try {
			zf = new ZipFile(file);
			zf.entries(); // forcing to do something with the file.
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			if (zf != null) {
				try {
					zf.close();
				} catch (IOException e) {
					// swallow the exception...
				}
			}
		}
	}

	public static Scope toScope(int access) {
		if ((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) {
			return Scope.PRIVATE;
		} else if ((access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED) {
			return Scope.PROTECTED;
		}
		if ((access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC) {
			return Scope.PUBLIC;
		} else {
			return Scope.PACKAGE;
		}
	}
}
