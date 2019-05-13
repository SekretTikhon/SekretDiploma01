package TendModule.builders;

import java.io.*;

public class WikiOnlyCatXmlBuilder {
    /**
     *
     * Класс работает с результатом работы класса WikiXmlBuilder.
     * Файл-результат работы класса WikiXmlBuilder требуется положить в корневую папку проекта
     * и при необходимости указать полное имя этого файла в File in.
     * Класс оставляет только страницы-категории, ничего больше не изменяя.
     * Результат в корневой папке проекта с именем, соответствующим в File out.
     */

    public static File in = new File("ruwiki_20190220_out.txt");
    public static File out = new File("ruwiki_categoryes_20190220_out.txt");

    public static void main(String[] args) throws IOException {
        out.delete();

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(in), "utf-8"));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "utf-8"));

        int num = 0;

        try {
            String xml;
            String title;
            while ((xml = br.readLine()) != null) {
                title = xml.split("<title>")[1].split("</title>")[0];
                if (title.contains("Категория:")) {
                    bw.append(xml + "\n");
                    num++;
                }
            }
        } finally {
            br.close();
            bw.close();
            System.out.println(num);
        }
    }
}
