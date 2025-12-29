package me.justeli.coins.hooks;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;

public final class WorldGuardHook {

    public static StateFlag COINS_DROP_FLAG;

    private WorldGuardHook() {}

    /** Register the coins-drop flag safely on the next tick */
    public static void register(JavaPlugin plugin) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                StateFlag flag = new StateFlag("coins-drop", true);
                WorldGuard.getInstance().getFlagRegistry().register(flag);
                COINS_DROP_FLAG = flag;
                plugin.getLogger().log(Level.INFO, "[Coins-SHFT] Successfully registered WorldGuard coins-drop flag!");
            } catch (FlagConflictException e) {
                Flag<?> existing = WorldGuard.getInstance().getFlagRegistry().get("coins-drop");
                if (existing instanceof StateFlag) {
                    COINS_DROP_FLAG = (StateFlag) existing;
                    plugin.getLogger().log(Level.INFO, "[Coins-SHFT] WorldGuard coins-drop flag already exists, using existing flag.");
                } else {
                    plugin.getLogger().log(Level.WARNING, "[Coins-SHFT] Failed to register WorldGuard coins-drop flag!", e);
                }
            }
        });
    }

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
