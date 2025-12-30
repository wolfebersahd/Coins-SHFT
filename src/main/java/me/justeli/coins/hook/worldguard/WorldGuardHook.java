package me.justeli.coins.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.justeli.coins.CustomFlags;
import org.bukkit.Location;

public final class WorldGuardHook {

    private WorldGuardHook() {}

    /**
     * @return false if coins-drop is DENY in any applicable region
     */
    public static boolean canDropCoins(Location location) {
        if (CustomFlags.COINS_DROP_FLAG == null) {
            return true; // fail open
        }

        RegionQuery query = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .createQuery();

        return query.testState(
                BukkitAdapter.adapt(location),
                null,
                CustomFlags.COINS_DROP_FLAG
        );
    }
}
