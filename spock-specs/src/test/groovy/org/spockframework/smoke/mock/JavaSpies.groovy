/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spockframework.smoke.mock

import org.spockframework.runtime.ConditionNotSatisfiedError

import spock.lang.Specification
import spock.lang.FailsWith

// TODO: probably should enforce that constructor args are provided for spies
class JavaSpies extends Specification {
  def "call real methods by default"() {
    def person = Spy(Person, constructorArgs: ["fred", 42])

    expect:
    person.name == "fred"
    person.age == 42
  }

  @FailsWith(value = ConditionNotSatisfiedError, reason = "TODO")
  def "call real Object methods by default"() {
    def person = Spy(Person, constructorArgs: ["fred", 42])

    expect:
    person.toString() == "Hi, I'm fred"
  }

  def "can verify interactions with real methods"() {
    def person = Spy(Person, constructorArgs: ["fred", 42])

    when:
    def result = person.work()

    then:
    1 * person.work()
    1 * person.getTask()
    1 * person.getWorkHours()
    result == "singing, singing"
  }

  def "can be used as partial mocks"() {
    def person = Spy(Person, constructorArgs: ["fred", 42]) {
      getWorkHours() >>> [3, 2, 1]
    }

    expect:
    person.work() == "singing, singing, singing"
    person.work() == "singing, singing"
    person.work() == "singing"
  }

  static class Person {
    String name
    int age
    List<String> children

    Person(String name, int age) {
      this.name = name
      this.age = age
    }

    def work() {
      ([task] * workHours).join(", ")
    }

    def getTask() {
      "singing"
    }

    def getWorkHours() {
      2
    }

    String toString() {
      "Hi I'm $name"
    }
  }
}