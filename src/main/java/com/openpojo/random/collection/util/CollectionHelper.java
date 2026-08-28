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

package com.openpojo.random.collection.util;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

import com.openpojo.random.RandomFactory;
import com.openpojo.reflection.impl.ParameterizableFactory;

/**
 * This Helper class populates the randomly generated collection with some
 * random elements.<br>
 * It is configured to generate anywhere between 0 - 10 elements in the
 * collection.
 *
 * @author oshoukry
 */
public class CollectionHelper {

	private static final Random RANDOM = new Random(new Date().getTime());
	/**
	 * Maximum number of elements generated in each collection.
	 */
	public static final int MAX_RANDOM_ELEMENTS = 5;

	/**
	 * Empties the collection and fills it with between 1 and {@link #MAX_RANDOM_ELEMENTS} random elements of the
	 * given type. If the collection or the type is missing, it is returned untouched.
	 *
	 * @param collection
	 *     The collection to populate.
	 * @param type
	 *     The type of the elements to generate.
	 * @return the same collection, now populated.
	 */
	public static Collection<Object> buildCollections(Collection<Object> collection, Type type) {
		if (type == null || collection == null)
			return collection;

		int counter = RANDOM.nextInt(MAX_RANDOM_ELEMENTS) + 1;

		collection.clear();
		while (counter-- > 0) {
			collection.add(RandomFactory.getRandomValue(ParameterizableFactory.getInstance(type)));
		}
		return collection;
	}

	/**
	 * The generators create the empty collection and this helper populates it, so its real element type is Object. A
	 * few types cannot be declared that way: EnumSet pins its element type, DelayQueue bounds it to Delayed, and the
	 * instances coming from InstanceFactory arrive as Object. The unchecked conversion is concentrated here instead
	 * of being repeated in each of those generators.
	 *
	 * @param collection
	 *     The collection to reinterpret.
	 * @return the same collection, seen as a collection of {@code Object}.
	 */
	@SuppressWarnings("unchecked")
	public static Collection<Object> asCollection(Collection<?> collection) {
		return (Collection<Object>) collection;
	}

	private CollectionHelper() {
		throw new UnsupportedOperationException(CollectionHelper.class.getName() + " should not be constructed!");
	}
}
