Feature: MainMenu Screenshot

  In order to give the screenshot to the user in respect of hole Pocket Code

  Background:
    Given I have a Program

  Scenario Outline: Screenshot of the labeled buttons in the MainMenu.
    Given I am in the main menu
    When I press the <MainMenu> button
    And I should switch to the <MainMenu> view
    Then I take the Screenshot

    Examples:
      | MainMenu |
      | Continue |
      | New      |
      | Programs |
      | Help     |
      | Explore  |
      | Upload   |

  Scenario Outline: Screenshot of the labeled buttons in the ProgramMenu.
    Given I am in the main menu
    When I press the <MainMenu> button
    And  I press the <Scene> button
    And  I press the <Background> button
    And  I press the <ProgramMenu> button
    And I should switch to the <ProgramMenu> view
    #Then I take the Screenshot
    Examples:
      | MainMenu | Scene | Background | ProgramMenu |
      | Continue | Scene | Background | Scripts     |
      | Continue | Scene | Background | Backgrounds |
      | Continue | Scene | Background | Sounds      |

  Scenario Outline: Screenshot of the Categories and Bricks.
    Given I am in the main menu
    When I press the <MainMenu> button
    And  I press the <Scene> button
    And  I press the <Background> button
    And  I press the <Scripts> button
    And  I press the image Add button
    And   I press the <Categories>
    #Then I take the Screenshot
    Examples:
      | MainMenu | Scene | Background | Scripts | Categories |
      | Continue | Scene | Background | Scripts | Control    |
      | Continue | Scene | Background | Scripts | Motion     |
      | Continue | Scene | Background | Scripts | Sound      |
      | Continue | Scene | Background | Scripts | Looks      |
      | Continue | Scene | Background | Scripts | Data       |
      | Continue | Scene | Background | Scripts | MyBricks   |

