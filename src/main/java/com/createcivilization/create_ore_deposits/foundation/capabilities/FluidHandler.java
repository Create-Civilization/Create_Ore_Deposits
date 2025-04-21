package com.createcivilization.create_ore_deposits.foundation.capabilities;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FluidHandler implements IFluidHandler {

    private final FluidTank tank;
    private final Set<Fluid> allowedFluids;

    public FluidHandler(int capacity, @Nullable Set<Fluid> allowedFluids) {
        this.tank = new FluidTank(capacity, fluidStack -> isAllowed(fluidStack.getFluid()));
        this.allowedFluids = allowedFluids;
    }

    public boolean isAllowed(Fluid fluid) {
        return allowedFluids == null || allowedFluids.contains(fluid);
    }


    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        return this.tank.getFluid();
    }

    @Override
    public int getTankCapacity(int i) {
        return this.tank.getCapacity();
    }

    @Override
    public boolean isFluidValid(int i, FluidStack fluidStack) {
        return isAllowed(fluidStack.getFluid());
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        return isAllowed(fluidStack.getFluid()) ? tank.fill(fluidStack, fluidAction) : 0;
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        return tank.drain(fluidStack, fluidAction);
    }

    @Override
    public FluidStack drain(int i, FluidAction fluidAction) {
        return tank.drain(i, fluidAction);
    }
}
