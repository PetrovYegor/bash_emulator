package bash_emulator;

import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Controller c = new Controller();
        c.initialize();
        System.out.println(Controller.getCurrentDirectory());

        Scanner s = new Scanner(System.in);

        while(true){
            String str = s.nextLine();
            if (str.equalsIgnoreCase("exit")){
                s.close();
                break;
            } else if (str.equalsIgnoreCase("ls")){
                c.showFiles(Paths.get(Controller.getCurrentDirectory()));
            } else if (str.contains("mkdir")){
                c.makeDirectory(str);
            } else if (str.equalsIgnoreCase("help")){
                c.showHelp();
            } else if (str.contains("cd ")){
                c.changeDirectory(str);
            } else if (str.contains("touch")){
                c.createOrUpdateFile(str);
            } else if (str.contains("cat ")){
                c.readAndPrint(str);
            } else if (str.contains("tail ")){
                c.printLastLines(str);
            } else if (str.contains("cp ")){
                c.copyFile(str);
            }
        }

    }
}
