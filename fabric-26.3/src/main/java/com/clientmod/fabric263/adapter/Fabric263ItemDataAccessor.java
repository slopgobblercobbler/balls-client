package com.clientmod.fabric263.adapter;

import com.clientmod.core.adapter.IItemDataAccessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fabric263ItemDataAccessor implements IItemDataAccessor<ItemStack> {
    @Override
    public List<ItemStack> getContainerContents(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return Collections.emptyList();
        }
        ItemContainerContents container = stack.get(DataComponents.CONTAINER);
        if (container == null) {
            return Collections.emptyList();
        }
        List<ItemStack> items = new ArrayList<>();
        container.nonEmptyItems().forEach(items::add);
        return items;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return stack != null ? stack.getDamageValue() : 0;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return stack != null ? stack.getMaxDamage() : 0;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return stack != null && stack.isDamageableItem();
    }
}
