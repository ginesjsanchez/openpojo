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

package com.openpojo.random.collection.queue;

import java.util.Arrays;
import java.util.Collection;
import java.util.PriorityQueue;

import com.openpojo.random.collection.util.BaseCollectionRandomGenerator;
import com.openpojo.random.util.Helper;

/**
 * Generates a {@code PriorityQueue} holding between 1 and 5 random elements.
 *
 * @author oshoukry
 */
public class PriorityQueueRandomGenerator extends BaseCollectionRandomGenerator {
	private final Class<?>[] TYPES = new Class<?>[]{PriorityQueue.class};
	/**
	 * Instancia unica compartida.
	 */
	public static final PriorityQueueRandomGenerator INSTANCE = new PriorityQueueRandomGenerator();

	/**
	 * Returns the single instance of this class; it is the one that gets registered and the one reused on every
	 * request.
	 *
	 * @return the shared generator.
	 */
	public static PriorityQueueRandomGenerator getInstance() {
		return INSTANCE;
	}

	@Override
	public Collection<Class<?>> getTypes() {
		return Arrays.asList(TYPES);
	}

	@Override
	protected Collection<Object> getBasicInstance(Class<?> type) {
		Helper.assertIsAssignableTo(type, getTypes());
		return new PriorityQueue<>();
	}

	private PriorityQueueRandomGenerator() {
	}
}
