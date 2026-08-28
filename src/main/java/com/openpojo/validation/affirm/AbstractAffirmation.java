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

import java.lang.reflect.Array;

/**
 * Base for the {@code Affirmation} implementations, holding the array and reference comparisons they all share.
 *
 * @author oshoukry
 */
public abstract class AbstractAffirmation implements Affirmation {

	/**
	 * Compares two arrays element by element and fails on the first one that differs.
	 *
	 * @param message
	 *     Text to show if it fails.
	 * @param expected
	 *     The expected array.
	 * @param actual
	 *     The actual array.
	 */
	public void affirmArrayEquals(String message, Object expected, Object actual) {
		Integer expectedLength = Array.getLength(expected);
		affirmEquals(message + " : Arrays are not the same length", expectedLength,
				actual == null ? null : Array.getLength(actual));

		for (int i = 0; i < expectedLength; i++) {
			Object expectedArrayElement = Array.get(expected, i);
			Object actualArrayElement = Array.get(actual, i);
			try {
				affirmEquals(message, actualArrayElement, expectedArrayElement);
			} catch (AssertionError ae) {
				fail("Array element mismatch value at index [" + i + "] :" + ae.getMessage());
			}
		}
	}

	/**
	 * Tells whether the object is an array.
	 *
	 * @param object
	 *     The object to inspect.
	 * @return {@code true} if it is.
	 */
	public boolean isArray(Object object) {
		return object != null && object.getClass().isArray();
	}

	/**
	 * Compares by reference, treating two nulls as equal.
	 *
	 * @param expected
	 *     The first reference.
	 * @param actual
	 *     The second reference.
	 * @return {@code true} if they are the same reference, or both null.
	 */
	public boolean objectPointersAreTheSame(Object expected, Object actual) {
		return (expected == null && actual == null) || (expected == actual);
	}

	/**
	 * Constructor reachable only from subclasses.
	 */
	protected AbstractAffirmation() {
	}
}
