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

package com.openpojo.reflection.java.load;

/**
 * Loads classes by name without initializing them, and lets callers ask whether a class is available. Every optional
 * integration in openpojo rests on this.
 *
 * @author oshoukry
 */
public class ClassUtil {

	/**
	 * Tells whether a class can be loaded, without initializing it or propagating the failure. Every optional
	 * integration rests on this check.
	 *
	 * @param className
	 *     The fully qualified class name.
	 * @return {@code true} if the class is available.
	 */
	public static boolean isClassLoaded(String className) {
		return loadClass(className) != null;
	}

	/**
	 * Loads a class without initializing it.
	 *
	 * @param className
	 *     The fully qualified class name.
	 * @return the class, or {@code null} if it is not available.
	 */
	public static Class<?> loadClass(String className) {
		return loadClass(className, true);
	}

	/**
	 * Loads a class, choosing whether to initialize it.
	 *
	 * @param className
	 *     The fully qualified class name.
	 * @param initialize
	 *     {@code true} to run its static initializers.
	 * @return the class, or {@code null} if it is not available.
	 */
	public static Class<?> loadClass(String className, boolean initialize) {
		return loadClass(className, initialize, getThreadClassLoader());
	}

	/**
	 * Loads a class using a specific class loader.
	 *
	 * @param className
	 *     The fully qualified class name.
	 * @param initialize
	 *     {@code true} to run its static initializers.
	 * @param classloader
	 *     The class loader to use.
	 * @return the class, or {@code null} if it is not available.
	 */
	public static Class<?> loadClass(String className, boolean initialize, ClassLoader classloader) {
		try {
			return Class.forName(className, initialize, classloader);
		} catch (LinkageError linkageError) { // class depends on another that wasn't found.
		} catch (ClassNotFoundException classNotFoundException) { // no such class found.
		}
		return null;
	}

	private static ClassLoader getThreadClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	private ClassUtil() {
		throw new UnsupportedOperationException(ClassUtil.class.getName() + " should not be constructed!");
	}
}
