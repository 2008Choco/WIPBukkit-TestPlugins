package wtf.choco.test.util;

import org.bukkit.ChatColor;

public final class TextUtil {

    private TextUtil() { }

    public static String colouredBoolean(boolean value) {
        return value ? ChatColor.GREEN.toString() + value : ChatColor.RED.toString() + value;
    }

}
