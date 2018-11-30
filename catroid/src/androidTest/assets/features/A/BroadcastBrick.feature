
Feature: Broadcast brick

  A Broadcast brick should send a message and When scripts should react to it.

  Background:
    Given I have a Program
    And this program has an Object 'Object'

  Scenario: A

   When I start the program
    And I wait until the program has stopped

