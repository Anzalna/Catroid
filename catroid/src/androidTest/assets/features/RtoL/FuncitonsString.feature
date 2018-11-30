@ignore
Feature: Text related functions.

  Behavior: - In this function we test alphabets of any word, join any two words
  as well as the length of any word in RtL languages

  Background:
    Given I have a program
    And this program has an object 'Object'

  Scenario: In this function, we test alphabets of any words regarding RtL languages.

    Given 'Object' has a start script
    And set 'test' to letter no '1' of "سافٹ ویئر"

    When I start the program
    Then the 'test' should be equal to "س"

  Scenario: In this function, we test and join any words RtL languages.

    Given 'Object' has a start script
    And set 'test' with join "ورلڈ" and "ہیلو"
    When I start the program
    Then the 'test' should be equal to "ہیلوورلڈ"

  Scenario: In this function, we test the length of the words in RtL languages.

    Given 'Object' has a start script
    And set 'test' to length "ہیلوورلڈ"

    When I start the program
    Then the 'test' should be equal to 8
