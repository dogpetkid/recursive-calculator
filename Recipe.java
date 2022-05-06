import java.util.HashMap;
import java.util.Map;

public class Recipe {
    private String name;
    /** A recipe is fundamental if it has no ingredients. */
    private boolean fundamental;
    private Map<Recipe,Integer> ingredients;
    /** The multiplier is the resulting amount of products by created by performing the recipe. */
    private int multiplier;

    /**
     * Create a recipe from an map of items.
     * @param name of the recipe.
     * @param ingredients is a list of the names of all recipes that make the original.
     */
    public Recipe(String name, Map<String,Integer> ingredients, int multiplier) {
        this.name = name;
        this.multiplier = multiplier > 0 ? multiplier : 1;
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

    public int getMultiplier() {
        return multiplier;
    }

    /**
     * Calculates the fundamental amount of things it takes to fulfill this recipe.
     * @return HashMap with all fundamental recipes and numbers.
     */
    public Map<Recipe,Integer> calculateFundamental() {
        // the map of leftovers is used such that if there are left over products from a recipe that weren't used, a recipe doesn't need to be redone, just use the leftovers
        Map<Recipe, Integer> leftovers = new HashMap<Recipe,Integer>();
        return calculateFundamentalWithLeftovers(leftovers);
    }

    private Map<Recipe,Integer> calculateFundamentalWithLeftovers(Map<Recipe,Integer> leftovers) {
        Map<Recipe, Integer> total = new HashMap<Recipe,Integer>();
        if (fundamental) {
            total.put(this, 1);
            return total;
        }

        // iterate all of this recipe's ingredients
        for (Map.Entry<Recipe, Integer> ingredient : ingredients.entrySet())
        {
            Integer currentLeftOver = leftovers.get(ingredient.getKey());
            if (currentLeftOver == null) currentLeftOver = 0;
            // toProduce is the amount that is not already left over
            int toProduce = ingredient.getValue() - currentLeftOver;
            if (toProduce < 0) toProduce = 0;

            // perform the recipe enough times to make sure there is sufficient ingredients
            // ie performTimes = ceil(amountneeded / amountobtained)
            int performTimes = (int) Math.ceil((double) toProduce / ingredient.getKey().getMultiplier());

            int leftoverAfterPerforming = currentLeftOver + performTimes * ingredient.getKey().getMultiplier() - ingredient.getValue();
            // only update the map if needed (not everything has to go into the map, this is to save runtime)
            if (currentLeftOver != leftoverAfterPerforming) leftovers.put(ingredient.getKey(), leftoverAfterPerforming);

            // prevent unnecessary recursion to save runtime
            if (toProduce == 0) continue;

            // iterate over all the fundamentals of the ingredients
            for (Map.Entry<Recipe, Integer> fundamental : ingredient.getKey().calculateFundamentalWithLeftovers(leftovers).entrySet())
            {
                // current is set to the number of uses of this fundamental
                Integer current = total.get(fundamental.getKey());
                // note that current could be set to null during this operation if it has not already been accounted for

                if (!(current == null))
                {
                    // update the total for the given fundamental to include the previous amount and the amount required to make the ingredient times the number of that ingrdient needed
                    total.put(fundamental.getKey(), current+fundamental.getValue()*performTimes);
                }
                else
                {
                    total.put(fundamental.getKey(), fundamental.getValue()*performTimes);
                }
            }
        }
        return total;
    }

}