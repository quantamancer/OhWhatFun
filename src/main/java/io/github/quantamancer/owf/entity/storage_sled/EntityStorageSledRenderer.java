package io.github.quantamancer.owf.entity.storage_sled;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class EntityStorageSledRenderer extends MobEntityRenderer<EntityStorageSled, EntityStorageSledModel> {

    public EntityStorageSledRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new EntityStorageSledModel(), 1.0F);
    }

    @Override
    public Identifier getTexture(EntityStorageSled entity) {
        return new Identifier("owf", "textures/entity/storage_sled.png");
    }
}
