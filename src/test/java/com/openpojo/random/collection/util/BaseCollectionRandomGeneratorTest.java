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

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.openpojo.random.ParameterizableRandomGenerator;
import com.openpojo.random.RandomFactory;
import com.openpojo.random.RandomGenerator;
import com.openpojo.random.collection.support.ALeafChildClass;
import com.openpojo.random.exception.RandomGeneratorException;
import com.openpojo.random.util.SerializableComparableObject;
import com.openpojo.reflection.Parameterizable;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoMethod;
import com.openpojo.reflection.impl.PojoClassFactory;

/**
 * @author oshoukry
 */
public abstract class BaseCollectionRandomGeneratorTest {

	protected abstract ParameterizableRandomGenerator getInstance();

	protected abstract Class<? extends ParameterizableRandomGenerator> getGeneratorClass();

	protected abstract Class<? extends Collection> getExpectedTypeClass();

	protected abstract Class<? extends Collection> getGeneratedTypeClass();

	protected abstract Class<?> getGenericType();

	protected Class<?> getDefaultType() {
		return SerializableComparableObject.class;
	}

	protected boolean validateCollectionContents() {
		return true;
	}

	@Test
	public void constructorShouldBePrivate() {
		final Class<?> randomGeneratorClass = getGeneratorClass();
		PojoClass randomGeneratorPojo = PojoClassFactory.getPojoClass(randomGeneratorClass);

		List<PojoMethod> constructors = new ArrayList<PojoMethod>();

		for (PojoMethod constructor : randomGeneratorPojo.getPojoConstructors()) {
			if (!constructor.isSynthetic())
				constructors.add(constructor);
		}
		Assertions.assertEquals(1, constructors.size(),
				"Should only have one constructor [" + randomGeneratorPojo.getPojoConstructors() + "]");

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
	public void shouldOnlyReturnCollectionClassFromGetTypes() {
		Collection<Class<?>> types = getInstance().getTypes();
		Assertions.assertNotNull(types);
		Assertions.assertEquals(1, types.size());
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
	public void shouldThrowExceptionForDoGenerateForOtherThanCollectionClass() {
		try {
			getInstance().doGenerate(ALeafChildClass.class);
			fail("Exception expected");
		} catch (RandomGeneratorException e) {
		} catch (Exception e) {
			fail("Exception expected");
		}
	}

	@Test
	public void shouldThrowExceptionForDoGenerateForParameterizedOtherThanCollectionClass() {
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
	public void shouldGenerateCorrectTypeCollectionForRequestedCollection() {
		Collection someObject = (Collection) getInstance().doGenerate(getExpectedTypeClass());
		Assertions.assertNotNull(someObject);
		Assertions.assertEquals(getGeneratedTypeClass(), someObject.getClass());
		if (validateCollectionContents())
			Assertions.assertTrue(someObject.size() > 0);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void shouldGenerateParametrizableCorrectCollectionForRequest() {
		Collection<?> collectionOfType = (Collection) getInstance().doGenerate(getParameterizedType());

		Assertions.assertNotNull(collectionOfType);
		if (validateCollectionContents())
			Assertions.assertTrue(collectionOfType.size() > 0);
		for (Object entry : collectionOfType) {
			Assertions.assertNotNull(entry);
			Assertions.assertEquals(getGenericType(), entry.getClass());
		}
	}

	@Test
	public void endToEnd() {
		Collection<?> generatedCollection = RandomFactory.getRandomValue(getExpectedTypeClass());
		assertCollectionHasExpectedTypes(generatedCollection, getDefaultType());
	}

	protected void assertCollectionHasExpectedTypes(Collection<?> generatedCollection, Class<?> type) {
		Assertions.assertNotNull(generatedCollection);
		Assertions.assertEquals(getGeneratedTypeClass(), generatedCollection.getClass());
		if (validateCollectionContents())
			Assertions.assertTrue(generatedCollection.size() > 0);
		for (Object entry : generatedCollection) {
			Assertions.assertNotNull(entry);
			Assertions.assertEquals(type, entry.getClass());
		}
	}

	@Test
	public void endToEndWithGenerics() {
		Collection<?> generatedCollection = (Collection) RandomFactory.getRandomValue(getParameterizedType());
		assertCollectionHasExpectedTypes(generatedCollection, getGenericType());
	}

	protected Parameterizable getParameterizedType() {
		return new Parameterizable() {
			private Type[] types = new Type[] { getGenericType() };

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
