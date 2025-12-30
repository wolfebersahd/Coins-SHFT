package me.justeli.coins.hooks;

import com.sk89q.worldguard.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionContainer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

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
        // Make sure the flag is initialized
        if (COINS_DROP_FLAG == null) {
            return true; // Default to true if the flag hasn't been initialized
        }

        try {
            // Get the region container from WorldGuard
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            // Convert the location to BlockVector3
            var vector = BukkitAdapter.asBlockVector(location);

            // Get the region manager for the world
            RegionManager regions = container.get(BukkitAdapter.adapt(location.getWorld()));

            if (regions == null) {
                return true; // No regions available, default to true
            }

            // Check if the player is inside any region and get the applicable regions at this location
            var applicableRegions = regions.getApplicableRegions(vector);

            // Check if any region denies the coin-drop flag
            if (applicableRegions.testState(COINS_DROP_FLAG) == StateFlag.State.DENY) {
                return false; // Return false if coin drop is denied
            }

            // If no region denies the flag, allow coin drop
            return true;
        } catch (Exception e) {
            // In case of an error, log it and allow coins to drop
            Bukkit.getLogger().log(Level.WARNING, "[Coins-SHFT] Failed to check WorldGuard coins-drop flag at location " + location, e);
            return true; // Default to true in case of error (fallback behavior)
        }
    }
}
