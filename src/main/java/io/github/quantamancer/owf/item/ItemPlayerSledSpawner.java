package io.github.quantamancer.owf.item;

import io.github.quantamancer.owf.entity.ModEntities;
import io.github.quantamancer.owf.entity.player_sled.EntityPlayerSled;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class ItemPlayerSledSpawner extends Item {
    public ItemPlayerSledSpawner(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if(!context.getWorld().isClient()) {
            EntityPlayerSled sled = new EntityPlayerSled(ModEntities.STORAGE_SLED, context.getWorld()).spawn(context.getWorld(), context.getBlockPos().getX()+ 0.5D, context.getBlockPos().getY() + 1.5D, context.getBlockPos().getZ()+ 0.5D, context.getPlayerYaw());
            context.getWorld().spawnEntity(sled);
            context.getStack().decrement(1);
        }

        return ActionResult.success(true);
    }
}
