package com.randioo.randioo_server_base.config;

import java.util.ArrayList;
import java.util.List;

import com.randioo.randioo_server_base.template.Parser;

/**
 * 命令参数转换器<br>
 * 用于将字符串数组转换为一条参数<br>
 * 数组中的顺序为键名,数据类型('int','string','boolean','float','double'),值<br>
 * 值的内容必须要与数据类型对应,否则无法使用java的包装基本数据类型进行转换<br>
 * 举例:<br>
 * <br>
 * String[] param =
 * {"port","int","10004","debug","boolean","true","projectName",
 * "string","randioo"}<br>
 * 
 * ->List size=3; <br>
 * 
 * @author wcy 2017年8月5日
 *
 */
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
