package io.github.ramboxeu.techworks.common.blockentity.machine;

import com.sun.jna.platform.win32.WinGDI;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.component.ComponentInventory;
import io.github.ramboxeu.techworks.common.api.widget.Widget;
import io.github.ramboxeu.techworks.common.registry.TechworksContainers;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.NameableContainerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.Tickable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMachineBlockEntity<TEntity extends AbstractMachineBlockEntity<TEntity>> extends BlockEntity implements NameableContainerFactory, Tickable {
    protected ComponentInventory<TEntity> componentList;
    protected List<Widget> widgets;

    public AbstractMachineBlockEntity(BlockEntityType<?> type) {
        super(type);
        MachineryBuilder builder = new MachineryBuilder();
        buildMachinery(builder);
        //noinspection unchecked
        Pair<ComponentInventory<TEntity>, List<Widget>> pair = builder.build((TEntity) this);
        componentList = pair.getLeft();
        widgets = pair.getRight();


        Techworks.LOG.info("ComponentInv: {}", componentList);
        Techworks.LOG.info("Widgets: {}", widgets);
    }

    @Override
    public void tick() {
        componentList.tick();
    }

    public ComponentInventory<TEntity> getComponentList() {
        return componentList;
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

    protected abstract void buildMachinery(MachineryBuilder machineryBuilder);

    public ActionResult onActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ContainerProviderRegistry.INSTANCE.openContainer(TechworksContainers.BOILER, player, buf -> {
            buf.writeBlockPos(pos);
        });
        return ActionResult.PASS;
    }

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

        private <T> Pair<ComponentInventory<T>, List<Widget>> build(T container) {
            ComponentInventory<T> inventory = new ComponentInventory<>(container, slots.toArray(new ComponentInventory.Slot[0]));
            return new Pair<>(inventory, widgets);
        }
    }

    protected interface WidgetFactory {
        Widget create(int i);
    }
}
