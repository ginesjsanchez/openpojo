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

package com.openpojo.random.util;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.openpojo.random.exception.RandomGeneratorException;

/**
 * @author oshoukry
 */
public class HelperTest {

	@SuppressWarnings("unchecked")
	@Test
	public void shouldThrowExceptionWithEmptyList() {
		try {
			Helper.assertIsAssignableTo(Object.class, Collections.EMPTY_LIST);
			fail("Exception expected");
		} catch (RandomGeneratorException e) {
		} catch (Exception e) {
			fail("Exception expected");
		}
	}

	@Test
	@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
	public void shouldNotThrowExceptionWithListHavingAssignableToItem() {
		Helper.assertIsAssignableTo(Object.class, Arrays.<Class<?>>asList(this.getClass()));
	}
}
