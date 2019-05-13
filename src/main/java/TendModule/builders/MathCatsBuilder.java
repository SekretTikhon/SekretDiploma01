package TendModule.builders;

import TendModule.model.utils.CatsTreeUtils;

import java.io.*;
import java.util.Set;

public class MathCatsBuilder {

    public static void main(String[] args)throws IOException {
        File out = new File("mathCats.txt");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "utf-8"));
        Set<String> subCats = CatsTreeUtils.getAllSubCats("математика");
        for (String cat : subCats) {
            bw.append(cat + "\n");
        }
        System.out.println(subCats.size());
    }



}
