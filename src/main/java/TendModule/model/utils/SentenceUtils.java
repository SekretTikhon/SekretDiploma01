package TendModule.model.utils;

import com.github.stagirs.lingvo.morph.MorphAnalyzer;
import com.github.stagirs.lingvo.morph.model.Morph;
import com.github.stagirs.lingvo.syntax.model.Sentence;
import com.github.stagirs.lingvo.syntax.model.SyntaxItem;
import com.github.stagirs.lingvo.syntax.model.items.AmbigSyntaxItem;
import com.github.stagirs.lingvo.syntax.model.items.WordSyntaxItem;
import com.github.stagirs.wiki.model.text.WikiLink;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class SentenceUtils {

    public static String removeStopWords(String line) {
        String[] words = line.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            Morph morph = MorphAnalyzer.get(word);
            if (morph != null && morph.isStop()) {
                continue;
            }
            result.append(word + " ");
        }
        return result.toString().trim();
    }

    public static boolean linkIntoSentence(Sentence sentence, WikiLink link) {
        return link.getPos() >= sentence.getPos() && link.getPos() < sentence.getPos() + sentence.getText().length();
    }

    public static String getNorm(Sentence sentence, WikiLink link, boolean removeStopWords) {
        if (removeStopWords)
            return removeStopWords(getNorm(sentence, link));
        else
            return getNorm(sentence, link);
    }

    public static String getNorm(Sentence sentence, boolean removeStopWords) {
        if (removeStopWords)
            return removeStopWords(getNorm(sentence));
        else
            return getNorm(sentence);
    }

    public static int getBeginTermIndex(Sentence sentence, WikiLink link) {
        int linkStart = link.getPos();

        /*
        StringBuilder resultBegin = new StringBuilder();
        for (SyntaxItem item : sentence.getSyntaxItem()) {
            //int itemStart = sentence.getPos() + item.getIndexFrom();
            int itemFinish = sentence.getPos() + item.getIndexTo();
            if (itemFinish < linkStart) {
                resultBegin.append(getNorm(item) + " ");
            } else {
                break;
            }
        }
        int strRes = resultBegin.length();
        */

        int intRes = 0;
        for (SyntaxItem item : sentence.getSyntaxItem()) {
            //int itemStart = sentence.getPos() + item.getIndexFrom();
            int itemFinish = sentence.getPos() + item.getIndexTo();
            int length = (getNorm(item) + " ").length();
            if (itemFinish < linkStart) {
                intRes += length;
            } else {
                break;
            }
        }


        //System.out.println("Sentence:\t" + sentence.getText());
        //System.out.println("resultBegin:\t" + resultBegin);

        //System.out.println("strRes:\t" + strRes);
        //System.out.println("intRes:\t" + intRes);

        //System.out.println();

        return intRes;
    }

    public static String getNorm(Sentence sentence, WikiLink link) {
        StringBuilder resultBegin = new StringBuilder();
        StringBuilder resultEnd = new StringBuilder();
        String resultLink = StringFormUtils.getConceptForm(link.getId());

        int linkStart = link.getPos();
        int linkFinish = link.getPos() + link.getText().length();

        for (SyntaxItem item : sentence.getSyntaxItem()) {
            //if (item instanceof PunctMarkSyntaxItem) continue;
            int itemStart = sentence.getPos() + item.getIndexFrom();
            int itemFinish = sentence.getPos() + item.getIndexTo();
            if (itemFinish < linkStart) {
                resultBegin.append(getNorm(item) + " ");
            }
            if (itemStart > linkFinish) {
                resultEnd.append(" " + getNorm(item));
            }
        }
        return (resultBegin.toString() + resultLink + resultEnd.toString()).trim();
    }

    public static String getNorm(Sentence sentence) {
        StringBuilder result = new StringBuilder();

        for (SyntaxItem item : sentence.getSyntaxItem()) {
            //if (item instanceof PunctMarkSyntaxItem) continue;
            result.append(" " + getNorm(item));
        }
        return result.toString().trim().toLowerCase();
    }

    private static String getNorm(SyntaxItem item) {
        if (item instanceof WordSyntaxItem) {
            return ((WordSyntaxItem)item).getNormTerm().trim();
        } else if (item instanceof AmbigSyntaxItem) {
            return ((AmbigSyntaxItem)item).getSyntaxItems().get(0).getNormTerm().trim();
        //} else if (item instanceof PunctMarkSyntaxItem) {
        //    return null;
        } else {
            return item.getName().trim();
        }
    }

    public static String getNormTerm(Sentence sentence, WikiLink link, boolean removeStopWords) {
        if (removeStopWords)
            return removeStopWords(getNormTerm(sentence, link));
        else
            return getNormTerm(sentence, link);
    }

    public static String getNormTerm(Sentence sentence, WikiLink link) {
        StringBuilder result = new StringBuilder();
        int linkStart = link.getPos();
        int linkFinish = link.getPos() + link.getText().length();

        for (SyntaxItem item : sentence.getSyntaxItem()) {
            //if (item instanceof PunctMarkSyntaxItem) continue;
            int itemStart = sentence.getPos() + item.getIndexFrom();
            int itemFinish = sentence.getPos() + item.getIndexTo();
            if (itemFinish > linkStart && itemStart < linkFinish) {
                result.append(getNorm(item) + " ");
            }
        }
        if (result.toString().trim().equals(""))
            System.out.println("bgbgbgbg");
        return result.toString().trim();
    }

    public static Set<String> getConcepts(String path) throws IOException {
        Set<String> result = new HashSet<String>();
        File inMathConcept = new File(path);
        BufferedReader brConcept = new BufferedReader(new InputStreamReader(new FileInputStream(inMathConcept), "utf-8"));
        try {
            String xml;
            while ((xml = brConcept.readLine()) != null) {
                result.add(CatUtils.getTitle(xml));
            }
        } finally {
            brConcept.close();
        }
        return result;
    }

    public static Set<String> getMathConcenpts() throws IOException {
        return getConcepts("ruwiki_onlymath_20190220_out.txt");
    }

}
