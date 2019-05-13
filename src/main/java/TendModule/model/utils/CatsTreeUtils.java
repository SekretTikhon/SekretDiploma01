package TendModule.model.utils;

import TendModule.model.Cat;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CatsTreeUtils {
    /**
     *
     *
     *
     */


    public static Map<String, Cat> getMap(String path) throws IOException {
        Map<String, Cat> result = new HashMap<String, Cat>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "utf-8"));
        try {
            String json;
            while ((json = br.readLine()) != null) {
                Cat fCategory = Cat.fromJSON(json);
                result.put(fCategory.getTitle(), fCategory);
            }
        } finally {
            br.close();
        }
        return result;
    }

    public static Map<String, Cat> getMap() throws IOException {
        return getMap("categoryes_json.txt");
    }

    public static Set<String> getAllSubCats(String catTitle){
        try {
            return getAllSubCats(getMap(), catTitle);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return new HashSet<String>();
    }

    public static Set<String> getAllSubCats(String pathToCats, String catTitle) throws IOException {
        return getAllSubCats(getMap(pathToCats), catTitle);
    }

    public static Set<String> getAllSubCats(Map<String,Cat> cats, String catTitle) throws IOException {
        if (!cats.containsKey(catTitle)) {
            return null;
        }
        Set<String> result = new HashSet<String>();
        addAllSubCats(cats, catTitle, result);
        return result;
    }

    private static void addAllSubCats(Map<String,Cat> cats, String cat, Set<String> result) {
        if (!cats.containsKey(cat)) {
            return;
        }
        if (result.contains(cat)) {
            return;
        }
        //if (catTitle.contains("википедия:")) return;
        if (cat.contains("википедия:") ||
                cat.substring(0, cat.length() > 11 ? 11 : cat.length()).contains("математики") ||
                cat.contains("незавершенные статьи") ||
                cat.contains("незавершённые статьи") ||
                cat.contains("элайдж бэйли") ||
                cat.contains("в литературе") ||
                cat.contains("в культуре и исскустве") ||
                cat.contains("лауреаты") ||
                cat.substring(0, cat.length() > 12 ? 12 : cat.length()).contains("криптографы") ||
                cat.substring(0, cat.length() > 12 ? 12 : cat.length()).contains("кибернетики") ||
                cat.substring(0, cat.length() > 7 ? 7 : cat.length()).contains("логики") ||
                cat.substring(0, cat.length() > 11 ? 11 : cat.length()).contains("статистики") ||
                cat.equals("кассетники") ||
                cat.contains("дети-математики") ||
                //cat.contains("программное обеспечение") ||
                cat.contains("выпускники") ||
                cat.contains("менеджеры паролей") ||
                cat.contains("рэй аянами") ||
                cat.contains("фильмы") ||
                cat.contains("персонажи") ||
                cat.contains("персоналии") ||
                cat.contains("боги") ||
                cat.contains("трансформеры") ||
                cat.contains("мультфильм") ||
                cat.contains("евангелион") ||
                cat.contains("роботы") ||
                cat.contains("школы и лицеи") ||
                cat.contains("лицей") ||
                cat.contains("изображения:") ||
                cat.contains("микроформат") ||
                cat.contains("наушники") ||
                cat.contains("преподаватели") ||
                cat.contains("сигнализация") ||
                cat.contains("виртуальные собеседники") ||
                cat.contains("алгебраисты") ||
                cat.contains("математические организации") ||
                cat.contains("источники питания") ||
                cat.contains("ветрогенераторы") ||
                cat.contains("олимпиады") ||
                cat.contains("управление предприятием") ||
                cat.contains("философы") ||
                cat.contains("школы") ||
                cat.contains("факультет") ||
                cat.contains("движение денежных средств") ||
                cat.contains("члены харьковского математического общества") ||
                cat.contains("профилировщики") ||
                cat.contains("математических наук") ||
                cat.contains("микрофоны") ||
                cat.contains("управление процессами") ||
                cat.contains("карты памяти") ||
                cat.contains("спидкубинг") ||
                cat.contains("искусственная жизнь") ||
                cat.contains("создатели головоломок") ||
                cat.contains("трансформаторы") ||
                cat.contains("смарт-карта") ||
                cat.contains("авторы") ||
                cat.contains("институт") ||
                cat.contains("иконика") ||
                cat.contains("биографии и автобиографии") ||
                cat.contains("виды кибернетических экспериментов") ||
                cat.contains("популяризаторы математики") ||
                cat.contains("программы для") ||
                cat.contains("производители") ||
                cat.contains("игры для программистов") ||
                cat.contains("авиационные происшествия вследствие турбулентности") ||
                cat.contains("математические сайты") ||
                cat.contains("умная колонка") ||
                cat.contains("графисты") ||
                cat.contains("лузитания") ||
                cat.contains("структура организации") ||
                cat.contains("математические соревнования") ||
                cat.contains("олимпиад") ||
                cat.contains("управление знаниями") ||
                cat.contains("организационная структура") ||
                cat.contains("планирование эксперимента") ||
                cat.contains("криптографические конкурсы") ||
                cat.contains("древнерусские тайнописи") ||
                cat.contains("математические награды") ||
                cat.contains("исследователи")

        ) return;
        result.add(cat);
        for (String subCat : cats.get(cat).getSubCats()) {
            addAllSubCats(cats, subCat, result);
        }
    }

}
