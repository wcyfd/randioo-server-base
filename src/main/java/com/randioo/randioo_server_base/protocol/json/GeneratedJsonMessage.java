package com.randioo.randioo_server_base.protocol.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GeneratedJsonMessage {

    public static <T extends GeneratedJsonMessage> T mergeFrom(Class<T> clazz, Object source) {
        Gson gson = new Gson();
        String sourceStr = (String) source;
        try {
            return gson.getAdapter(clazz).fromJson(sourceStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String serialize() {
        return serialize(false);
    }

    public String serialize(boolean pretty) {
        Gson gson = pretty ? new GsonBuilder().setPrettyPrinting().create() : new Gson();
        String value = gson.toJson(this);
        return value;
    }

    @Override
    public String toString() {
        return serialize();
    }
}
