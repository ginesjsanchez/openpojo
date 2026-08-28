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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author oshoukry
 */
public class CollectionHelperTest {
	@Test
	public void shouldReturnWithoutGenerationIfTypeIsNull() {
		List<Object> emptyList = new ArrayList<>();

		Collection<Object> actual = CollectionHelper.buildCollections(emptyList, null);

		Assertions.assertSame(emptyList, actual, "Should return the very same instance, untouched");
		Assertions.assertEquals(0, actual.size(), "Should not generate any element when the type is null");
	}

	@Test
	public void shouldReturnNullIfCollectionIsNull() {
		Assertions.assertNull(CollectionHelper.buildCollections(null, null));
		Assertions.assertNull(CollectionHelper.buildCollections(null, this.getClass()));
	}
}
