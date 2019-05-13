package TendModule.builders;

import TendModule.model.utils.StringFormUtils;
import com.github.stagirs.lingvo.syntax.DisambiguityProcessor;
import com.github.stagirs.lingvo.syntax.SentenceExtractor;
import com.github.stagirs.lingvo.syntax.model.Sentence;
import com.github.stagirs.wiki.model.WikiPage;
import com.github.stagirs.wiki.model.WikiText;
import com.github.stagirs.wiki.model.text.WikiLink;
import TendModule.model.utils.SentenceUtils;

import java.io.*;
import java.util.List;
import java.util.Set;

public class PreW2VTextBuilder {

    private static boolean removeStopWords = true;

    private static File in = new File("ruwiki_20190220_out.txt");
    private static File out = new File("text_.txt");
    private static File outSource = new File("sourceText_.txt");

    private static int total = 4400000;

    public static void main(String[] args) throws IOException {
        out.delete();

        Set<String> concepts = SentenceUtils.getMathConcenpts();

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(in), "utf-8"));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "utf-8"));
        BufferedWriter sbw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outSource), "utf-8"));

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
                    List<Sentence> sentenceList = SentenceExtractor.extract(text.replace(String.valueOf((char) 769), ""));//todo del replase??
                    for (Sentence sentence : sentenceList) {
                        DisambiguityProcessor.process(sentence);
                        bw.append(SentenceUtils.getNorm(sentence, true) + "\n");
                        sbw.append(SentenceUtils.getNorm(sentence, false) + "\n");

                        for (WikiLink link : wikiText.getLinks()) {
                            if (!(concepts.contains(StringFormUtils.getSimpleForm(link.getId())))) {
                                continue;
                            }
                            if (SentenceUtils.linkIntoSentence(sentence, link)) {
                                bw.append(SentenceUtils.getNorm(sentence, link, true) + "\n");
                                countLinks++;
                            }
                        }
                    }
                }
                if (++count % 1000 == 0)
                    System.out.printf("process: %d\t(%.3f)\tlinks: %d\n", count, count*100.0/total, countLinks);
            }
        } finally {
            br.close();
            bw.close();
        }

    }

}
