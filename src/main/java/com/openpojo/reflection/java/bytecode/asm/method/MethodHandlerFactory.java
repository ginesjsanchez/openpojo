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

package com.openpojo.reflection.java.bytecode.asm.method;

import java.util.HashMap;
import java.util.Map;

import com.openpojo.reflection.java.bytecode.asm.method.impl.*;

/**
 * Picks the right {@code MethodHandler} according to the return type of the method to implement.
 *
 * @author oshoukry
 */
public class MethodHandlerFactory {

	private final Map<String, MethodHandler> SIGNATURE_METHOD_HANDLERS = new HashMap<>();
	private final Map<String, MethodHandler> METHOD_NAME_HANDLERS = new HashMap<>();
	private final Map<String, MethodHandler> RETURN_TYPE_METHOD_HANDLERS = new HashMap<>();

	private final MethodHandler DEFAULT_RETURN_TYPE_METHOD_HANDLER = new DefaultReturnTypeMethodHandler();
	private static final MethodHandlerFactory INSTANCE = new MethodHandlerFactory();

	private MethodHandlerFactory() {
		SIGNATURE_METHOD_HANDLERS.put("toString()Ljava/lang/String;", new ToStringMethodHandler());

		METHOD_NAME_HANDLERS.put("<init>", new InitMethodHandler());

		RETURN_TYPE_METHOD_HANDLERS.put("boolean", new BooleanReturnTypeMethodHandler());
		RETURN_TYPE_METHOD_HANDLERS.put("byte", new ByteReturnTypeMethodHandler());
		RETURN_TYPE_METHOD_HANDLERS.put("char", new CharReturnTypeMethodHandler());
		RETURN_TYPE_METHOD_HANDLERS.put("float", new FloatReturnTypeMethodHandler());
		RETURN_TYPE_METHOD_HANDLERS.put("short", new ShortReturnTypeMethodHandler());
		RETURN_TYPE_METHOD_HANDLERS.put("int", new IntReturnTypeMethodHandler());
		RETURN_TYPE_METHOD_HANDLERS.put("long", new LongReturnTypeMethodHandler());
		RETURN_TYPE_METHOD_HANDLERS.put("double", new DoubleReturnTypeMethodHandler());
		RETURN_TYPE_METHOD_HANDLERS.put("void", new VoidReturnTypeMethodHandler());
	}

	/**
	 * Picks the right body generator for the return type of the method.
	 *
	 * @param methodSignature
	 *     The method signature.
	 * @param methodName
	 *     The method name.
	 * @param returnType
	 *     The descriptor of the return type.
	 * @return the handler able to implement it.
	 */
	public MethodHandler getHandler(String methodSignature, String methodName, String returnType) {
		MethodHandler methodHandler = SIGNATURE_METHOD_HANDLERS.get(methodSignature);

		if (methodHandler == null)
			methodHandler = METHOD_NAME_HANDLERS.get(methodName);

		if (methodHandler == null)
			methodHandler = RETURN_TYPE_METHOD_HANDLERS.get(returnType);

		if (methodHandler == null)
			methodHandler = DEFAULT_RETURN_TYPE_METHOD_HANDLER;
		return methodHandler;
	}

	/**
	 * Returns the single instance of this class; it is the one that gets registered and the one reused on every
	 * request.
	 *
	 * @return the shared instance.
	 */
	public static MethodHandlerFactory getInstance() {
		return INSTANCE;
	}
}
