import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RecipeManager {

    private static ArrayList<Recipe> allrecipes = new ArrayList<Recipe>();

    /**
     * Create a recipe from user input.
     * @param name of the recipe being made.
     */
    public static Recipe createUserRecipe(String name) {
        Scanner scan = Main.scan;
        Map<String,Integer> ingredients = new HashMap<String,Integer>();
        String input="";
        int multiplier = 1;
        System.out.println("Input the ingredients of "+name);
        System.out.println("Format is '# name'");
        System.out.println("Use 'x#' to change the multiplier.");
        System.out.println("(Enter an blank line to end recipe.)");
        while (true) {
            input = scan.nextLine();

            // stop collecting ingredients on blank
            if (input.isBlank()) {
                break;
            }

            // allow comments
            if (input.charAt(0) == '#') {
                continue;
            }

            // provide help
            if (input.charAt(0) == '?') {
                System.out.println("Help not implemented."); // TODO Help
                continue;
            }

            // obtain multiplier (the amount of this item which is produced by this recipe)
            if (input.charAt(0) == 'x') {
                try {
                    int i = Integer.parseInt(input.substring(1));
                    // do not set a non-positive multiplier, ie the recipe must do something
                    if (0 < i) {
                        multiplier = i;
                        System.out.println("Multiplier updated.");
                    } else {
                        System.out.println("Invalid multiplier amount.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid multiplier amount.");
                }
                continue;
            }

            try {
                String[] i = input.split(" ",2);
                ingredients.put(i[1],Integer.parseInt(i[0]));
            } catch (NumberFormatException e) {
                System.out.println("Invalid format of: "+input);
                System.out.println("Format is '# name'");
                System.out.println("(Continue your list or enter a blank to end recipe.)");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid format of: "+input);
                System.out.println("Format is '# name'");
                System.out.println("(Continue your list or enter a blank to end recipe.)");
            }
        }
        return new Recipe(name,ingredients,multiplier);
    }

    /**
     * Checks if a recipe with that name already exists on the list (to prevent duplicates).
     * @param name of the recipe being checked.
     */
    public static boolean checkRecipe(String name) {
        for (Recipe r : allrecipes) {
            if (name.equals(r.getName())) return true;
        }
        return false;
    }

    /**
     * Gets a recipe from the list, if no recipe exists with that name, make a user one.
     * @param name of the recipe being searched for.
     * @return The recipe with the name OR a newly created recipe.
     */
    public static Recipe getRecipe(String name) {
        for (Recipe r : allrecipes) {
            if (name.equals(r.getName())) return r;
        }
        return createUserRecipe(name);
    }

    /**
     * Add a recipe to the master list.
     * @param r Recipe to be added.
     */
    public static void addRecipe(Recipe r) {
        if (!checkRecipe(r.getName())) allrecipes.add(r);
    }

}