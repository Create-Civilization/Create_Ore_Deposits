package com.createcivilization.create_ore_deposits.content.kinetics.drill;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import com.createcivilization.create_ore_deposits.content.materials.SimpleBaseDeposit;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.CreateLang;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiPredicate;

public abstract class BaseDrillBlockEntity extends KineticBlockEntity {

    protected LerpedFloat drillOffset;
    protected boolean isExtending;
    private boolean isMoving = false;
    protected boolean target = false;
    protected BlockPos targetPos = BlockPos.ZERO;
    protected int breakingProgress;
    protected int tickMilestone;
    private final int BREAKER_ID = getBlockPos().hashCode();
    protected float breakingSpeed = getSpeed() / 100f;
    protected int currentTick;
    protected BlockPos drillPos;
    private final ItemStackHandler itemHandler = new ItemStackHandler(1);
    Random random = new Random();


    protected int resourcePullSpeed;

    public BaseDrillBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        drillOffset = LerpedFloat.linear().startWithValue(0);
        isExtending = false;
    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        if(drillPos == null){
            CreateLang.text("NULL MOMENT").style(ChatFormatting.AQUA).forGoggles(tooltip);
            return true;
        }


        int drillX = drillPos.getX();
        int drillY = drillPos.getY();
        int drillZ = drillPos.getZ();
        CreateLang.text("Drill is at " + drillX + "," + drillY + "," + drillZ)
                .style(ChatFormatting.AQUA)
                .forGoggles(tooltip);


        return true;
    }

    @Override
    public void tick() {
        super.tick();
        updateDrillExtension();
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        isMoving = true;

        breakingSpeed = getSpeed() / 100f;
        if (getSpeed() == 0) {
            drillOffset.forceNextSync();
            drillOffset.setValue(Math.round(drillOffset.getValue()));
            isMoving = false;
        }

        if (isMoving) {
            float newOffset = drillOffset.getValue() + getMovementSpeed();
            if (newOffset < 0)
                isMoving = false;
            if (!level.getBlockState(worldPosition.below((int) Math.ceil(newOffset)))
                    .canBeReplaced()) {
                isMoving = false;
            }
            if (isMoving) {
                //They had drainer and filler resets here.
            }
        }

        super.onSpeedChanged(previousSpeed);
    }

    public void updateDrillExtension() {
        float newOffset = drillOffset.getValue() + getMovementSpeed();
        if (newOffset < 0) {
            newOffset = 0;
            isMoving = false;
        }

        int ceil = (int) Math.ceil(newOffset);
        drillPos = worldPosition.below(ceil);
        BlockPos target = drillPos;
        BlockState targetState = level.getBlockState(target);
        if (level.isEmptyBlock(target) && breakingProgress != 0) finishExtraction(target, targetState);

        if (!targetState.canBeReplaced() && !target.equals(getBlockPos())) {
            newOffset = ceil - 1;
            isMoving = false;

            float hardness = targetState.getDestroySpeed(level, target);
            ItemStack slot = itemHandler.getStackInSlot(0);
            boolean unbreakable = hardness == -1
                    || target.equals(this.getBlockPos())
                    || AllTags.AllBlockTags.NON_BREAKABLE.matches(targetState)
                    || slot.getCount() == slot.getMaxStackSize();
            CreateOreDeposits.LOGGER.info(String.valueOf(slot.getCount()));

            if (isDeposit(targetState)) target = findFurthestDeposit(level, drillPos);

            if (!unbreakable) {
                if (currentTick >= tickMilestone) {
                    breakingProgress++;
                    currentTick = 0;
                    if (isDeposit(targetState)) extractDeposit(target, targetState);
                }
                else currentTick++;

                float breakSpeed = getSpeed() / 100f;

                tickMilestone = (int) (hardness / breakSpeed);

                level.playSound(null, worldPosition, targetState.getSoundType().getHitSound(),
                        SoundSource.BLOCKS, 0.25f, 1f);
                level.destroyBlockProgress(BREAKER_ID, target, breakingProgress);

                if (breakingProgress >= 10)
                    finishExtraction(target, targetState);
            }
        }

        drillOffset.setValue(newOffset);
    }

    private void finishExtraction(BlockPos target, BlockState state) {
        assert level != null;
        BlockHelper.destroyBlock(level, target, 1f, (drop) -> {
            if (isDeposit(state)) extractDeposit(target, state); else dropItem(target, drop);
        });
        breakingProgress = 0;
        tickMilestone = 0;
        currentTick = 0;
        level.destroyBlockProgress(BREAKER_ID, target, -1);
    }

    private void extractDeposit(BlockPos pos, BlockState state) {
        assert level != null;
        if (level.isClientSide) return;
        SimpleBaseDeposit baseDeposit = (SimpleBaseDeposit) state.getBlock();
        DepositBlock depositBlock = baseDeposit.getDepositBlock();
        int min = depositBlock.min();
        List<ItemStack> drops = baseDeposit.getDepositDrops((ServerLevel) level, pos, this);
        if (drops.isEmpty()) return;
        ItemStack drop = drops.getFirst();
        drop.setCount(random.nextInt(depositBlock.max() - min + 1) + min);
        itemHandler.insertItem(
                0,
                drop,
                false
        );
    }

    private void dropItem(BlockPos pos, ItemStack drop) {
        assert level != null;
        ItemEntity item = new ItemEntity(
                level,
                pos.getX(), pos.getY(), pos.getZ(),
                drop.copy()
        );
        level.addFreshEntity(item);
    }

    public float getMovementSpeed() {
        float movementSpeed = convertToLinear(getSpeed());
        if (level.isClientSide)
            movementSpeed *= ServerSpeedProvider.get();
        return movementSpeed;
    }

    public static List<BlockPos> getPositions(BlockPos pos) {
        List<BlockPos> positions = new ArrayList<>();

        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    positions.add(pos.offset(x, y, z));
                }
            }
        }

        return positions;
    }

    public boolean isDeposit(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return isDeposit(state);
    }

    public boolean isDeposit(BlockState state) {
        return state.getBlock() instanceof SimpleBaseDeposit;
    }

    public Set<BlockPos> getAllConnectedBlocks(Level level, BlockPos initialPos, BiPredicate<Level, BlockPos> predicate) {
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();

        queue.add(initialPos);
        visited.add(initialPos);

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                BlockPos current = queue.poll();

                for (BlockPos neighbor : getPositions(current)) {
                    if (!visited.contains(neighbor) && predicate.test(level, neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                    }
                }
            }
        }
        return visited;
    }

    public BlockPos findFurthestDeposit(Level level, BlockPos initialPos) {
        List<BlockPos> deposit = new ArrayList<>(getAllConnectedBlocks(level, initialPos, this::isDeposit));

        deposit.sort((pos1, pos2) -> {
            double distance1 = pos1.distSqr(drillPos);
            double distance2 = pos2.distSqr(drillPos);

            if (distance2 == distance1) return pos2.hashCode() > pos1.hashCode() ? -1 : 1;

            return distance2 > distance1 ? -1 : 1;
        });

        return deposit.getLast();
    }

    public float getInterpolatedOffset(float partialTicks) {
        return Math.max(drillOffset.getValue(partialTicks), 3 / 16f);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().expandTowards(0, -drillOffset.getValue(), 0);
    }

    public void setTargetPos(int[] targetPosArray) {
        setTargetPos(new BlockPos(targetPosArray[0], targetPosArray[1], targetPosArray[2]));
    }

    public void setTargetPos(BlockPos pos) {
        targetPos = pos;
    }

    public BlockPos getTargetPos() {
        return targetPos;
    }

    public void setHasTarget(boolean hasTarget) {
        target = hasTarget;
    }

    public boolean hasTarget() {
        return target;
    }

    public void setResourcePullSpeed(int speed) {
        resourcePullSpeed = speed;
    }

    // NBT handling
    public void read(CompoundTag compound, HolderLookup.Provider provider, boolean clientPacket) {
        super.read(compound, provider, clientPacket);
        if (clientPacket)
            drillOffset.readNBT(compound.getCompound("DrillOffset"), clientPacket);
        isExtending = compound.getBoolean("IsExtending");
        setHasTarget(compound.getBoolean("HasTarget"));
        setTargetPos(compound.getIntArray("TargetPos"));
        itemHandler.deserializeNBT(provider, compound.getCompound("inventory"));
    }

    public void write(CompoundTag compound, HolderLookup.Provider provider, boolean clientPacket) {
        super.write(compound, provider, clientPacket);
        if (clientPacket)
            compound.put("DrillOffset", drillOffset.writeNBT());
        compound.putBoolean("IsExtending", isExtending);
        compound.putBoolean("HasTarget", hasTarget());
        compound.putIntArray("TargetPos", new int[]{
                targetPos.getX(),
                targetPos.getY(),
                targetPos.getZ()
        });
        compound.put("inventory", itemHandler.serializeNBT(provider));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.handleUpdateTag(tag, registries);
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }
}