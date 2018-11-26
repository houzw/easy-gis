package org.egc.gis.taudem;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.egc.commons.command.AbstractRunCommand;

@Slf4j
public abstract class BaseTauDEM extends AbstractRunCommand {

    /**
     * 初始化命令行
     * @return
     */
    @Override
    public CommandLine initCmd() {
        CommandLine cmd = new CommandLine("mpiexec");
        cmd.addArgument("-n");
        cmd.addArgument(String.valueOf(RunUtils.processors()));
        return cmd;
    }
}