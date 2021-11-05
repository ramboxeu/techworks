package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.client.container.machine.SteamEngineContainer;
import io.github.ramboxeu.techworks.common.component.ComponentStorage;
import io.github.ramboxeu.techworks.common.energy.EnergyBattery;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.registration.TechworksFluids;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import io.github.ramboxeu.techworks.common.util.machineio.data.EnergyHandlerData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class SteamEngineTile extends BaseMachineTile {
    private final EnergyBattery battery;
    private final EnergyHandlerData batteryData;

    private BoilerTile boiler;
    private BlockPos boilerPos;
    private SteamEngineTile next;
    private BlockPos nextPos;
    private SteamEngineTile previous;
    private BlockPos previousPos;
    private FluidStack steam;
    private boolean isLinked;
    private boolean isWorking;
    private int maxPower;
    private int maxEnergyOutput;
    private float efficiency;

    public SteamEngineTile() {
        super(TechworksTiles.STEAM_ENGINE.get());

        battery = new EnergyBattery(0, 100, 100);
        batteryData = machineIO.getHandlerData(battery);

        components = new ComponentStorage.Builder()
                .component(TechworksComponents.STEAM_TURBINE.get(), (component, stack) -> {
                    maxEnergyOutput = component.getMaxEnergy();
                    efficiency = component.getEfficiency();
                })
                .component(TechworksComponents.ENERGY_STORAGE.get(), battery)
                .build();
        steam = FluidStack.EMPTY;
    }

    @Override
    public void remove() {
        super.remove();

        if (isLinked) {
            unlinkChain();
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();

        if (isLinked) {
            boiler.unlinkEngine(this);
            unlink();
        }
    }

    @Override
    protected void onFirstTick() {
        if (boilerPos != null) {
            BoilerTile boilerTile = (BoilerTile) world.getTileEntity(boilerPos);

            if (boilerTile.linkEngine(this)) {
                boiler = boilerTile;
                isLinked = true;
            } else {
                boiler = null;
            }

            boilerPos = null;
        }

        if (isLinked && previousPos != null) {
            previous = (SteamEngineTile) world.getTileEntity(previousPos);
            previousPos = null;
        }

        if (isLinked && nextPos != null) {
            next = (SteamEngineTile) world.getTileEntity(nextPos);
            nextPos = null;
        }
    }

    @Override
    protected void workTick() {
        if (!steam.isEmpty()) {
            int energy = Math.min((int) (steam.getAmount() * efficiency), Math.min(maxPower, maxEnergyOutput));
            battery.receiveEnergy(energy, false);
        }
    }

    @Override
    protected void serverTick() {
        super.serverTick();
        steam = FluidStack.EMPTY;
        maxPower = 0;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("Battery", battery.serializeNBT());
        tag.putBoolean("IsWorking", isWorking);

        putTilePos(tag, "Next", next);
        putTilePos(tag, "Previous", previous);
        putTilePos(tag, "Boiler", boiler);

        return super.write(tag);
    }

    @Override
    protected CompoundNBT writeUpdateTag(CompoundNBT tag) {
        putTilePos(tag, "Boiler", boiler);

        return super.writeUpdateTag(tag);
    }

    private void putTilePos(CompoundNBT tag, String key, TileEntity tile) {
        if (tile != null)
            NBTUtils.putBlockPos(tag, key, tile.getPos());
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        battery.deserializeNBT(tag.getCompound("Battery"));
        isWorking = tag.getBoolean("IsWorking");

        nextPos = getTilePos(tag, "Next");
        previousPos = getTilePos(tag, "Previous");
        boilerPos = getTilePos(tag, "Boiler");

        super.read(state, tag);
    }

    @Override
    protected void readUpdateTag(CompoundNBT tag, BlockState state) {
        boilerPos = getTilePos(tag, "Boiler");

        super.readUpdateTag(tag, state);
    }

    private BlockPos getTilePos(CompoundNBT tag, String key) {
        if (tag.contains(key, Constants.NBT.TAG_COMPOUND)) {
            return NBTUtils.getBlockPos(tag, key);
        }

        return null;
    }

    @Override
    public ITextComponent getDisplayName() {
        return TranslationKeys.STEAM_ENGINE.text();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity entity) {
        return new SteamEngineContainer(id, inventory, this);
    }

    public void receiveSteam(int amount, int maxPower) {
        this.maxPower = maxPower;
        steam = new FluidStack(TechworksFluids.STEAM.get(), amount);
    }

    @Override
    public boolean configure(PlayerEntity player, ItemStack wrench, World world, BlockPos pos, Direction face, Vector3d hitVec) {
        CompoundNBT wrenchTag = wrench.getOrCreateChildTag("Wrench");

        if (wrenchTag.contains("BoilerChain", Constants.NBT.TAG_COMPOUND)) {
            BlockPos startPos = NBTUtils.getBlockPos(wrenchTag, "BoilerChain");

            if (!startPos.equals(pos)) {
                TileEntity tile = world.getTileEntity(startPos);

                if (tile != null && tile == boiler) {
                    wrenchTag.remove("BoilerChain");

                    BoilerTile boilerTile = (BoilerTile) tile;
                    BlockPos boilerPos = boilerTile.getPos();

                    if (isLinked && next != null) {
                        player.sendStatusMessage(TranslationKeys.ENGINE_UNLINKING_FAILURE.text(pos.getX(), pos.getY(), pos.getZ(), boilerPos.getX(), boilerPos.getY(), boilerPos.getZ()), true);
                    } else {
                        unlink();
                        player.sendStatusMessage(TranslationKeys.ENGINE_UNLINKED.text(pos.getX(), pos.getY(), pos.getZ(), boilerPos.getX(), boilerPos.getY(), boilerPos.getZ()), true);
                    }
                } else {
                    if (checkPos(startPos)) {
                        if (tile instanceof SteamEngineTile) {
                            wrenchTag.remove("BoilerChain");

                            SteamEngineTile engineTile = (SteamEngineTile) tile;
                            BoilerTile boilerTile = engineTile.boiler;
                            BlockPos boilerPos = boilerTile.getPos();

                            if (isLinked && (next != null || boiler == boilerTile)) {
                                player.sendStatusMessage(TranslationKeys.ENGINE_ALREADY_LINKED.text(pos.getX(), pos.getY(), pos.getZ(), boilerPos.getX(), boilerPos.getY(), boilerPos.getZ()), true);
                                return true;
                            }

                            if (boilerTile.linkEngine(this)) {
                                if (isLinked)
                                    unlink();

                                isLinked = true;
                                engineTile.next = this;
                                previous = engineTile;
                                boiler = boilerTile;
                                player.sendStatusMessage(TranslationKeys.ENGINE_LINKING_SUCCESS.text(pos.getX(), pos.getY(), pos.getZ(), boilerPos.getX(), boilerPos.getY(), boilerPos.getZ()), true);
                            } else {
                                player.sendStatusMessage(TranslationKeys.ENGINE_LINKING_FAILURE.text(pos.getX(), pos.getY(), pos.getZ(), boilerPos.getX(), boilerPos.getY(), boilerPos.getZ()), true);
                            }
                        } else if (tile instanceof BoilerTile) {
                            wrenchTag.remove("BoilerChain");

                            BoilerTile boilerTile = (BoilerTile) tile;
                            BlockPos boilerPos = boilerTile.getPos();

                            if (isLinked) {
                                if (next != null) {
                                    player.sendStatusMessage(TranslationKeys.ENGINE_ALREADY_LINKED.text(pos.getX(), pos.getY(), pos.getZ(), boilerPos.getX(), boilerPos.getY(), boilerPos.getZ()), true);
                                } else {
                                    unlink();
                                    player.sendStatusMessage(TranslationKeys.ENGINE_UNLINKED.text(pos.getX(), pos.getY(), pos.getZ(), boilerPos.getX(), boilerPos.getY(), boilerPos.getZ()), true);
                                }

                                return true;
                            }

                            if (boilerTile.linkEngine(this)) {
                                if (isLinked)
                                    unlink();

                                isLinked = true;
                                boiler = boilerTile;
                                player.sendStatusMessage(TranslationKeys.ENGINE_LINKING_SUCCESS.text(pos.getX(), pos.getY(), pos.getZ(), boilerPos.getX(), boilerPos.getY(), boilerPos.getZ()), true);
                            } else {
                                player.sendStatusMessage(TranslationKeys.ENGINE_LINKING_FAILURE.text(pos.getX(), pos.getY(), pos.getZ(), boilerPos.getX(), boilerPos.getY(), boilerPos.getZ()), true);
                            }
                        }
                    } else {
                        player.sendStatusMessage(TranslationKeys.ENGINE_LINKING_FAILURE.text(pos.getX(), pos.getY(), pos.getZ(), startPos.getX(), startPos.getY(), startPos.getZ()), true);
                        wrenchTag.remove("BoilerChain");
                    }
                }
            } else {
                wrenchTag.remove("BoilerChain");
                player.sendStatusMessage(TranslationKeys.ENGINE_LINKING_CANCELLED.text(startPos.getX(), startPos.getY(), startPos.getZ()), true);
            }
        } else {
            if (boiler != null) {
                NBTUtils.putBlockPos(wrenchTag, "BoilerChain", pos);
                BlockPos boilerPos = boiler.getPos();
                player.sendStatusMessage(TranslationKeys.LINKING_ENGINES.text(boilerPos.getX(), boilerPos.getY(), boilerPos.getZ()), true);
            }
        }

        return true;
    }

    private boolean checkPos(BlockPos pos) {
        BlockPos diff = this.pos.subtract(pos);

        if (Math.abs(diff.getX()) == 1 && diff.getY() == 0 && diff.getZ() == 0) return true;
        if (diff.getX() == 0 && Math.abs(diff.getY()) == 1 && diff.getZ() == 0) return true;
        return diff.getX() == 0 && diff.getY() == 0 && Math.abs(diff.getZ()) == 1;
    }

    private void unlink() {
        boiler.unlinkEngine(this);
        boiler = null;
        isLinked = false;

        if (next != null)
            next.previous = null;

        next = null;

        if (previous != null)
            previous.next = null;

        previous = null;
    }

    private void unlinkChain() {
        if (next != null) {
            next.unlinkChain();
            unlink();
        }
    }

    public void unlinkBoiler() {
        boiler = null;
        next = null;
        previous = null;
        isLinked = false;
    }

    public BoilerTile getBoiler() {
        return boiler;
    }

    public EnergyHandlerData getBatteryData() {
        return batteryData;
    }
}
