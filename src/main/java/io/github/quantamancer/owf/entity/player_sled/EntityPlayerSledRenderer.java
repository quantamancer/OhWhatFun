package io.github.quantamancer.owf.entity.player_sled;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class EntityPlayerSledRenderer extends LivingEntityRenderer<EntityPlayerSled, EntityPlayerSledModel> {

    public EntityPlayerSledRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new EntityPlayerSledModel(), 1.0f);
    }

    @Override
    public void render(EntityPlayerSled entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(EntityPlayerSled entity) {
        return new Identifier("owf", "textures/entity/player_sled.png");
    }
}
