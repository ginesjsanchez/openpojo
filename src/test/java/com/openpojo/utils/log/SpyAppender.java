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

package com.openpojo.utils.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

/**
 * Captures the events openpojo emits through SLF4J, so tests can assert on them.
 * <p>
 * It relies on the logback binding, which is only present on the test classpath:
 * openpojo does not impose any log implementation on its consumers.
 *
 * @author oshoukry
 */
public class SpyAppender {

	private final Map<String, ListAppender<ILoggingEvent>> appenders = new HashMap<>();
	private final Map<String, Level> originalLevels = new HashMap<>();

	public void startCaptureForLogger(final Class<?> clazz) {
		startCaptureForLogger(clazz.getName());
	}

	public void startCaptureForLogger(final String loggerName) {
		final Logger logger = logger(loggerName);

		originalLevels.put(loggerName, logger.getLevel());
		logger.setLevel(Level.TRACE);

		final ListAppender<ILoggingEvent> appender = new ListAppender<>();
		appender.setContext(logger.getLoggerContext());
		appender.start();
		logger.addAppender(appender);

		appenders.put(loggerName, appender);
	}

	public void stopCaptureForLogger(final Class<?> clazz) {
		stopCaptureForLogger(clazz.getName());
	}

	public void stopCaptureForLogger(final String loggerName) {
		final ListAppender<ILoggingEvent> appender = appenders.remove(loggerName);
		if (appender == null)
			return;

		final Logger logger = logger(loggerName);
		logger.detachAppender(appender);
		appender.stop();
		logger.setLevel(originalLevels.remove(loggerName));
	}

	public List<ILoggingEvent> getEventsForLogger(final Class<?> clazz) {
		return getEventsForLogger(clazz.getName());
	}

	public List<ILoggingEvent> getEventsForLogger(final String loggerName) {
		final ListAppender<ILoggingEvent> appender = appenders.get(loggerName);
		if (appender == null)
			return Collections.emptyList();
		return Collections.unmodifiableList(new ArrayList<>(appender.list));
	}

	public List<ILoggingEvent> getEventsForLogger(final Class<?> clazz, final Level level) {
		return getEventsForLogger(clazz.getName(), level);
	}

	public List<ILoggingEvent> getEventsForLogger(final String loggerName, final Level level) {
		final List<ILoggingEvent> matching = new ArrayList<>();
		for (final ILoggingEvent event : getEventsForLogger(loggerName))
			if (event.getLevel() == level)
				matching.add(event);
		return matching;
	}

	private static Logger logger(final String loggerName) {
		return (Logger) LoggerFactory.getLogger(loggerName);
	}
}
