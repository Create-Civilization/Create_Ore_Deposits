package com.createcivilization.create_ore_deposits.block.entity.custom.base;

import com.createcivilization.create_ore_deposits.block.custom.gen.SimpleBaseDeposit;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.ItemStackHandler;

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


    protected int resourcePullSpeed;
    protected double breakingProgressMilestone = -1;

    // Inventory
    protected final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

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
        if (level.isEmptyBlock(target) && breakingProgress != 0) finishExtraction(target);

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
                }
                else currentTick++;

                float breakSpeed = getSpeed() / 100f;

                tickMilestone = (int) (hardness / breakSpeed);

                level.playSound(null, worldPosition, targetState.getSoundType().getHitSound(),
                        SoundSource.BLOCKS, 0.25f, 1f);
                level.destroyBlockProgress(breakerId, target, breakingProgress);

                if ((tickMilestone > 0 && breakingProgress >= 10))
                    finishExtraction(target);
            }
        }

        drillOffset.setValue(newOffset);
    }

    private void finishExtraction(BlockPos target) {
        assert level != null;
        BlockHelper.destroyBlock(level, target, 1f);
        breakingProgress = 0;
        tickMilestone = 0;
        currentTick = 0;
        level.destroyBlockProgress(breakerId, target, -1);
        isMoving = true;
    }

    protected void processDeposit(){
        //First Checks if it has a target
        //If it has a target already it should already have deposit ore value.
        //Do work on deposit and make sure block breaking anim is scalled
        //If out of materials break block set has target to false


        //If does not have target
        //find furthest deposit, set to target
        //Check if blockstate of target is drilled
        //If its drilled just break it and find a new target.
        //Generate ore count then set has target
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

    public int getBreakingProgress(double breakingProgressMilestone, int resourceLevel) {
        double progressRatio = (double) resourceLevel / breakingProgressMilestone;
        int progress = 9 - (int) Math.ceil(progressRatio);
        return Math.min(9, Math.max(1, progress));
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

    // Getters and setters
    public double getBreakingProgressMilestone() {
        return breakingProgressMilestone;
    }

    public void setBreakingProgressMilestone(double value) {
        breakingProgressMilestone = value;
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
        inventory.deserializeNBT(provider, compound.getCompound("inventory"));
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
        compound.put("inventory", inventory.serializeNBT(provider));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);
    }
}