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

package com.openpojo.validation.utils;

/**
 * Invokes {@code toString()} safely: if the implementation fails, it returns the error text instead of propagating
 * the exception.
 *
 * @author oshoukry
 */
public final class ToStringHelper {
	/**
	 * Invokes {@code toString()} without letting a failure in the implementation break the validation.
	 *
	 * @param o
	 *     The object to render.
	 * @return its text, or the error description if {@code toString()} fails.
	 */
	public static String safeToString(Object o) {
		try {
			return "" + o;
		} catch (Exception e) {
			return "Error calling toString: '" + e.toString() + "'";
		}
	}

	private ToStringHelper() {
		throw new UnsupportedOperationException(ToStringHelper.class.getName() + " should not be constructed!");
	}
}
