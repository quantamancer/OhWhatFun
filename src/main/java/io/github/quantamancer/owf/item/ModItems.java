package io.github.quantamancer.owf.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static final ItemPlayerSledSpawner PLAYER_SLED_SPAWNER = new ItemPlayerSledSpawner(new FabricItemSettings().group(ItemGroup.TRANSPORTATION));

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier("owf", "spawn_player_sled"), PLAYER_SLED_SPAWNER);
    }

}
