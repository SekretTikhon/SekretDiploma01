package TendModule.builders;

import TendModule.model.TCC;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TermCountMetBuilder {

    private static File in = new File("sourceText.txt");
    private static File out = new File("Term_CountMet.txt");
    private static String path = "term_concept.txt";

    private static int total = 4400000;

    public static void main(String[] args) throws IOException {
        out.delete();

        Map<String, Integer> termsCount = new HashMap<String, Integer>();

        for (TCC tcc : TCC.getAllST(path)) {
            String term = tcc.getTerm();
            termsCount.put(term, 0);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(in), "utf-8"));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "utf-8"));
        try {
            String text;// = br.readLine();
            int whileCount = 0;
            while ((text = br.readLine()) != null) {
            //while (true) {
                text += /*br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +*/
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine() + "\n" +
                        br.readLine();
                //if (text.trim().equals("")) break;
                for (String term : termsCount.keySet()) {
                    int indexFrom = 0;
                    int count = 0;
                    int indexFind;
                    while ((indexFind = text.indexOf(term, indexFrom)) != -1) {
                        indexFrom = indexFind + 1;
                        count++;
                    }
                    if (count != 0) {
                        termsCount.put(term, termsCount.get(term) + count);
                    }
                }
                whileCount += 10;
                if (whileCount % 1000 == 0)
                    System.out.printf("process: %d\t(%.3f)\n", whileCount, whileCount*100.0/total);
            }

            for (String term : termsCount.keySet()) {
                if (term.contains("___")) System.out.println("lol,\t" + term + "\tcontains ___");
                bw.append(term + "___" + termsCount.get(term) + "\n");
            }



        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            br.close();
            bw.close();
        }
    }


}
