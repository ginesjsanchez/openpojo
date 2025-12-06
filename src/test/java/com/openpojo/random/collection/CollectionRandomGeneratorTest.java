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

package com.openpojo.random.collection;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.openpojo.random.RandomFactory;
import com.openpojo.random.collection.sample.AClassWithExhaustiveCollection;
import com.openpojo.random.collection.support.ALeafChildClass;
import com.openpojo.random.exception.RandomGeneratorException;
import com.openpojo.reflection.Parameterizable;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.SetterTester;

/**
 * @author oshoukry
 */
public class CollectionRandomGeneratorTest {

	@Test
	public void shouldBeAbleToCreate() {
		Assertions.assertNotNull(CollectionRandomGenerator.getInstance());
		Assertions.assertEquals(CollectionRandomGenerator.class, CollectionRandomGenerator.getInstance().getClass());
	}

	@Test
	public void shouldGenerateForCollectionOnly() {
		Collection<Class<?>> types = CollectionRandomGenerator.getInstance().getTypes();
		Assertions.assertEquals(1, types.size());
		Assertions.assertEquals(Collection.class, types.iterator().next());
	}

	@Test
	public void whenGenerateWithCollection_ThrowsException() {
		try {
			CollectionRandomGenerator.getInstance().doGenerate(ALeafChildClass.class);
			fail("Exception expected");
		} catch (RandomGeneratorException e) {
		} catch (Exception e) {
			fail("Exception expected");
		}
	}

	@Test
	public void whenGenerateWithCollection_ReturnNonEmpty() {
		Collection collection = CollectionRandomGenerator.getInstance().doGenerate(Collection.class);
		Assertions.assertNotNull(collection);
		Assertions.assertTrue(collection.size() > 0);
		Assertions.assertTrue(collection instanceof ArrayList);

	}

	@Test
	@SuppressWarnings("unchecked")
	public void whenGenerateWithGeneric_GenerateCorrectly() {
		Parameterizable parameterizable = new Parameterizable() {
			@Override
			public Class<?> getType() {
				return Collection.class;
			}

			@Override
			public boolean isParameterized() {
				return true;
			}

			@Override
			public List<Type> getParameterTypes() {
				return Arrays.asList(new Type[] { String.class });
			}
		};

		Collection<String> aCollectionOfStrings;
		aCollectionOfStrings = CollectionRandomGenerator.getInstance().doGenerate(parameterizable);
		Assertions.assertNotNull(aCollectionOfStrings);
		Assertions.assertTrue(aCollectionOfStrings.size() > 0);
		for (Object s : aCollectionOfStrings) {
			Assertions.assertNotNull(s);
			Assertions.assertTrue(s instanceof String);
		}
	}

	@Test
	public void testEndToEnd() {
		Collection collection = RandomFactory.getRandomValue(Collection.class);
		Assertions.assertNotNull(collection, "Should not be null");
		Assertions.assertTrue(collection.size() > 0, "Should not be empty");
		Assertions.assertTrue(collection instanceof ArrayList, "Should be an ArrayList");
		collection = RandomFactory.getRandomValue(Collection.class); // double check in case.
		Assertions.assertTrue(collection instanceof ArrayList, "Should be an ArrayList");
	}

	@Test
	public void exhaustiveTest() {
		PojoClass pojoClass = PojoClassFactory.getPojoClass(AClassWithExhaustiveCollection.class);
		Validator pojoValidator = ValidatorBuilder.create().with(new SetterMustExistRule()).with(new SetterTester())
				.build();

		pojoValidator.validate(pojoClass);
	}
}
