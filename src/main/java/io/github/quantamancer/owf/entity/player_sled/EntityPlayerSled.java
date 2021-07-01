package io.github.quantamancer.owf.entity.player_sled;

import io.github.quantamancer.owf.entity.ModEntities;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class EntityPlayerSled extends HorseEntity implements Inventory, NamedScreenHandlerFactory {

    private DefaultedList<ItemStack> inventory;

    public EntityPlayerSled(EntityType<? extends HorseEntity> entityType, World world) {
        super(entityType, world);
        this.inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);
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
    protected void initGoals() {
        this.goalSelector.add(1, new LookAroundGoal(this));
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        ItemStack currStack = player.getStackInHand(hand);
        if(!currStack.isEmpty()) {
            ActionResult result = currStack.useOnEntity(player, this, hand);
            if (result.isAccepted())
                return result;
        } else if (player.isSneaking()) {
            player.openHandledScreen(this);
            return ActionResult.SUCCESS;
        } else if (currStack.isEmpty() && !player.isSneaking()) { return ActionResult.success(player.startRiding(this)); }
        return super.interact(player, hand);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return ActionResult.PASS;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ITEM_SHIELD_BLOCK;
    }

    @Override
    protected void playJumpSound() {
        this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.4F, 1.0F);
    }

    @Override
    protected void playWalkSound(BlockSoundGroup group) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.4F, 1.0F);
    }

    @Override
    protected boolean receiveFood(PlayerEntity player, ItemStack item) {
        return false;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ITEM_BREAK;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canJump() {
        return false;
    }

    @Override
    public double getMountedHeightOffset() {
        return 0.1D;
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        return false;
    }

    @Override
    public boolean isTame() {
        return true;
    }

    @Override
    public boolean isSaddled() {
        return true;
    }

    @Override
    public boolean hasArmorSlot() {
        return false;
    }

    @Override
    public boolean hasArmorInSlot() {
        return false;
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
