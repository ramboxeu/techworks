package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.api.wrench.IWrenchable;
import io.github.ramboxeu.techworks.common.component.BaseStorageComponent;
import io.github.ramboxeu.techworks.common.component.Component;
import io.github.ramboxeu.techworks.common.component.ComponentManager;
import io.github.ramboxeu.techworks.common.component.ComponentType;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public abstract class StorageTile<T extends BaseStorageComponent> extends BaseTechworksTile implements IWrenchable {
    private final ComponentType<T> componentType;
    protected T component;
    protected ItemStack componentStack;

    public StorageTile(TileEntityType<?> type, ComponentType<T> componentType) {
        super(type);
        this.componentType = componentType;
        component = ComponentManager.getInstance().getComponent(componentType.getBaseComponentId());
        componentStack = new ItemStack(component);
    }

    @Override
    protected CompoundNBT writeUpdateTag(CompoundNBT tag) {
        tag.put("ComponentStack", componentStack.serializeNBT());
        NBTUtils.serializeComponent(tag, "Component", component);
        return super.writeUpdateTag(tag);
    }

    @Override
    protected void readUpdateTag(CompoundNBT tag, BlockState state) {
        super.readUpdateTag(tag, state);
        componentStack = ItemStack.read(tag.getCompound("ComponentStack"));
        component = NBTUtils.deserializeComponent(tag, "Component");
        onComponentsChanged(component, componentStack);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        componentStack = ItemStack.read(tag.getCompound("ComponentStack"));
        component = NBTUtils.deserializeComponent(tag, "Component");
        onComponentsChanged(component, componentStack);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("ComponentStack", componentStack.serializeNBT());
        NBTUtils.serializeComponent(tag, "Component", component);
        return super.write(tag);
    }

    @SuppressWarnings("unchecked")
    public ItemStack onRightClick(ItemStack stack, PlayerEntity player) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();

            if (ComponentManager.getInstance().isItemComponent(item)) {
                Component component = ComponentManager.getInstance().getComponent(item);

                if (component == this.component) {
                    player.sendStatusMessage(TranslationKeys.COMPONENT_ALREADY_INSTALLED.text(), true);
                    return null;
                }

                if (component.getType() == componentType) {
                    T storageComponent = ((T) component);

                    if (storageComponent.getStorageCapacity() < getAmountStored()) {
                        player.sendStatusMessage(TranslationKeys.CANT_INSTALL_COMPONENT.text().appendSibling(TranslationKeys.COMPONENT_CAPACITY_TOO_SMALL.text()), true);
                        return null;
                    }

                    if (!this.component.isBase()) {
                        player.inventory.placeItemBackInInventory(world, componentStack);
                    }

                    this.component = storageComponent;
                    componentStack = stack.split(1);
                    onComponentsChanged(this.component, componentStack);
                    player.sendStatusMessage(TranslationKeys.COMPONENT_INSTALLED.text(), true);
                    return stack;
                }
            }
        } else {
            if (player.isSneaking()) {
                if (!component.isBase()) {
                    T baseComponent = ComponentManager.getInstance().getComponent(component.getType().getBaseComponentId());

                    if (baseComponent.getStorageCapacity() < getAmountStored()) {
                        player.sendStatusMessage(TranslationKeys.CANT_UNINSTALL_COMPONENT.text().appendSibling(TranslationKeys.COMPONENT_CAPACITY_TOO_SMALL.text()), true);
                        return null;
                    }

                    player.inventory.placeItemBackInInventory(world, componentStack);
                    component = baseComponent;
                    componentStack = new ItemStack(component);
                    onComponentsChanged(component, componentStack);
                    player.sendStatusMessage(TranslationKeys.COMPONENT_UNINSTALLED.text(), true);
                } else {
                    player.sendStatusMessage(TranslationKeys.CANT_UNINSTALL_COMPONENT.text().appendSibling(TranslationKeys.BASE_COMPONENT_INSTALLED.text()), true);
                }
            }
        }

        return null;
    }

    protected abstract void onComponentsChanged(T component, ItemStack stack);
    protected abstract int getAmountStored();
}
