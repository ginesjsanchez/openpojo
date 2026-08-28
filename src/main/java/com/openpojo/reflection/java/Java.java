/*
 * Copyright (c) 2010-2018 Osman Shoukry
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.openpojo.reflection.java;

import java.io.File;

/**
 * Constants of the Java naming and path format: separators, class extension and the name of the package descriptor.
 *
 * @author oshoukry
 */
public final class Java {
	/**
	 * Separator between the segments of a package name.
	 */
	public static final char PACKAGE_DELIMITER = '.';
	/**
	 * Path separator inside a jar and in resources.
	 */
	public static final char PATH_DELIMITER = '/';
	/**
	 * Separates the jar from the inner path in a {@code jar:} URL.
	 */
	public static final char JAR_FILE_PATH_SEPARATOR = '!';
	/**
	 * Extension of bytecode files.
	 */
	public static final String CLASS_EXTENSION = ".class";
	/**
	 * Name of the file that documents a package.
	 */
	public static final String PACKAGE_INFO = "package-info";
	/**
	 * Separator between classpath entries, platform dependent.
	 */
	public static final String CLASSPATH_DELIMITER = File.pathSeparator;

	private Java() {
		throw new UnsupportedOperationException(Java.class.getName() + " should not be constructed!");
	}
}
