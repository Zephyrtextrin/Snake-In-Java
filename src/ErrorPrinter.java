import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class ErrorPrinter {
    private Map<String,Error> errorDB = new HashMap<>();

    public void errorHandler(String code, boolean error){

        String errorCause;
        //first portion of error printing system: specifies whether the error is an actual issue or just something weird that shouldnt go unnoticed, and prints out details of what happened
        if(error){System.out.println("----[[[[ERROR!]]]]----");
        }else{System.out.println("---[[[ABNORMALITY]]]---");}
        switch(code){
            case "DNE": errorCause = "Length high-score not found or invalid!";
            break;

            default: errorCause = "Error code passed that does not exist!";
            break;
        }

        System.out.println();
    }

    private class Error{
        String tempstring = "----[[[[]]]]----\nLength high-score not found or invalid!\n\n---[[[DETAILS]]]---\nDoes the high-score document exist? "+DATA_FILE.exists()+"\nIs there a readable integer in the file? "+scan.hasNextInt()+"\nValue read: "+abnormalValue+"\n\n---[[[WHAT TO DO]]]---\nIf this is your first time running the program, you can probably ignore this.\n(if it is not your first time running the program and this is a genuine error, please contact me.)");

        String code;
        String cause;
        boolean isError;
        String details;
        String additional;

        private Error(String code, String cause, boolean isError String details, String additional){
            this.code = code;
            this.cause = cause;
            this.details = details;
            this.isError = isError;

            if(additional.equals("0")){additional="No additional Details.";
            this.additional = additional;
            errorDB.put(code, this);
        }
    }

    private void Initialize() throws FileNotFoundException {
        new Error("DNE", "Length high-score not found or invalid!", false, "Does the high-score document exist? "+DataReadingInterface.DATA_FILE.exists()+"\nIs there a readable integer in the file?\nValue read: "+DataReadingInterface.isReadable())
    }
}

