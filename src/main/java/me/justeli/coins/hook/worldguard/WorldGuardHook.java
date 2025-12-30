package me.justeli.coins.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * Handles WorldGuard integration for Coins-SHFT.
 * Uses the custom "coins-drop" flag for regions.
 */
public final class WorldGuardHook {

    /** The custom coins-drop flag. True = coins can drop, False = cannot drop. */
    public static StateFlag COINS_DROP_FLAG;

    // Private constructor to prevent instantiation
    private WorldGuardHook() {}

    /**
     * Initializes the COINS_DROP_FLAG by checking if WorldGuard is present.
     * This method should be called during plugin setup to ensure the flag is registered.
     *
     * @param plugin the plugin instance (for logging)
     */
    public static void initialize(JavaPlugin plugin) {
        if (WorldGuard.getInstance() == null) {
            Bukkit.getLogger().log(Level.WARNING, "[Coins-SHFT] WorldGuard is not present, coins drop flag will not be available.");
            return;
        }

        COINS_DROP_FLAG = new StateFlag("coins-drop", false);  // Default to deny coins-drop

        // Register the flag with WorldGuard
        try {
            WorldGuard.getInstance().getFlagRegistry().register(COINS_DROP_FLAG);
            Bukkit.getLogger().log(Level.INFO, "[Coins-SHFT] Registered custom coins-drop flag with WorldGuard.");
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "[Coins-SHFT] Failed to register coins-drop flag with WorldGuard.", e);
        }
    }

    /**
     * Checks whether coins can drop at the given location, according to WorldGuard region flags.
     *
     * @param location the location to test
     * @return true if coins can drop, false if blocked by region
     */
    public static boolean canDropCoins(Location location) {
        // Ensure WorldGuard and flag are properly initialized
        if (COINS_DROP_FLAG == null) {
            Bukkit.getLogger().log(Level.WARNING, "[Coins-SHFT] COINS_DROP_FLAG is not initialized. Returning default true.");
            return true; // Default to true if the flag hasn't been initialized
        }

        try {
            // Query the region container for the state of the "coins-drop" flag
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            if (container == null) {
                Bukkit.getLogger().log(Level.WARNING, "[Coins-SHFT] WorldGuard region container is null. Returning default true.");
                return true;
            }

            // Check the flag state at the given location
            return container.createQuery().testState(
                    BukkitAdapter.adapt(location),
                    null,
                    COINS_DROP_FLAG
            );
        } catch (Exception e) {
            // In case WorldGuard is not available or any other error occurs
            Bukkit.getLogger().log(Level.WARNING, "[Coins-SHFT] Failed to check WorldGuard coins-drop flag at location " + location, e);
            return true; // Default to true in case of error (fallback behavior)
        }
    }
}
