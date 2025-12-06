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

package com.openpojo.random.dynamic;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.openpojo.random.exception.RandomGeneratorException;

/**
 * @author oshoukry
 */
public class EnumRandomGeneratorTest {

	@Test
	public void shouldThrowExceptionWhenEnumHasNoValues() {
		try {
			EnumRandomGenerator.getInstance().doGenerate(EmptyEnum.class);
			fail("Exception expected");
		} catch (RandomGeneratorException e) {
		} catch (Exception e) {
			fail("Exception expected");
		}
	}

	public enum EmptyEnum {

	}
}
