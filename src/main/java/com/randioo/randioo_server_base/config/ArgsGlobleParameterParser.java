package com.randioo.randioo_server_base.config;

import java.util.ArrayList;
import java.util.List;

import com.randioo.randioo_server_base.template.Parser;

public class ArgsGlobleParameterParser implements Parser<List<GlobleParameter>, String[]> {

    @Override
    public List<GlobleParameter> parse(String[] param) {
        List<GlobleParameter> globleParameters = new ArrayList<>(param.length / 3);
        int i = 0;

        while (i < param.length) {
            GlobleParameter parameter = new GlobleParameter();
            parameter.key = param[i++];
            parameter.type = GlobleTypeEnum.getGlobleType(param[i++]);
            parameter.value = param[i++];

            globleParameters.add(parameter);
        }
        return globleParameters;
    }
}
