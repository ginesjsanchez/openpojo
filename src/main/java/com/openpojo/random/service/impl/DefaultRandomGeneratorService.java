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

package com.openpojo.random.service.impl;

import java.util.*;

import com.openpojo.random.ParameterizableRandomGenerator;
import com.openpojo.random.RandomGenerator;
import com.openpojo.random.service.RandomGeneratorService;
import com.openpojo.reflection.Parameterizable;

/**
 * Registry of generators. It works out which generator serves each type; if none covers it exactly, it looks for one
 * registered for an assignable type and adapts it, and failing that it falls back on the default generator.
 *
 * @author oshoukry
 */
public class DefaultRandomGeneratorService implements RandomGeneratorService {
	private final Map<Class<?>, RandomGenerator> concreteRandomGenerator = new HashMap<>();
	private final Map<Class<?>, ParameterizableRandomGenerator> concreteParameterizableRandomGenerators = new HashMap<>();
	private RandomGenerator defaultRandomGenerator;
	private static final Random RANDOM = new Random(new Date().getTime());

	public String getName() {
		return this.getClass().getName();
	}

	public void registerRandomGenerator(final RandomGenerator randomGenerator) {
		for (final Class<?> type : randomGenerator.getTypes()) {
			concreteRandomGenerator.put(type, randomGenerator);
			if (randomGenerator instanceof ParameterizableRandomGenerator)
				concreteParameterizableRandomGenerators.put(type, (ParameterizableRandomGenerator) randomGenerator);
		}
	}

	public void setDefaultRandomGenerator(final RandomGenerator randomGenerator) {
		defaultRandomGenerator = randomGenerator;
	}

	public RandomGenerator getDefaultRandomGenerator() {
		return defaultRandomGenerator;
	}

	public RandomGenerator getRandomGeneratorByType(final Class<?> type) {
		return getAppropriateRandomGenerator(type, concreteRandomGenerator);
	}

	public RandomGenerator getRandomGeneratorByParameterizable(Parameterizable type) {
		if (!type.isParameterized())
			return getAppropriateRandomGenerator(type.getType(), concreteRandomGenerator);

		return getAppropriateRandomGenerator(type.getType(), concreteParameterizableRandomGenerators);
	}

	private RandomGenerator getAppropriateRandomGenerator(Class<?> type,
			Map<Class<?>, ? extends RandomGenerator> randomGenerators) {
		RandomGenerator appropriateRandomGenerator = randomGenerators.get(type);
		if (appropriateRandomGenerator == null) {
			final List<Class<?>> assignableTypes = getAssignableTypesForType(type, randomGenerators.keySet());
			if (assignableTypes.isEmpty()) {
				appropriateRandomGenerator = getDefaultRandomGenerator();
			} else {
				final Class<?> adaptToType = assignableTypes.get(RANDOM.nextInt(assignableTypes.size()));
				appropriateRandomGenerator = new RandomGeneratorAdapter(type, adaptToType,
						randomGenerators.get(adaptToType));
			}
		}
		return appropriateRandomGenerator;
	}

	public Collection<Class<?>> getRegisteredTypes() {
		return Collections.unmodifiableSet(concreteRandomGenerator.keySet());
	}

	private List<Class<?>> getAssignableTypesForType(final Class<?> type, final Set<Class<?>> registeredTypes) {
		final List<Class<?>> assignableTypes = new LinkedList<>();
		for (final Class<?> registeredType : registeredTypes) {
			if (type.isAssignableFrom(registeredType)) {
				assignableTypes.add(registeredType);
			}
		}
		return assignableTypes;
	}

	/**
	 * Creates an instance ready to use.
	 */
	public DefaultRandomGeneratorService() {
	}
}
