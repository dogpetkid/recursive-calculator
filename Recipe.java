import java.util.HashMap;
import java.util.Map;

public class Recipe {
    private String name;
    /** A recipe is fundamental if it has no ingredients. */
    private boolean fundamental;
    private Map<Recipe,Integer> ingredients;

    /**
     * Create a recipe from an map of items.
     * @param name of the recipe.
     * @param ingredients is a list of the names of all recipes that make the original.
     */
    public Recipe(String name, Map<String,Integer> ingredients) {
        this.name = name;
        RecipeManager.addRecipe(this);
        if (ingredients.isEmpty()) fundamental = true;
        else {
            this.ingredients = new HashMap<Recipe,Integer>();
            for (Map.Entry<String, Integer> e : ingredients.entrySet()) {
                Recipe ingredient = RecipeManager.getRecipe(e.getKey());
                this.ingredients.put(ingredient, e.getValue());
            }
        }
    }

    public String getName() {
        return name;
    }

    /**
     * Calculates the fundamental amount of things it takes to fulfill this recipe.
     * @return HashMap with all fundamental recipes and numbers.
     */
    public Map<Recipe,Integer> calculateFundamental() {
        Map<Recipe, Integer> total = new HashMap<Recipe,Integer>();
        if (fundamental) {
            total.put(this, 1);
            return total;
        }

        // iterate all of this recipe's ingredients
        for (Map.Entry<Recipe, Integer> ingredient : ingredients.entrySet())
        {
            // iterate over all the fundamentals of the ingredients
            for (Map.Entry<Recipe, Integer> fundamental : ingredient.getKey().calculateFundamental().entrySet())
            {
                // current is set to the number of uses of this fundamental
                Integer current = total.get(fundamental.getKey());
                // note that current could be set to null during this operation if it has not already been accounted for

                if (!(current == null))
                {
                    // update the total for the given fundamental to include the previous amount and the amount required to make the ingredient times the number of that ingrdient needed
                    total.put(fundamental.getKey(), current+fundamental.getValue()*ingredient.getValue());
                }
                else
                {
                    total.put(fundamental.getKey(), fundamental.getValue()*ingredient.getValue());
                }
            }
        }
        return total;
    }

}