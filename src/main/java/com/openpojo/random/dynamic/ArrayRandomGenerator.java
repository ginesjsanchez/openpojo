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

package com.openpojo.random.dynamic;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.Random;

import com.openpojo.random.RandomFactory;

/**
 * Generates arrays: creates one of the requested component type and fills it by delegating to the generator matching
 * that component.
 *
 * @author oshoukry
 */
public class ArrayRandomGenerator {
	private static final Random RANDOM = new Random(new Date().getTime());
	private static final int MAX_RANDOM_ELEMENTS = 5;

	private ArrayRandomGenerator() {
	}

	/**
	 * Returns the single instance of this class; it is the one that gets registered and the one reused on every
	 * request.
	 *
	 * @return the shared generator.
	 */
	public static ArrayRandomGenerator getInstance() {
		return Instance.INSTANCE;
	}

	/**
	 * Creates an array of the requested component type and fills it with random values.
	 *
	 * @param type
	 *     The array type to generate.
	 * @return the generated array.
	 */
	public Object doGenerate(final Class<?> type) {
		final int count = RANDOM.nextInt(MAX_RANDOM_ELEMENTS) + 1;
		final Object arrayReturn = Array.newInstance(type.getComponentType(), count);
		for (int i = 0; i < count; i++) {
			Array.set(arrayReturn, i, RandomFactory.getRandomValue(type.getComponentType()));
		}

		return arrayReturn;
	}

	private static class Instance {
		private static final ArrayRandomGenerator INSTANCE = new ArrayRandomGenerator();
	}

}
