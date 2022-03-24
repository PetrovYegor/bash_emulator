package bash_emulator.util;

import bash_emulator.Controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PathUtil {
    public static final Character[] INVALID_WINDOWS_SPECIFIC_CHARS = {'"', '*', ':', '<', '>', '?', '\\', '|', 0x7F};
    public static final Character[] INVALID_UNIX_SPECIFIC_CHARS = {'\000'};
    public static final Map<String, Path> map = new HashMap<>();

    public static void InitializeMap(){
        map.put("~", Paths.get(System.getProperty("user.home")));
        map.put("/", Paths.get("/").toAbsolutePath());
        //map.put("\\", Paths.get("/").toAbsolutePath());//должен быть ввод
        map.put(".", Paths.get(Controller.currentDir));
        map.put("./", Paths.get(Controller.currentDir));
        map.put(".\\", Paths.get(Controller.currentDir));
        map.put("..", Paths.get(Controller.currentDir).getParent());
        map.put("../", Paths.get(Controller.currentDir).getParent());
        map.put("..\\", Paths.get(Controller.currentDir).getParent());
    }



    public static Character[] getInvalidCharsByOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return INVALID_WINDOWS_SPECIFIC_CHARS;
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return INVALID_UNIX_SPECIFIC_CHARS;
        } else {
            return new Character[]{};
        }
    }

    public static int countBackwardsSlashes(String str){
        int counter = 0;

        for (int i = 0; i < str.length(); i++){
            if (i != str.length() - 1){
                if (str.substring(i, i+1).equals("\\")){
                    counter++;
                }
            } else {
                if (str.substring(i).equals("\\")){
                    counter++;
                }
            }
        }
        return counter;
    }

    //https://www.baeldung.com/java-validate-filename#1-using-stringcontains
    public static boolean validateStringFilenameUsingContains(String filename) {
        if (filename == null || filename.isEmpty() || filename.length() > 255) {
            return false;
        }
        return Arrays.stream(getInvalidCharsByOS())
                .noneMatch(ch -> filename.contains(ch.toString()));
    }

    public static void recalculateMap() {
            map.put(".", Paths.get(Controller.currentDir));//возможно заменить put на другой метод типо ifAbsent()
            map.put("./", Paths.get(Controller.currentDir));//возможно заменить put на другой метод типо ifAbsent()
            map.put(".\\", Paths.get(Controller.currentDir));//возможно заменить put на другой метод типо ifAbsent()
            map.put("..", Paths.get(Controller.currentDir).getParent());
            map.put("../", Paths.get(Controller.currentDir).getParent());
            map.put("..\\", Paths.get(Controller.currentDir).getParent());
    }
}
