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

package com.openpojo.reflection.coverage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openpojo.reflection.coverage.CoverageDetector;
import com.openpojo.reflection.coverage.impl.Clover3;
import com.openpojo.reflection.coverage.impl.Clover4;
import com.openpojo.reflection.coverage.impl.Cobertura;
import com.openpojo.reflection.coverage.impl.Jacoco;
import com.openpojo.reflection.coverage.service.impl.DefaultPojoCoverageFilterService;

/**
 * Builds the {@code PojoCoverageFilterService} by walking the known detectors and keeping those whose tools are
 * present.
 *
 * @author oshoukry
 */
public class PojoCoverageFilterServiceFactory {
	private static final CoverageDetector[] KNOWN_COVERAGE_DETECTORS = new CoverageDetector[]{
			Clover3.getInstance(),
			Clover4.getInstance(),
			Cobertura.getInstance(),
			Jacoco.getInstance()
	};

	/**
	 * Builds the service registering the detectors of the tools that are present.
	 *
	 * @return the configured service.
	 */
	public static PojoCoverageFilterService configureAndGetPojoCoverageFilterService() {
		PojoCoverageFilterService pojoCoverageFilterService = new DefaultPojoCoverageFilterService();
		for (CoverageDetector coverageDetector : KNOWN_COVERAGE_DETECTORS) {
			if (coverageDetector.isLoaded()) {
				Logger logger = LoggerFactory.getLogger(PojoCoverageFilterServiceFactory.class);
				logger.info(
						coverageDetector.getName() + " detected, auto-configuring OpenPojo to ignore its structures.");
				pojoCoverageFilterService.registerCoverageDetector(coverageDetector);
			}
		}
		return pojoCoverageFilterService;
	}

	/**
	 * Builds a service with the given detector, without looking at the classpath.
	 *
	 * @param coverageDetector
	 *     The detector to register.
	 * @return the configured service.
	 */
	public static PojoCoverageFilterService createPojoCoverageFilterServiceWith(CoverageDetector coverageDetector) {
		PojoCoverageFilterService pojoCoverageFilterService = new DefaultPojoCoverageFilterService();
		pojoCoverageFilterService.registerCoverageDetector(coverageDetector);
		return pojoCoverageFilterService;
	}

	private PojoCoverageFilterServiceFactory() {
		throw new UnsupportedOperationException(
				PojoCoverageFilterServiceFactory.class.getName() + " should not be constructed!");
	}
}
