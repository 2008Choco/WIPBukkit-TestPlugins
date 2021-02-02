package wtf.choco.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import wtf.choco.test.executions.CommandSourcedTestExecution;
import wtf.choco.test.executions.TestExecutionBlocks;
import wtf.choco.test.executions.TestExecutionItems;
import wtf.choco.test.executions.TestExecutionMeta;
import wtf.choco.test.executions.TextExecutionWorld;

public class TestPlugin extends JavaPlugin implements Listener {

    private static final Map<String, CommandSourcedTestExecution> TEST_EXECUTIONS = new HashMap<>();

    static {
        TEST_EXECUTIONS.put("blocks", new TestExecutionBlocks());
        TEST_EXECUTIONS.put("items", new TestExecutionItems());
        TEST_EXECUTIONS.put("meta", new TestExecutionMeta());
        TEST_EXECUTIONS.put("world", new TextExecutionWorld());
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("Which type of registerable materials would you like to test?");
            return true;
        }

        CommandSourcedTestExecution test = TEST_EXECUTIONS.get(args[0].toLowerCase());
        if (test == null) {
            sender.sendMessage("Invalid type of registerable object");
            return true;
        }

        test.runTest(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 1) {
            CommandSourcedTestExecution test = TEST_EXECUTIONS.get(args[0].toLowerCase());
            return test != null ? test.queryTabCompletions(sender, args) : Collections.emptyList();
        }

        return StringUtil.copyPartialMatches(args[0].toLowerCase(), TEST_EXECUTIONS.keySet(), new ArrayList<>());
    }

}
