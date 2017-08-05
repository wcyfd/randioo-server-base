package com.randioo.randioo_server_base.config;

import java.util.List;

/**
 * GlobleMap 命令行参数加载器<br>
 * 与GlobleXmlLoader等价 <br>
 * 加载以后则能在GlobleMap中找到
 * 
 * @author wcy 2017年8月5日
 *
 */
public class GlobleArgsLoader {

    /**
     * 字符串格式要求请参见ArgsGlboleParameterParser类
     * 
     * @param args
     * @author wcy 2017年8月5日
     */
    public static void init(String[] args) {
        GlobleArgsLoader loader = new GlobleArgsLoader();
        loader.analysis(args);
    }

    /**
     * 解析参数并加入到全局参数表中
     * 
     * @param args
     * @author wcy 2017年8月5日
     */
    public void analysis(String[] args) {
        ArgsGlobleParameterParser parser = new ArgsGlobleParameterParser();
        List<GlobleParameter> list = parser.parse(args);
        for (GlobleParameter parameter : list) {
            GlobleMap.putParam(parameter);
        }
    }
}
