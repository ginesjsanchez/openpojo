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

import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.openpojo.business.BusinessIdentity;
import com.openpojo.business.annotation.BusinessKey;

/**
 * Package discovery rests solely on {@link ClassLoader#getResources(String)}, through {@link PackageLoader}. A manual
 * scan of java.class.path used to be merged in as well; it was dropped as unnecessary, and because it followed the
 * declared classpath rather than the real loading context (fat jars, module path, custom class loaders).
 *
 * @author oshoukry
 */
public final class Package {

	@BusinessKey
	private final String packageName;

	/**
	 * Creates the abstraction of a package from its name.
	 *
	 * @param packageName
	 *     The fully qualified package name.
	 */
	public Package(final String packageName) {
		this.packageName = packageName;
	}

	/**
	 * Name of the package represented.
	 *
	 * @return the fully qualified package name.
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Tells whether the package actually exists.
	 *
	 * @return {@code true} if the package exists somewhere on the classpath.
	 */
	public boolean isValid() {
		return !getPackageLoaders().isEmpty();
	}

	/**
	 * The classes held by this package, gathering every place on the classpath where it shows up.
	 *
	 * @return the types found.
	 */
	public Set<Type> getTypes() {
		Set<Type> types = new HashSet<>();
		for (PackageLoader packageLoader : getPackageLoaders()) {
			for (Type type : packageLoader.getTypes()) {
				types.add(type);
			}
		}
		return types;
	}

	/**
	 * Packages hanging directly off this one.
	 *
	 * @return the sub-packages found.
	 */
	public Set<Package> getSubPackages() {
		Set<Package> subPackages = new HashSet<>();
		Set<String> subPackageNames = new HashSet<>();
		for (PackageLoader packageLoader : getPackageLoaders()) {
			subPackageNames.addAll(packageLoader.getSubPackages());
		}

		for (String packageName : subPackageNames) {
			subPackages.add(new Package(packageName));
		}

		return subPackages;
	}

	private Set<PackageLoader> getPackageLoaders() {
		Set<PackageLoader> packageLoaders = new HashSet<>();

		Set<URL> resources = PackageLoader.getThreadResources(packageName);
		for (URL resource : resources) {
			packageLoaders.add(PackageLoader.getPackageLoaderByURL(resource, packageName));
		}

		return packageLoaders;
	}

	@Override
	public int hashCode() {
		return BusinessIdentity.getHashCode(this);
	}

	@Override
	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	public boolean equals(final Object obj) {
		return BusinessIdentity.areEqual(this, obj);
	}

	@Override
	public String toString() {
		return BusinessIdentity.toString(this);
	}

}
