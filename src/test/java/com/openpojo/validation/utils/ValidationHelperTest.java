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

package com.openpojo.validation.utils;

import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoField;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.reflection.java.bytecode.asm.ASMNotLoadedException;
import com.openpojo.utils.log.SpyAppender;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.impl.DefaultValidator;
import com.openpojo.validation.test.Tester;

public class ValidationHelperTest {
	@Test
	public void testIsStaticFinal() {
		PojoClass pojoClass = PojoClassFactory.getPojoClass(StaticFinalData.class);
		List<PojoField> pojoFields = pojoClass.getPojoFields();
		Assertions.assertEquals(4, pojoFields.size());
		for (PojoField fieldEntry : pojoFields) {
			if (fieldEntry.getName().equals("staticAndNotFinal")) {
				Assertions.assertTrue(
						fieldEntry.isStatic() && !fieldEntry.isFinal() && !ValidationHelper.isStaticFinal(fieldEntry));
			}
			if (fieldEntry.getName().equals("notStaticAndNotFinal")) {
				Assertions.assertTrue(
						!fieldEntry.isStatic() && !fieldEntry.isFinal() && !ValidationHelper.isStaticFinal(fieldEntry));
			}
			if (fieldEntry.getName().equals("STATIC_AND_FINAL")) {
				Assertions.assertTrue(
						fieldEntry.isStatic() && fieldEntry.isFinal() && ValidationHelper.isStaticFinal(fieldEntry));
			}
			if (fieldEntry.getName().equals("finalAndNotStatic")) {
				Assertions.assertTrue(
						!fieldEntry.isStatic() && fieldEntry.isFinal() && !ValidationHelper.isStaticFinal(fieldEntry));
			}
		}
	}

	@Test
	public void shouldReportMissingASMProperly() {
		Validator validator = ValidatorBuilder.create().with(new Tester() {
			@Override
			public void run(PojoClass pojoClass) {
				throw ASMNotLoadedException.getInstance();
			}
		}).build();

		SpyAppender spyAppender = new SpyAppender();
		spyAppender.startCaptureForLogger(DefaultValidator.class);
		try {
			validator.validate(PojoClassFactory.getPojoClass(this.getClass()));
			List<ILoggingEvent> warnEvents = spyAppender.getEventsForLogger(DefaultValidator.class, Level.WARN);
			Assertions.assertEquals(1, warnEvents.size());
			String expectedMessage = "ASM not loaded while attempting to execute behavioural tests on non-constructable class["
					+ this.getClass() + "], either filter abstract classes or add asm to your classpath.";
			Assertions.assertEquals(expectedMessage, warnEvents.get(0).getFormattedMessage());
		} finally {
			spyAppender.stopCaptureForLogger(DefaultValidator.class);
		}
	}

	private static class StaticFinalData {

		@SuppressWarnings("unused")
		private static int staticAndNotFinal = 0;

		@SuppressWarnings("unused")
		private int notStaticAndNotFinal;

		@SuppressWarnings("unused")
		private static final int STATIC_AND_FINAL = 0;

		@SuppressWarnings("unused")
		private final int finalAndNotStatic = 0;
	}
}
