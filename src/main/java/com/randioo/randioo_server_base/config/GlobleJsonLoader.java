package com.randioo.randioo_server_base.config;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author zsy
 * @Description:
 * @create 2017-11-26 13:19
 **/
public class GlobleJsonLoader {
    public static void init(String file) {
        Gson gson = new Gson();
        try {
            GlobleJsonParameter object = gson.fromJson(new FileReader(file), GlobleJsonParameter.class);
            GlobleClass._G = object;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
