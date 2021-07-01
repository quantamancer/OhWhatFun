package io.github.quantamancer.owf.entity.player_sled;

import com.google.common.collect.Maps;
import io.github.quantamancer.owf.entity.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityPlayerSled extends HorseEntity implements Inventory, NamedScreenHandlerFactory {

    private DefaultedList<ItemStack> inventory;
    private static final TrackedData<Byte> COLOR;
    private static final Map<DyeColor, float[]> COLORS;

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
    public void openInventory(PlayerEntity player) {
        if (!this.world.isClient && (!this.hasPassengers() || this.hasPassenger(player)) && this.isTame()) {
            player.openHandledScreen(this);
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new EatGrassGoal(this));
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        ItemStack currStack = player.getStackInHand(hand);
        if(!currStack.isEmpty()) {
            ActionResult result = currStack.useOnEntity(player, this, hand);
            if (result.isAccepted())
                return result;
        } else if (currStack.isEmpty() && !player.isSneaking()) { return ActionResult.success(player.startRiding(this)); }
        return super.interact(player, hand);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return ActionResult.PASS;
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        super.onBlockCollision(state);
        BlockState currBlock = this.getLandingBlockState();
        if (this.getVelocity() != Vec3d.ZERO) {
            if (currBlock.getBlock() == Blocks.SNOW || currBlock.getBlock() == Blocks.SNOW_BLOCK || currBlock.getBlock() == Blocks.POWDER_SNOW) {
                for (int i = 0; i < 2; ++i)
                    this.world.addParticle(ParticleTypes.SNOWFLAKE, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public boolean hasPlayerRider() {
        return false;
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
    protected void playStepSound(BlockPos pos, BlockState state) {
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

    private static float[] getDyedColor(DyeColor color) {
        if (color == DyeColor.WHITE) {
            return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
        } else {
            float[] fs = color.getColorComponents();
            float f = 0.75F;
            return new float[]{fs[0] * 0.75F, fs[1] * 0.75F, fs[2] * 0.75F};
        }
    }

    public static float[] getRgbColor(DyeColor dyeColor) {
        return (float[])COLORS.get(dyeColor);
    }

    public void setColor(DyeColor color) {
        byte b = (Byte)this.dataTracker.get(COLOR);
        this.dataTracker.set(COLOR, (byte)(b & 240 | color.getId() & 15));
    }

    private static CraftingInventory createDyeMixingCraftingInventory(DyeColor firstColor, DyeColor secondColor) {
        CraftingInventory craftingInventory = new CraftingInventory(new ScreenHandler((ScreenHandlerType)null, -1) {
            public boolean canUse(PlayerEntity player) {
                return false;
            }
        }, 2, 1);
        craftingInventory.setStack(0, new ItemStack(DyeItem.byColor(firstColor)));
        craftingInventory.setStack(1, new ItemStack(DyeItem.byColor(secondColor)));
        return craftingInventory;
    }

    static {
        COLOR = DataTracker.registerData(EntityPlayerSled.class, TrackedDataHandlerRegistry.BYTE);
        COLORS = Maps.newEnumMap((Map) Arrays.stream(DyeColor.values()).collect(Collectors.toMap((dyeColor) -> {
            return dyeColor;
            }, EntityPlayerSled::getDyedColor)));
    }
}
