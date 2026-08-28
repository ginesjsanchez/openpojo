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

package com.openpojo.random.generator.time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.TimeZone;

import com.openpojo.random.RandomGenerator;

/**
 * Generates a {@code TimeZone} picked at random from the identifiers available on the machine.
 *
 * @author oshoukry
 */
public class TimeZoneRandomGenerator implements RandomGenerator {
	private static final Class<?>[] TYPE = {TimeZone.class};
	private static final TimeZoneRandomGenerator INSTANCE = new TimeZoneRandomGenerator();
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	private final ArrayList<String> availableZones;

	private TimeZoneRandomGenerator() {
		availableZones = new ArrayList<>();
		String[] zones = TimeZone.getAvailableIDs();
		availableZones.addAll(Arrays.asList(zones));
	}

	public Collection<Class<?>> getTypes() {
		return Arrays.asList(TYPE);
	}

	public Object doGenerate(Class<?> type) {
		String anyZone = availableZones.get(RANDOM.nextInt(availableZones.size()));
		return TimeZone.getTimeZone(anyZone);
	}

	/**
	 * Returns the single instance of this class; it is the one that gets registered and the one reused on every
	 * request.
	 *
	 * @return the shared generator.
	 */
	public static TimeZoneRandomGenerator getInstance() {
		return INSTANCE;
	}

}
