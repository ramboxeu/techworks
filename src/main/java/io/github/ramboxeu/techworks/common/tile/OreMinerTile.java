package io.github.ramboxeu.techworks.common.tile;

import com.mojang.authlib.GameProfile;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.machine.OreMinerContainer;
import io.github.ramboxeu.techworks.common.component.ComponentStorage;
import io.github.ramboxeu.techworks.common.energy.EnergyBattery;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.network.SyncOreMinerFilterPacket;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.util.ItemUtils;
import io.github.ramboxeu.techworks.common.util.machineio.data.EnergyHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.ItemHandlerData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class OreMinerTile extends BaseMachineTile {
    private final EnergyBattery battery;
    private final EnergyHandlerData batteryData;

    private final ItemStackHandler inv;
    private final ItemHandlerData invData;

    private final AtomicBoolean cancelFlag = new AtomicBoolean();
    private CompletableFuture<Deque<BlockPos>> searchTask;
    private List<ItemStack> cachedDrops;
    private Status status = Status.IDLE;
    private Deque<BlockPos> blocks;
    private FakePlayer digger;
    private IOreFilter filter;
    private boolean check;
    private int waitTime = -1;
    private int miningTime;
    private int miningTimer;
    private int energyUsage;
    private int blocksMined;

    public OreMinerTile() {
        super(TechworksTiles.ORE_MINER_TILE.get());

        battery = new EnergyBattery() {
            @Override
            protected void onContentsChanged() {
                check = true;
            }
        };
        batteryData = machineIO.getHandlerData(battery);

        inv = new ItemStackHandler(12) {
            @Override
            protected void onContentsChanged(int slot) {
                check = true;
            }
        };
        invData = machineIO.getHandlerData(inv);

        components = new ComponentStorage.Builder()
                .component(TechworksComponents.ENERGY_STORAGE.get(), battery)
                .component(TechworksComponents.MINING.get(), (component, stack) -> {
                    energyUsage = component.getEnergyUsage();
                    miningTime = component.getMiningTime();
                })
                .build();
    }

    @Override
    protected void serverTick() {
        if (check) {
            if (battery.getEnergyStored() > 0 && !ItemUtils.isHandlerFull(inv) && isFilterSet()) {
                if (blocks != null) {
                    if (cachedDrops == null)
                        cachedDrops = getBlockDrops();

                    if (!ItemUtils.insertItems(inv, cachedDrops, true).isEmpty()) {
                        setStatus(Status.IDLE, true);
                    } else {
                        setStatus(Status.MINING, true);
                    }
                } else {
                    if (status != Status.SCANNING && waitTime == -1) {
                        scan();
                    }
                }
            } else {
                setStatus(Status.IDLE, true);
                miningTimer = 0;
            }

            setWorkingState(status == Status.MINING);
            TechworksPacketHandler.syncMinerWorkingState(world.getChunkAt(pos), pos, status == Status.MINING);
            check = false;
        }

        if (status == Status.SCANNING) {
            if (searchTask.isDone()) {
                if (searchTask.isCompletedExceptionally() || searchTask.isCancelled()) {
                    blocks = null;
                    setStatus(Status.IDLE, true);
                } else {
                    try {
                        Deque<BlockPos> result = searchTask.join();

                        if (result.isEmpty()) {
                            blocks = null;
                            setStatus(Status.IDLE, true);
                            waitTime = 0;
                        } else {
                            blocks = result;
                            cachedDrops = null;
                            blocksMined = 0;
                            setStatus(Status.MINING, true);
                            waitTime = -1;
                        }
                    } catch (CompletionException | CancellationException ignored) {}
                }

                searchTask = null;
            }
        }

        if (status == Status.MINING) {
            if (standbyMode.canWork() && redstoneMode.canWork(world.isBlockPowered(pos))) {
                if (blocks == null || blocks.isEmpty()) {
                    setStatus(Status.IDLE, true);
                    blocks = null;
                    check = true;
                } else {
                    if (miningTimer < miningTime) {
                        miningTimer++;
                        battery.extractEnergy(energyUsage, false);
                        BlockPos pos = blocks.peekLast();
                        world.sendBlockBreakProgress(digger.getEntityId(), pos, miningTimer / 10);
                    }

                    if (miningTimer == miningTime) {
                        BlockPos pos = blocks.peekLast();

                        if (cachedDrops == null)
                            cachedDrops = getBlockDrops();

                        if (ItemUtils.insertItems(inv, cachedDrops, true).isEmpty()) {
                            if (world.removeBlock(pos, false)) {
                                Techworks.LOGGER.debug("Mined: {}", pos);
                                blocks.pollLast();
                                ItemUtils.insertItems(inv, cachedDrops, false);
                                blocksMined++;
                                miningTimer = 0;
                                check = true;
                                cachedDrops = null;
                            }
                        }
                    }
                }
            }
        }

        if (waitTime >= 0)
            waitTime++;

        if (waitTime == 600) {
            waitTime = -1;
            check = true;
        }
    }

    private List<ItemStack> getBlockDrops() {
        BlockPos pos = blocks.peekLast();

        if (pos != null) {
            BlockState block = world.getBlockState(pos);
            LootContext.Builder builder = new LootContext.Builder((ServerWorld) world).withRandom(world.rand).withParameter(LootParameters.ORIGIN, Vector3d.copyCentered(pos)).withParameter(LootParameters.TOOL, ItemStack.EMPTY);
            return block.getDrops(builder);
        }

        return Collections.emptyList();
    }

    private void setStatus(Status status, boolean sync) {
        setWorkingState(status.isWorking());
        this.status = status;

        if (sync) {
            TechworksPacketHandler.syncMinerStatus(world.getChunkAt(pos), pos, status);
        }
    }

    public void setStatus(Status status) {
        setStatus(status, false);
    }

    private void scan() {
        if (searchTask != null) {
            clearCachedSearch();
        }

        setStatus(Status.SCANNING, true);

        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;

        CompletableFuture<Deque<BlockPos>> task = null;

        for (int z = -1; z <= 1; z++) {
            for (int x = -1; x <= 1; x++) {
                ChunkPos pos = new ChunkPos(chunkX + x, chunkZ + z);
                Chunk chunk = world.getChunkProvider().getChunk(pos.x, pos.z, false);

                if (chunk == null) continue;

                CompletableFuture<Deque<BlockPos>> chunkTask = CompletableFuture.supplyAsync(searchChunkAsync(chunk));

                if (task != null) {
                    task = task.thenCombine(chunkTask, (a, b) -> { a.addAll(b); return a; });
                } else {
                    task = chunkTask;
                }
            }
        }

        searchTask = task;
    }

    private Supplier<Deque<BlockPos>> searchChunkAsync(Chunk chunk) {
        return () -> {
            int maxY = pos.getY();
            Deque<BlockPos> result = new ArrayDeque<>();

            BlockPos.Mutable pos = chunk.getPos().asBlockPos().toMutable();
            for (int y = 0; y < maxY; y++) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        BlockState state = chunk.getBlockState(pos);

                        if (filter.test(state.getBlock())) {
                            result.add(pos.toImmutable());
                        }

                        pos.move(0, 0, 1);
                    }

                    if (cancelFlag.get()) {
                        throw new CancellationException();
                    }

                    pos.move(1, 0, -16);
                }

                pos.move(-16, 1, 0);
            }

            return result;
        };
    }

    private void clearCachedSearch() {
        if (searchTask != null) {
            cancelFlag.set(true);

            try {
                searchTask.join();
            } catch (CompletionException | CancellationException ignored) {}

            searchTask = null;
            cancelFlag.set(false);
        }

        blocks = null;
    }

    @Override
    public void setWorldAndPos(World world, BlockPos pos) {
        super.setWorldAndPos(world, pos);

        if (world instanceof ServerWorld) {
            digger = new FakePlayer((ServerWorld) world, new GameProfile(UUID.randomUUID(), "OreMinerDigger"));
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        clearCachedSearch();
    }

    @Override
    public void remove() {
        super.remove();
        clearCachedSearch();
    }

    @Override
    public ITextComponent getDisplayName() {
        return TranslationKeys.ORE_MINER.text();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
        return new OreMinerContainer(id, playerInv, this);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        deserializeFilter(tag.getCompound("Filter"));
        battery.deserializeNBT(tag.getCompound("Battery"));
        inv.deserializeNBT(tag.getCompound("Inv"));

        super.read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("Filter", serializeFilter());
        tag.put("Battery", battery.serializeNBT());
        tag.put("Inv", inv.serializeNBT());

        return super.write(tag);
    }

    @Override
    protected CompoundNBT writeUpdateTag(CompoundNBT tag) {
        tag.put("Filter", serializeFilter());

        return super.writeUpdateTag(tag);
    }

    @Override
    protected void readUpdateTag(CompoundNBT tag, BlockState state) {
        deserializeFilter(tag.getCompound("Filter"));

        super.readUpdateTag(tag, state);
    }

    private void deserializeFilter(CompoundNBT tag) {
        byte type = tag.getByte("Type");

        ResourceLocation id = new ResourceLocation(tag.getString("Id"));

        if (type == 0) {
            Block block = ForgeRegistries.BLOCKS.getValue(id);
            updateFilter(block);
        } else if (type == 1) {
            ITag<Block> filterTag = TagCollectionManager.getManager().getBlockTags().get(id);
            updateFilter(filterTag);
        } else {
            clearFilter();
        }
    }

    private CompoundNBT serializeFilter() {
        CompoundNBT tag = new CompoundNBT();

        if (isFilterSet()) {
            tag.putByte("Type", (byte) filter.type().ordinal());

            if (filter.type() == FilterType.TAG) {
                ResourceLocation id = TagCollectionManager.getManager().getBlockTags().getDirectIdFromTag(((TagOreFilter) filter).getTag());
                tag.putString("Id", id.toString());
            } else {
                ResourceLocation id = ((BlockOreFilter) filter).getBlock().getRegistryName();
                tag.putString("Id", id.toString());
            }
        } else {
            tag.putByte("Type", (byte) -1);
        }

        return tag;
    }

    public void updateFilter(Block block) {
        updateFilter(block, false);
    }

    public void updateFilter(Block block, boolean sync) {
        if (block.hasTileEntity(block.getDefaultState()))
            return;

        if (filter instanceof BlockOreFilter) {
            if (((BlockOreFilter) filter).getBlock() == block)
                return;
        }

        filter = new BlockOreFilter(block);
        check = true;
        waitTime = -1;
        clearCachedSearch();

        if (sync)
            TechworksPacketHandler.syncOreFilter(world.getChunkAt(pos), pos, SyncOreMinerFilterPacket.Type.BLOCK, block.getRegistryName());
    }

    public void updateFilter(ITag<Block> tag) {
        updateFilter(tag, false);
    }

    public void updateFilter(ITag<Block> tag, boolean sync) {
        ResourceLocation id = TagCollectionManager.getManager().getBlockTags().getValidatedIdFromTag(tag);
        ResourceLocation name;

        if (tag instanceof ITag.INamedTag) name = ((ITag.INamedTag<Block>) tag).getName();
        else name = id;

        if (filter instanceof TagOreFilter) {
            if (((TagOreFilter) filter).getTag() == tag)
                return;
        }

        filter = new TagOreFilter(tag, name);
        check = true;
        waitTime = -1;
        clearCachedSearch();

        if (sync)
            TechworksPacketHandler.syncOreFilter(world.getChunkAt(pos), pos, SyncOreMinerFilterPacket.Type.TAG, id);
    }

    public void clearFilter() {
        clearFilter(false);
    }

    public void clearFilter(boolean sync) {
        clearCachedSearch();
        filter = null;
        check = true;
        waitTime = -1;

        if (sync)
            TechworksPacketHandler.syncOreFilter(world.getChunkAt(pos), pos, SyncOreMinerFilterPacket.Type.CLEAR, null);
    }

    public void updateFilterItem(ItemStack stack) {
        Item item = stack.getItem().getItem();

        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();
            updateFilter(block, true);
        }
    }

    public boolean updateFilterTag(String text) {
        ResourceLocation id = ResourceLocation.tryCreate(text);

        if (id == null)
            return false;

        ITag<Block> tag = TagCollectionManager.getManager().getBlockTags().get(id);

        if (tag == null)
            return false;

        updateFilter(tag, true);
        return true;
    }

    public void syncFilter(FilterType type, ResourceLocation id) {
        if (type == FilterType.TAG) {
            ITagCollection<Block> tags = TagCollectionManager.getManager().getBlockTags();
            ITag<Block> tag = tags.get(id);
            updateFilter(tag);
        } else if (type == FilterType.BLOCK) {
            Block block = ForgeRegistries.BLOCKS.getValue(id);
            updateFilter(block);
        } else {
            clearFilter();
        }
    }

    public void rescan() {
        waitTime = -1;
        check = true;
    }

    public EnergyHandlerData getBatteryData() {
        return batteryData;
    }

    public ItemHandlerData getInvData() {
        return invData;
    }

    public Status getStatus() {
        return status;
    }

    public int getBlocksToMine() {
        return blocks != null ? blocks.size() : 0;
    }

    public int getBlocksMined() {
        return blocksMined;
    }

    public boolean isFilterSet() {
        return filter != null;
    }

    public IOreFilter getFilter() {
        return filter;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWorking(boolean working) {
        setWorkingState(working);
    }

    public interface IOreFilter extends Predicate<Block> {
        FilterType type();
        ITextComponent text();
    }

    public static class BlockOreFilter implements IOreFilter {
        private final Block block;

        public BlockOreFilter(Block block) {
            this.block = block;
        }

        @Override
        public FilterType type() {
            return FilterType.BLOCK;
        }

        @Override
        public ITextComponent text() {
            return TranslationKeys.BLOCK.text().appendString(" - ").appendSibling(block.getTranslatedName());
        }

        @Override
        public boolean test(Block block) {
            return this.block == block;
        }

        public Block getBlock() {
            return block;
        }
    }

    public static class TagOreFilter implements IOreFilter {
        private final ITag<Block> tag;
        private final ResourceLocation name;

        public TagOreFilter(ITag<Block> tag, ResourceLocation name) {
            this.tag = tag;
            this.name = name;
        }

        @Override
        public FilterType type() {
            return FilterType.TAG;
        }

        @Override
        public ITextComponent text() {
            return TranslationKeys.TAG.text().appendString(" - ").appendString(name.toString());
        }

        @Override
        public boolean test(Block block) {
            return tag.contains(block);
        }

        public ITag<Block> getTag() {
            return tag;
        }

        public ResourceLocation getName() {
            return name;
        }
    }

    public enum FilterType {
        BLOCK,
        TAG
    }

    public enum Status {
        IDLE,
        SCANNING,
        MINING;

        public boolean isWorking() {
            return this != IDLE;
        }
    }
}
