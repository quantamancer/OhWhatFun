package io.github.quantamancer.owf.entity.storage_sled;

import io.github.quantamancer.owf.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class EntityStorageSled extends MobEntity implements Inventory, NamedScreenHandlerFactory {

    private float yawVelocity;
    private DefaultedList<ItemStack> inventory;

    public EntityStorageSled(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
        this.inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);
    }

    public static EntityStorageSled spawn(World world, double x, double y, double z, float yaw) {
        EntityStorageSled sled = new EntityStorageSled(ModEntities.STORAGE_SLED, world);
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
        ItemStack handStack = player.getStackInHand(hand);
        if (!handStack.isEmpty()) {
            ActionResult result = handStack.useOnEntity(player, this, hand);
            if (result.isAccepted())
                return result;
        }
        if (player.isSneaking()) {
            player.openHandledScreen(this);
            return ActionResult.SUCCESS;
        }
        if(handStack.isEmpty() && !player.isSneaking()) {
            return ActionResult.success(player.startRiding(this));
        }

        return super.interact(player, hand);
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

    @Override
    public int size() {
        return 9*6;
    }

    @Override
    public boolean isEmpty() {
        Iterator<ItemStack> itr = this.inventory.iterator();
        ItemStack itemStack;
        do {
            if (!itr.hasNext()) {
                return true;
            }
            itemStack = itr.next();
        } while (itemStack.isEmpty());
        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack itemStack = this.inventory.get(slot);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.inventory.set(slot, ItemStack.EMPTY);
            return itemStack;
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
    }

    @Override
    public void markDirty() {}

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.isRemoved()) {
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return GenericContainerScreenHandler.createGeneric9x6(syncId, inv, this);
    }

    @Override
    public void clear() {}

    @Override
    protected void drop(DamageSource source) {
        super.drop(source);
        if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            for (ItemStack itemStack : this.inventory) {
                dropStack(itemStack);
            }
        }
    }
}
