package TendModule.model;

import java.io.Serializable;
import java.util.*;

public class Term implements Serializable {
    String name;
    int countMet = 0;
    int countOfHL;
    Map<String, List<String>> concepts = new HashMap<String, List<String>>();

    public String getName() {
        return name;
    }
    public int getCountMet() { return countMet; }
    public void setCountMet(int value) {
        countMet = value;
    }
    public boolean isSingleConcept() {
        return concepts.size() == 1;
    }
    public int conceptsCount() {
        return concepts.size();
    }
    public List<String> getContexts(String concept) {
        return concepts.get(concept);
    }
    public Set<String> getConcepts() {
        return concepts.keySet();
    }
    public void calculateCountIsConcept() {
        countOfHL= 0;
        for (String concept: concepts.keySet()) {
            countOfHL += concepts.get(concept).size();
        }
    }
    public double getTF() {
        return 1.0*countOfHL/countMet;
    }
    public int getCountOfHL() { return countOfHL; }

    //public Map<String, List<String>> getConcepts() {}


    public Term (String name) {
        this.name = name;
    }

    public void addConcept(TCC tcc) {
        if (!name.equals(tcc.term)) {
            System.out.println("error addConcept!");
            return;
        }
        addConcept(tcc.concept, tcc.context);
    }

    public void addConcept(String concept, String context) {
        if (!concepts.containsKey(concept)) {
            concepts.put(concept, new LinkedList<String>());
        }
        concepts.get(concept).add(context);
    }



}