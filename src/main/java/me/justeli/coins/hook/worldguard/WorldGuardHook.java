package me.justeli.coins.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public final class WorldGuardHook {

    /** The custom coins-drop flag. */
    public static StateFlag COINS_DROP_FLAG;

    private static WorldGuardPlugin wgPlugin;

    // Private constructor to prevent instantiation
    private WorldGuardHook() {}

    /**
     * Initialize the WorldGuardHook class.
     * @param plugin the plugin instance
     */
    public static void init(Plugin plugin) {
        if (wgPlugin == null) {
            wgPlugin = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
            if (wgPlugin == null) {
                Bukkit.getLogger().log(Level.WARNING, "[Coins-SHFT] WorldGuard plugin not found!");
            } else {
                Bukkit.getLogger().log(Level.INFO, "[Coins-SHFT] WorldGuard plugin found.");
            }
        }
    }

    /**
     * Checks whether coins can drop at the given location, according to WorldGuard region flags.
     *
     * @param location the location to test
     * @return true if coins can drop, false if blocked by region
     */
    public static boolean canDropCoins(Location location) {
        if (COINS_DROP_FLAG == null) {
            return true; // Default to true if the flag hasn't been initialized
        }

        try {
            // Get the region container from WorldGuard
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            // Convert the location to WorldGuard's format
            RegionManager regions = container.get(BukkitAdapter.adapt(location.getWorld()));

            if (regions == null) {
                return true; // No regions available, default to true
            }

            // Query the region for the coins-drop flag
            boolean canDrop = regions.getApplicableRegions(BukkitAdapter.adapt(location))
                                      .testState(COINS_DROP_FLAG);

            return canDrop; // Returns true if the flag allows coins to drop, false if blocked
        } catch (Exception e) {
            // In case WorldGuard is not available or any other error occurs
            Bukkit.getLogger().log(Level.WARNING, "[Coins-SHFT] Failed to check WorldGuard coins-drop flag at location " + location, e);
            return true; // Default to true in case of error (fallback behavior)
        }
    }
}
