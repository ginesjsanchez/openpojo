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

package com.openpojo.random.generator;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.openpojo.random.RandomFactory;
import com.openpojo.random.RandomGenerator;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoMethod;
import com.openpojo.reflection.java.load.ClassUtil;

/**
 * @author oshoukry
 */
public abstract class AbstractGeneratorTest {

	protected abstract PojoClass getPojoClass();

	protected abstract String getTypeName();

	protected abstract RandomGenerator getRandomGenerator();

	@BeforeEach
	public void before() {
//    //Assume.assumeTrue(getTypeName() != null);
	}

	@Test
	public void singlePrivateConstructor() {
		List<PojoMethod> constructors = getPojoClass().getPojoConstructors();
		Assertions.assertEquals(1, constructors.size());
		Assertions.assertTrue(constructors.get(0).isPrivate());
	}

	@Test
	public void canConstruct() {
		Assertions.assertNotNull(getRandomGenerator());
	}

	private void assumeClassIsLoaded() {
		// //Assume.assumeTrue(ClassUtil.isClassLoaded(getTypeName()));
	}

	@Test
	public void whenGetTypesShouldReturnExpectedType() {
		assumeClassIsLoaded();

		Collection<Class<?>> types = getRandomGenerator().getTypes();
		Assertions.assertEquals(1, types.size());
		Assertions.assertEquals(getTypeName(), types.iterator().next().getName());
	}

	@Test
	public void whenDoGenerateReturnsDifferentInstances() {
		assumeClassIsLoaded();

		Object first = getRandomGenerator().doGenerate(null);
		Assertions.assertNotNull(first);

		Object second = getRandomGenerator().doGenerate(null);
		Assertions.assertNotNull(second);

		Class<?> expectedClass = ClassUtil.loadClass(getTypeName());
		Assertions.assertTrue(expectedClass.isAssignableFrom(first.getClass()));
		Assertions.assertTrue(

				expectedClass.isAssignableFrom(second.getClass()));

		if (first.equals(second)) // by chance same object, try one more time.
			second = getRandomGenerator().doGenerate(null);

		Assertions.assertNotEquals(first, second);
	}

	@Test
	public void end2end() {
		assumeClassIsLoaded();

		Class<?> type = ClassUtil.loadClass(getTypeName());
		Object instance = RandomFactory.getRandomValue(type);
		Assertions.assertNotNull(instance);
		Assertions.assertTrue(type.isAssignableFrom(instance.getClass()));
	}
}
