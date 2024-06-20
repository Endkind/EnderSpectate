package net.endkind.enderspectate;

import org.bukkit.plugin.java.JavaPlugin;

public final class EnderSpectate extends JavaPlugin {

    private static EnderSpectate instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Enabling EnderSpectate");

        instance = this;

        if (getCommand("enderspectate") != null) {
            getCommand("enderspectate").setExecutor(new CommandHandler());
        } else {
            getLogger().severe("Command 'enderspectate' could not be found!");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Disabling EnderSpectate");
    }

    public static EnderSpectate getInstance() {
        return instance;
    }
}
