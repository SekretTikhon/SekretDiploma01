package TendModule.model.utils;

public class StringFormUtils {

    public static String getSimpleForm (String str) {
        return str
                .replace("_", " ")
                .toLowerCase()
                .trim()
                .replace("\u200E", "");
    }
    public static String getConceptForm (String str) {
        return "_" + str
                .replace("\u200E", "")
                .toLowerCase()
                .trim()
                .replace(" ", "_");

    }



}
