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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.openpojo.random.RandomFactory;
import com.openpojo.random.map.support.SimpleType1;
import com.openpojo.random.map.support.SimpleType2;
import com.openpojo.reflection.Parameterizable;

/**
 * @author oshoukry
 */
public class DictionaryRandomGeneratorTest {

	@Test
	public void canGenerateDictionary() {
		Assertions.assertNotNull(RandomFactory.getRandomValue(Dictionary.class));
	}

	@Test
	public void dictionaryCreatedIsHashtable() {
		Object object = RandomFactory.getRandomValue(Dictionary.class);
		Assertions.assertNotNull(object);
		Assertions.assertEquals(Hashtable.class, object.getClass());
	}

	@Test
	public void shouldNotBeEmpty() {
		Hashtable hashtable = (Hashtable) RandomFactory.getRandomValue(Dictionary.class);
		Assertions.assertNotNull(hashtable);
		Assertions.assertTrue(hashtable.size() > 0);
	}

	@Test
	public void canGenerateGenerics() {
		Hashtable<?, ?> hashtable = (Hashtable) RandomFactory.getRandomValue(new Parameterizable() {
			@Override
			public Class<?> getType() {
				return Dictionary.class;
			}

			@Override
			public boolean isParameterized() {
				return true;
			}

			@Override
			public List<Type> getParameterTypes() {
				List<Type> parameterTypes = new ArrayList<Type>(2);
				parameterTypes.add(SimpleType1.class);
				parameterTypes.add(SimpleType2.class);
				return parameterTypes;
			}
		});
		Assertions.assertNotNull(hashtable);
		Assertions.assertTrue(!hashtable.isEmpty());
		for (Map.Entry<?, ?> entry : hashtable.entrySet()) {
			Assertions.assertNotNull(entry);
			Assertions.assertNotNull(entry.getKey());
			Assertions.assertEquals(SimpleType1.class, entry.getKey().getClass());
			Assertions.assertNotNull(entry.getValue());
			Assertions.assertEquals(SimpleType2.class, entry.getValue().getClass());
		}
	}
}
