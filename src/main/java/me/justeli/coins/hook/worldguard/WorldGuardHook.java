package me.justeli.coins.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;

public final class WorldGuardHook {

    private WorldGuardHook() {}

    /**
     * @return false if coins-drop is DENY in any applicable region
     */
    public static boolean canDropCoins(Location location) {
        // If flag was not registered for some reason, allow drops
        if (CustomFlags.COINS_DROP_FLAG == null) {
            return true;
        }

        RegionQuery query = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .createQuery();

        // testState returns TRUE only if the final result is ALLOW
        return query.testState(
                BukkitAdapter.adapt(location),
                null,
                CustomFlags.COINS_DROP_FLAG
        );
    }
}
