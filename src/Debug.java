public class Debug {
    public static void runDebugFunctions(char key){
        boolean isRealFunction = true;
        switch(key){
            case 'q':
                System.out.println("[DEBUG]: ATTEMPTING TO PRINT CURRENT FOOD POSITION");
                getFoodPosition();
                break;

            case 'e':
                System.out.println("[DEBUG]: ATTEMPTING TO GENERATE THE FRUIT OF LIFE");
                Board.createFood();
                break;

            case 'r':
                System.out.println("[DEBUG]: ATTEMPTING TO INVOKE ABSTRUSE ERROR");
                ErrorPrinter.errorHandler("DEBUG", null);
                break;

            case 't':
                displayOptions();
                isRealFunction = false;
                break;

            default:
                isRealFunction = false;
                break;
        }
        if(isRealFunction){System.out.println("\n[DEBUG]: CURRENT FUNCTION FINISHED\n");}
    }

    //[DEBUG ONLY] not optimized idgaf tho
    private static void getFoodPosition(){

        try {
            int food = 0;
            for (int row = 1; row < Board.getBoardSize(); row++) {
                for (int col = 1; col < Board.getBoardSize(); col++) {
                    Board.Cell cell = Board.cellList[row][col];
                    if (cell.type == Board.STRING_CONSTANTS.TYPE_FOOD) {
                        System.out.println("\n[ROW]: " + cell.ROW + "\n[COLUMN]: " + cell.COLUMN);
                        food++;
                    }
                }
            }
            if (food == 0) {
                System.out.println("[DEBUG]: DOES NOT EXIST");
            }
        }catch(Exception e){ErrorPrinter.errorHandler("ABN_GM_DEBUG_GENERIC_EXCEPTION", e);}
    }

    public static void displayOptions(){
        System.out.println("\nQ: PRINT CURRENT FRUIT POSITION\nE: CREATE A NEW FRUIT\nR: INVOKE ABSTRUSE ERROR\nT: DISPLAY ALL DEBUG OPTIONS\n");
        System.out.println("note: u dont type these in to the terminal u just hit the key while the game is running. doesnt work when ur in the color settings tho cause im lazy\nmost of these probably wont be of use to you specifically but if u get that stupid fucking glitch where the food just doesnt respawn \nafter u eat it and theres no way to grow bigger pllleeeaaasssseeee hit Q bro and send me the results pls\n-alexander");
    }
}
