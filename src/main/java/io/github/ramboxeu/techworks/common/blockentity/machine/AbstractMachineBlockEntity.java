package io.github.ramboxeu.techworks.common.blockentity.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.component.ComponentInventory;
import io.github.ramboxeu.techworks.common.api.sync.EventEmitter;
import io.github.ramboxeu.techworks.common.api.widget.Widget;
import io.github.ramboxeu.techworks.common.registry.TechworksContainers;
import io.github.ramboxeu.techworks.common.registry.TechworksItems;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
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
        if (player.getMainHandStack().getItem().equals(TechworksItems.WRENCH)) {
            ContainerProviderRegistry.INSTANCE.openContainer(TechworksContainers.MACHINE_COMPONENTS, player, buf -> {
                buf.writeBlockPos(pos);
                buf.writeInt(0);
            });
        } else {
            ContainerProviderRegistry.INSTANCE.openContainer(TechworksContainers.BOILER, player, buf -> {
                buf.writeBlockPos(pos);
                int dataSize = (int) componentList.stream().filter(c -> c instanceof EventEmitter).count();
//                Techworks.LOG.info("Machine dataSize: {}", dataSize);
                buf.writeInt(dataSize);
            });
        }
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
