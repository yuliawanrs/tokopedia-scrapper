package com.example.tokopedia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Main {

    private static final String URL = "https://www.tokopedia.com/p/handphone-tablet/handphone";
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:90.0) Gecko/20100101 Firefox/90.0";
    private static final int DATA_TO_SEARCH = 100;

    public static void main(String[] args) {
        Document doc;
        try {
            Scrapper scrapper = new Scrapper(URL, USER_AGENT, DATA_TO_SEARCH);
            while (scrapper.getIndex() < DATA_TO_SEARCH) {
                doc = Jsoup.connect(URL + "?page=" + scrapper.getPage()).userAgent(USER_AGENT).get();
                Elements links = doc.select("a[data-testid='lnkProductContainer']");
                scrapper.parseData(links);
                scrapper.addOneToPage();
            }
            scrapper.writeToCSV(scrapper.getParsedData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
