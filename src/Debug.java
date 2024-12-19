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
            for (int row = 1; row < GameUI.boardSize; row++) {
                for (int col = 1; col < GameUI.boardSize; col++) {
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

    public static void displayOptions(){System.out.println("\nQ: PRINT CURRENT FRUIT POSITION\nE: CREATE A NEW FRUIT\nR: INVOKE ABSTRUSE ERROR\nT: DISPLAY ALL DEBUG OPTIONS\n");}
}
