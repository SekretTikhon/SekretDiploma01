package TendModule.model.utils;

import com.github.stagirs.wiki.model.WikiPage;
import com.github.stagirs.wiki.model.WikiText;
import com.github.stagirs.wiki.model.text.WikiCategory;

import java.util.HashSet;
import java.util.Set;

public class CatUtils {

    public static Set<String> getAllCats(WikiPage page) {
        Set<String> result = new HashSet<String>();
        for (WikiText wikiText : page.getAllPoints()) {
            for (WikiCategory wikiCategory : wikiText.getCategories()) {
                result.add(StringFormUtils.getSimpleForm(wikiCategory.getId()));
            }
        }
        return result;
    }

    public static String getCatTitle(WikiPage page) {
        return StringFormUtils.getSimpleForm(
                page
                        .getTitle()
                        .getText()
                        .split(":",2)[1]//TendModule.parseWiki.category
        );
    }

    public static String getTitle(String xml) {
        String title = xml.split("<title>")[1].split("</title>")[0];
        return StringFormUtils.getSimpleForm(title);
    }

    public static String getTitle(WikiPage page) {
        String title = page.getTitle().getText();
        return StringFormUtils.getSimpleForm(title);
    }

}
