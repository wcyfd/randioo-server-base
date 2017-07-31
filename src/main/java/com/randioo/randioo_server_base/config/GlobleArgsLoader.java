package com.randioo.randioo_server_base.config;

import java.util.List;

public class GlobleArgsLoader {

    public static void init(String[] args) {
        GlobleArgsLoader loader = new GlobleArgsLoader();
        loader.analysis(args);
    }

    public void analysis(String[] args) {
        ArgsGlobleParameterParser parser = new ArgsGlobleParameterParser();
        List<GlobleParameter> list = parser.parse(args);
        for (GlobleParameter parameter : list) {
            GlobleMap.putParam(parameter);
        }
    }
}
