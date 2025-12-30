package me.justeli.coins.hooks;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class WorldGuardHook {
    
    public static StateFlag COINS_DROP_FLAG;

    private WorldGuardPlugin worldGuardPlugin;

    public WorldGuardHook(JavaPlugin plugin) {
        // Ensure WorldGuardPlugin is available
        worldGuardPlugin = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
        
        if (worldGuardPlugin != null) {
            // Register the flag with a delayed task to ensure it's fully initialized
            Bukkit.getScheduler().runTask(plugin, this::registerFlag);
        } else {
            plugin.getLogger().warning("WorldGuard plugin is not found!");
        }
    }

    private void registerFlag() {
        try {
            // Register the coins-drop flag
            COINS_DROP_FLAG = new StateFlag("coins-drop", true);
            worldGuardPlugin.getFlagRegistry().register(COINS_DROP_FLAG);
            Bukkit.getLogger().info("[Coins-SHFT] Custom WorldGuard flag 'coins-drop' registered successfully.");
        } catch (Exception e) {
            Bukkit.getLogger().warning("[Coins-SHFT] Failed to register coins-drop flag with WorldGuard: " + e.getMessage());
        }
    }

    // Other WorldGuard integration methods...
}
