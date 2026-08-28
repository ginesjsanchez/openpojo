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

package com.openpojo.random.map.util;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import com.openpojo.random.RandomFactory;
import com.openpojo.reflection.impl.ParameterizableFactory;

/**
 * This Helper class populates the randomly generated map with some random elements.<br>
 * It is configured to generate anywhere between 1 - 5 elements in the map.
 *
 * @author oshoukry
 */
public class MapHelper {

	private static final Random RANDOM = new Random(new Date().getTime());
	private static final int MAX_RANDOM_ELEMENTS = 5;

	/**
	 * Empties the map and fills it with between 1 and 5 random entries of the given types. If the map or either type
	 * is missing, it is returned untouched.
	 *
	 * @param map
	 *     The map to populate.
	 * @param key
	 *     The key type.
	 * @param value
	 *     The value type.
	 * @return the same map, now populated.
	 */
	public static Map<Object, Object> buildMap(Map<Object, Object> map, Type key, Type value) {
		if (key == null || value == null || map == null)
			return map;

		int counter = RANDOM.nextInt(MAX_RANDOM_ELEMENTS) + 1;
		map.clear();

		while (counter-- > 0) {
			Object nextKey = RandomFactory.getRandomValue(ParameterizableFactory.getInstance(key));
			Object nextValue = RandomFactory.getRandomValue(ParameterizableFactory.getInstance(value));
			map.put(nextKey, nextValue);
		}
		return map;
	}

	/**
	 * The map counterpart of CollectionHelper.asCollection: EnumMap bounds its key to the enum and cannot be declared
	 * as {@code Map<Object, Object>}.
	 *
	 * @param map
	 *     The map to reinterpret.
	 * @return the same map, seen as a map from {@code Object} to {@code Object}.
	 */
	@SuppressWarnings("unchecked")
	public static Map<Object, Object> asMap(Map<?, ?> map) {
		return (Map<Object, Object>) map;
	}

	private MapHelper() {
		throw new UnsupportedOperationException(MapHelper.class.getName() + " should not be constructed!");
	}
}
