package me.justeli.coins.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * Handles WorldGuard integration for Coins-SHFT.
 * Registers a custom "coins-drop" flag for regions.
 */
public final class WorldGuardHook {

    /** The custom coins-drop flag. True = coins can drop, False = cannot drop. */
    public static StateFlag COINS_DROP_FLAG;

    // Private constructor to prevent instantiation
    private WorldGuardHook() {}

    /**
     * Register the coins-drop flag safely.
     * Must be called before plugin onEnable finishes (ideally in onLoad()).
     */
    public static void register(JavaPlugin plugin) {
        // Run one tick later to ensure WorldGuard has initialized
        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                // Create and register the "coins-drop" flag
                StateFlag flag = new StateFlag("coins-drop", true);
                WorldGuard.getInstance().getFlagRegistry().register(flag);
                COINS_DROP_FLAG = flag;
                plugin.getLogger().log(Level.INFO, "[Coins-SHFT] Successfully registered WorldGuard coins-drop flag!");
            } catch (FlagConflictException e) {
                // If the flag already exists, use the existing one
                Flag<?> existing = WorldGuard.getInstance().getFlagRegistry().get("coins-drop");
                if (existing instanceof StateFlag) {
                    COINS_DROP_FLAG = (StateFlag) existing;
                    plugin.getLogger().log(Level.INFO, "[Coins-SHFT] WorldGuard coins-drop flag already exists, using existing flag.");
                } else {
                    // Log the error if there's a conflict with an incompatible flag type
                    plugin.getLogger().log(Level.WARNING, "[Coins-SHFT] Failed to register WorldGuard coins-drop flag due to flag conflict!", e);
                }
            } catch (Exception e) {
                // Log any other errors that might occur during registration
                plugin.getLogger().log(Level.SEVERE, "[Coins-SHFT] Unexpected error while registering WorldGuard coins-drop flag.", e);
            }
        });
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
            return true; // Default to true if the flag hasn't been initialized
        }

        try {
            // Query the region container for the state of the "coins-drop" flag
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
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
