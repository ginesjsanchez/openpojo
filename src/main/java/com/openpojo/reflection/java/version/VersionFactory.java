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

package com.openpojo.reflection.java.version;

import com.openpojo.reflection.java.version.impl.VersionImp;

/**
 * Creates {@code Version} instances from a string or from the manifest of a class.
 *
 * @author oshoukry
 */
public class VersionFactory {

	/**
	 * The version declared in the manifest of the jar where the given class lives.
	 *
	 * @param clazz
	 *     Any class from the jar.
	 * @return the version, or an empty one if it is not stated.
	 */
	public static Version getImplementationVersion(Class<?> clazz) {
		if (clazz != null)
			return getVersion(clazz.getPackage().getImplementationVersion());
		return new VersionImp(null);
	}

	/**
	 * Breaks a version string down.
	 *
	 * @param version
	 *     The version text.
	 * @return the resulting version.
	 */
	public static Version getVersion(String version) {
		return new VersionImp(version);
	}

	private VersionFactory() {
		throw new UnsupportedOperationException(VersionFactory.class.getName() + " should not be constructed!");

	}
}
