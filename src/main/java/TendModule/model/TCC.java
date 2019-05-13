package TendModule.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class TCC {
    protected String term;
    protected String concept;
    protected String context;
    protected int beginIndex;
    public String getTerm() {
        return term;
    }
    public String getConcept() {
        return concept;
    }
    public String getContext() {
        return context;
    }
    public int getBeginIndex() { return beginIndex; }

    public TCC(String term, String concept, String context, int beginIndex) {
        this.term = term;
        this.concept = concept;
        this.context = context;
        this.beginIndex = beginIndex;
    }

    public static TCC fromJSON(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String term = jsonObject.getString("term");
        String concept = jsonObject.getString("concept");
        String context = jsonObject.getString("context");
        int beginIndex = jsonObject.getInteger("beginIndex");
        return new TCC(term, concept, context, beginIndex);
    }

    public String toJSON() {
        return JSON.toJSONString(this);
    }


    public static List<TCC> getAllST() {
        return getAllST("term_concept.txt");
    }

    public static List<TCC> getAllST(String path) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "utf-8"));
            List<TCC> result = new LinkedList<TCC>();
            try {
                String json;
                while ((json = br.readLine()) != null) {
                    result.add(TCC.fromJSON(json));
                }
            } finally {
                br.close();
            }

            return result;
        } catch (IOException ex) {
            return null;
        }
    }

}
