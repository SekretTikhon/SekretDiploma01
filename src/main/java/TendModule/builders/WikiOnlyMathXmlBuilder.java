package TendModule.builders;

import TendModule.model.utils.CatUtils;
import TendModule.model.utils.CatsTreeUtils;
import com.github.stagirs.wiki.model.WikiPage;

import java.io.*;
import java.util.Set;

public class WikiOnlyMathXmlBuilder {
    /**
     * Класс работает с результатом работы класса WikiXmlBuilder (прописать путь в File in)
     * и с результатом работы класса TendModule.builders.CatsJSONBuilder (прописать путь в pathToCats)
     *
     * Класс без изменений оставляет xml только тех статей википедии,
     * которые входят в одну из подкатегорий (полный обход в глубину) категории "математика"
     *
     * Результат записывается в том же формате, что и после WikiXmlBuilder в File out
     */


    private static File in = new File("ruwiki_20190220_out.txt");
    private static File out = new File("ruwiki_onlymath_20190220_out.txt");
    private static String pathToCats = "categoryes_json.txt";
    private static int total = 4400000;

    public static void main(String[] args) throws IOException {
        out.delete();

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(in), "utf-8"));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "utf-8"));

        Set<String> mathCats = CatsTreeUtils.getAllSubCats(pathToCats,"математика");

        int num = 0;
        int processed = 0;
        try {
            String xml;
            while ((xml = br.readLine()) != null) {
                WikiPage page = WikiPage.fromXml(xml);
                boolean isMath = false;
                for (String superCat : CatUtils.getAllCats(page)) {
                    if (mathCats.contains(superCat)) {
                        isMath = true;
                        break;
                    }
                }
                if (isMath) {
                    num++;
                    bw.append(xml + "\n");
                }
                if (++processed % 1000 == 0)
                    System.out.printf("process: %d\t(%.3f)\tmath pages: %d\n", processed, processed*100.0/total, num);
            }
        } finally {
            br.close();
            bw.close();
        }
    }


}
