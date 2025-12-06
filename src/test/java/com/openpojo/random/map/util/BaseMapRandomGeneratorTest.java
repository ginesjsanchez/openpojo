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

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.openpojo.random.ParameterizableRandomGenerator;
import com.openpojo.random.RandomFactory;
import com.openpojo.random.RandomGenerator;
import com.openpojo.random.exception.RandomGeneratorException;
import com.openpojo.random.map.support.ALeafChildClass;
import com.openpojo.random.map.support.SimpleType1;
import com.openpojo.random.map.support.SimpleType2;
import com.openpojo.random.util.SerializableComparableObject;
import com.openpojo.reflection.Parameterizable;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoMethod;
import com.openpojo.reflection.impl.PojoClassFactory;

/**
 * @author oshoukry
 */
public abstract class BaseMapRandomGeneratorTest {

	protected abstract ParameterizableRandomGenerator getInstance();

	protected abstract Class<? extends ParameterizableRandomGenerator> getGeneratorClass();

	protected abstract Class<? extends Map> getExpectedTypeClass();

	protected abstract Class<? extends Map> getGeneratedTypeClass();

	protected abstract Class<?> getGenericType1();

	protected abstract Class<?> getGenericType2();

	protected Class<?> getDefaultType1() {
		return SerializableComparableObject.class;
	}

	protected Class<?> getDefaultType2() {
		return SerializableComparableObject.class;
	}

	@Test
	public void constructorShouldBePrivate() {
		final Class<?> mapRandomGeneratorClass = getGeneratorClass();
		PojoClass mapRandomGeneratorPojo = PojoClassFactory.getPojoClass(mapRandomGeneratorClass);

		List<PojoMethod> constructors = new ArrayList<PojoMethod>();

		for (PojoMethod constructor : mapRandomGeneratorPojo.getPojoConstructors()) {
			if (!constructor.isSynthetic())
				constructors.add(constructor);
		}
		Assertions.assertEquals(1, constructors.size(),
				"Should only have one constructor [" + mapRandomGeneratorPojo.getPojoConstructors() + "]");

		PojoMethod constructor = constructors.get(0);

		Assertions.assertTrue(constructor.isPrivate());
	}

	@Test
	public void shouldBeAbleToCreate() {
		final RandomGenerator instance = getInstance();
		Assertions.assertNotNull(instance);
		Assertions.assertEquals(getGeneratorClass(), instance.getClass());
	}

	@Test
	public void shouldOnlyReturnMapClassFromGetTypes() {
		Collection<Class<?>> types = getInstance().getTypes();
		Assertions.assertNotNull(types, "Should not be null");
		Assertions.assertEquals(1, types.size(), "Should only have one type");
		Assertions.assertEquals(getExpectedTypeClass(), types.iterator().next(),
				"Should only be " + getExpectedTypeClass().getName());
	}

	@Test
	public void generatedTypeShouldBeAssignableToDeclaredType() {
		Class<?> declaredType = getInstance().getTypes().iterator().next();
		Object generatedInstance = getInstance().doGenerate(declaredType);
		Assertions.assertTrue(

				declaredType.isAssignableFrom(generatedInstance.getClass()),
				"[" + declaredType.getName() + " is not assignable to " + generatedInstance.getClass().getName() + "]");
	}

	@Test
	public void shouldThrowExceptionForDoGenerateForOtherThanMapClass() {
		try {
			getInstance().doGenerate(ALeafChildClass.class);
			fail("Exception expected");
		} catch (RandomGeneratorException e) {
		} catch (Exception e) {
			fail("Exception expected");
		}
	}

	@Test
	public void shouldThrowExceptionForDoGenerateForParameterizedOtherThanMapClass() {
		try {
			getInstance().doGenerate(new Parameterizable() {
				@Override
				public Class<?> getType() {
					return ALeafChildClass.class;
				}

				@Override
				public boolean isParameterized() {
					throw new IllegalStateException("Unimplemented!!");
				}

				@Override
				public List<Type> getParameterTypes() {
					throw new IllegalStateException("Unimplemented!!");
				}
			});
			fail("Exception expected");
		} catch (RandomGeneratorException e) {
		} catch (Exception e) {
			fail("Exception expected");
		}
	}

	@Test
	public void shouldGenerateCorrectTypeMapForRequestedMap() {
		Map someObject = (Map) getInstance().doGenerate(getExpectedTypeClass());
		Assertions.assertNotNull(someObject, "Should not be null");
		Assertions.assertEquals(getGeneratedTypeClass(), someObject.getClass(),
				"Should be a " + getGeneratedTypeClass().getName());
		Assertions.assertTrue(someObject.size() > 0, "Should not be Empty");
	}

	@Test
	@SuppressWarnings("unchecked")
	public void shouldGenerateParametrizableCorrectMapForRequest() {
		Map<SimpleType1, SimpleType2> mapOfType1AndType2 = (Map) getInstance().doGenerate(getParameterizedType());

		Assertions.assertNotNull(mapOfType1AndType2, "Should not be null");
		Assertions.assertTrue(mapOfType1AndType2.size() > 0, "Should not be empty");
		for (Map.Entry<?, ?> entry : mapOfType1AndType2.entrySet()) {
			Assertions.assertNotNull(entry, "Should not be null");
			Assertions.assertEquals(getGenericType1(), entry.getKey().getClass(),
					"Key should be " + getGenericType1().getName());
			Assertions.assertEquals(getGenericType2(), entry.getValue().getClass(),
					"Value be " + getGenericType2().getName());
		}
	}

	@Test
	public void endToEnd() {
		Map<?, ?> generatedMap = RandomFactory.getRandomValue(getExpectedTypeClass());
		assertMapHasExpectedTypes(generatedMap, getDefaultType1(), getDefaultType2());
	}

	protected void assertMapHasExpectedTypes(Map<?, ?> generatedMap, Class<?> type1, Class<?> type2) {
		Assertions.assertNotNull(generatedMap, "Should not be null");
		Assertions.assertEquals(getGeneratedTypeClass(), generatedMap.getClass());
		Assertions.assertTrue(generatedMap.size() > 0, "Should not be empty");
		for (Map.Entry<?, ?> entry : generatedMap.entrySet()) {
			Assertions.assertNotNull(entry.getKey(), "Should not be null");
			Assertions.assertEquals(type1, entry.getKey().getClass(), "Key should be " + type1.getName());
			Assertions.assertNotNull(entry.getValue(), "Should not be null");
			Assertions.assertEquals(type2, entry.getValue().getClass(), "Key should be " + type2.getName());
		}
	}

	@Test
	public void endToEndWithGenerics() {
		Map<?, ?> generatedMap = (Map) RandomFactory.getRandomValue(getParameterizedType());
		assertMapHasExpectedTypes(generatedMap, getGenericType1(), getGenericType2());
	}

	protected Parameterizable getParameterizedType() {
		return new Parameterizable() {
			private Type[] types = new Type[] { getGenericType1(), getGenericType2() };

			@Override
			public Class<?> getType() {
				return getExpectedTypeClass();
			}

			@Override
			public boolean isParameterized() {
				return true;
			}

			@Override
			public List<Type> getParameterTypes() {
				return Arrays.asList(types);
			}
		};
	}
}
