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

package com.openpojo.reflection.java.packageloader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openpojo.business.BusinessIdentity;
import com.openpojo.business.annotation.BusinessKey;
import com.openpojo.reflection.exception.ReflectionException;
import com.openpojo.reflection.java.Java;
import com.openpojo.reflection.java.packageloader.impl.FilePackageLoader;
import com.openpojo.reflection.java.packageloader.impl.JARPackageLoader;

/**
 * Enumerates the classes and sub-packages of a package. Which concrete subclass is used depends on where the package
 * lives: on the file system or inside a jar.
 *
 * @author oshoukry
 */
public abstract class PackageLoader {

	/**
	 * Log of the concrete subclass.
	 */
	protected final Logger logger;

	/**
	 * Resource the classes are read from: a directory or a jar.
	 */
	@BusinessKey
	protected final URL packageURL;

	/**
	 * Name of the package being loaded.
	 */
	@BusinessKey
	protected final String packageName;

	/**
	 * Creates a loader for a package located in a specific resource.
	 *
	 * @param packageURL
	 *     The resource the package lives in.
	 * @param packageName
	 *     The fully qualified package name.
	 */
	public PackageLoader(final URL packageURL, final String packageName) {
		this.packageURL = packageURL;
		this.packageName = packageName;
		logger = LoggerFactory.getLogger(this.getClass());
	}

	/**
	 * The classes held by the loaded package.
	 *
	 * @return the types found, loaded but not initialized.
	 */
	public abstract Set<Type> getTypes();

	/**
	 * Names of the packages hanging directly off the loaded package.
	 *
	 * @return the sub-packages found.
	 */
	public abstract Set<String> getSubPackages();

	/**
	 * Asks the class loader of the current thread for every resource answering to a path. The same package may show
	 * up in several places on the classpath.
	 *
	 * @param path
	 *     The package name, in package notation.
	 * @return the URLs where that package shows up.
	 */
	public static Set<URL> getThreadResources(final String path) {
		String normalizedPath = fromJDKPackageToJDKPath(path);
		Enumeration<URL> urls;
		try {
			urls = getThreadClassLoader().getResources(normalizedPath);
		} catch (IOException e) {
			throw ReflectionException.getInstance("Failed to getThreadResources for path[" + path + "]", e);
		}
		Set<URL> returnURLs = new HashSet<>();
		while (urls.hasMoreElements()) {
			returnURLs.add(urls.nextElement());
		}
		return returnURLs;
	}

	/**
	 * Picks the right subclass according to the URL protocol: file system or jar.
	 *
	 * @param packageURL
	 *     The resource the package lives in.
	 * @param packageName
	 *     The fully qualified package name.
	 * @return the loader able to read that resource.
	 */
	protected static PackageLoader getPackageLoaderByURL(final URL packageURL, final String packageName) {
		if (packageURL.getProtocol().equalsIgnoreCase("jar")) {
			return new JARPackageLoader(packageURL, packageName);
		}
		if (packageURL.getProtocol().equalsIgnoreCase("file")) {
			return new FilePackageLoader(packageURL, packageName);
		}
		throw new IllegalArgumentException("Unknown package loader protocol: " + packageURL.getProtocol());
	}

	private static ClassLoader getThreadClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	private static String fromJDKPackageToJDKPath(final String path) {
		return path.replace(Java.PACKAGE_DELIMITER, Java.PATH_DELIMITER);
	}

	@Override
	public String toString() {
		return BusinessIdentity.toString(this);
	}
}
