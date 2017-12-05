package com.randioo.randioo_server_base.processor;

import java.util.Arrays;

/**
 * 流程命令
 * 
 * @author wcy 2017年10月27日
 *
 */
public class FlowCommand {
    private String flowName;
    private String[] params;

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FlowCommand [flowName=")
                .append(flowName)
                .append(", params=")
                .append(Arrays.toString(params))
                .append("]");
        return builder.toString();
    }

}
