package io.github.quantamancer.owf.entity.player_sled;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class EntityPlayerSledRenderer extends MobEntityRenderer<EntityPlayerSled, EntityPlayerSledModel> {

    public EntityPlayerSledRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new EntityPlayerSledModel(), 1.0f);
    }

    @Override
    public Identifier getTexture(EntityPlayerSled entity) {
        return new Identifier("owf", "textures/entity/player_sled.png");
    }
}
