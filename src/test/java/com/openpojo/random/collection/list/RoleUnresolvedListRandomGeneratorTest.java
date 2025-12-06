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

package com.openpojo.random.collection.list;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.management.relation.RoleUnresolvedList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.openpojo.random.RandomFactory;
import com.openpojo.random.collection.support.ALeafChildClass;
import com.openpojo.random.exception.RandomGeneratorException;
import com.openpojo.random.util.SomeRoleUnresolved;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoMethod;
import com.openpojo.reflection.impl.PojoClassFactory;

/**
 * @author oshoukry
 */
public class RoleUnresolvedListRandomGeneratorTest {
	private final RoleUnresolvedListRandomGenerator randomGenerator = RoleUnresolvedListRandomGenerator.getInstance();
	private final Class<RoleUnresolvedList> expectedTypeClass = RoleUnresolvedList.class;

	@Test
	public void constructorShouldBePrivate() {
		PojoClass randomGeneratorPojo = PojoClassFactory.getPojoClass(randomGenerator.getClass());

		List<PojoMethod> constructors = new ArrayList<PojoMethod>();

		for (PojoMethod constructor : randomGeneratorPojo.getPojoConstructors()) {
			if (!constructor.isSynthetic())
				constructors.add(constructor);
		}
		Assertions.assertEquals(1, constructors.size());

		PojoMethod constructor = constructors.get(0);

		Assertions.assertTrue(constructor.isPrivate());
	}

	@Test
	public void shouldBeAbleToCreate() {
		Assertions.assertEquals(RoleUnresolvedListRandomGenerator.class, randomGenerator.getClass());
	}

	@Test
	public void shouldOnlyReturnCollectionClassFromGetTypes() {
		Collection<Class<?>> types = randomGenerator.getTypes();
		Assertions.assertNotNull(types);
		Assertions.assertEquals(1, types.size());
		Assertions.assertEquals(expectedTypeClass, types.iterator().next());
	}

	@Test
	public void generatedTypeShouldBeAssignableToDeclaredType() {
		Class<?> declaredType = randomGenerator.getTypes().iterator().next();
		Object generatedInstance = randomGenerator.doGenerate(declaredType);
		Assertions.assertTrue(declaredType.isAssignableFrom(generatedInstance.getClass()));
	}

	@Test
	public void shouldThrowExceptionForDoGenerateForOtherThanCollectionClass() {
		try {
			randomGenerator.doGenerate(ALeafChildClass.class);
			fail("Exception expected");
		} catch (RandomGeneratorException e) {
		} catch (Exception e) {
			fail("Exception expected");
		}
	}

	@Test
	public void shouldGenerateCorrectTypeCollectionForRequestedCollection() {
		Collection someObject = randomGenerator.doGenerate(expectedTypeClass);
		Assertions.assertNotNull(someObject);
		Assertions.assertEquals(expectedTypeClass, someObject.getClass());
		Assertions.assertTrue(someObject.size() > 0);
	}

	@Test
	public void endToEnd() {
		Collection<?> generatedCollection = RandomFactory.getRandomValue(expectedTypeClass);
		assertCollectionHasExpectedTypes(generatedCollection, SomeRoleUnresolved.class);
	}

	protected void assertCollectionHasExpectedTypes(Collection<?> generatedCollection, Class<?> type) {
		Assertions.assertNotNull(generatedCollection);
		Assertions.assertEquals(expectedTypeClass, generatedCollection.getClass());
		Assertions.assertTrue(generatedCollection.size() > 0);
		for (Object entry : generatedCollection) {
			Assertions.assertNotNull(entry);
			Assertions.assertEquals(type, entry.getClass());
		}
	}

}
