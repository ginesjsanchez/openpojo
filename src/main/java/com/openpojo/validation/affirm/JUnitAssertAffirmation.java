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

package com.openpojo.validation.affirm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.openpojo.business.BusinessIdentity;
import com.openpojo.reflection.exception.ReflectionException;
import com.openpojo.reflection.java.load.ClassUtil;

/**
 * @author oshoukry
 */
public class JUnitAssertAffirmation extends AbstractAffirmation implements Affirmation {
	static {
		if (!ClassUtil.isClassLoaded("org.junit.jupiter.api.Assertions"))
			throw ReflectionException.getInstance("org.junit.jupiter.api.Assertions class not found");
	}

	private JUnitAssertAffirmation() {
	}

	@Override
	public void fail(final String message) {
		org.junit.jupiter.api.Assertions.fail(message);
	}

	@Override
	public void affirmTrue(final String message, final boolean condition) {
		assertTrue(condition, message);
	}

	@Override
	public void affirmFalse(final String message, final boolean condition) {
		assertFalse(condition, message);
	}

	@Override
	public void affirmNotNull(final String message, final Object object) {
		assertNotNull(object, message);
	}

	@Override
	public void affirmNull(final String message, final Object object) {
		assertNull(object, message);
	}

	@Override
	public void affirmEquals(final String message, final Object expected, final Object actual) {
		if (objectPointersAreTheSame(expected, actual))
			return;

		if (isArray(expected)) {
			affirmArrayEquals(message, expected, actual);
		} else {
			assertEquals(expected, actual, message);
		}
	}

	@Override
	public void affirmSame(String message, Object first, Object second) {
		assertSame(first, second, message);
	}

	@Override
	public String toString() {
		return BusinessIdentity.toString(this);
	}
}
