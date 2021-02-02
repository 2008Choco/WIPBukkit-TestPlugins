package wtf.choco.test.executions;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.MaterialItem;
import org.bukkit.command.CommandSender;

import wtf.choco.test.util.TextUtil;

public class TestExecutionItems implements CommandSourcedTestExecution {

    @Override
    public void runTest(CommandSender sender, String[] args) {
        sendItemTo(sender, MaterialItem.APPLE, ChatColor.RED);
        sendItemTo(sender, MaterialItem.NETHERITE_SWORD, ChatColor.GRAY);
        sendItemTo(sender, MaterialItem.BEACON, ChatColor.AQUA);
        sendItemTo(sender, MaterialItem.HONEY_BOTTLE, ChatColor.GOLD);
        sendItemTo(sender, MaterialItem.COAL_BLOCK, ChatColor.DARK_GRAY);
    }

    private static void sendItemTo(CommandSender sender, MaterialItem item, ChatColor colour) {
        sender.sendMessage("");
        sender.sendMessage("Information on " + colour + item.getKey());
        sender.sendMessage(colour + StringUtils.repeat("-", 44));

        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Block: " + ChatColor.WHITE + (item.hasBlock() ? item.asBlock().getKey() : "None"));
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Consumed sound: " + ChatColor.WHITE + item.getConsumedSound().getKey());
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Drinking sound: " + ChatColor.WHITE + item.getDrinkingSound().getKey());
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Crafting result: " + ChatColor.WHITE + (item.getCraftingRemainingItem() != null ? item.getCraftingRemainingItem().getKey() : "None"));
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Localized name: " + ChatColor.WHITE + item.getLocalizedName());
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Max durability: " + ChatColor.WHITE + (item.hasDurability() ? item.getMaxDurability() : -1));
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Max stack size: " + ChatColor.WHITE + item.getMaxStackSize());
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Item rarity: " + ChatColor.WHITE + item.getRarity());
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Is consumable: " + TextUtil.colouredBoolean(item.isConsumable()));
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Is fire proof: " + TextUtil.colouredBoolean(item.isFireProof()));
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Is furnace fuel: " + TextUtil.colouredBoolean(item.isFuel()));
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Is block: " + TextUtil.colouredBoolean(item.isBlock()));
        sender.sendMessage(colour.toString() + ChatColor.BOLD + "Is item: " + TextUtil.colouredBoolean(item.isItem()));
        sender.sendMessage("");
    }

}
