package TendModule.builders;

import TendModule.predict.ConceptPredictor;

import java.io.*;
import java.util.*;

public class ModelBuilder {

    public static String path = "D://GitHub//downloads PDF//downloads//";
    public static String folder = "MathNet//";
    //public static int[] years = new int[]{2018,2017/*,2016,2015,2014,2013,2012,2011,2010,2009,2008,2007,2006,2005,2004,2003,2002,2001,2000/*,1999,1998,1997,1996,1995,1994,1993,1992,1991,1990,1989,1988,1987,1986,1985,1984,1983,1982,1981*/};
    //public static int[] years = new int[]{11};

    /*
    public static void main1(String[] args) throws IOException {
        int[] years = new int[]{2018,2017,2016,2015,2014,2013};
        ConceptPredictor conceptPredictor = ConceptPredictor.getPredictorAndReadW2V();
        Map<String, Double> totalConcepts = new HashMap<String, Double>();

        for(int year : years) {
            File in = new File(path+folder+year);
            int countNotEmptyFile = 0;
            for (File file : in.listFiles()) {
                if (!file.getName().contains(".txt")) continue;
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                try {
                    Map<String, Double> conceptsFile = new HashMap<String, Double>();
                    //int count = 0;
                    String text;
                    while ((text = br.readLine()) != null) {
                        Map<String, Double> concepts = conceptPredictor.predictConcepts(text);
                        for (String concept : concepts.keySet()) {
                            double newValue = concepts.get(concept);
                            double oldValue = conceptsFile.containsKey(concept) ? conceptsFile.get(concept) : 0;
                            conceptsFile.put(concept, oldValue + newValue);
                            //count++;
                        }
                        /////////////переместить из цикла while
                    }
                    if (conceptsFile.isEmpty()) {
                        System.out.print(".");
                    } else {
                        countNotEmptyFile++;
                        System.out.print("|");
                    }
                    for (String concept : conceptsFile.keySet()) {
                        double newValue = conceptsFile.get(concept);
                        double oldValue = totalConcepts.containsKey(concept) ? totalConcepts.get(concept) : 0;
                        totalConcepts.put(concept, oldValue + newValue);
                    }
                } catch (Exception ex) {System.out.println("\n" + ex + "\n");} finally {
                    br.close();
                }
            }

            System.out.println("year " + year + ":");
            System.out.println(totalConcepts);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("year"+year+".txt")), "utf-8"));
            try {
                bw.append(countNotEmptyFile + "\n");
                for (String concept : totalConcepts.keySet()) {
                    bw.append(concept + "___" + totalConcepts.get(concept)*1000 + "\n");
                }
            } finally {
                bw.close();
            }

        }
    }

    public static void main2(String[] args) throws IOException {
        int[] years = new int[]{2018,2017,2016,2015,2014,2013};
        ConceptPredictor conceptPredictor = ConceptPredictor.getPredictorAndReadW2V();

        for(int year : years) {
            File in = new File(path+folder+year);
            Map<String, Double> yearConcepts = new HashMap<String, Double>();
            int countNotEmptyFile = 0;
            for (File file : in.listFiles()) {
                if (!file.getName().contains(".txt")) continue;
                Map<String, Double> conceptsFile = new HashMap<String, Double>();
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                try {
                    String text;
                    while ((text = br.readLine()) != null) {
                        Map<String, Double> concepts = conceptPredictor.predictConcepts(text);
                        for (String concept : concepts.keySet()) {
                            double newValue = concepts.get(concept);
                            double oldValue = conceptsFile.containsKey(concept) ? conceptsFile.get(concept) : 0;
                            conceptsFile.put(concept, oldValue + newValue);
                        }
                    }

                } catch (Exception ex) {System.out.println("\n" + ex + "\n");} finally {
                    br.close();
                }
                if (conceptsFile.isEmpty()) {
                    System.out.print(".");
                } else {
                    countNotEmptyFile++;
                    System.out.print("|");
                }
                for (String concept : conceptsFile.keySet()) {
                    double newValue = conceptsFile.get(concept);
                    double oldValue = yearConcepts.containsKey(concept) ? yearConcepts.get(concept) : 0;
                    yearConcepts.put(concept, oldValue + newValue);
                }
            }

            System.out.println("year " + year + ":");
            System.out.println(yearConcepts);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("year"+year+".txt")), "utf-8"));
            try {
                bw.append(countNotEmptyFile + "\n");
                for (String concept : yearConcepts.keySet()) {
                    bw.append(concept + "___" + yearConcepts.get(concept)*1000 + "\n");
                }
            } finally {
                bw.close();
            }

        }
    }

    public static void main4(String[] args) throws IOException {
        int[] years = new int[]{2018,2017,2016,2015,2014,2013};
        ConceptPredictor conceptPredictor = ConceptPredictor.getPredictorAndReadW2V();

        for(int year : years) {
            File in = new File(path+folder+year);
            Map<String, Integer> yearConcepts = new HashMap<String, Integer>();
            int countNotEmptyFile = 0;
            for (File file : in.listFiles()) {
                if (!file.getName().contains(".txt")) continue;
                Map<String, Double> conceptsFile = new HashMap<String, Double>();
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                try {
                    String text;
                    while ((text = br.readLine()) != null) {
                        Map<String, Double> concepts = conceptPredictor.predictConcepts(text);
                        for (String concept : concepts.keySet()) {
                            double newValue = concepts.get(concept);
                            double oldValue = conceptsFile.containsKey(concept) ? conceptsFile.get(concept) : 0;
                            conceptsFile.put(concept, oldValue + newValue);
                        }
                    }

                } catch (Exception ex) {System.out.println("\n" + ex + "\n");} finally {
                    br.close();
                }
                if (conceptsFile.isEmpty()) {
                    System.out.print(".");
                } else {
                    countNotEmptyFile++;
                    System.out.print("|");
                }
                List<Double> values = new ArrayList<Double>(conceptsFile.values());
                Collections.sort(values);

                for (String concept : conceptsFile.keySet()) {
                    if (conceptsFile.get(concept) > values.get(values.size()*8/10)) {
                        Integer oldValue = yearConcepts.containsKey(concept) ? yearConcepts.get(concept) : 0;
                        yearConcepts.put(concept, oldValue + 1);
                    }
                }
            }

            System.out.println("\nyear " + year + ":");
            System.out.println(yearConcepts);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("year"+year+".txt")), "utf-8"));
            try {
                bw.append(countNotEmptyFile + "\n");
                for (String concept : yearConcepts.keySet()) {
                    bw.append(concept + "___" + yearConcepts.get(concept) + "\n");
                }
            } finally {
                bw.close();
            }

        }
    }

    public static void main5(String[] args) throws IOException {
        int[] years = new int[]{2018,2017,2016,2015,2014,2013};
        ConceptPredictor conceptPredictor = ConceptPredictor.getPredictorAndReadW2V();

        for(int year : years) {
            File in = new File(path+folder+year);
            Map<String, Integer> yearConcepts = new HashMap<String, Integer>();
            int countNotEmptyFile = 0;
            for (File file : in.listFiles()) {
                if (!file.getName().contains(".txt")) continue;
                Map<String, Double> conceptsFile = new HashMap<String, Double>();
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                try {
                    String text;
                    while ((text = br.readLine()) != null) {
                        Map<String, Double> concepts = conceptPredictor.predictConcepts(text);
                        for (String concept : concepts.keySet()) {
                            double newValue = concepts.get(concept) * 100;
                            double oldValue = conceptsFile.containsKey(concept) ? conceptsFile.get(concept) : 0;
                            conceptsFile.put(concept, oldValue + newValue);
                        }
                    }

                } catch (Exception ex) {System.out.println("\n" + ex + "\n");} finally {
                    br.close();
                }

                //System.out.println(conceptsFile);

                if (conceptsFile.isEmpty()) {
                    System.out.print(".");
                } else {
                    countNotEmptyFile++;
                    System.out.print("|");
                }
                //List<Double> values = new ArrayList<Double>(conceptsFile.values());
                //Collections.sort(values);

                for (String concept : conceptsFile.keySet()) {
                    if (conceptsFile.get(concept) > 1) {
                        Integer oldValue = yearConcepts.containsKey(concept) ? yearConcepts.get(concept) : 0;
                        yearConcepts.put(concept, oldValue + 1);
                    }
                }
            }

            System.out.println("\nyear " + year + ":");
            System.out.println(yearConcepts);

            File out = new File("year"+year+".txt");
            out.delete();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "utf-8"));
            try {
                bw.append(countNotEmptyFile + "\n");
                for (String concept : yearConcepts.keySet()) {
                    bw.append(concept + "___" + yearConcepts.get(concept) + "\n");
                }
            } finally {
                bw.close();
            }

        }
    }
    */

    public static void main(String[] args) throws IOException {
        int[] years = new int[]{2018,2017,2016,2015,2014,2013};
        ConceptPredictor conceptPredictor = ConceptPredictor.getPredictorAndReadW2V();

        for (int year : years) {
            File in = new File(path+folder+year);
            Map<String, Integer> yearConcepts = new HashMap<String, Integer>();
            int countNotEmptyFile = 0;
            for (File file : in.listFiles()) {
                if (!file.getName().contains(".txt")) continue;
                Map<String, Integer> conceptsFile = new HashMap<String, Integer>();
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                try {
                    String text;
                    while ((text = br.readLine()) != null) {
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

                if (conceptsFile.isEmpty()) {
                    System.out.print(".");
                } else {
                    countNotEmptyFile++;
                    System.out.print("|");
                }


                for (String concept : conceptsFile.keySet()) {
                    //if (conceptsFile.get(concept) > 1) {
                        Integer oldValue = yearConcepts.containsKey(concept) ? yearConcepts.get(concept) : 0;
                        Integer newValue = conceptsFile.get(concept);
                        yearConcepts.put(concept, oldValue + newValue);
                    //}
                }
            }

            System.out.println("\nyear " + year + ":");
            System.out.println(yearConcepts);

            File out = new File("year"+year+".txt");
            out.delete();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "utf-8"));
            try {
                bw.append(countNotEmptyFile + "\n");
                for (String concept : yearConcepts.keySet()) {
                    bw.append(concept + "___" + yearConcepts.get(concept) + "\n");
                }
            } finally {
                bw.close();
            }

        }
    }

}
