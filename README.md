# Hamcrest Compose

This library provides [Hamcrest](http://hamcrest.org/) matchers to easily build composite matchers for objects. For example:

	public static Matcher<Person> personEqualTo(Person expected) {
		return compose("a person with", hasFeature("title", Person::getTitle, equalTo(expected.getTitle())))
			.and(hasFeature("first name", Person::getFirstName, equalTo(expected.getFirstName())))
			.and(hasFeature("last name", Person::getLastName, equalTo(expected.getLastName())));
	}

See the [demo](demo) module to run this example. The [PersonMatchersTest](demo/src/test/java/org/hobsoft/hamcrest/compose/demo/PersonMatchersTest.java) test case defines the behaviour of this matcher. 

## Getting started

Hamcrest Compose is available in the [Maven Central repository](http://search.maven.org/). Start by adding a dependency to your Maven project:

	<dependency>
		<groupId>org.hobsoft.hamcrest</groupId>
		<artifactId>hamcrest-compose</artifactId>
		<version>0.1.0</version>
		<scope>test</scope>
	</dependency>

## Usage

Hamcrest Compose provides two matchers:

### ComposeMatchers.compose

This factory method builds a composite matcher that logically ANDs a number of other matchers. For example:

	assertThat("ham", compose(startsWith("h")).and(containsString("a")).and(endsWith("m")));

This differs from Hamcrest's composite matchers [allOf](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/CoreMatchers.html#allOf(org.hamcrest.Matcher...)) and [both](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/CoreMatchers.html#both(org.hamcrest.Matcher)) in the following ways:

* It does not short circuit. This means that all mismatches are reported, not just the first one.
* It does not describe itself using parenthesis. This produces more readable descriptions.
* It supports an optional description to help describe the composition
* It does not repeat the matcher's description when describing a mismatch

It can also be built from a list of matchers when a fluent style is inconvenient:

	assertThat("ham", compose(asList(startsWith("h"), containsString("a"), endsWith("m"))));

### ComposeMatchers.hasFeature

This factory method builds a matcher that matches a 'feature' of an object. A feature is any value that can be obtained from the object by a [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html). Typically this is a lambda such as a method reference, for example:

	assertThat(person, hasFeature(Person::getFirstName, equalTo("ham")));

By default this matcher will describe itself and any mismatches by using the `toString` method of the feature function. When using lambdas this is not particularly informative so a feature description can be specified: 

	assertThat(person, hasFeature("a person with first name", Person::getFirstName, equalTo("ham")));
	
This feature description is also used to describe any mismatches. To specify a feature name for the mismatch only:

	assertThat(person, hasFeature("a person with first name", "first name", Person::getFirstName, equalTo("ham")));

## Links

* [Javadoc](http://www.hobsoft.org/hamcrest-compose/hamcrest-compose/apidocs/)

## License

* [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

[![Build Status](https://travis-ci.org/markhobson/hamcrest-compose.svg?branch=master)](https://travis-ci.org/markhobson/hamcrest-compose)
