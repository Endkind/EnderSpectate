package net.endkind.enderspectate;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CommandHandler implements CommandExecutor {

    private final File playerDataFile;
    private final YamlConfiguration playerDataConfig;

    public CommandHandler() {
        playerDataFile = new File(EnderSpectate.getInstance().getDataFolder(), "playerData.yml");
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§5EnderSpectate§8 >>§4 This command can only be executed by a player");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        String path = "players." + playerUUID.toString();

        if (player.getGameMode() == GameMode.SPECTATOR) {
            if (playerDataConfig.contains(path + ".location") && playerDataConfig.contains(path + ".gamemode") ) {
                Location originalLocation = playerDataConfig.getLocation(path + ".location");
                GameMode originalGameMode = GameMode.valueOf(playerDataConfig.getString(path + ".gamemode"));

                if (originalLocation != null && originalGameMode != null) {
                    player.teleport(originalLocation);
                    player.setGameMode(originalGameMode);
                    player.sendMessage("§5EnderSpectate§8 >>§a You have been returned to your original location and game mode.");
                    playerDataConfig.set(path, null);
                    savePlayerData();
                } else {
                    player.sendMessage("§5EnderSpectate§8 >>§c Error: Original location or game mode not found.");
                }
            } else {
                player.sendMessage("§5EnderSpectate§8 >>§c Error: Original location or game mode not found.");
            }
        } else {
            playerDataConfig.set(path + ".location", player.getLocation());
            playerDataConfig.set(path + ".gamemode", player.getGameMode().name());
            savePlayerData();
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage("§5EnderSpectate§8 >>§a You are now in spectator mode.");
        }

        return true;
    }

    private void savePlayerData() {
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
