package bash_emulator.util;

public class RegExpUtil {
    public static String removeBeginningAndEndingDoubleQuotes(String str){
        return str.replaceAll("^\"|\"$", "");
    }

    public static String removeBeginningAndEndingQuotes(String str){
        return str.replaceAll("^\'|\'$", "");
    }

    public static String removeFirstSlashes(String str){
        return str.replaceFirst("^/+", "/");
    }

    public static String removeLastSlashed(String str){
        return str.replaceFirst("/+$", "/");
    }

    public static String prepareInputString(String str){
        // если слеши будут и там и там после всех обработок - другой сценарий
        String temp = removeBeginningAndEndingDoubleQuotes(str);
        temp = removeBeginningAndEndingQuotes(temp);
        temp = removeFirstSlashes(temp);
        temp = removeLastSlashed(temp);
        return temp;
    }
}
