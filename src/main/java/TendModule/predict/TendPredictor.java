package TendModule.predict;

import com.github.stagirs.wiki.model.WikiPage;
import TendModule.model.utils.CatUtils;
import TendModule.model.utils.CatsTreeUtils;

import java.io.*;
import java.util.*;

public class TendPredictor {

    static String pathToSaveCatToConcepts0 = "CatToConcepts0.txt";
    static String pathToSaveCatToConcepts = "CatToConcepts.txt";
    static Set<String> Topics = CatsTreeUtils.getAllSubCats("математика");
    static Map<String, Double> ConceptToTend = new HashMap<String, Double>();
    static Map<String, Double> TopicToTend = new HashMap<String, Double>();
    static Map<String, Set<String>> CatToConcepts0 = new HashMap<String, Set<String>>();
    static Map<String, Set<String>> CatToConcepts = new HashMap<String, Set<String>>();


    static void buildConceptToTend() throws IOException {
        Map<Integer, Map<String, Double>> yearsMap = new HashMap<Integer, Map<String, Double>>();
        Set<String> concepts = new HashSet<String>();
        for (int i = 13; i <= 18; i++) {
            String year = "" + i;
            yearsMap.put(i, new HashMap<String, Double>());
            File in = new File("res7freq//year20" + year + ".txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(in), "utf-8"));
            try {
                //int countText = Integer.parseInt(br.readLine());
                String line;
                while ((line = br.readLine()) != null) {
                    String[] args = line.split("___");
                    String concept = args[0];
                    Double value = Double.parseDouble(args[1]);
                    if (true) {
                        yearsMap.get(i).put(concept, value);
                        concepts.add(concept);
                    }
                }
            } finally {
                br.close();
            }
        }
        Map<Integer, Double> koef = new HashMap<Integer, Double>();
        koef.put(13, -0.2);
        koef.put(14, 1.25);
        koef.put(15, -10.0/3);
        koef.put(16, 5.0);
        koef.put(17, -5.0);
        koef.put(18, 137.0/60);
        for (String concept : concepts) {
            Double tend = new Double(0);
            for (int i = 13; i <= 18; i++)
                tend += yearsMap.get(i).containsKey(concept) ? (koef.get(i) * yearsMap.get(i).get(concept)) : 0;
            ConceptToTend.put(concept, tend);
        }
        System.out.println("buildConceptToTend done!");
    }

    static void buildCatToConcepts0() throws IOException {
        for (String cat : Topics) {
            CatToConcepts0.put(cat, new HashSet<String>());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("ruwiki_onlymath_20190220_out.txt")), "utf-8"));
        try {
            String xml;
            int count = 0;
            while ((xml = br.readLine()) != null) {
                WikiPage page = WikiPage.fromXml(xml);
                String concept = CatUtils.getTitle(page);
                for (String cat : CatUtils.getAllCats(page)) {
                    if (Topics.contains(cat)) {
                        CatToConcepts0.get(cat).add(concept);
                    }
                }
                System.out.println(++count);
            }
        } finally {
            br.close();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(pathToSaveCatToConcepts0)), "utf-8"));
        try {
            for (String cat : CatToConcepts0.keySet()) {
                bw.append(cat);
                for (String concept : CatToConcepts0.get(cat)) {
                    bw.append("___" + concept);
                }
                bw.append("\n");
            }
        } finally {
            bw.close();
        }
    }

    static void readCatToConcepts0() throws IOException {
        for (String cat : Topics) {
            CatToConcepts0.put(cat, new HashSet<String>());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(pathToSaveCatToConcepts0)), "utf-8"));
        try {
            String line;
            //int count = 0;
            while ((line = br.readLine()) != null) {
                String[] args = line.split("___", 2);
                String cat = args[0];
                String[] concepts = args.length>1 ? args[1].split("___") : new String[0];
                for (String concept : concepts) {
                    CatToConcepts0.get(cat).add(concept);
                }
                //System.out.println(++count);
            }
        } finally {
            br.close();
        }
        System.out.println("readCatToConcepts0 done!");
    }

    static void buildCatToConcepts() throws IOException {
        readCatToConcepts0();

        int count = 0;
        for (String cat : Topics) {
            Set<String> concepts = new HashSet<String>();
            for (String subCat : CatsTreeUtils.getAllSubCats(cat)) {
                concepts.addAll(CatToConcepts0.get(subCat));
            }
            CatToConcepts.put(cat, concepts);
            System.out.println(++count + "\t" + cat);
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(pathToSaveCatToConcepts)), "utf-8"));
        try {
            for (String cat : CatToConcepts.keySet()) {
                bw.append(cat);
                for (String concept : CatToConcepts.get(cat)) {
                    bw.append("___" + concept);
                }
                bw.append("\n");
            }
        } finally {
            bw.close();
        }
    }

    static void readCatToConcepts() throws IOException {
        for (String cat : Topics) {
            CatToConcepts.put(cat, new HashSet<String>());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(pathToSaveCatToConcepts)), "utf-8"));
        try {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] args = line.split("___", 2);
                String cat = args[0];
                String[] concepts = args.length>1 ? args[1].split("___") : new String[0];
                for (String concept : concepts) {
                    CatToConcepts.get(cat).add(concept);
                }
                //System.out.println(++count);
            }
        } finally {
            br.close();
        }
        System.out.println("readCatToConcepts done!");
    }

    static void predictTopicsTend() throws IOException {

        for (String topic : Topics) {//del
            Double tend = 0.0;
            for (String concept : CatToConcepts.get(topic)) {
                if (ConceptToTend.containsKey(concept))
                    tend += ConceptToTend.get(concept);
            }
            TopicToTend.put(topic, tend);

            //System.out.println(CatToConcepts.get(topic));
        }
        System.out.println("predictTopicsTend done!");
    }

    public static void main(String[] args) throws IOException {
        buildConceptToTend();
        readCatToConcepts();
        predictTopicsTend();
        System.out.println("\n\n\n");

        List<Double> values = new ArrayList<Double>(TopicToTend.values());
        Collections.sort(values);
        Double limitMax = values.get(values.size()*990/1000);
        Double limitMin = values.get(values.size()*10/1000);

        System.out.println("Max:");
        for (String topic : TopicToTend.keySet()) {
            if (TopicToTend.get(topic) > limitMax) {
                System.out.println(topic + "\t" + TopicToTend.get(topic));
            }
        }
        System.out.println("\n\n\n\n\n\n\nMin:");
        for (String topic : TopicToTend.keySet()) {
            if (TopicToTend.get(topic) < limitMin) {
                System.out.println(topic + "\t" + TopicToTend.get(topic));
            }
        }

        Scanner in = new Scanner(System.in);
        String str;
        while (!(str = in.nextLine()).equals("")) {
            if (TopicToTend.containsKey(str)) {
                System.out.println(TopicToTend.get(str));
            }
        }



    }

    public static void main2(String[] args) throws IOException {
        buildCatToConcepts0();
    }

    public static void main3(String[] args) throws IOException {
        buildCatToConcepts();
    }


}
