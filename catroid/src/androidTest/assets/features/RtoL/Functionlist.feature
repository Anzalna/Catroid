
@ignore
Feature: Functions list.
  Behavior:- In the List function using RTL(Urdu/Arabic), we test number of items, exact
  element as well as the element in the list in the form of true and false

  Background:
    Given I have a program
    And this program has an object 'Object'

  Scenario: In this function, we test number of items in the list.

    Given 'Object' has a start script
    And add 100 to list "پہلی فہرست"
    And add 200 to list "پہلی فہرست"
    And set 'test' with the no of items in the list "پہلی فہرست"

    When I start the program
    Then the 'test' should be equal to 2

  Scenario: In this function, we test the exact element in the list.

    Given 'Object' has a start script
    And add 10 to list "دوسری فہرست"
    And add 20 to list "دوسری فہرست"
    And set 'test' with the element 2 of "دوسری فہرست"
    When I start the program
    Then the 'test' should be equal to 20

  Scenario: In this Function, We test the element in the list in the form of true and false.

    Given 'Object' has a start script
    And add 10 to list "تیسری فہرست"
    And add 20 to list "تیسری فہرست"
    And set 'test' with the "تیسری فہرست" contain 20
    When I start the program
    Then the 'test' should be true
