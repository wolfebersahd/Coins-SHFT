package me.justeli.coins.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public final class WorldGuardHook {

    public static StateFlag COINS_DROP_FLAG;
    private static boolean initialized = false;

    private WorldGuardHook() {}

    /**
     * Safely registers the coins-drop flag.
     * Must be called after plugin enable, but will not crash if it's too late.
     */
    public static void register(Plugin plugin) {
        if (initialized) return; // already done
        initialized = true;

        // Delay registration by 1 tick to avoid WorldGuard initialization issues
        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                StateFlag flag = new StateFlag("coins-drop", true);
                WorldGuard.getInstance().getFlagRegistry().register(flag);
                COINS_DROP_FLAG = flag;
                plugin.getLogger().info("Registered WorldGuard coins-drop flag.");
            } catch (FlagConflictException e) {
                Flag<?> existing = WorldGuard.getInstance().getFlagRegistry().get("coins-drop");
                if (existing instanceof StateFlag) {
                    COINS_DROP_FLAG = (StateFlag) existing;
                    plugin.getLogger().info("Using existing WorldGuard coins-drop flag.");
                }
            } catch (IllegalStateException e) {
                // Happens if WorldGuard does not allow new flags at this time
                Flag<?> existing = WorldGuard.getInstance().getFlagRegistry().get("coins-drop");
                if (existing instanceof StateFlag) {
                    COINS_DROP_FLAG = (StateFlag) existing;
                    plugin.getLogger().warning("WorldGuard flag registration too late; using existing flag.");
                } else {
                    plugin.getLogger().severe("Failed to register WorldGuard coins-drop flag!");
                }
            }
        });
    }

    /**
     * Checks if coins can drop at a given location.
     */
    public static boolean canDropCoins(Location location) {
        if (COINS_DROP_FLAG == null) return true;

        RegionContainer container = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer();

        return container.createQuery().testState(
                BukkitAdapter.adapt(location),
                null,
                COINS_DROP_FLAG
        );
    }
}
