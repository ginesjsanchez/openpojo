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

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openpojo.random.ParameterizableRandomGenerator;
import com.openpojo.random.RandomGenerator;
import com.openpojo.random.exception.RandomGeneratorException;
import com.openpojo.reflection.Parameterizable;

/**
 * Wraps a generator registered for a type other than the one requested, when the requested type is assignable from
 * it. Used by {@code DefaultRandomGeneratorService}.
 *
 * @author oshoukry
 */
public final class RandomGeneratorAdapter implements RandomGenerator, ParameterizableRandomGenerator {

	private final Class<?> fromType;
	private final Class<?> toType;
	private final RandomGenerator adaptedRandomGenerator;
	private final static Logger LOGGER = LoggerFactory.getLogger(RandomGeneratorAdapter.class);

	/**
	 * Wraps a generator registered for another type, so it can serve the requested type.
	 *
	 * @param fromType
	 *     The type being asked for.
	 * @param toType
	 *     The type a generator is registered for.
	 * @param adaptedRandomGenerator
	 *     The generator to reuse.
	 */
	public RandomGeneratorAdapter(final Class<?> fromType, final Class<?> toType,
			final RandomGenerator adaptedRandomGenerator) {
		this.fromType = fromType;
		this.toType = toType;
		this.adaptedRandomGenerator = adaptedRandomGenerator;
		LOGGER.debug("Mapping [{}] to [{}] for generator [{}]", fromType, toType, adaptedRandomGenerator);
	}

	public Collection<Class<?>> getTypes() {
		throw RandomGeneratorException.getInstance("Illegal use of RandomGeneratorAdapter([" + fromType + "] to ["
				+ toType + "]");
	}

	public Object doGenerate(final Class<?> type) {
		if (type == fromType) {
			return adaptedRandomGenerator.doGenerate(toType);
		}
		throw RandomGeneratorException.getInstance("Unsupported type requested [" + type + "]");
	}

	public Object doGenerate(Parameterizable parameterizedType) {
		return ((ParameterizableRandomGenerator) adaptedRandomGenerator).doGenerate(parameterizedType);
	}
}
