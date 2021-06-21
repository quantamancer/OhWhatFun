package io.github.quantamancer.owf.entity.player_sled;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.world.World;

public class EntityPlayerSled extends BoatEntity {

    public EntityPlayerSled(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntityPlayerSled(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

}
