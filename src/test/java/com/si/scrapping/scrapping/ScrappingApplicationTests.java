package com.si.scrapping.scrapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@SpringBootTest
class ScrappingApplicationTests {

    @Autowired
    public ScrappingService scrappingService;

    @Test
    void contextLoads() {
    }

    @Test
    void redisConnectiontest() {
        JedisPool pool = new JedisPool("172.17.0.3", 6379);

        try (Jedis jedis = pool.getResource()) {
            // Store & Retrieve a simple string
            jedis.set("foo", "bar");
            System.out.println(jedis.get("foo")); // prints bar

            // Store & Retrieve a HashMap
            Map<String, String> hash = new HashMap<>();
            hash.put("name", "John");
            hash.put("surname", "Smith");
            hash.put("company", "Redis");
            hash.put("age", "29");
            jedis.hset("user-session:123", hash);
            System.out.println(jedis.hgetAll("user-session:123"));
            // Prints: {name=John, surname=Smith, company=Redis, age=29}
        }
    }

    @Test
    void scrappingTest() throws IOException {
        String url = "https://developer.mozilla.org/es/docs/Web/HTML/Element/article";
        System.out.println("Parsing page " + url + "...");

        Document doc = Jsoup.connect(url).get();

        doc.select("p").forEach(a -> System.out.println(a.html() + ":" + a.attr("href")));

        Elements links = doc.select("a");
        Elements logo = doc.select(".spring-logo--container");
        Elements pagination = doc.select("#pagination_control");
        Elements divsDescendant = doc.select("header div");
        Elements divsDirect = doc.select("header > div");

        Elements articles = doc.select("article");
        if (!articles.isEmpty()) {
            Element firstArticle = articles.first();
            Element lastSection = articles.last();
            Element secondSection = articles.size() > 2 ? articles.get(2) : null;

            Elements allParents = firstArticle.parents();
            Element parent = firstArticle.parent();
            Elements children = firstArticle.children();
            Elements siblings = firstArticle.siblingElements();

            Element titleElement = firstArticle.select("h1 a").first();
            String titleText = (titleElement != null) ? titleElement.text() : "Elemento no encontrado";
            String articleHtml = firstArticle.html();
            String outerHtml = firstArticle.outerHtml();
        } else {
            System.out.println("No se encontraron elementos <article> en la página.");
        }
    }

    @Test
    void scrapTest(){
       Helbidea test = new Helbidea("https://developer.mozilla.org/es/docs/Web/HTML/Element/article",null);
        List<Helbidea> emaitza= scrappingService.scrap(test);

        emaitza.forEach(helbidea -> System.out.println(helbidea.toString()));
    }
    @Test
    void scrapFroga() throws IOException {
        redisConnectiontest();
    
        scrappingTest();
    
        System.out.println("Recopilando información de las pruebas anteriores...");
    
        int resultadoPruebaRedis = procesarResultadoPruebaRedis();
        String resultadoPruebaScrapping = procesarResultadoPruebaScrapping();
    
        System.out.println("Resultado de la prueba Redis: " + resultadoPruebaRedis);
        System.out.println("Resultado de la prueba Scrapping: " + resultadoPruebaScrapping);
    }
    
    private int procesarResultadoPruebaRedis() {
        return 0; 
    }
    
    private String procesarResultadoPruebaScrapping() {
        return "Resultado procesado de scrappingTest"; 
    }

    // @Test
    // void webScrapingAndSavingTest() {
    //     // Helbide bat sortu
    //     Helbidea helbidea = new Helbidea("HTML kodea hemen", "https://developer.mozilla.org/es/docs/Web/HTML/Element/article");

    //     // Zerbitzuaren instantzia sortu
    //     WebScraperService webScraperService = new WebScraperService();

    //     // Web arakatu eta helbideak gorde
    //     List<Helbidea> helbideak = webScraperService.scrapAndSave(helbidea);

    //     // Gorde diren helbideen informazioa erakutsi
    //     for (Helbidea h : helbideak) {
    //         System.out.println("HTML: " + h.getHtml());
    //         System.out.println("Href: " + h.getHref());
    //     }
    // }

}