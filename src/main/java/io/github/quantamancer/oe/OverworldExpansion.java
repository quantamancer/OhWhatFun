package io.github.quantamancer.oe;

import io.github.quantamancer.oe.block.ModBlocks;
import io.github.quantamancer.oe.entity.ModEntities;
import io.github.quantamancer.oe.item.ModItems;
import net.fabricmc.api.ModInitializer;

public class OverworldExpansion implements ModInitializer {

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModItems.register();
        ModEntities.register();
    }

}
