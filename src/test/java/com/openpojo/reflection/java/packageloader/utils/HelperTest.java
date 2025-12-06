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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.openpojo.random.RandomFactory;
import com.openpojo.reflection.java.Java;

/**
 * @author oshoukry
 */
public class HelperTest {

	@Test
	public void whenChildSubPath_thenSubPath() {
		String somePackage = "com.openpojo.parent";
		String subPackage = "com.openpojo.parent.subpath";

		//// Assertions.//assertThat(Helper.getDirectSubPackageName(somePackage,
		//// subPackage), is(subPackage));
	}

	@Test
	public void whenNonChildPath_thenNull() {
		String somePackage = "com.openpojo.parent";
		String subPackage = "com.openpojo.subpath";

		//// Assertions.//assertThat(Helper.getDirectSubPackageName(somePackage,
		//// subPackage), is(nullValue()));
	}

	@Test
	public void whenGrandChildPath_thenChildPath() {
		String somePackage = "com.openpojo.parent";
		String subPackage = "com.openpojo.parent.childpath";
		String grandChild = subPackage + ".grandChild";

		//// Assertions.//assertThat(Helper.getDirectSubPackageName(somePackage,
		//// grandChild), is(subPackage));
	}

	@Test
	public void whenNonChildPathButNameOverlap_thenNull() {
		String somePackage = "com.openpojo.parent";
		String subPackage = "com.openpojo.parentalso.childpath";
		String grandChild = subPackage + ".grandChild";

		//// Assertions.//assertThat(Helper.getDirectSubPackageName(somePackage,
		//// grandChild), is(nullValue()));
	}

	@Test
	public void returnsFalseWhenEntryIsNullAndIsClass() {
		Assertions.assertFalse(Helper.isClass(null));
	}

	@Test
	public void returnsFlaseIfNotEndsWithDotClass() {
		String someEntry = RandomFactory.getRandomValue(String.class);
		Assertions.assertFalse(Helper.isClass(someEntry));
	}

	@Test
	public void whenEndsWithDotClassIsClassIsTrue() {
		String someEntry = RandomFactory.getRandomValue(String.class) + Java.CLASS_EXTENSION;
		Assertions.assertTrue(Helper.isClass(someEntry));
	}

	@Test
	public void whenClassEndsWithDotClassAndGetFQClassName_ReturnsValidClassName() {
		String someEntry = RandomFactory.getRandomValue(String.class);
		Assertions.assertEquals(someEntry, Helper.getFQClassName(someEntry + Java.CLASS_EXTENSION));
	}

	@Test
	public void whenGetFQClassNameReplacePathDelimiters() {
		char slash = Java.PATH_DELIMITER;
		String someEntry = "com" + slash + "package" + slash + "className" + Java.CLASS_EXTENSION;
		String expected = "com.package.className";
		Assertions.assertEquals(expected, Helper.getFQClassName(someEntry));

	}
}