package com.si.scrapping.scrapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Arrays;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@SpringBootTest
class ScrappingApplicationTests {

    public static final String redishost = "172.17.0.2";

    @Autowired
    public ScrappingService scrappingService;

	@Test
	void contextLoads() {
	}


    @Test
    void redisConnectionTest(){

        JedisPool pool = new JedisPool(redishost, 6379);

        try (Jedis jedis = pool.getResource()) {

            // Store & Retrieve a simple string
            jedis.set("test.foo", "bar");

            assertEquals("bar", jedis.get("test.foo"));
            
            jedis.del("test.foo");

            // Store and Retrieve a HashMap
            Map<String, String> hash = new HashMap<>();;
            hash.put("name", "John");
            hash.put("surname", "Smith");
            hash.put("company", "Redis");
            hash.put("age", "29");
            
            jedis.hset("test.user.John", hash);

            assertEquals(hash, jedis.hgetAll("test.user.John"));

            jedis.del("test.user.John");

        }   
    }

    @Test
    void scrappingTest() throws IOException{

        String url = "https://www.google.com";
        System.out.println("Parsing page " + url + "...");

        Document doc = Jsoup.connect(url).get();

        doc.select("a")
            .forEach(a -> System.out.println(a.html() + ":" + a.attr("href")));
    }   


    @Test
    void scrapTest(){

        Helbidea test = new Helbidea("https://www.google.com", null);
        List<Helbidea> emaitza = scrappingService.scrap(test);

        emaitza.forEach(helbidea -> System.out.println(helbidea.toString()));
    }

    @Test
    void saveTest(){

        // Datuak prestatzea
        List<Helbidea> testData = new ArrayList<>();
        testData.add(new Helbidea("href1", "asasd"));
        testData.add(new Helbidea("href2", "ffff"));
        testData.add(new Helbidea("href4", "00"));
            
        // Frogatzea
        scrappingService.save(testData);

        // Ziurtatzea
         JedisPool pool = new JedisPool(redishost, 6379);

        try (Jedis jedis = pool.getResource()) {
            Map<String, String> h1 = jedis.hgetAll("href1");
            Map<String, String> h2 = jedis.hgetAll("href2");
            Map<String, String> h4 = jedis.hgetAll("href4");

            assertEquals("asasd", h1.get("html"));
            assertEquals("ffff", h2.get("html"));
            assertEquals("00", h4.get("html"));
        }
    }
}
