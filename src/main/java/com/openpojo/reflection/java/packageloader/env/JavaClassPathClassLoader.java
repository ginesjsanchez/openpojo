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

package com.openpojo.reflection.java.packageloader.env;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

import com.openpojo.log.LoggerFactory;
import com.openpojo.reflection.java.Java;
import com.openpojo.reflection.java.packageloader.reader.JarFileReader;
import com.openpojo.reflection.java.packageloader.utils.Helper;

/**
 * @author oshoukry
 */
public class JavaClassPathClassLoader {
	private static final String DEFAULT_CLASS_PATH_PROPERTY_NAMES[] = { "java.library.path", "java.class.path" };

	private static final JavaClassPathClassLoader INSTANCE = new JavaClassPathClassLoader();

	private final Set<String> classPathPropertyNames = new HashSet<String>();
	private Set<String> classNames = new HashSet<String>();

	private JavaClassPathClassLoader(String... propertyNames) {
		classPathPropertyNames.addAll(Arrays.asList(propertyNames));
		loadClassNames();
	}

	private JavaClassPathClassLoader() {
		this(DEFAULT_CLASS_PATH_PROPERTY_NAMES);
	}

	public static JavaClassPathClassLoader getInstance() {
		return INSTANCE;
	}

	public static JavaClassPathClassLoader getInstance(String... propertyNames) {
		return new JavaClassPathClassLoader(propertyNames);
	}

	public Set<Type> getTypesInPackage(String packageName) {
		return Helper.loadClassesFromGivenPackage(classNames, packageName);
	}

	public Set<String> getClassPathKeys() {
		return Collections.unmodifiableSet(classPathPropertyNames);
	}

	public Set<String> getClassNames() {
		return Collections.unmodifiableSet(classNames);
	}

	private void loadClassNames() {

		for (String name : classPathPropertyNames) {
			String envProperty = System.getProperty(name);
			if (envProperty != null) {
				loadClassNamesInPath(envProperty);
			} else
				LoggerFactory.getLogger(this.getClass()).warn("Failed to get value for environment variable: [{0}]",
						name);
		}
	}

	private void loadClassNamesInPath(String envProperty) {
		String path = getRelativePath(envProperty);
		String[] entries = path.split(Java.CLASSPATH_DELIMITER);
		for (String entry : entries) {
			LoggerFactory.getLogger(this.getClass()).info("Loading classes from: {0}", entry);

			JarFileReader jarFileReader = JarFileReader.getInstance(entry);
			if (jarFileReader.isValid())
				classNames.addAll(jarFileReader.getClassNames());
			else
				LoggerFactory.getLogger(this.getClass()).warn("Failed to load entries from: [{0}]", entry);
		}
	}

	public boolean hasPackage(String packageName) {
		for (String entry : classNames)
			if (entry.startsWith(packageName + Java.PACKAGE_DELIMITER))
				return true;
		return false;
	}

	public Set<String> getSubPackagesFor(String packageName) {
		return Helper.getSubPackagesOfPackage(classNames, packageName);
	}

	/**
	 * Normalize path.
	 *
	 * @param path the path
	 * @return the string
	 */
	private String normalizePath(String path) {
		String normalized = path;
		if (!org.apache.commons.lang3.StringUtils.isEmpty(path)) {
			String unixPath = FilenameUtils.separatorsToUnix(new File(path).getAbsolutePath());
			String[] parts = unixPath.split(":");
			normalized = parts[parts.length - 1];
			if (normalized.endsWith("/.")) {
				normalized = normalized.substring(0, normalized.length() - 2);
			}
		}
		return normalized;
	}

	/**
	 * Gets the relative path.
	 *
	 * @param absolutePath the absolute path
	 * @return the relative path
	 */
	private String getRelativePath(String absolutePath) {
		StringBuilder sb = new StringBuilder();
		sb.append('.');
		if (!org.apache.commons.lang3.StringUtils.isEmpty(absolutePath)) {
			File initialPath = new File(absolutePath);
			File currentDir = new File(".").getAbsoluteFile().getParentFile();
			while (Objects.nonNull(currentDir.getParentFile())) {
				sb.append("/..");
				currentDir = currentDir.getParentFile();
			}
			sb.append(normalizePath(initialPath.getAbsolutePath()));
		}
		return sb.toString();
	}

}