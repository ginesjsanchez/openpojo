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

package com.openpojo.business.cache;

/**
 * Metadata of a field annotated with {@code @BusinessKey}: whether it is part of a composite key, whether it is case
 * sensitive and whether it is required.
 *
 * @author oshoukry
 */
public interface BusinessKeyField {
	/**
	 * Tells whether the key is composite.
	 *
	 * @return {@code true} if the field is part of a composite key.
	 */
	boolean isComposite();

	/**
	 * Tells whether the comparison is case sensitive.
	 *
	 * @return {@code true} if text comparison is case sensitive.
	 */
	boolean isCaseSensitive();

	/**
	 * Tells whether the field is required.
	 *
	 * @return {@code true} if the field cannot be null.
	 */
	boolean isRequired();

	/**
	 * Reads the value of the field on the given instance.
	 *
	 * @param instance
	 *     The object to read from.
	 * @return the value held by the field.
	 */
	Object get(Object instance);

	/**
	 * Tells whether the field is an array.
	 *
	 * @return {@code true} if the field is an array.
	 */
	boolean isArray();
}
