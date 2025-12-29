package me.justeli.coins.hooks;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import org.bukkit.Location;
import java.util.logging.Level;

/**
 * Handles WorldGuard integration for Coins-SHFT.
 * Registers a custom "coins-drop" flag for regions.
 */
public final class WorldGuardHook {

    /** The custom coins-drop flag. True = coins can drop in region, False = cannot drop. */
    public static StateFlag COINS_DROP_FLAG;

    // Private constructor to prevent instantiation
    private WorldGuardHook() {}

    /**
     * Register the coins-drop flag safely.
     * Must be called synchronously before plugin onEnable finishes (ideally in onLoad()).
     */
    public static void register() {
        try {
            StateFlag flag = new StateFlag("coins-drop", true);
            WorldGuard.getInstance().getFlagRegistry().register(flag);
            COINS_DROP_FLAG = flag;
            WorldGuardHook.logInfo("[Coins-SHFT] Successfully registered WorldGuard coins-drop flag!");
        } catch (FlagConflictException e) {
            Flag<?> existing = WorldGuard.getInstance().getFlagRegistry().get("coins-drop");
            if (existing instanceof StateFlag) {
                COINS_DROP_FLAG = (StateFlag) existing;
                WorldGuardHook.logInfo("[Coins-SHFT] WorldGuard coins-drop flag already exists, using existing flag.");
            } else {
                WorldGuardHook.logWarning("[Coins-SHFT] Failed to register WorldGuard coins-drop flag!", e);
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
        if (COINS_DROP_FLAG == null) return true;

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        return container.createQuery().testState(
                BukkitAdapter.adapt(location),
                null,
                COINS_DROP_FLAG
        );
    }

    /** Logs an info message to the console */
    private static void logInfo(String message) {
        WorldGuard.getInstance().getPlatform().getLogger().info(message);
    }

    /** Logs a warning message to the console */
    private static void logWarning(String message, Exception e) {
        WorldGuard.getInstance().getPlatform().getLogger().log(Level.WARNING, message, e);
    }
}
