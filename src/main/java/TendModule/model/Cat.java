package TendModule.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Cat {
    /**
     * Класс представляет собой абстракцию для одной категории
     * Категория содержит в себе:
     *      имя (title)
     *      список подкатегорий (subCats)
     *      список категорий, для которых данная является подкатегорией (superCats)
     */

    protected String title;
    protected List<String> subCats = new LinkedList<String>();
    protected List<String> superCats = new LinkedList<String>();

    public Cat(String title) {
        this.title = title;
    }

    public void addSubCat(String subCat) {
        subCats.add(subCat);
    }

    public void addSuperCats(String superCat) {
        superCats.add(superCat);
    }

    public String getTitle() {
        return title;
    }

    public List<String> getSubCats() {
        return subCats;
    }

    public List<String> getSuperCats() {
        return superCats;
    }

    public static Cat fromJSON (String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Cat result = new Cat(jsonObject.getString("title"));
        if (jsonObject.containsKey("subCats")) {
            result.subCats.addAll(jsonObject.getJSONArray("subCats").toJavaList(String.class));
        }
        if (jsonObject.containsKey("superCats")) {
            result.superCats = new LinkedList<String>(jsonObject.getJSONArray("superCats").toJavaList(String.class));
        }
        return result;
    }


    public String toJSON() {
        return JSON.toJSONString(this);
    }
}
