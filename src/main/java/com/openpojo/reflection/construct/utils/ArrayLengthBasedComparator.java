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

package com.openpojo.reflection.construct.utils;

/**
 * Compares two parameter type lists by length. Used to pick a constructor when looking for the most complete or the
 * simplest one.
 *
 * @author oshoukry
 */
public abstract class ArrayLengthBasedComparator {
	/**
	 * Compares two parameter lists by length.
	 *
	 * @param leftHand
	 *     The first list.
	 * @param rightHand
	 *     The second list.
	 * @return {@code true} if the first one wins under the subclass criterion.
	 */
	public abstract boolean compare(final Class<?>[] leftHand, final Class<?>[] rightHand);

	/**
	 * Length of the array, treating null as -1 so it always loses.
	 *
	 * @param array
	 *     The array to measure.
	 * @return its length, or -1 if it is null.
	 */
	protected int getLength(final Object[] array) {
		return array == null ? -1 : array.length;
	}

	/**
	 * Constructor reachable only from subclasses.
	 */
	protected ArrayLengthBasedComparator() {
	}
}
