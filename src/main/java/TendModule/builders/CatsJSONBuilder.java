package TendModule.builders;

import com.github.stagirs.wiki.model.WikiPage;
import com.github.stagirs.wiki.model.WikiText;
import com.github.stagirs.wiki.model.text.WikiCategory;
import TendModule.model.utils.StringFormUtils;
import TendModule.model.Cat;
import TendModule.model.utils.CatUtils;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CatsJSONBuilder {
    /**
     * Класс работает с результатом работы класса TendModule.builders.WikiOnlyCatXmlBuilder,
     * путь к результату его работы следует указать в File in.
     * Класс строит полное дерево для категорий из File in
     * и записывает в формате JSON в File out.
     * Для получения объекта TendModule.model.Cat, нужно подать строку из File out в TendModule.model.Cat.fromJSON
     */

    public static File in = new File("ruwiki_categoryes_20190220_out.txt");
    public static File out = new File("categoryes_json.txt");
    public static File outFails = new File("categoryes_fails.txt");

    public static void main(String[] args) throws IOException {
        out.delete();

        BufferedReader br;
        BufferedWriter bw;
        BufferedWriter bwF;

        Map<String, Cat> FCategoryes = new HashMap<String, Cat>();

        br = new BufferedReader(new InputStreamReader(new FileInputStream(in), "utf-8"));
        try {
            String xml;
            WikiPage page;
            System.out.println("Load categoryes: process");
            while ((xml = br.readLine()) != null) {
                page = WikiPage.fromXml(xml);
                String cat = CatUtils.getCatTitle(page);
                FCategoryes.put(cat, new Cat(cat));
            }
            System.out.println("Load categoryes: done");
        } finally {
            br.close();
        }

        Set<String> nullCat = new HashSet<String>();

        br = new BufferedReader(new InputStreamReader(new FileInputStream(in), "utf-8"));
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "utf-8"));
        bwF = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFails), "utf-8"));
        try {
            String xml;
            WikiPage page;
            System.out.println("Add sub- and super-categoryes: process");
            while ((xml = br.readLine()) != null) {
                page = WikiPage.fromXml(xml);
                String cat = CatUtils.getCatTitle(page);
                for (WikiText wikiText : page.getAllPoints()) {
                    for (WikiCategory wikiCategory : wikiText.getCategories()) {
                        String superCat = StringFormUtils.getSimpleForm(wikiCategory.getId());
                        FCategoryes.get(cat).addSuperCats(superCat);
                        if (FCategoryes.containsKey(superCat))
                            FCategoryes.get(superCat).addSubCat(cat);
                        else {
                            nullCat.add(superCat);
                        }
                    }
                }
            }
            System.out.println("Add sub- and super-categoryes: done");

            System.out.println("Write categoryes to file: process");
            for (Cat fCategory : FCategoryes.values()) {
                String catJSON = fCategory.toJSON();
                Cat fromJSON = Cat.fromJSON(catJSON);
                bw.append(catJSON + "\n");
            }
            System.out.println("Write categoryes to file: done");

            System.out.println("Counter not exist categoryes: " + nullCat.size());
            System.out.println("Write null-categoryes to file: process");
            for (String cat : nullCat) {
                bwF.append(cat + "\n");
            }
            System.out.println("Write null-categoryes to file: done");
        } finally {
            br.close();
            bw.close();
            bwF.close();
        }

    }

}
