package io.github.quantamancer.owf.entity.storage_sled;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.util.Identifier;

public class EntityStorageSledRenderer extends LivingEntityRenderer<EntityStorageSled, EntityStorageSledModel> {

    public EntityStorageSledRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new EntityStorageSledModel(), 1.0F);
    }

    @Override
    public Identifier getTexture(EntityStorageSled entity) {
        return null;
    }
}
