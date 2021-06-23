package io.github.quantamancer.owf.entity;

import io.github.quantamancer.owf.entity.player_sled.EntityPlayerSled;
import io.github.quantamancer.owf.entity.player_sled.EntityPlayerSledRenderer;
import io.github.quantamancer.owf.entity.storage_sled.EntityStorageSled;
import io.github.quantamancer.owf.entity.storage_sled.EntityStorageSledRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {

    public static final EntityType<EntityPlayerSled> PLAYER_SLED = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("owf", "player_sled"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, EntityPlayerSled::new).build()
    );

    public static final EntityType<EntityStorageSled> STORAGE_SLED = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("owf", "storage_sled"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, EntityStorageSled::new).build()
    );

    @Environment(EnvType.CLIENT)
    public static void register() {
        FabricDefaultAttributeRegistry.register(PLAYER_SLED, EntityPlayerSled.createLivingAttributes());
        FabricDefaultAttributeRegistry.register(STORAGE_SLED, EntityStorageSled.createLivingAttributes());
        EntityRendererRegistry.INSTANCE.register(PLAYER_SLED, EntityPlayerSledRenderer::new);
        EntityRendererRegistry.INSTANCE.register(STORAGE_SLED, EntityStorageSledRenderer::new);
    }

}
