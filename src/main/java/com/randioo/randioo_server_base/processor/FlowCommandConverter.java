package com.randioo.randioo_server_base.processor;

import org.springframework.stereotype.Component;

import com.randioo.randioo_server_base.template.Parser;
import com.randioo.randioo_server_base.utils.StringUtils;

@Component
public class FlowCommandConverter implements Parser<FlowCommand, String> {

    @Override
    public FlowCommand parse(String cmdStr) {
        if (StringUtils.isNullOrEmpty(cmdStr)) {
            return null;
        }
        String[] cmdArray = cmdStr.split(" ");

        FlowCommand flowCommand = new FlowCommand();
        if (cmdArray.length > 0) {
            String flowName = cmdArray[0];
            flowCommand.setFlowName(flowName);
        }

        if (cmdArray.length > 1) {
            int len = cmdArray.length - 1;
            String[] params = new String[len];
            for (int i = 0; i < len; i++) {
                params[i] = cmdArray[i + 1];
            }
            flowCommand.setParams(params);
        }
        return flowCommand;
    }

}
