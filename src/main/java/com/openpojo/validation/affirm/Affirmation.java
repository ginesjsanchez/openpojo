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

/**
 * Abstraction over the assertions of whichever test framework is present, so that openpojo fails the way that
 * framework expects.
 *
 * @author oshoukry
 */
public interface Affirmation {
	/**
	 * Always fails, with the given message.
	 *
	 * @param message
	 *     Text to show.
	 */
	void fail(final String message);

	/**
	 * Fails if the condition is false.
	 *
	 * @param message
	 *     Text to show if it fails.
	 * @param condition
	 *     The condition expected to be true.
	 */
	void affirmTrue(final String message, final boolean condition);

	/**
	 * Fails if the condition is true.
	 *
	 * @param message
	 *     Text to show if it fails.
	 * @param condition
	 *     The condition expected to be false.
	 */
	void affirmFalse(final String message, final boolean condition);

	/**
	 * Fails if the object is {@code null}.
	 *
	 * @param message
	 *     Text to show if it fails.
	 * @param object
	 *     The object that must not be null.
	 */
	void affirmNotNull(final String message, final Object object);

	/**
	 * Fails if the object is not {@code null}.
	 *
	 * @param message
	 *     Text to show if it fails.
	 * @param object
	 *     The object that must be null.
	 */
	void affirmNull(final String message, final Object object);

	/**
	 * Fails if the two values are not equal.
	 *
	 * @param message
	 *     Text to show if it fails.
	 * @param expected
	 *     The expected value.
	 * @param actual
	 *     The actual value.
	 */
	void affirmEquals(final String message, final Object expected, final Object actual);

	/**
	 * Fails if they are not the same instance, comparing by reference.
	 *
	 * @param message
	 *     Text to show if it fails.
	 * @param first
	 *     The first reference.
	 * @param second
	 *     The second reference.
	 */
	void affirmSame(final String message, final Object first, final Object second);
}
