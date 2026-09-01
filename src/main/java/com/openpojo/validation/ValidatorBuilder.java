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

package com.openpojo.validation;

import java.util.ArrayList;
import java.util.List;

import com.openpojo.validation.exception.ValidationException;
import com.openpojo.validation.impl.DefaultValidator;
import com.openpojo.validation.rule.Rule;
import com.openpojo.validation.test.Tester;

/**
 * Builds a {@code Validator} by gathering the structural rules and behavioural testers to apply.
 *
 * @author oshoukry
 */
public class ValidatorBuilder {

	private List<Rule> rules = new ArrayList<>();
	private List<Tester> testers = new ArrayList<>();

	private ValidatorBuilder() {
	}

	/**
	 * Starts building a validator.
	 *
	 * @return an empty builder.
	 */
	public static ValidatorBuilder create() {
		return new ValidatorBuilder();
	}

	/**
	 * Adds structural rules.
	 *
	 * Same as {@link #withRules(Rule...)}. Prefer that one when the argument is a lambda or a method
	 * reference: {@link Rule} and {@link Tester} are both functional interfaces taking a PojoClass, so the
	 * compiler cannot tell which overload of {@code with} is meant and rejects the call as ambiguous.
	 *
	 * @param rules
	 *     The rules to apply.
	 * @return this same builder, so calls can be chained.
	 */
	public ValidatorBuilder with(Rule... rules) {
		return withRules(rules);
	}

	/**
	 * Adds structural rules. Unambiguous alternative to {@link #with(Rule...)}, valid for lambdas and
	 * method references.
	 *
	 * @param rules
	 *     The rules to apply. A null array, and null entries in it, are ignored.
	 * @return this same builder, so calls can be chained.
	 */
	public ValidatorBuilder withRules(Rule... rules) {
		if (rules != null)
			for (Rule rule : rules) {
				if (rule != null)
					this.rules.add(rule);
			}
		return this;
	}

	/**
	 * Rules added so far.
	 *
	 * @return the rules gathered so far.
	 */
	public List<Rule> getRules() {
		return rules;
	}

	/**
	 * Adds behavioural testers.
	 *
	 * Same as {@link #withTesters(Tester...)}. Prefer that one when the argument is a lambda or a method
	 * reference; see {@link #with(Rule...)} for why.
	 *
	 * @param testers
	 *     The testers to apply.
	 * @return this same builder, so calls can be chained.
	 */
	// The overloads lint flags this pair as potentially ambiguous, and it is right: a lambda cannot pick
	// between them. Both overloads stay for backwards compatibility, and withRules/withTesters are the way
	// out, so the warning has nothing left to report here.
	@SuppressWarnings("overloads")
	public ValidatorBuilder with(Tester... testers) {
		return withTesters(testers);
	}

	/**
	 * Adds behavioural testers. Unambiguous alternative to {@link #with(Tester...)}, valid for lambdas and
	 * method references.
	 *
	 * @param testers
	 *     The testers to apply. A null array, and null entries in it, are ignored.
	 * @return this same builder, so calls can be chained.
	 */
	public ValidatorBuilder withTesters(Tester... testers) {
		if (testers != null)
			for (Tester tester : testers) {
				if (tester != null)
					this.testers.add(tester);
			}
		return this;
	}

	/**
	 * Testers added so far.
	 *
	 * @return the testers gathered so far.
	 */
	public List<Tester> getTesters() {
		return testers;
	}

	/**
	 * Closes the build and returns the validator with everything gathered so far.
	 *
	 * @return the validator, ready to use.
	 */
	public Validator build() {
		if (rules.size() == 0 && testers.size() == 0)
			throw ValidationException.getInstance("You must add at least 1 Rule or Tester before building Validator");
		return new DefaultValidator(rules, testers);
	}

}
