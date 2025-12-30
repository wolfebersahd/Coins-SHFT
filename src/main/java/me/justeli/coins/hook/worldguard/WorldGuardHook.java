package me.justeli.coins.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
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
            // Query the region container for the state of the "coins-drop" flag
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            StateFlag.State state = container.createQuery().getFlag(COINS_DROP_FLAG, BukkitAdapter.adapt(location));

            // If the state is ALLOW, coins can drop
            return state == StateFlag.State.ALLOW;
        } catch (Exception e) {
            // In case WorldGuard is not available or any other error occurs
            Bukkit.getLogger().log(Level.WARNING, "[Coins-SHFT] Failed to check WorldGuard coins-drop flag at location " + location, e);
            return true; // Default to true in case of error (fallback behavior)
        }
    }
}
