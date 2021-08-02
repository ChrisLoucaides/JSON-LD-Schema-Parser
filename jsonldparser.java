import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class jsonldparser {
    public static void main(String[] args) throws InterruptedException{
        parseJSON();
        System.exit(0);
    }

    public static String inputString(String message){
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        return scanner.nextLine();
    }

    public static int inputInt(String message){
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        return scanner.nextInt();
    }

    public static void printArray(String[] array){
        for(int i = 0; i < array.length; i++){
            System.out.println(array[i]);
        }
    }

    public static String jsonLDSignature(){
        String atType = inputString("Please enter the @type:  ");
        return "<script type='application/ld+json'>{\n \t '@context': 'https://schema.org/', \n \t '@type': '" + atType + "',\n";
    }

    public static String addSingleField(){
        String fieldName = inputString("Enter field name: ");
        String fieldValue = inputString("Enter field value: ");
        return "\n \t '" + fieldName + "': " + "'" + fieldValue +"'";
    }

    public static String addCommaAndNewLine(){
        return ", \n";
    }

    public static String addComma(){
        return ",";
    }

    public static String addSingleTypeList(){
        String listName = inputString("Enter list name: ");
        listName = "'" + listName + "': [";
        String choice = "";
        boolean addNew = true;

        while(addNew){
            listName += "'" + inputString("Enter new value: ") + "'";
            choice = inputString("Continue? Y/N: ");
            if(choice.equals("Y")){
                listName += ", ";
            }
            else{
                addNew = false;
                listName += "]";
                break;
            }
        }
        return "\t" + listName;
    }

    public static String addMultiObjectList(){
        String multiListName = inputString("Enter list name: ");
        multiListName = "\t \t'" + multiListName + "': [";
        boolean addNew = true;
        boolean addAnotherSingle = true;
        int choice = 0;
        boolean first = true;
        String innerChoice = "";
        String[] options = new String[3];
        options[0] = "Select [1] for single field schema object.";
        options[1] = "Select [2] for multi-field schema object.";
        options[2] = "Select [3] to cancel.";

        while(addNew){
            printArray(options);
            choice = inputInt("Enter choice: ");

            if(choice == 1){

                while(addAnotherSingle){
                    if(first){
                        multiListName += "{";
                        first = false;
                    }
                    multiListName += "\n \t \t";
                    multiListName += addSingleField();
                    innerChoice = inputString("Add another single field value? Y/N: ");
                    if(innerChoice.equals("Y")){
                        multiListName += ", \n \t";
                        addAnotherSingle = true;
                    }
                    else{
                        innerChoice = inputString("add more fields after? Y/N");
                        if(innerChoice.equals("Y")){
                            addAnotherSingle = false; //idk it works
                            break;
                        }
                        else{
                            addAnotherSingle = false;
                            break;
                        }
                    }

                }
            }
            else if(choice == 2){
                first = false;
                multiListName += "{";
                multiListName += "\n \t \t";
                multiListName += addSingleTypeList();
            }
            else{
                addNew = false;
                break;
            }
            innerChoice = inputString("Add another value? Y/N: ");
            if(innerChoice.equals("Y")){
                multiListName += "}, \n";
                addNew = true;
            }
            else{
                multiListName += "}]";
                addNew = false;
                break;
            }
        }
        return multiListName;
    }

    public static void parseJSON(){
        String toBeParsed = jsonLDSignature();
        boolean keepAddingValues = true;
        String[] options = new String[4];
        options[0] = "[1] Add a single field";
        options[1] = "[2] Add a single type list as a field";
        options[2] = "[3] Add a multiple object list as a field";
        options[3] = "[4] Stop adding values";
        String innerChoice = "";
        while(keepAddingValues){
            printArray(options);
            int choice = inputInt("Enter choice: ");
            if(choice == 4){
                System.out.println("end");
                keepAddingValues = false;
            }
            else if(choice == 1){
                toBeParsed += addSingleField();
                innerChoice = inputString("Add another field? Y/N: ");
                if(innerChoice.equals("Y")) {
                    toBeParsed += ", \n";
                }
                else{
                    keepAddingValues = false;
                }
            }
            else if(choice == 2){
                toBeParsed += addSingleTypeList();
                innerChoice = inputString("Add another field? Y/N: ");
                if(innerChoice.equals("Y")) {
                    toBeParsed += ",";
                }
                else{
                    keepAddingValues = false;
                }
            }
            else if(choice == 3){
                toBeParsed += addMultiObjectList();
                innerChoice = inputString("Add another field? Y/N: ");
                if(innerChoice.equals("Y")) {
                    toBeParsed += ",";
                }
                else{
                    keepAddingValues = false;
                }
            }
            else{
                System.out.println("Invalid number");
            }
        }
        toBeParsed += "} \n </script>";
        for(int i = 0; i <toBeParsed.length(); i++){
            toBeParsed.replace("'", "test" );
        }

        try{
            FileWriter myWriter = new FileWriter("JSON-LD-Schema.txt");
            myWriter.write(toBeParsed);
            myWriter.close();
            System.out.println("JSON-LD Parse was successful!");
        }
        catch(IOException e){
            System.out.println("Error writing to file");
            e.printStackTrace();
        }

    }
}
