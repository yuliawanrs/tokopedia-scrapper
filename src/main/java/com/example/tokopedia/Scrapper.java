package com.example.tokopedia;


import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scrapper {
    private static final String USERAGENT = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:90.0) Gecko/20100101 Firefox/90.0";
    private static final String URL = "https://www.tokopedia.com/p/handphone-tablet/handphone";
    private static final String CSV_SEPARATOR = ";";

    public static void main(String[] args) {
        Document doc;
        try {
            doc = Jsoup.connect("https://www.tokopedia.com/p/handphone-tablet/handphone").userAgent("Mozilla").get();

            Elements links = doc.select("a[data-testid='lnkProductContainer']");
            Scrapper scrapper = new Scrapper();
            scrapper.parseData(links);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseData(Elements links) {
        int index = 0;
        List<ParsedData> parsedDatas = new ArrayList<>();
        for (Element link : links) {
            Document doc = getListing(link);
            if( doc != null) {
                index++;
                // Name of Product
                Element name = doc.select("h1[data-testid='lblPDPDetailProductName']").first();
                // Description.
                Element desc = doc.select("div[data-testid='lblPDPDescriptionProduk']").first();
                // Image Link.
                Element img = doc.select("div[data-testid='PDPImageMain']").first().child(1).child(1).child(0);
                // Price.
                Element price = doc.select("div[data-testid='lblPDPDetailProductPrice']").first();
                // Rating(out of 5 stars).
                Element rating = doc.select("meta[itemprop='ratingValue']").first();
                // Name of store or merchant.
                String store =  img.attr("alt").split("dari")[1].trim();

                parsedDatas.add(new ParsedData(index, name.text(), desc.text(), img.attr("src"), price.text(), rating == null ? "0" : rating.attr("content"), store));
            }
        }
        writeToCSV(parsedDatas);

        System.out.println("Data: " + index);
    }

    private Document getListing(Element link) {
        Document doc = null;
        String urlUsed;
        try {
            URL parsedLink = new URL(link.attr("href"));
            if("www.tokopedia.com".equalsIgnoreCase(parsedLink.getHost())) {
                urlUsed = parsedLink.toString();
            } else {
                Map<String, String> queryParam = getQueryMap(parsedLink.getQuery());
                urlUsed = URLDecoder.decode(queryParam.get("r"), StandardCharsets.UTF_8.toString());
            }
            Response response= Jsoup.connect(urlUsed)
                        .ignoreContentType(true)
                        .userAgent(USERAGENT)
                        .referrer(URL)
                        .timeout(0)
                        .followRedirects(true)
                        .execute();

            doc = response.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    private static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<>();

        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    private static void writeToCSV(List<ParsedData> parsedData)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("target/parse.csv"), StandardCharsets.UTF_8.toString()));
            for (ParsedData data : parsedData)
            {
                String oneLine = data.getName() +
                        CSV_SEPARATOR +
                        data.getDesc() +
                        CSV_SEPARATOR +
                        data.getImg() +
                        CSV_SEPARATOR +
                        data.getPrice() +
                        CSV_SEPARATOR +
                        data.getRating() +
                        CSV_SEPARATOR +
                        data.getStore();
                bw.write(oneLine);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

}
