package TendModule.predict;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConceptsValueExtractor {

    public static int[] years = ConceptsNumExtractor.years;
    public static String pathFolderIn = ConceptsNumExtractor.pathOut;
    public static String pathFolderOut = pathFolderIn.replace("num", "value");

    public static void main(String[] args) throws IOException {
        //create folder to results
        PrUtils.createFolder(pathFolderOut);

        for (int year : years) {
            Map<String, Integer> DocsMod = new HashMap<String, Integer>();
            Map<String, Integer> SemDocsMod = new HashMap<String, Integer>();
            Map<String, Map<String, Integer>> DocsIn = new HashMap<String, Map<String, Integer>>();
            Map<String, Integer> ConceptInDocs = new HashMap<String, Integer>();

            for (File file : new File(pathFolderIn+"//"+year).listFiles()) {
                String fileName = file.getName();
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                try {
                    DocsMod.put(fileName, Integer.parseInt(br.readLine()));
                    Integer semDocMod = 0;
                    Map<String, Integer> res = new HashMap<String, Integer>();
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] tokens = line.split("___");
                        String concept = tokens[0];
                        Integer num = Integer.parseInt(tokens[1]);

                        semDocMod += num;
                        ConceptInDocs.put(concept, ConceptInDocs.containsKey(concept) ? ConceptInDocs.get(concept) + 1 : 1);
                        res.put(concept, num);
                    }
                    SemDocsMod.put(fileName, semDocMod);
                    DocsIn.put(fileName, res);
                } catch (Exception ex) {System.out.println("\n" + ex + "\n");} finally {
                    br.close();
                }
                System.out.print("|");
            }

            System.out.println();

            for (String doc : DocsIn.keySet()) {
                File out = new File(pathFolderOut+"//"+year+"//"+doc);
                out.delete();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "utf-8"));
                try {
                    int docMod = DocsMod.get(doc);
                    int semDocMod = SemDocsMod.get(doc);
                    Map<String, Integer> thisDoc = DocsIn.get(doc);
                    bw.append(docMod + "\n");
                    bw.append(semDocMod + "\n");
                    for (String concept : thisDoc.keySet()) {
                        Double TF_IDF = (thisDoc.get(concept)*1.0/semDocMod)*(Math.log(DocsMod.size()*1.0/ConceptInDocs.get(concept)));
                        bw.append(concept + "___" + TF_IDF + "\n");
                    }
                } finally {
                    bw.close();
                }
                System.out.print("|");
            }

            System.out.println();
            System.out.println("year " + year + " done!");


        }

    }



}
