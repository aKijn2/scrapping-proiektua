package com.si.scrapping.scrapping;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Helbidea {
 
    public String href;

    public String html;

    public Map<String,String> toMap(){

        Map<String, String> hash = new HashMap<>();;
        hash.put("href", this.href);
        hash.put("html", this.html);

        return hash;
    }

}
