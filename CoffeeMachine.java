package machine;

import java.util.Scanner;

class CoffeeMachineInterface {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write action (buy, fill, take, remaining, exit): ");

        CoffeeMachine machine = new CoffeeMachine(550, 400, 540, 120, 9);

        while (! machine.state.equals(CoffeeMachine.machineState.EXIT)) {
            String input = scanner.nextLine();
            input = input.toUpperCase();
            machine.processInput(input);
        }

    }
}

public class CoffeeMachine {
    int money;
    int mlOfWater;
    int mlOfMilk;
    int gramsOfCoffeeBeans;
    int disposableCups;
    static machineState state = machineState.AWAITING_INSTRUCTION;

    public enum machineState {
        AWAITING_INSTRUCTION, ADDING_COFFEE_BEANS, ADDING_WATER, ADDING_MILK, ADDING_CUPS, SELLING_COFFEE, EXIT;
    }

    public enum coffeeRecipes {
        ESPRESSO(250, 0, 16, 4),// ml of water, ml of milk, grams of coffee, cost in dollars
        LATTE (350, 75, 20, 7),
        CAPPUCCINO (200, 100, 12, 6);

        private int mlOfWater;
        private int mlOfMilk;
        private int gramsOfCoffeeBeans;
        private int costInDollars;

        coffeeRecipes(int water, int milk, int coffeeBeans, int cost) {
            this.mlOfWater = water;
            this.mlOfMilk = milk;
            this.gramsOfCoffeeBeans = coffeeBeans;
            this.costInDollars = cost;
        }
        int getMlOfWater(){
            return mlOfWater;
        }
        int getMlOfMilk(){
            return mlOfMilk;
        }
        int getGramsOfCoffeeBeans(){
            return gramsOfCoffeeBeans;
        }
        int getCostInDollars() {
            return costInDollars;
        }

    }

    CoffeeMachine (int money, int mlOfWater, int mlOfMilk, int gramsOfCoffeeBeans, int disposableCups) {
        this.money = money;
        this.mlOfWater = mlOfWater;
        this.mlOfMilk = mlOfMilk;
        this.gramsOfCoffeeBeans = gramsOfCoffeeBeans;
        this.disposableCups = disposableCups;
    }

    public void processInput (String input) {
        if (input.equals("EXIT")) {
            state = machineState.EXIT;
        }
        switch (state) {
            case AWAITING_INSTRUCTION:
                processNewInstruction (input);
                break;
            case ADDING_WATER:
                int numOfWater = Integer.valueOf(input);
                mlOfWater += numOfWater;
                state = machineState.ADDING_MILK;
                System.out.println("Write how many ml of milk you want to add: ");
                break;
            case ADDING_MILK:
                int numOfMilk = Integer.valueOf(input);
                mlOfMilk += numOfMilk;
                state = machineState.ADDING_COFFEE_BEANS;
                System.out.println("Write how many grams of coffee beans you want to add: ");
                break;
            case ADDING_COFFEE_BEANS:
                int numOfBeans = Integer.valueOf(input);
                gramsOfCoffeeBeans += numOfBeans;
                state = machineState.ADDING_CUPS;
                System.out.println("Write how many disposable cups of coffee you want to add: ");
                break;
            case ADDING_CUPS:
                int numOfCups = Integer.valueOf(input);
                disposableCups += numOfCups;
                state = machineState.AWAITING_INSTRUCTION;
                System.out.println("Write action (buy, fill, take, remaining, exit): ");
                break;
            case SELLING_COFFEE:
                if (input.equals("BACK")) {
                    state = machineState.AWAITING_INSTRUCTION;
                    System.out.println("Write action (buy, fill, take, remaining, exit): ");
                    break;
                }
                if (!input.equals("1") && !input.equals("2")  && !input.equals("3") ){
                    System.out.println("Invalid Selection");
                    System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to " +
                            "main menu:");
                    break;
                }
                int coffeeRecipe = Integer.valueOf(input);
                makeCoffee(coffeeRecipe);
                state = machineState.AWAITING_INSTRUCTION;
                System.out.println("Write action (buy, fill, take, remaining, exit): ");
                break;
            case EXIT:
                break;
        }

    }

    public void makeCoffee(int recipe) {
        coffeeRecipes currentRecipe = null;
        switch (recipe){
            case 1:
                currentRecipe = coffeeRecipes.ESPRESSO;
                break;
            case 2:
                currentRecipe = coffeeRecipes.LATTE;
                break;
            case 3:
            default:
                currentRecipe = coffeeRecipes.CAPPUCCINO;//
                break;

        }
        if (resourceCheck(currentRecipe, mlOfWater, mlOfMilk, gramsOfCoffeeBeans, disposableCups)){
            System.out.println("I have enough resources, making you a coffee!");
            mlOfWater -= currentRecipe.getMlOfWater();
            mlOfMilk -= currentRecipe.getMlOfMilk();
            gramsOfCoffeeBeans -= currentRecipe.getGramsOfCoffeeBeans();
            money += currentRecipe.getCostInDollars();
            disposableCups --;
        }
    }

    public void printRemainingResources() {
        System.out.println("The coffee machine has:");
        System.out.println(mlOfWater + " ml of water");
        System.out.println(mlOfMilk + " ml of milk");
        System.out.println(gramsOfCoffeeBeans + " g of coffee beans");
        System.out.println(disposableCups + " disposable cups");
        System.out.println("$" + money + " of money");
    }


    public void processNewInstruction (String input) {
        switch (input) {
            case "BUY":
                System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to " +
                        "main menu:");
                state = machineState.SELLING_COFFEE;
                break;
            case "FILL":
                System.out.println("Write how many ml of water you want to add: ");
                state = machineState.ADDING_WATER;
                break;
            case "TAKE":
                System.out.println("I gave you $" + money);
                money = 0;
                System.out.println("Write action (buy, fill, take, remaining, exit): ");
                break;
            case "REMAINING":
                printRemainingResources();
                System.out.println("Write action (buy, fill, take, remaining, exit): ");
                break;
            case "EXIT":
                //System.out.println
                state = machineState.EXIT;
                break;
            default:
                System.out.println("Invalid Instruction");
                System.out.println("Write action (buy, fill, take, remaining, exit): ");
                break;
            }
        }

    public static boolean resourceCheck(coffeeRecipes recipe, int water, int milk, int coffee, int cups) {
        boolean enoughResources = true;
        if (water < recipe.getMlOfWater()) {
            System.out.println("Sorry, not enough water!");
            enoughResources = false;
        }
        if (milk < recipe.getMlOfMilk()) {
            System.out.println("Sorry, not enough milk!");
            enoughResources = false;
        }
        if (coffee < recipe.getGramsOfCoffeeBeans()) {
            System.out.println("Sorry, not enough coffee beans!");
            enoughResources = false;
        }
        if (cups < 1) {
            System.out.println("Sorry, not enough cups!");
            enoughResources = false;
        }
        return enoughResources;

    }


}


