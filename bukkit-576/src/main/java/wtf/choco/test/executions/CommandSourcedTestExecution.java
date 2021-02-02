package wtf.choco.test.executions;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

public interface CommandSourcedTestExecution {

    public void runTest(CommandSender sender, String[] args);

    /**
     * @param sender
     * @param args
     *
     * @return suggestions
     */
    public default List<String> queryTabCompletions(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

}
