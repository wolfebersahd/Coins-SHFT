package me.justeli.coins.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;

public final class WorldGuardHook {

    public static StateFlag COINS_DROP_FLAG;

    private WorldGuardHook() {}

    public static void register() {
        try {
            StateFlag flag = new StateFlag("coins-drop", true);
            WorldGuard.getInstance().getFlagRegistry().register(flag);
            COINS_DROP_FLAG = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = WorldGuard.getInstance().getFlagRegistry().get("coins-drop");
            if (existing instanceof StateFlag) {
                COINS_DROP_FLAG = (StateFlag) existing;
            }
        }
    }

    public static boolean canDropCoins(Location location) {
        if (COINS_DROP_FLAG == null) {
            return true;
        }

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
