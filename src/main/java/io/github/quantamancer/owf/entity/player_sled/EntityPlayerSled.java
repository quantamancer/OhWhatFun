package io.github.quantamancer.owf.entity.player_sled;


import io.github.quantamancer.owf.entity.ModEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityPlayerSled extends MobEntity {

    private float yawVelocity;

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
        return 0.1D;
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        return ActionResult.success(player.startRiding(this));
    }

    @Override
    public void updatePassengerPosition(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            float f = 0.0F;
            float g = (float)((this.isRemoved() ? 0.009999999776482582D : this.getMountedHeightOffset()) + passenger.getHeightOffset());
            if (this.getPassengerList().size() > 1) {
                int i = this.getPassengerList().indexOf(passenger);
                if (i == 0) {
                    f = 0.2F;
                } else {
                    f = -0.6F;
                }
            }
            Vec3d vec3d = (new Vec3d((double)f, 0.0D, 0.0D)).rotateY(this.getYaw() * 0.017453292F - 1.5707964F);
            passenger.setPosition(this.getX() + vec3d.x, this.getY() + (double)g, this.getZ() + vec3d.z);
            passenger.setYaw(passenger.getYaw() + this.yawVelocity);
            passenger.setHeadYaw(passenger.getHeadYaw() + this.yawVelocity);
            this.copyEntityData(passenger);
        }
    }

    protected void copyEntityData(Entity entity) {
        entity.setBodyYaw(this.getYaw());
        float f = MathHelper.wrapDegrees(entity.getYaw() - this.getYaw());
        float g = MathHelper.clamp(f, -105.0F, 105.0F);
        entity.prevYaw += g - f;
        entity.setYaw(entity.getYaw() + g - f);
        entity.setHeadYaw(entity.getYaw());
    }
}
