@drinks
Feature: Drinks
  I want to use this template for my feature file


  @drinks
  Scenario Outline: Assestment 1
  # The scenario starts by making a GET call to a specific API endpoint, specified by the variable <API> and <EndPoint>.# The next step verifies that the status code returned by the API is equal to the expected code, specified by the <Code> variable.
  # The scenario then specifies that the API should return specific fields for the ingredients search.
  # These fields include idIngredient, strIngredient, strDescription, strAlcohol, and strABV.
  # The next step verifies that the returned JSON object contains all of these fields and that they have the correct data types.
  # The scenario also specifies that if the ingredient is non-alcoholic, both strAlcohol and strABV should be null.
  # If the ingredient is alcoholic, strAlcohol should be "yes" and strABV should not be null.
  # The final step verifies the consistency between these two fields for the first ingredient returned by the API.
  # The scenario includes three examples, each with a different API endpoint and expected status code. The examples test the API by searching for different ingredients, including "margarita", "vodka", and "MARGARITA" (note that the capitalization of the search term is different in the last example).

    Given I make a GET call to "<API>" to "<EndPoint>"
    Then I verify that status code is equal to <Code>
    #The system shall include a method to search by ingredient name and return the following fields: (Schema validation)
    And I verify below assets from the jasonpaths: "$.ingredients[0]." are exist and as required
      | idIngredient   | String |
      | strIngredient  | String |
      | strDescription | String |
      | strAlcohol     | String |
      | strABV         | String |
    #If an ingredient is non-alcoholic, Alcohol is null and ABV is null
    #If an ingredient is alcoholic, Alcohol is yes and ABV is not null.
    And I verify consistency between assets: "$.ingredients[0].strAlcohol" and "$.ingredients[0].strABV"

    Examples:
      | API            | EndPoint               | Code |
      | api/json/v1/1/ | search.php?i=margarita | 200  |
      | api/json/v1/1/ | search.php?i=vodka     | 200  |
      | api/json/v1/1/ | search.php?i=MARGARITA | 200  |


  @drinks
  Scenario Outline: Assestment 2

  #The scenario starts by making a GET call to a specific API endpoint, specified by the variables <API> and <EndPoint>.
  # The next step verifies that the status code returned by the API is equal to the expected code, specified by the <Code> variable.
  #The scenario then specifies that the API should return a specific schema for the drink recipe search.
  # The schema includes fields such as strDrink, strTags, strCategory, strIBA, strAlcoholic, strGlass, strInstructions, strDrinkThumb, strIngredient1, strMeasure1, strImageSource, strImageAttribution, strCreativeCommonsConfirmed, and dateModified.
  # The next step verifies that the returned JSON object contains all of these fields and that they have the correct data types.
  # Additionally, the schema specifies which fields are nullable and which are not, and the verification step ensures that the required fields are not null.
  #The scenario includes four examples, each with a different search term specified in the API endpoint and expected status code. The examples test the API by searching for different drink recipes, including "margarita", "vodka", "VoDkA" (note that the capitalization of the search term is different in this example), and "kola".

     #The system shall include a method to search by cocktail name.
     #If the cocktail does not exist in the cocktail DB, the API shall return drinks as null.
     #Searching for a cocktail by name is case-insensitive
    Given I make a GET call to "<API>" to "<EndPoint>"
    Then I verify that status code is equal to <Code>
     #(Schema validation)
    And I verify below schema in "<EndPoint>" from the jasonarray: "$.drinks" is exist and as required
      | strDrink                    | notNullable |
      | strDrinkAlternate           | Nullable    |
      | strTags                     | notNullable |
      | strVideo                    | Nullable    |
      | strCategory                 | notNullable |
      | strIBA                      | Nullable    |
      | strAlcoholic                | notNullable |
      | strGlass                    | notNullable |
      | strInstructions             | notNullable |
      | strDrinkThumb               | Nullable    |
      | strIngredient1              | notNullable |
      | strMeasure1                 | notNullable |
      | strImageSource              | Nullable    |
      | strImageAttribution         | Nullable    |
      | strCreativeCommonsConfirmed | notNullable |
      | dateModified                | notNullable |

    Examples:
      | API            | EndPoint               | Code |
      | api/json/v1/1/ | search.php?s=margarita | 200  |
      | api/json/v1/1/ | search.php?s=vodka     | 200  |
      | api/json/v1/1/ | search.php?s=VoDkA     | 200  |
      | api/json/v1/1/ | search.php?s=kola      | 200  |

  @drinks
  Scenario Outline: Assestment 3
  #The scenario specifies that the API should include a method to search for cocktails by name, and that the search should be case-insensitive.
  # If the cocktail name does not exist in the cocktail database, the API should return a null value for the "drinks" field.
  #The scenario starts by making a GET call to a specific API endpoint, specified by the variables <API> and <EndPoint>.
  # The next step verifies that the status code returned by the API is equal to the expected code, specified by the <Code> variable.
  #The scenario then specifies that the API should return a specific schema for the cocktail search.
  # The schema includes fields such as strDrink, strDrinkThumb, and idDrink. The next step verifies that the returned JSON object contains all of these fields and that they have the correct data types. Additionally, the verification step ensures that the required fields are not null.
  #The scenario includes two examples, each with a different filter specified in the API endpoint and expected status code.
  # The examples test the API by filtering cocktails by alcoholic and non-alcoholic drinks, and ensure that the expected status code is returned.

    #The system shall include a method to search by cocktail name.
     #If the cocktail does not exist in the cocktail DB, the API shall return drinks as null.
     #Searching for a cocktail by name is case-insensitive
    Given I make a GET call to "<API>" to "<EndPoint>"
    Then I verify that status code is equal to <Code>
     #(Schema validation)
    And I verify below schema in "<EndPoint>" from the jasonarray: "$.drinks" is exist and as required
      | strDrink      | notNullable |
      | strDrinkThumb | notNullable |
      | idDrink       | notNullable |

    Examples:
      | API            | EndPoint                   | Code |
      | api/json/v1/1/ | filter.php?a=Alcoholic     | 200  |
      | api/json/v1/1/ | filter.php?a=Non_Alcoholic | 200  |

