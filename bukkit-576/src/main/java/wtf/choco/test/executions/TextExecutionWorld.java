package wtf.choco.test.executions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.MaterialBlock;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.StringUtil;

import wtf.choco.test.util.TextUtil;

public class TextExecutionWorld implements CommandSourcedTestExecution {

    private static final List<String> BLOCK_KEYS = Registry.BLOCKS.getValues().stream().map(block -> block.getKey().toString()).collect(Collectors.toList());

    @Override
    public void runTest(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Missing operation");
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can run this test");
            return;
        }

        Player player = (Player) sender;

        if (args[1].equalsIgnoreCase("query")) {
            RayTraceResult result = player.rayTraceBlocks(10.0);
            if (result == null || result.getHitBlock() == null) {
                player.sendMessage("Look at a block to query its data");
                return;
            }

            Block targetBlock = result.getHitBlock();

            if (args.length < 3) {
                player.sendMessage("Looking at " + ChatColor.YELLOW + targetBlock.getType().getKey() + ChatColor.RESET + " (" + targetBlock.getState() + ")");
            }
            else {
                MaterialBlock blockToCheck = MaterialBlock.match(args[2]);
                if (blockToCheck == null) {
                    player.sendMessage("Could not find block with id " + args[2] + ". Does it exist?");
                    return;
                }

                player.sendMessage("Matches " + ChatColor.YELLOW + blockToCheck.getKey() + ChatColor.RESET + "? " + TextUtil.colouredBoolean(blockToCheck == targetBlock.getType()));
            }
        }
        else if (args[1].equalsIgnoreCase("set")) {
            if (args.length < 3) {
                player.sendMessage("Missing block type to set");
                return;
            }

            MaterialBlock blockToSet = MaterialBlock.match(args[2]);
            if (blockToSet == null) {
                player.sendMessage("Could not find block with id " + args[2] + ". Does it exist?");
                return;
            }

            RayTraceResult result = player.rayTraceBlocks(10.0);
            if (result == null || result.getHitBlock() == null) {
                player.sendMessage("Look at a block to set its type");
                return;
            }

            Block targetBlock = result.getHitBlock();
            if (args.length < 4 || !args[3].equalsIgnoreCase("-s")) {
                targetBlock = targetBlock.getRelative(result.getHitBlockFace());
            }

            targetBlock.setType(blockToSet);
            player.sendMessage("Set block to " + ChatColor.YELLOW + targetBlock.getType().getKey());
        }
    }

    @Override
    public List<String> queryTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], Arrays.asList("query", "set"), new ArrayList<>());
        }
        else if (args.length == 3) {
            String materialArg = args[2];
            if (!materialArg.startsWith("minecraft:")) {
                materialArg = (materialArg.startsWith(":") ? "minecraft" : "minecraft:") + materialArg;
            }

            return StringUtil.copyPartialMatches(materialArg, BLOCK_KEYS, new ArrayList<>());
        }
        else if (args.length == 4 && args[1].equalsIgnoreCase("set")) {
            return StringUtil.copyPartialMatches(args[3], Arrays.asList("-s"), new ArrayList<>());
        }

        return Collections.emptyList();
    }

}
