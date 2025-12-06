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

package com.openpojo.business;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.openpojo.business.exception.BusinessException;
import com.openpojo.business.sampleclasses.Child;

public class BusinessIdentityInheritenceTest {

	@Test
	public void shouldEquateUsingInheritence() {
		Child first = new Child("First", "Last", 'M');
		Child second = new Child("First", "Last", 'M');
		Assertions.assertTrue(BusinessIdentity.areEqual(first, second));
	}

	@Test
	public void shouldFailEquateUsingInheritence() {
		Child first = new Child("First", "Last", 'F');
		Child second = new Child("First", "LastName", 'F');
		Assertions.assertFalse(BusinessIdentity.areEqual(first, second));
	}

	@Test
	public void shouldFailIncomplete() {
		try {
			Child first = new Child("firstName", "last", null);
			Child second = new Child("First", "LastName", null);
			Assertions.assertTrue(BusinessIdentity.areEqual(first, second));
			fail("Exception expected");
		} catch (BusinessException e) {
		} catch (Exception e) {
			fail("Exception expected");
		}
	}
}
