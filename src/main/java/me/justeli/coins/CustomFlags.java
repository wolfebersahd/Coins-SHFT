package me.justeli.coins;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import java.util.logging.Level;

public final class CustomFlags {

    // true = default ALLOW
    public static final StateFlag COINS_DROP_FLAG =
            new StateFlag("coins-drop", true);

    private CustomFlags() {}

    public static void register() {
        try {
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
            registry.register(COINS_DROP_FLAG);

            Coins.getInstance().getLogger()
                    .log(Level.INFO, "Custom WorldGuard flag 'coins-drop' registered.");
        } catch (Exception e) {
            Coins.getInstance().getLogger()
                    .log(Level.WARNING, "Failed to register WorldGuard flags", e);
        }
    }
}
