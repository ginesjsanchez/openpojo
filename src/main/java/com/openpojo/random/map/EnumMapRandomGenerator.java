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

package com.openpojo.random.map;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import com.openpojo.random.map.util.BaseMapRandomGenerator;
import com.openpojo.random.map.util.MapHelper;
import com.openpojo.random.util.Helper;
import com.openpojo.random.util.SerializableComparableObject;
import com.openpojo.random.util.SomeEnum;
import com.openpojo.reflection.Parameterizable;

/**
 * Generates an {@code EnumMap}. When the real enum is unknown it uses {@code SomeEnum}; when the type carries
 * parameters, it builds the map with the enum given.
 *
 * @author oshoukry
 */
public class EnumMapRandomGenerator extends BaseMapRandomGenerator {
	private static final Class<?>[] TYPES = new Class<?>[]{EnumMap.class};
	private static final EnumMapRandomGenerator INSTANCE = new EnumMapRandomGenerator();

	/**
	 * Returns the single instance of this class; it is the one that gets registered and the one reused on every
	 * request.
	 *
	 * @return the shared generator.
	 */
	public static EnumMapRandomGenerator getInstance() {
		return INSTANCE;
	}

	public Collection<Class<?>> getTypes() {
		return Arrays.asList(TYPES);
	}

	@Override
	protected Map<Object, Object> getBasicInstance(Class<?> type) {
		Helper.assertIsAssignableTo(type, getTypes());
		return MapHelper.buildMap(MapHelper.asMap(new EnumMap<>(SomeEnum.class)), SomeEnum.class,
				SerializableComparableObject.class);
	}

	@Override
	public Map<Object, Object> doGenerate(Parameterizable parameterizedType) {
		Helper.assertIsAssignableTo(parameterizedType.getType(), getTypes());

		Class<?> type = (Class<?>) parameterizedType.getParameterTypes().get(0);
		Map<Object, Object> returnedMap = MapHelper.asMap(newEnumMap(type));
		return MapHelper.buildMap(returnedMap, parameterizedType.getParameterTypes().get(0),
				parameterizedType.getParameterTypes().get(1));
	}

	/**
	 * EnumMap bounds its key with {@code K extends Enum<K>}, a self-referential bound that a wildcard cannot express.
	 * This generic method captures that type variable so the map can be built from a {@code Class<?>} obtained at
	 * runtime.
	 */
	private static <K extends Enum<K>> EnumMap<K, Object> newEnumMap(Class<?> keyType) {
		@SuppressWarnings("unchecked")
		final Class<K> enumType = (Class<K>) keyType;
		return new EnumMap<>(enumType);
	}

	private EnumMapRandomGenerator() {
	}

}
