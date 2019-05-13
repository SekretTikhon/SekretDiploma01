package TendModule.predict;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConceptsNumExtractor {

    public static String pathFolder = "D://GitHub//downloads PDF//downloads//MathNet//";
    public static int[] years = new int[]{2018,2017,2016,2015,2014,2013};
    public static String pathOut = "res7num";

    public static void main(String[] args) throws IOException {
        ConceptPredictor conceptPredictor = ConceptPredictor.getPredictorAndReadW2V();

        //create folder to results
        PrUtils.createFolder(pathOut);

        for (int year : years) {
            //create folder to year resilts
            PrUtils.createFolder(pathOut+"//"+year);

            for (File file : new File(pathFolder+year).listFiles()) {
                if (!file.getName().contains(".txt")) continue;

                int countOfWords = 0;
                Map<String, Integer> conceptsFile = new HashMap<String, Integer>();

                //read file & predict concepts
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                try {
                    String text;
                    while ((text = br.readLine()) != null) {
                        countOfWords += text.split(" ").length;
                        Map<String, Integer> concepts = conceptPredictor.predictConcepts(text);
                        for (String concept : concepts.keySet()) {
                            Integer newValue = concepts.get(concept);
                            Integer oldValue = conceptsFile.containsKey(concept) ? conceptsFile.get(concept) : 0;
                            conceptsFile.put(concept, oldValue + newValue);
                        }
                    }

                } catch (Exception ex) {System.out.println("\n" + ex + "\n");} finally {
                    br.close();
                }

                //write results
                if (!conceptsFile.isEmpty()) {
                    //запись в файл найденные понятия и их количество
                    File out = new File(pathOut+"//"+year+"//"+file.getName());
                    out.delete();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "utf-8"));
                    try {
                        bw.append(countOfWords + "\n");
                        for (String concept : conceptsFile.keySet()) {
                            bw.append(concept + "___" + conceptsFile.get(concept) + "\n");
                        }
                    } finally {
                        bw.close();
                    }
                    System.out.print("|");
                } else {
                    System.out.print(".");
                }
            }
        }
    }
}
