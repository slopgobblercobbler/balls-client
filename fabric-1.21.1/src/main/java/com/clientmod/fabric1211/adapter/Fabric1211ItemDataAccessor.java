package com.clientmod.fabric1211.adapter;

import com.clientmod.core.adapter.IItemDataAccessor;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fabric1211ItemDataAccessor implements IItemDataAccessor<ItemStack> {
    @Override
    public List<ItemStack> getContainerContents(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return Collections.emptyList();
        }
        ContainerComponent container = stack.get(DataComponentTypes.CONTAINER);
        if (container == null) {
            return Collections.emptyList();
        }
        List<ItemStack> items = new ArrayList<>();
        container.iterateNonEmpty().forEach(items::add);
        return items;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return stack != null ? stack.getDamage() : 0;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return stack != null ? stack.getMaxDamage() : 0;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return stack != null && stack.isDamageable();
    }
}
