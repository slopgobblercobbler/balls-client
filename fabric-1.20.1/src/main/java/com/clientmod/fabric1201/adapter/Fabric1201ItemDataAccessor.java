package com.clientmod.fabric1201.adapter;

import com.clientmod.core.adapter.IItemDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fabric1201ItemDataAccessor implements IItemDataAccessor<ItemStack> {
    @Override
    public List<ItemStack> getContainerContents(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return Collections.emptyList();
        }
        NbtCompound nbt = stack.getNbt();
        if (nbt == null || !nbt.contains("BlockEntityTag", NbtElement.COMPOUND_TYPE)) {
            return Collections.emptyList();
        }
        NbtCompound blockEntityTag = nbt.getCompound("BlockEntityTag");
        if (!blockEntityTag.contains("Items", NbtElement.LIST_TYPE)) {
            return Collections.emptyList();
        }
        NbtList itemsList = blockEntityTag.getList("Items", NbtElement.COMPOUND_TYPE);
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < itemsList.size(); i++) {
            NbtCompound itemCompound = itemsList.getCompound(i);
            items.add(ItemStack.fromNbt(itemCompound));
        }
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
