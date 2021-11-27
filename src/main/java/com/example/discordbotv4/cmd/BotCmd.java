package com.example.discordbotv4.cmd;

import java.util.Collections;
import java.util.List;

public class BotCmd {
    public static final String CMD_PREFIX = "!";
    private final String cmd;
    private final CmdEnum cmdEnum;
    private final List<String> parameters;
    private String description;

    public BotCmd(CmdEnum cmdName, String cmd, List<String> parameters) {
        this.cmdEnum = cmdName;
        this.cmd = cmd;
        this.parameters = parameters;
    }

    public BotCmd(CmdEnum cmdName, String cmd, String description) {
        this.cmdEnum = cmdName;
        this.cmd = cmd;
        this.parameters = Collections.emptyList();
        this.description = description;
    }

    public BotCmd(CmdEnum cmdName, String cmd, List<String> parameters, String description) {
        this.cmdEnum = cmdName;
        this.cmd = cmd;
        this.parameters = parameters;
        this.description = description;
    }

    public String getCmd() {
        return CMD_PREFIX + cmd;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public boolean hasParameters() {
        return !parameters.isEmpty();
    }

    public String getDescription() {
        return description;
    }

    public CmdEnum getCmdEnum() {
        return cmdEnum;
    }
}
