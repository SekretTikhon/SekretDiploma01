package TendModule.predict;

import java.io.File;

public class PrUtils {

    static void createFolder(String path) {
        File theDir = new File(path);
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                System.out.println(se);
            }
            if (result) {
                System.out.println("DIR created");
            }
        }
    }

}
