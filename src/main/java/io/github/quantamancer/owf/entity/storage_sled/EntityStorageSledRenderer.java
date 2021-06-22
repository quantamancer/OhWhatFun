package io.github.quantamancer.owf.entity.storage_sled;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class EntityStorageSledRenderer extends EntityRenderer<EntityStorageSled> {

    public EntityStorageSledRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(EntityStorageSled entity) {
        return null;
    }
}
