package io.github.quantamancer.owf.item;

import io.github.quantamancer.owf.entity.ModEntities;
import io.github.quantamancer.owf.entity.storage_sled.EntityStorageSled;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class ItemStorageSledSpawner extends Item {
    public ItemStorageSledSpawner(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if(!context.getWorld().isClient()) {
            EntityStorageSled sled = new EntityStorageSled(ModEntities.STORAGE_SLED, context.getWorld()).spawn(context.getWorld(), context.getBlockPos().getX()+ 0.5D, context.getBlockPos().getY() + 1.5D, context.getBlockPos().getZ()+ 0.5D, context.getPlayerYaw());
            context.getWorld().spawnEntity(sled);
            context.getStack().decrement(1);
        }

        return ActionResult.success(true);
    }
}
