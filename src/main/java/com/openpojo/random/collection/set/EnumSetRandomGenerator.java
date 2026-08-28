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

package com.openpojo.random.collection.set;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import com.openpojo.random.collection.util.BaseCollectionRandomGenerator;
import com.openpojo.random.collection.util.CollectionHelper;
import com.openpojo.random.util.Helper;
import com.openpojo.random.util.SomeEnum;
import com.openpojo.reflection.Parameterizable;

/**
 * Generates an {@code EnumSet} from random {@code SomeEnum} values, the filler enum openpojo uses when the real type
 * is unknown.
 *
 * @author oshoukry
 */
public class EnumSetRandomGenerator extends BaseCollectionRandomGenerator {
	private static final Random RANDOM = new Random(new Date().getTime());
	private static final Class<?>[] TYPES = new Class<?>[]{EnumSet.class};
	private static final EnumSetRandomGenerator INSTANCE = new EnumSetRandomGenerator();

	/**
	 * Returns the single instance of this class; it is the one that gets registered and the one reused on every
	 * request.
	 *
	 * @return the shared generator.
	 */
	public static EnumSetRandomGenerator getInstance() {
		return INSTANCE;
	}

	@Override
	public Collection<Class<?>> getTypes() {
		return Arrays.asList(TYPES);
	}

	@Override
	protected Collection<Object> getBasicInstance(Class<?> type) {
		Helper.assertIsAssignableTo(type, getTypes());
		List<SomeEnum> someEnums = new ArrayList<>();
		for (int i = 0; i < CollectionHelper.MAX_RANDOM_ELEMENTS; i++) {
			someEnums.add(SomeEnum.values()[RANDOM.nextInt(SomeEnum.values().length - 1)]);
		}

		return CollectionHelper.asCollection(EnumSet.copyOf(someEnums));
	}

	@Override
	public Collection<Object> doGenerate(Class<?> type) {
		return getBasicInstance(type);
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Collection<Object> doGenerate(Parameterizable parameterizedType) {
		Helper.assertIsAssignableTo(parameterizedType.getType(), getTypes());
		return EnumSet.allOf((Class) parameterizedType.getParameterTypes().get(0));
	}

	private EnumSetRandomGenerator() {
	}
}
