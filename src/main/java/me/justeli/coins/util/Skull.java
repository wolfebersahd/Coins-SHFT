package me.justeli.coins.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

/* Eli @ January 6, 2020 (creation) */
public final class Skull
{
    private static final HashMap<String, ItemStack> COIN = new HashMap<>();
    private static final UUID SKULL_UUID = UUID.fromString("00000001-0001-0001-0001-000000000002");

    public static ItemStack of (String texture)
    {
        if (texture == null || texture.isEmpty())
            return null;

        if (COIN.containsKey(texture))
            return COIN.get(texture).clone();

        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();

        if (skullMeta == null)
            return skullItem;

        if (!applyProfileViaApi(skullMeta, texture))
        {
            applyProfileViaReflection(skullMeta, texture);
        }

        skullItem.setItemMeta(skullMeta);

        COIN.put(texture, skullItem);
        return skullItem.clone();
    }

    private static boolean applyProfileViaApi (SkullMeta skullMeta, String texture)
    {
        try
        {
            PlayerProfile profile = Bukkit.createProfile(SKULL_UUID, "randomCoin");
            profile.setProperty(new ProfileProperty("textures", texture));
            skullMeta.setPlayerProfile(profile);
            return true;
        }
        catch (Throwable ignored)
        {
            return false;
        }
    }

    private static void applyProfileViaReflection (SkullMeta skullMeta, String texture)
    {
        GameProfile profile = new GameProfile(SKULL_UUID, "randomCoin");
        profile.getProperties().put("textures", new Property("textures", texture));

        try
        {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        }
        catch (ReflectiveOperationException | IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }
}
