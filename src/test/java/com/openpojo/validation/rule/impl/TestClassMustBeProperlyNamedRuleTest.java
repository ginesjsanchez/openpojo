/*
 * Copyright (c) 2010-2019 Osman Shoukry
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

package com.openpojo.validation.rule.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestClassMustBeProperlyNamedRuleTest {

	@Test
	public void shouldThrowExceptionIfNoAnnotationsLoaded() {
		final String noneExistentClass = this.getClass().getName() + "DoesNotExist";

		IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class,
				() -> new TestClassMustBeProperlyNamedRule(getEmptyList(), getEmptyList(),
						Arrays.asList(noneExistentClass)));

		Assertions.assertEquals("No annotations loaded, expected any of [" + noneExistentClass + "]",
				thrown.getMessage());
	}

	@Test
	public void assertDefaultAnnotation() {
		List<String> defaults = Arrays.asList(TestClassMustBeProperlyNamedRule.DEFAULT_ANNOTATIONS);

		Assertions.assertTrue(defaults.contains("org.testng.annotations.Test"),
				"Missing org.testng.annotations.Test in " + defaults);
		Assertions.assertTrue(defaults.contains("org.junit.jupiter.api.Test"),
				"Missing org.junit.jupiter.api.Test in " + defaults);
	}

	private List<String> getEmptyList() {
		return Collections.<String>emptyList();
	}
}
