package TendModule.builders;

import com.github.stagirs.lingvo.syntax.DisambiguityProcessor;
import com.github.stagirs.lingvo.syntax.SentenceExtractor;
import com.github.stagirs.lingvo.syntax.model.Sentence;
import com.github.stagirs.wiki.model.WikiPage;
import com.github.stagirs.wiki.model.WikiText;
import com.github.stagirs.wiki.model.text.WikiLink;
import TendModule.model.utils.StringFormUtils;
import TendModule.model.TCC;
import TendModule.model.utils.CatUtils;
import TendModule.model.utils.SentenceUtils;

import java.io.*;
import java.util.List;
import java.util.Set;

public class SimpleTermBuilder {

    private static File in = new File("ruwiki_20190220_out.txt");
    private static File out = new File("term_concept.txt");
    private static File inMath = new File("ruwiki_onlymath_20190220_out.txt");

    private static int total = 4400000;

    public static void main(String[] args) throws IOException {
        out.delete();

        Set<String> concepts = SentenceUtils.getMathConcenpts();

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(in), "utf-8"));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out, true), "utf-8"));

        try {
            String xml;
            WikiPage page;
            int count = 0;
            int countLinks = 0;
            while ((xml = br.readLine()) != null) {
                page = WikiPage.fromXml(xml);
                for (WikiText wikiText : page.getAllPoints()) {
                    String text = wikiText.getText();
                    if (text.trim().equals("")) continue;
                    List<Sentence> sentenceList = SentenceExtractor.extract(text);
                    for (Sentence sentence : sentenceList) {
                        DisambiguityProcessor.process(sentence);
                        //System.out.println("Sentence:\n" + sentence.getText());

                        for (WikiLink link : wikiText.getLinks()) {
                            if (!(concepts.contains(StringFormUtils.getSimpleForm(link.getId())))) {
                                continue;
                            }
                            if (SentenceUtils.linkIntoSentence(sentence, link)) {
                                String term = SentenceUtils.getNormTerm(sentence, link, false);
                                //if (term.length() < 4) continue;
                                String concept = StringFormUtils.getSimpleForm(link.getId());
                                String context = SentenceUtils.getNorm(sentence, false);//todo
                                int beginIndex = SentenceUtils.getBeginTermIndex(sentence, link);

                                TCC tc = new TCC(term, concept, context, beginIndex);

                                bw.append(tc.toJSON() + "\n");
                                if (term.equals(""))
                                    System.out.println(tc.toJSON());
                                countLinks++;
                            }
                        }
                    }
                }
                count++;
                if (count % 1000 == 0)
                    System.out.printf("process: %d\t(%.3f)\tlinks: %d\n", count, count*100.0/total, countLinks);
            }
        } finally {
            br.close();
            //bw.close();
        }

        br = new BufferedReader(new InputStreamReader(new FileInputStream(inMath), "utf-8"));
        try {
            String xml;
            int count = 0;
            //int countLinks = 0;
            while ((xml = br.readLine()) != null) {
                WikiPage page = WikiPage.fromXml(xml);
                String concept = CatUtils.getTitle(page);
                Sentence sentence = SentenceExtractor.extract(concept.replace(String.valueOf((char) 769), "")).get(0);
                DisambiguityProcessor.process(sentence);
                String term = SentenceUtils.getNorm(sentence);

                //context, new version:
                String context = StringFormUtils.getConceptForm(concept);

                TCC st = new TCC(term, concept, context, 0);
                bw.append(st.toJSON() + "\n");
                if (term.equals(""))
                    System.out.println(st.toJSON());
                count++;
                System.out.println("process: " + count);
            }

        } finally {
            br.close();
        }

        bw.close();

    }
}
