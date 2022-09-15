package org.egc.gis.whitebox;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.AbstractRunCommand;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public abstract class BaseWhitebox extends AbstractRunCommand {

    public String workspace;

    /**
     * recommend to configure it (whitebox.workspace) in Spring Framework configuration files <br/>
     * (e.g., application.yml in Spring Boot)
     *
     * @param userWorkspace re-initialize workspace/output directory <br/>
     */
    public void init(String userWorkspace) {
        if (StringUtils.isBlank(userWorkspace)) {
            workspace = System.getProperty("java.io.tmpdir");
        }
        workspace = userWorkspace;
    }

    /**
     * 初始化命令行
     *
     * @return
     */
    @Override
    public CommandLine initCmd() {
        CommandLine cmd = new CommandLine("whitebox_tools");
        cmd.addArgument("--wd=");
        cmd.addArgument(workspace);
        return cmd;
    }

}