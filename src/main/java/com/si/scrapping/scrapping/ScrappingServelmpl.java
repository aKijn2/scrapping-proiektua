package com.si.scrapping.scrapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class ScrappingServelmpl implements ScrappingService {

    @Override
    public List<Helbidea> scrap(Helbidea helbidea) {

        List<Helbidea> emaitza = new ArrayList<>();

    Document doc;
    try {
        doc = Jsoup.connect(helbidea.getHref()).get();
        
        doc.select("a")
            .forEach(a -> emaitza.add(new Helbidea(a.attr("href"),a.html())));
    
    } catch (IOException e) {

        e.printStackTrace();
}
        return emaitza;
}

    @Override
    public void save(List<Helbidea> helbideak) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public List<Helbidea> scrapAndSave(Helbidea helbidea) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'scrapAndSave'");
    }
    
}
