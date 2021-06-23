package io.github.quantamancer.owf.entity.player_sled;

import io.github.quantamancer.owf.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityPlayerSled extends MobEntity {

    public EntityPlayerSled(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    public static EntityPlayerSled spawn(World world, double x, double y, double z, float yaw) {
        EntityPlayerSled sled = new EntityPlayerSled(ModEntities.PLAYER_SLED, world);
        sled.setPosition(x, y ,z);
        sled.setVelocity(Vec3d.ZERO);
        sled.prevX = x;
        sled.prevY = y;
        sled.prevZ = z;
        sled.setYaw(yaw);
        return sled;
    }

    @Override
    public double getMountedHeightOffset() {
        return 0.15D;
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        return ActionResult.success(player.startRiding(this));
    }
}
