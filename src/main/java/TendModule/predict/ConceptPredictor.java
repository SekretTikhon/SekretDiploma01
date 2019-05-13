package TendModule.predict;

import com.github.stagirs.lingvo.syntax.DisambiguityProcessor;
import com.github.stagirs.lingvo.syntax.SentenceExtractor;
import com.github.stagirs.lingvo.syntax.model.Sentence;
import com.twelvemonkeys.util.LinkedMap;
import TendModule.model.TCC;
import TendModule.model.Term;
import TendModule.model.utils.SentenceUtils;
import org.deeplearning4j.examples.nlp.word2vec.W2VUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.ops.transforms.Transforms;

import java.io.Serializable;
import java.util.*;
import java.io.*;

public class ConceptPredictor implements Serializable {
    private static String path = "term_concept.txt";
    public Map<String, Term> termsMap = new HashMap<String, Term>();
    private Map<String, Integer> conceptCountMap = new HashMap<String, Integer>();
    private static int delta = 5;
    //private static Set<String> termStartSet = new HashSet<String>();
    private Set<String> termSet;
    //public static List<String> getListTerms() {
    //    return new LinkedList<String>(getTerms());
    //}

    public ConceptPredictor() {
        System.out.println("ConceptPredictor bulid ... ");
        List<TCC> allTCC = TCC.getAllST(path);
        Map<String, Integer> termsCount = new HashMap<String, Integer>();

        System.out.println("....foreach ST start");
        for (TCC tcc : allTCC) {
            String term = tcc.getTerm();
            //termsCount.put(term, 0);
            String concept = tcc.getConcept();
            String context = getSubContext(tcc);//tcc.context;
            //String context = tcc.getContext();

            if (!termsMap.containsKey(term) && term != null) {
                termsMap.put(term, new Term(term));
                //String termStart = term.split(" ")[0];
                //termStartSet.add(termStart);
            }
            termsMap.get(term).addConcept(concept, context);

            if (conceptCountMap.containsKey(concept)) {
                conceptCountMap.put(concept, conceptCountMap.get(concept) + 1);
            } else {
                conceptCountMap.put(concept, 1);
            }
        }
        System.out.println("....foreach ST finish");

        System.out.println("....calculate terms start");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("Term_CountMet.txt")), "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] args = line.split("___");
                String term = args[0];
                int termCount = Integer.parseInt(args[1]);
                termsCount.put(term, termCount);
            }
            br.close();
        } catch (Exception e){
            System.out.println("error in ConceptPredictor build!\n" + e.getMessage());
        }
        System.out.println("....calculate terms finish");

        System.out.println("....calculate countIsConcept in terms start");
        for (String term : termsMap.keySet()) {
            termsMap.get(term).setCountMet(termsCount.containsKey(term) ? termsCount.get(term) : termsMap.get(term).getCountOfHL());
            termsMap.get(term).calculateCountIsConcept();
            //if (!(termsMap.get(term).getCountOfHL() > 5)) { termsMap.remove(term); }
        }
        System.out.println("....calculate countIsConcept in terms finish");

        termSet = termsMap.keySet();

        //System.out.println("....Word2VecModel deserialize start");
        //W2VUtils.setWord2VecModel();
        //System.out.println("....Word2VecModel deserialize finish");

        System.out.println("ConceptPredictor build done");
    }

    public static ConceptPredictor getPredictorAndReadW2V() {
        W2VUtils.setWord2VecModel();
        return new ConceptPredictor();
    }

    public static String getSubContext(TCC tcc) {
        String context = tcc.getContext();
        String term = tcc.getTerm();
        int index = tcc.getBeginIndex();

        return getSubContext(context, term, index);
    }

    public static String getSubContext(String context, String term, int index) {
        StringBuilder str = new StringBuilder();
        String[] beginStr = context.substring(0, index).split(" ");
        String[] endStr = new String[]{};
        if (index + term.length() + 1 < context.length())
            endStr = context.substring(index + term.length() + 1).split(" ");

        for (int i = 0; i < delta; i++) {
            try {
                int startIndex = beginStr.length - delta;
                str.append(beginStr[startIndex + i] + " ");
            } catch (Exception e){ }
        }
        str.append(term);
        for (int i = 0; i < delta; i++) {
            try {
                str.append(" " + endStr[i]);
            } catch (Exception e){ }
        }

        return str.toString();
    }

    public void serialize() {
        serialize("ConceptPredictor.txt");
    }

    public void serialize(String path) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(this);
        } catch (Exception e) {
            System.out.println("error in ConceptPredictor serialize!\t" + e.getMessage());
        }
    }

    public static ConceptPredictor deserialize() {
        return deserialize("ConceptPredictor.txt");
    }

    public static ConceptPredictor deserialize(String path) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            W2VUtils.setWord2VecModel();
            return (ConceptPredictor)ois.readObject();
        } catch (Exception e) {
            System.out.println("error in ConceptPredictor serialize!\n" + e.getMessage());
            return null;
        }
    }

    /*
    public double getTF_IDF(String term, String concept) {
        return getTF(term)/getIDF(concept);
    }

    private double getTF(String term) {
        return termsMap.get(term).getTF();
    }

    private double getIDF(String concept) {
        //if (conceptCountMap.get(predict) == 1) return 3;
        return Math.log(conceptCountMap.get(concept) + 1);
    }
    */

    public Map<String, Term> getTermsMap() {
        return termsMap;
    }
    public Set<Term> getTermsSet() {
        return new HashSet<Term>(termsMap.values());
    }

    public Map<String, Integer> predictConcepts(String text) {
        Map<String, Integer> resultConcepts = new LinkedMap<String, Integer>();
        for (Sentence sentence : SentenceExtractor.extract(text)) {
            Map<String, Integer> predictConcepts = predictConcepts(sentence);
            for (String concept : predictConcepts.keySet()) {
                Integer newValue = predictConcepts.get(concept);
                Integer oldValue = resultConcepts.containsKey(concept) ? resultConcepts.get(concept) : 0;
                resultConcepts.put(concept, oldValue + newValue);
            }
        }
        return resultConcepts;
    }

    private Map<String, Integer> predictConcepts(Sentence sentence) {
        DisambiguityProcessor.process(sentence);
        String normSent = SentenceUtils.getNorm(sentence, false);

        List<Term> predictTerms = predictTerms(normSent);

        Map<String, Integer> resultConcepts = predictConcepts(predictTerms, normSent);

        return resultConcepts;
    }

    private Map<String, Integer> predictConcepts(List<Term> termList, String commonContext) {
        //List<String> resultConcepts = new LinkedList<String>();
        Map<String, Integer> resultConcepts = new HashMap<String, Integer>();
        INDArray commonContextVec = W2VUtils.avgVector(commonContext);
        if (commonContextVec == null) {
            //System.out.println("Aaaaaaaa, null pointer commonContext!!!");
            return resultConcepts;
        }

        for (Term term : termList) {
            double maxSim = -2;
            String closestConcept = null;
            if (term.isSingleConcept()) {
                closestConcept = term.getConcepts().toArray(new String[0])[0];
                for (String context : term.getContexts(closestConcept)) {
                    INDArray conceptContextVec = W2VUtils.avgVector(context);
                    double similarity = -2;
                    if (conceptContextVec == null) {
                        //System.out.println("Aaaaaaaa, null pointer conceptContext!!!");
                    }
                    if(commonContextVec != null && conceptContextVec != null) {
                        similarity = Transforms.cosineSim(commonContextVec, conceptContextVec);//todo добаыить /IDF? чтобы частые понятия
                    }
                    if (similarity > maxSim) {
                        maxSim = similarity;
                    }
                }
            } else {
                for (String concept : term.getConcepts()) {
                    for (String context : term.getContexts(concept)) {
                        INDArray conceptContextVec = W2VUtils.avgVector(context);
                        if (conceptContextVec == null) {
                            //System.out.println("Aaaaaaaa, null pointer conceptContext!!!");
                        }

                        double similarity = -2;
                        if (commonContextVec != null && conceptContextVec != null)
                            similarity = Transforms.cosineSim(commonContextVec, conceptContextVec);
                        if (similarity > maxSim) {
                            closestConcept = concept;
                            maxSim = similarity;
                        }
                    }
                }
            }
            if (closestConcept != null && maxSim > 0.8/* && getTF_IDF(term.getName(), closestConcept)*1000 > 1*/) {
                //double newValue = getTF_IDF(term.getName(), closestConcept);
                Integer newValue = 1;
                Integer oldValue = resultConcepts.containsKey(closestConcept) ? resultConcepts.get(closestConcept) : 0;
                resultConcepts.put(closestConcept, oldValue + newValue);

            }
        }

        return resultConcepts;
    }

    private List<Term> predictTerms(String normSent) {
        List<Term> resultTerms = new LinkedList<Term>();

        //int index = 0;
        //int beginIndexMaxTerm;
        //String maxTerm = null;
        //String maxSubTerm = null;
        String sent = normSent;
        String[] tokens = normSent.split(" ");
        int wait = 0;
        for (String token : tokens) {
            if (wait > 0) {
                wait--;
                if (sent.split(" ").length > 1) {
                    sent = sent.split(" ", 2)[1];
                }
                continue;
            }
            String resultTerm = null;
            for (String term : termsBeginingFrom(token)) {
                if (sent.startsWith(term) && (resultTerm == null || term.length() > resultTerm.length()))
                    resultTerm = term;
            }
            if (resultTerm != null) {
                resultTerms.add(termsMap.get(resultTerm));
                wait = resultTerm.split(" ").length - 1;
            }
            if (sent.split(" ").length > 1) {
                sent = sent.split(" ", 2)[1];
            }



            /*
            if (maxSubTerm != null) {//сейчас есть на примете какой-то термин
                String checkStr = maxSubTerm + " " + token;
                if (termsContains(checkStr)) {//идем дальше, термин еще не закончился
                    maxSubTerm = checkStr;
                    if (termSet.contains(maxSubTerm)) {
                        maxTerm = maxSubTerm;
                    }
                    continue;
                } else {//конец термина
                    if (maxTerm != null) {
                        resultTerms.add(termsMap.get(maxTerm));
                        //index!!
                        maxTerm = null;
                    }
                    maxSubTerm = null;
                }
            }
            if (maxSubTerm == null) {//сейчас нет терминов, или последний только что закончился
                if (termsContains(token)) {//токен встречается в нашем списке терминов
                    beginIndexMaxTerm = index;
                    maxSubTerm = token;
                    if (termSet.contains(maxSubTerm)) {
                        maxTerm = maxSubTerm;
                    }
                }
            }
            index += token.length() + 1;
            */
        }
        return resultTerms;

    }

    private boolean termsContains(String token) {
        for (String term : termSet) {
            if (term.contains(token)) {
                return true;
            }
        }
        return false;
    }

    private List<String>termsBeginingFrom(String token) {
        List<String> result = new LinkedList<String>();
        for (String term : termSet) {
            if (term.startsWith(token))
                result.add(term);
        }
        return result;
    }

    public List<String> termsWithConcept(String concept) {
        List<String> result = new LinkedList<String>();
        for (String term : termsMap.keySet())
            for (String concept0 : termsMap.get(term).getConcepts())
                if (concept.equals(concept0)) {
                    result.add(term);
                    break;
                }
        return result;

    }

}
