package io.github.ramboxeu.techworks.common.blockentity.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.component.ComponentInventory;
import io.github.ramboxeu.techworks.common.api.sync.EventEmitter;
import io.github.ramboxeu.techworks.common.api.widget.Widget;
import io.github.ramboxeu.techworks.common.registry.TechworksContainers;
import io.github.ramboxeu.techworks.common.registry.TechworksItems;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.Tickable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMachineBlockEntity extends BlockEntity implements Tickable {
    protected ComponentInventory componentList;
    protected List<Widget> widgets;

    public AbstractMachineBlockEntity(BlockEntityType<?> type) {
        super(type);
        MachineryBuilder builder = new MachineryBuilder();
        buildMachinery(builder);
        Pair<ComponentInventory, List<Widget>> pair = builder.build(this);
        componentList = pair.getLeft();
        widgets = pair.getRight();


//        Techworks.LOG.info("ComponentInv: {}", componentList);
//        Techworks.LOG.info("Widgets: {}", widgets);
    }

    @Override
    public void tick() {
        componentList.tick();
    }

    public ComponentInventory getComponentList() {
        return componentList;
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

    protected void buildMachinery(MachineryBuilder machineryBuilder) {}

    public ActionResult onActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        // Is this stupid? Yes
        // I have to fix it later
//        player.openHandledScreen(new ExtendedScreenHandlerFactory() {
//            @Override
//            public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
//
//            }
//
//            @Override
//            public Text getDisplayName() {
//                return null;
//            }
//
//            @Override
//            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
//                return null;
//            }
//        }(syncId, playerInventory, packetByteBuf) -> {
//            BlockEntity blockEntity = playerInventory.world.getBlockEntity(packetByteBuf.readBlockPos());
//            int dataSize = packetByteBuf.readInt();
//
//            return TechworksContainers.BOILER.create(syncId, playerInventory, blockEntity, dataSize);
//        });
        return ActionResult.CONSUME;
    }

    public abstract Text getComponentsContainerName();

    protected static class MachineryBuilder {
        private List<Widget> widgets;
        private List<ComponentInventory.Slot> slots;

        private MachineryBuilder() {
            widgets = new ArrayList<>();
            slots = new ArrayList<>();
        }

        protected MachineryBuilder add(ComponentInventory.Slot slot, WidgetFactory factory) {
            slots.add(slot);
            widgets.add(factory.create(slots.size() - 1));
            return this;
        }

        protected MachineryBuilder add(ComponentInventory.Slot slot) {
            slots.add(slot);
            return this;
        }

        protected MachineryBuilder add(Widget widget) {
            widgets.add(widget);
            return this;
        }

        private <T extends BlockEntity> Pair<ComponentInventory, List<Widget>> build(T container) {
            ComponentInventory inventory = new ComponentInventory(container, slots.toArray(new ComponentInventory.Slot[0])) {
                @Override
                protected void onContentsChanged() {
                    container.markDirty();
                }
            };
            return new Pair<>(inventory, widgets);
        }
    }

    protected interface WidgetFactory {
        Widget create(int i);
    }
}
