import java.util.Map;
import java.util.Scanner;

public class Main {

    public static final Scanner scan = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("WARNING: do not circularly define things, if you do you run the risk of crashing the program or your computer!");
        Recipe finished;
        finished = RecipeManager.createUserRecipe("Final Product");
        scan.close();
        System.out.println("BOM:");
        for (Map.Entry<Recipe,Integer> i : finished.calculateFundamental().entrySet()) {
            System.out.println(Integer.toString(i.getValue()) + "x " + i.getKey().getName());
        }
    }

}