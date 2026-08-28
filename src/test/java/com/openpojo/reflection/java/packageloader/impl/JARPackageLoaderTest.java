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

package com.openpojo.reflection.java.packageloader.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.openpojo.reflection.java.Java;
import com.openpojo.validation.affirm.Affirm;

/**
 * Checks that JARPackageLoader can enumerate classes and sub-packages inside a jar.
 * Any jar on the test classpath will do; the logback one is used here.
 * <p>
 * Assertions are by containment rather than by exact count on purpose: the previous
 * version compared against a fixed class count from another library, so it broke on
 * every version bump of that library rather than when JARPackageLoader was wrong.
 *
 * @author oshoukry
 */
public class JARPackageLoaderTest {
	private final String packageName = "ch.qos.logback.classic";

	private final String[] expectedSubPackagesNames = new String[]{packageName + ".spi", packageName + ".joran",
			packageName + ".util", packageName + ".pattern"};

	private final String[] expectedClassesNames = new String[]{packageName + ".Logger", packageName + ".Level",
			packageName + ".LoggerContext", packageName + ".PatternLayout"};

	@Test
	public final void shouldGetJarSubPackages() {
		JARPackageLoader jarPackage = getJarPackageLoader(packageName);

		Set<String> subPackagesNames = jarPackage.getSubPackages();

		Affirm.affirmTrue("No sub-packages found under [" + packageName + "]", !subPackagesNames.isEmpty());

		for (String expectedPackageName : expectedSubPackagesNames) {
			Affirm.affirmTrue("Expected package[" + expectedPackageName + "] not found in " + subPackagesNames,
					subPackagesNames.contains(expectedPackageName));
		}
	}

	@Test
	public final void shouldGetJarSubClasses() {
		JARPackageLoader jarPackage = getJarPackageLoader(packageName);

		Set<String> classesNames = new LinkedHashSet<>();
		for (Type type : jarPackage.getTypes()) {
			classesNames.add(((Class<?>) type).getName());
		}

		Affirm.affirmTrue("No classes found in [" + packageName + "]", !classesNames.isEmpty());

		for (String expectedClassName : expectedClassesNames) {
			Affirm.affirmTrue("Expected class[" + expectedClassName + "] not found",
					classesNames.contains(expectedClassName));
		}
	}

	private JARPackageLoader getJarPackageLoader(final String packageName) {
		Enumeration<URL> resources = null;
		try {
			resources = Thread.currentThread().getContextClassLoader()
					.getResources(packageName.replace(Java.PACKAGE_DELIMITER, Java.PATH_DELIMITER));
		} catch (IOException e) {
			Affirm.fail("Failed to get resources for package[" + packageName + "] got exception[" + e + "]");
		}
		URL resource = resources.nextElement();

		Affirm.affirmEquals("[" + packageName + "] not located in a jar file!!", "jar", resource.getProtocol());

		return new JARPackageLoader(resource, packageName);
	}

}
