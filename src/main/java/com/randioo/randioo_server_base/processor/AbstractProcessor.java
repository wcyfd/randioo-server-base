package com.randioo.randioo_server_base.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 流程控制器
 *
 * @author wcy 2017年8月24日
 */
public abstract class AbstractProcessor<GAME_ENTITY extends ICommandStoreable, CALL_BACK extends ICommandCallback<GAME_ENTITY>>
        implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(AbstractProcessor.class);

    private Map<String, Flow<GAME_ENTITY>> flows = new HashMap<>();
    private FlowCommandConverter flowCommandConverter = new FlowCommandConverter();

    public void setFlowCommandConverter(FlowCommandConverter flowCommandConverter) {
        this.flowCommandConverter = flowCommandConverter;
    }

    public abstract CALL_BACK getICommandCallback(GAME_ENTITY game);

    public abstract boolean forceExitLoop(Flow<GAME_ENTITY> flow, String executedFlowCmd);

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        Map<String, Flow> map = context.getBeansOfType(Flow.class);
        for (Map.Entry<String, Flow> entrySet : map.entrySet()) {
            Flow flow = entrySet.getValue();
            logger.debug("load flow : {}", flow.getClass());
            flows.put(flow.getClass().getSimpleName(), flow);
        }
    }

    /**
     * 执行下一个动作
     *
     * @param game
     * @author wcy 2017年8月23日
     */
    public void process(GAME_ENTITY game) {
        // 继续执行执行出栈
        Stack<String> cmdStack = game.getCmdStack();
        // 栈顶为等待操作状态时不继续流程
        while (cmdStack.size() != 0 && !cmdStack.peek().equals(ICommandCallback.WAIT)) {

            CALL_BACK rule = getICommandCallback(game);

            String topOperation = cmdStack.pop();
            game.logger().info("pop process={}", topOperation);

            FlowCommand flowCommand = flowCommandConverter.parse(topOperation);
            // 获得当前事件
            Flow<GAME_ENTITY> flow = flows.get(flowCommand.getFlowName());

            if (flow != null) {
                // 执行流过程
                flow.execute(game, flowCommand.getParams());
            } else {
                this.noFlowException(game, flowCommand.getFlowName());
            }

            // 游戏结束标识,直接结束
            if (forceExitLoop(flow, topOperation)) {
                return;
            }

            List<String> list = rule.afterCommandExecute(game, flowCommand.getFlowName(), flowCommand.getParams());
            this.addProcesses(cmdStack, list);
            game.logger().info("add process= {}", list);
            game.logger().info("remain process {}", cmdStack);
        }

    }

    public void pop(GAME_ENTITY game) {
        if (game.getCmdStack().size() > 0) {
            game.getCmdStack().pop();
        } else {
            game.logger().error("栈已空不可弹出");
        }
    }

    public void push(GAME_ENTITY game, List<String> states) {
        for (String item : states) {
            game.getCmdStack().push(item);
        }
    }

    public void push(GAME_ENTITY game, String... states) {
        for (String item : states) {
            game.getCmdStack().push(item);
        }
    }

    /**
     * 添加流程 例子：<br>
     * p1,p2,p3->p3,p2,p1<br>
     *
     * @param stack
     * @param list
     * @author wcy 2017年8月25日
     */
    private void addProcesses(Stack<String> stack, List<String> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            String state = list.get(i);
            stack.push(state);
        }
    }

    public void nextProcess(GAME_ENTITY game, String flowName) {
        pop(game);
        push(game, flowName);

        process(game);
    }

    public void noFlowException(GAME_ENTITY game, String stateEnum) {
        String equals = "==========================================================";
        String context = " no flow : " + stateEnum + " ";
        int len1 = equals.length();
        int len2 = context.length();

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(equals).append("\n");
        for (int i = 0; i < (len1 - len2) / 2; i++) {
            sb.append("=");
        }
        sb.append(context);
        for (int i = 0; i < (len1 - len2) / 2; i++) {
            sb.append("=");
        }
        sb.append("\n");
        sb.append(equals).append("\n");
        game.logger().info(sb.toString());
    }
}
