package TendModule.predict;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConceptsFrequencyExtractor {

    public static int[] years = ConceptsValueExtractor.years;
    public static String pathFolderIn = ConceptsValueExtractor.pathFolderOut;
    public static String pathFolderOut = pathFolderIn.replace("value", "freq");

    public static void main(String[] args) throws IOException {
        //create folder to results
        PrUtils.createFolder(pathFolderOut);

        Map<Integer, Map<String, Double>> yearsMap = new HashMap<Integer, Map<String, Double>>();

        for (int year : years) {
            Map<String, Double> ConceptToFreq = new HashMap<String, Double>();
            Double totalDocIndex = 0.0;

            for (File file : new File(pathFolderIn+"//"+year).listFiles()) {
                Map<String, Double> fileConcepts = new HashMap<String, Double>();
                Double totalValue = 0.0;
                Integer docMod = 0;
                Integer semDocMod;
                Double docIndex = 0.0;

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                try {
                    docMod = Integer.parseInt(br.readLine());
                    semDocMod = Integer.parseInt(br.readLine());
                    docIndex = Math.log(semDocMod);
                    totalDocIndex += docIndex;
                    //Map<String, Double> res = new HashMap<String, Double>();
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] tokens = line.split("___");
                        String concept = tokens[0];
                        Double value = Double.parseDouble(tokens[1]);

                        totalValue += value;
                        fileConcepts.put(concept, value);
                    }
                } catch (Exception ex) {System.out.println("\n" + ex + "\n");} finally {
                    br.close();
                }

                for (String concept : fileConcepts.keySet()) {
                    Double oldValue = ConceptToFreq.containsKey(concept) ? ConceptToFreq.get(concept) : 0.0;
                    Double newValue = fileConcepts.get(concept)/totalValue*docIndex;/////////////////////
                    ConceptToFreq.put(concept, oldValue + newValue);
                }
            }

            for (String concept : ConceptToFreq.keySet()) {
                ConceptToFreq.put(concept, ConceptToFreq.get(concept)/totalDocIndex);
            }


            File out = new File(pathFolderOut+"//year"+year+".txt");
            out.delete();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "utf-8"));
            try {
                for (String concept : ConceptToFreq.keySet()) {
                    bw.append(concept + "___" + ConceptToFreq.get(concept) + "\n");
                }
            } finally {
                bw.close();
            }
        }

    }

}
