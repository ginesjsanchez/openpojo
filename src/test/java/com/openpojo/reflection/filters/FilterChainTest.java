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

package com.openpojo.reflection.filters;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoClassFilter;

/**
 * @author oshoukry
 */
public class FilterChainTest extends IdentitiesAreEqual {

	@Test
	public void newFilterChain_hasNoFilters() {
		FilterChain filter = new FilterChain();
		Assertions.assertEquals(0, filter.size());
	}

	@Test
	public void filterChain_shouldIgonreNullFilterArray() {
		FilterChain filter = new FilterChain((PojoClassFilter[]) null);
		Assertions.assertEquals(0, filter.size());
	}

	@Test
	public void oneOneFilterAdded_FilterChainHasOneFilter() {
		PojoClassFilter dummyFilter = new DummyPojoClassFilter();
		FilterChain filter = new FilterChain(dummyFilter);
		Assertions.assertEquals(1, filter.size());
		Assertions.assertTrue(filter.getPojoClassFilters().contains(dummyFilter));
	}

	@Test
	public void addingArrayWithNullFilters_ignored() {
		PojoClassFilter dummyFilter = new DummyPojoClassFilter();
		FilterChain filter = new FilterChain(dummyFilter, null);
		Assertions.assertEquals(1, filter.size());
		Assertions.assertTrue(filter.getPojoClassFilters().contains(dummyFilter));
	}

	@Test
	public void retrivedFilterCollectionIsUnmodifiable() {
		try {
			FilterChain filter = new FilterChain();
			filter.getPojoClassFilters().add(new DummyPojoClassFilter());
			fail("Exception expected");
		} catch (UnsupportedOperationException e) {
		} catch (Exception e) {
			fail("Exception expected");
		}
	}

	@Test
	public void shouldBeIdentityEqual() {
		FilterChain instanceOne = new FilterChain();
		FilterChain instanceTwo = new FilterChain();

		checkEqualityAndHashCode(instanceOne, instanceTwo);
	}

	private class DummyPojoClassFilter implements PojoClassFilter {
		@Override
		public boolean include(PojoClass pojoClass) {
			return false;
		}

	}
}
