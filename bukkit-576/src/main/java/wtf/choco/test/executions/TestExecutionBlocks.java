package wtf.choco.test.executions;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.MaterialBlock;
import org.bukkit.command.CommandSender;

import wtf.choco.test.util.TextUtil;

public class TestExecutionBlocks implements CommandSourcedTestExecution {

    @Override
    public void runTest(CommandSender sender, String[] args) {
        sendBlockTo(sender, MaterialBlock.ICE, ChatColor.AQUA);
        sendBlockTo(sender, MaterialBlock.ANVIL, ChatColor.GRAY);
        sendBlockTo(sender, MaterialBlock.LAVA, ChatColor.RED);
    }

    private static void sendBlockTo(CommandSender sender, MaterialBlock block, ChatColor colour) {
        sender.sendMessage("");
        sender.sendMessage("Information on " + colour + block.getKey());
        sender.sendMessage(colour + StringUtils.repeat("-", 44));

        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Blast resistance: " + ChatColor.WHITE + block.getBlastResistance());
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Friction factor: " + ChatColor.WHITE + block.getFrictionFactor());
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Jump factor: " + ChatColor.WHITE + block.getJumpFactor());
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Speed factor: " + ChatColor.WHITE + block.getSpeedFactor());
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Localized name: " + ChatColor.WHITE + block.getLocalizedName());
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Step sound: " + ChatColor.WHITE + block.getSoundGroup().getStepSound().getKey());
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Gravity: " + TextUtil.colouredBoolean(block.hasGravity()));
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Air: " + TextUtil.colouredBoolean(block.isAir()));
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Is Block: " + TextUtil.colouredBoolean(block.isBlock()));
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Is Item: " + TextUtil.colouredBoolean(block.isItem()));
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Is Flammable: " + TextUtil.colouredBoolean(block.isFlammable()));
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Is Fluid: " + TextUtil.colouredBoolean(block.isFluid()));
        sender.sendMessage("");
    }

}
