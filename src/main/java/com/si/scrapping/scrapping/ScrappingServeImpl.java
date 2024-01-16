package com.si.scrapping.scrapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class ScrappingServeImpl implements ScrappingService {

    @Override
    public List<Helbidea> scrap(Helbidea helbidea) {

        List<Helbidea> emaitza = new ArrayList<>();

        Document doc;
        try {
            doc = Jsoup.connect(helbidea.getHref()).get();

            doc.select("a")
                .forEach(a -> emaitza.add(new Helbidea(a.attr("href"), a.html())));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return emaitza;
    }

    @Override
    public void save(List<Helbidea> helbideak) {
        
         JedisPool pool = new JedisPool("172.17.0.2", 6379);

        try (Jedis jedis = pool.getResource()) {

            helbideak.forEach(h -> {
                jedis.hset(h.getHref(), h.toMap());
            });
        }
    }

    @Override
    public List<Helbidea> scrapAndSave(Helbidea helbidea) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'scrapAndSave'");
    }


}
