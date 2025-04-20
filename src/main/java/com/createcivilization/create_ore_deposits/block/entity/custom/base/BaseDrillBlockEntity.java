package com.createcivilization.create_ore_deposits.block.entity.custom.base;

import com.createcivilization.create_ore_deposits.CODConfig;
import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import com.createcivilization.create_ore_deposits.block.custom.gen.DepositBlock;
import com.createcivilization.create_ore_deposits.block.custom.gen.SimpleBaseDeposit;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.jarjar.nio.util.Lazy;
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
    protected int breakerId = getBlockPos().hashCode();
    protected float breakingSpeed = getSpeed() / 100f;
    protected int currentTick;
    protected BlockPos drillBit;
    private final Lazy<ItemStackHandler> itemHandler = Lazy.of(this::createItemHandler);
    Random random = new Random();


    protected int resourcePullSpeed;

    public BaseDrillBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        drillOffset = LerpedFloat.linear().startWithValue(0);
        isExtending = false;
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
        drillBit = worldPosition.below(ceil);
        BlockPos target = drillBit;
        BlockState targetState = level.getBlockState(target);
        if (level.isEmptyBlock(target) && breakingProgress != 0) finishExtraction(target, targetState);

        if (!targetState.canBeReplaced() && !target.equals(getBlockPos())) {
            newOffset = ceil - 1;
            isMoving = false;

            float hardness = targetState.getDestroySpeed(level, target);
            boolean unbreakable = hardness == -1
                    || target.equals(this.getBlockPos())
                    || AllTags.AllBlockTags.NON_BREAKABLE.matches(targetState);

            if (isDeposit(targetState)) target = findFurthestDeposit(level, drillBit);

            if (!unbreakable) {
                if (currentTick >= tickMilestone) {
                    breakingProgress++;
                    currentTick = 0;
                    if (isDeposit(targetState)) {
                        DepositBlock depositBlock = CODConfig.REGISTERED_DEPOSITS.get(BuiltInRegistries.BLOCK.getKey(targetState.getBlock()));
                        int min = depositBlock.min();
                        CreateOreDeposits.LOGGER.info("Would've extracted {}", random.nextInt(depositBlock.max() - min + 1) + min);
                        // store
                    }
                }
                else currentTick++;

                float breakSpeed = getSpeed() / 100f;

                tickMilestone = (int) (hardness / breakSpeed);

                level.playSound(null, worldPosition, targetState.getSoundType().getHitSound(),
                        SoundSource.BLOCKS, 0.25f, 1f);
                level.destroyBlockProgress(breakerId, target, breakingProgress);

                if (breakingProgress >= 10)
                    finishExtraction(target, targetState);
            }
        }

        drillOffset.setValue(newOffset);
    }

    private void finishExtraction(BlockPos target, BlockState state) {
        assert level != null;
        BlockHelper.destroyBlock(level, target, 1f);
        if (!isDeposit(state)) {
            // drop
        }
        breakingProgress = 0;
        tickMilestone = 0;
        currentTick = 0;
        level.destroyBlockProgress(breakerId, target, -1);
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
            BlockPos lastInLevel = null;
            for (int i = 0; i < size; i++) {
                BlockPos current = queue.poll();
                lastInLevel = current;

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
            double distance1 = pos1.distSqr(drillBit);
            double distance2 = pos2.distSqr(drillBit);

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
        itemHandler.get().deserializeNBT(provider, compound.getCompound("inventory"));
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
        compound.put("inventory", itemHandler.get().serializeNBT(provider));
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

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if (level != null) {
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                }
            }
        };
    }

    public IItemHandler getItemHandler() {
        return itemHandler.get();
    }
}