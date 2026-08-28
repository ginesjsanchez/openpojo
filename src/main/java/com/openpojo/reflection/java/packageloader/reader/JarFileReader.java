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

package com.openpojo.reflection.java.packageloader.reader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.openpojo.reflection.exception.ReflectionException;
import com.openpojo.reflection.java.Java;
import com.openpojo.reflection.java.packageloader.impl.URLToFileSystemAdapter;
import com.openpojo.reflection.java.packageloader.utils.Helper;

import static com.openpojo.reflection.java.packageloader.utils.Helper.getFQClassName;
import static com.openpojo.reflection.java.packageloader.utils.Helper.isClass;

/**
 * This is a facade that simplifies reading classes out of a JarFile. This class
 * is also lazy loading classes upon demand, and will not initialize any of the
 * loaded classes.
 *
 * @author oshoukry
 */
public class JarFileReader {

	private JarFile jarFile = null;
	private Set<String> classNames;

	private JarFileReader(String jarFilePath) {
		try {
			jarFile = new JarFile(jarFilePath, true);
			initClassNames();
		} catch (Exception e) {
		}
	}

	private JarFileReader(URL jarURL) {
		try {
			jarFile = ((JarURLConnection) jarURL.openConnection()).getJarFile();
			initClassNames();
		} catch (Exception e) {
		}
	}

	/**
	 * Creates a reader for the jar at the given path. If the path is not a readable jar, the returned reader will not
	 * be valid instead of failing.
	 *
	 * @param jarFilePath
	 *     The path to the jar file.
	 * @return the reader for that jar.
	 */
	public static JarFileReader getInstance(String jarFilePath) {
		return new JarFileReader(jarFilePath);
	}

	/**
	 * Creates a reader from a {@code jar:} URL, of the kind the class loader returns. If it cannot be opened, the
	 * returned reader will not be valid.
	 *
	 * @param jarURL
	 *     URL pointing at the jar.
	 * @return the reader for that jar.
	 */
	public static JarFileReader getInstance(URL jarURL) {
		return new JarFileReader(jarURL);
	}

	/**
	 * Tells whether the jar could be opened. The constructor does not propagate open failures, so this is how to know
	 * whether the reader is of any use.
	 *
	 * @return {@code true} if the jar was opened successfully.
	 */
	public boolean isValid() {
		return jarFile != null;
	}

	/**
	 * The entries of the main section of the jar manifest.
	 *
	 * @return the manifest entries, with keys and values as text.
	 */
	public Map<String, String> getManifestEntries() {
		Map<String, String> manifestEntries = new HashMap<>();
		Manifest manifest;
		try {
			manifest = jarFile.getManifest();
		} catch (IOException e) {
			throw ReflectionException.getInstance("Failed to load Manifest-File for: " + jarFile.getName(), e);
		}

		Attributes mainAttributes = manifest.getMainAttributes();

		for (Attributes.Entry<Object, Object> entry : mainAttributes.entrySet()) {
			String key = entry.getKey() == null ? "null" : entry.getKey().toString();
			String value = entry.getValue() == null ? "null" : entry.getValue().toString();
			manifestEntries.put(key, value);
		}
		return manifestEntries;
	}

	/**
	 * Value of a specific manifest entry.
	 *
	 * @param name
	 *     The entry name.
	 * @return its value, or {@code null} if it is absent.
	 */
	public String getManifestEntry(String name) {
		return getManifestEntries().get(name);
	}

	private Set<String> getAllEntries() {
		Set<String> entries = new HashSet<>();

		ArrayList<JarEntry> jarEntries = Collections.list(jarFile.entries());
		for (JarEntry entry : jarEntries)
			entries.add(entry.getName());

		return entries;
	}

	/**
	 * The classes of the given package held by this jar.
	 *
	 * @param packageName
	 *     The package to read.
	 * @return the types found.
	 */
	public Set<Type> getTypesInPackage(String packageName) {
		return Helper.loadClassesFromGivenPackage(classNames, packageName);
	}

	/**
	 * Packages hanging directly off the given one, inside this jar.
	 *
	 * @param packageName
	 *     The starting package.
	 * @return the names of the sub-packages.
	 */
	public Set<String> getSubPackagesOfPackage(String packageName) {
		return Helper.getSubPackagesOfPackage(classNames, packageName);
	}

	private void initClassNames() {
		classNames = new HashSet<>();
		for (String entry : getAllEntries()) {
			if (isClass(entry))
				classNames.add(getFQClassName(entry));
		}
		classNames = Collections.unmodifiableSet(classNames);
	}

	/**
	 * Fully qualified names of every class held by the jar.
	 *
	 * @return the class names found.
	 */
	public Set<String> getClassNames() {
		return classNames;
	}

	/**
	 * Extracts the jar path from a {@code jar:} URL, keeping whatever comes before the {@code !} separator.
	 *
	 * @param name
	 *     The path part of the URL.
	 * @return the path to the jar file.
	 */
	public static String getJarFileNameFromURLPath(String name) {
		String fileName = "";

		if (null != name && name.indexOf(Java.JAR_FILE_PATH_SEPARATOR) > 0) {
			fileName = name.substring(0, name.indexOf(Java.JAR_FILE_PATH_SEPARATOR));
			try {
				URLToFileSystemAdapter urlToFileSystemAdapter = new URLToFileSystemAdapter(new URI(fileName).toURL());
				fileName = urlToFileSystemAdapter.getAsFile().getAbsolutePath();
			} catch (MalformedURLException | URISyntaxException ignored) {
			}
		}

		return fileName;
	}
}
