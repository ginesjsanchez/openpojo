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

package com.openpojo.business.utils;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.openpojo.business.cache.BusinessKeyField;
import com.openpojo.business.exception.BusinessException;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.construct.InstanceFactory;
import com.openpojo.reflection.exception.ReflectionException;
import com.openpojo.reflection.impl.PojoClassFactory;

public class BusinessIdentityUtilsTest {

	@Test
	public void shouldNotBeAbleToConstruct() {
		try {
			try {
				PojoClass pojoClass = PojoClassFactory.getPojoClass(BusinessIdentityUtils.class);
				org.testng.Assert.assertEquals(1, pojoClass.getPojoConstructors().size());
				InstanceFactory.getLeastCompleteInstance(pojoClass);
			} catch (ReflectionException re) {
				Throwable cause = re.getCause();
				while (cause != null) {
					if (cause instanceof UnsupportedOperationException)
						throw (UnsupportedOperationException) cause;
					cause = cause.getCause();
				}
			}
			fail("Should have not been able to construct");
		} catch (UnsupportedOperationException e) {
		} catch (Exception e) {
			fail("Exception expected");
		}
	}

	@Test
	public void shouldThrowBusinessExceptionWhenNullParameter() {
		try {
			BusinessIdentityUtils.anyNull((Object[]) null);
			fail("Expected BusinessException not thrown");
		} catch (final BusinessException be) {
			Assertions.assertEquals("objects parameter cannot be null", be.getMessage());
		}
	}

	@Test
	public void whenArrayAndSecondElementHasNonNullAndFirstElementIsNullShouldReturnFalse() {
		final Integer[] firstObject = new Integer[] { 1, null };
		Integer[] secondObject = new Integer[] { 1, 2 };
		Assertions.assertFalse(BusinessIdentityUtils.areEqual(new BusinessKeyField() {
			@Override
			public boolean isComposite() {
				return false;
			}

			@Override
			public boolean isCaseSensitive() {
				return false;
			}

			@Override
			public boolean isRequired() {
				return false;
			}

			@Override
			public Object get(Object instance) {
				return instance;
			}

			@Override
			public boolean isArray() {
				return true;
			}
		}, firstObject, secondObject, false), "Should return false");
	}
}
