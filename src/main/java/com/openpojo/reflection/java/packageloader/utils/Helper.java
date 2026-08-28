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

package com.openpojo.reflection.java.packageloader.utils;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import com.openpojo.reflection.java.Java;
import com.openpojo.reflection.java.load.ClassUtil;

/**
 * Helpers over entry names: telling class entries apart, turning a path into a fully qualified name and filtering by
 * package.
 *
 * @author oshoukry
 */
public class Helper {

	/**
	 * Tells whether an entry corresponds to a class file.
	 *
	 * @param entry
	 *     The entry name.
	 * @return {@code true} if it ends with the class extension.
	 */
	public static boolean isClass(String entry) {
		return entry != null && entry.endsWith(Java.CLASS_EXTENSION);
	}

	/**
	 * Turns the path of an entry into a fully qualified class name.
	 *
	 * @param entry
	 *     The entry path, with slashes and extension.
	 * @return the matching class name.
	 */
	public static String getFQClassName(String entry) {
		String fullyQualifiedName = entry.substring(0, entry.length() - Java.CLASS_EXTENSION.length());
		return fullyQualifiedName.replace(Java.PATH_DELIMITER, Java.PACKAGE_DELIMITER);
	}

	/**
	 * Loads the classes from the list that belong to the given package, silently discarding those that cannot be
	 * loaded.
	 *
	 * @param classNames
	 *     Candidate class names.
	 * @param packageName
	 *     The package to filter by.
	 * @return the loaded types.
	 */
	public static Set<Type> loadClassesFromGivenPackage(Set<String> classNames, String packageName) {
		Set<Type> entries = new HashSet<>();
		for (String entry : classNames) {
			int endIndex = entry.lastIndexOf(Java.PACKAGE_DELIMITER);

			String entryPackageName = "";

			if (endIndex > 0)
				entryPackageName = entry.substring(0, endIndex);

			if (entryPackageName.equals(packageName)) {
				Type entryClass = ClassUtil.loadClass(entry, false);
				if (entryClass != null)
					entries.add(entryClass);
			}
		}
		return entries;
	}

	/**
	 * Works out the direct sub-packages from a list of class names.
	 *
	 * @param classNames
	 *     The class names to search through.
	 * @param packageName
	 *     The starting package.
	 * @return the sub-packages found.
	 */
	public static Set<String> getSubPackagesOfPackage(Set<String> classNames, String packageName) {
		Set<String> subPackages = new HashSet<>();
		for (String entry : classNames) {
			int endIndex = entry.lastIndexOf(Java.PACKAGE_DELIMITER);
			String typeClassPackageName;
			if (endIndex > 0) {
				typeClassPackageName = entry.substring(0, endIndex);
				String directSubPackageName = getDirectSubPackageName(packageName, typeClassPackageName);
				if (directSubPackageName != null)
					subPackages.add(directSubPackageName);
			}
		}
		return subPackages;
	}

	/**
	 * This method breaks up a package path into its elements returning the first sub-element only.
	 * For example, if packageName is "com" and the JAR file has only one class
	 * "com.openpojo.reflection.SomeClass.class", then the return will be set to "com.openpojo".
	 *
	 * @param parentPackageName
	 *     The reference package name.
	 * @param subPackageName
	 *     The subpackage name.
	 * @return A first sub level bellow packageName.
	 */
	static String getDirectSubPackageName(final String parentPackageName, final String subPackageName) {
		String parentPackageNameAsPath = "";

		if (parentPackageName != null && parentPackageName.length() > 0)
			parentPackageNameAsPath = parentPackageName + Java.PACKAGE_DELIMITER;

		if (subPackageName.startsWith(parentPackageNameAsPath)
				&& subPackageName.length() > parentPackageNameAsPath.length()) {
			String[] subPackageTokens;
			subPackageTokens = subPackageName.substring(parentPackageNameAsPath.length())
					.split("\\" + Java.PACKAGE_DELIMITER);
			if (subPackageTokens.length > 0) {
				return parentPackageNameAsPath + subPackageTokens[0];
			}
		}
		return null;
	}

	private Helper() {
		throw new UnsupportedOperationException(Helper.class.getName() + " should not be constructed!");
	}
}
