/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.item.inventory.lens.impl.comp;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.common.interfaces.entity.player.IMixinInventoryPlayer;
import org.spongepowered.common.item.inventory.adapter.InventoryAdapter;
import org.spongepowered.common.item.inventory.adapter.impl.comp.HotbarAdapter;
import org.spongepowered.common.item.inventory.lens.Fabric;
import org.spongepowered.common.item.inventory.lens.SlotProvider;
import org.spongepowered.common.item.inventory.lens.comp.HotbarLens;


public class HotbarLensImpl extends InventoryRowLensImpl implements HotbarLens<IInventory, net.minecraft.item.ItemStack> {

    public HotbarLensImpl(int base, int width, SlotProvider<IInventory, ItemStack> slots) {
        this(base, width, 0, 0, HotbarAdapter.class, slots);
    }

    public HotbarLensImpl(int base, int width, Class<? extends Inventory> adapterType, SlotProvider<IInventory, ItemStack> slots) {
        this(base, width, 0, 0, adapterType, slots);
    }
    
    public HotbarLensImpl(int base, int width, int xBase, int yBase, SlotProvider<IInventory, ItemStack> slots) {
        this(base, width, xBase, yBase, HotbarAdapter.class, slots);
    }
    
    public HotbarLensImpl(int base, int width, int xBase, int yBase, Class<? extends Inventory> adapterType, SlotProvider<IInventory, ItemStack> slots) {
        super(base, width, xBase, yBase, adapterType, slots);
    }

    @Override
    public InventoryAdapter<IInventory, ItemStack> getAdapter(Fabric<IInventory> inv, Inventory parent) {
        return new HotbarAdapter(inv, this, parent);
    }

    @Override
    public int getSelectedSlotIndex(Fabric<IInventory> inv) {
        for (IInventory inner : inv.allInventories()) {
            if (inner instanceof InventoryPlayer) {
                return ((InventoryPlayer) inner).currentItem;
            }
        }
        return 0;
    }

    @Override
    public void setSelectedSlotIndex(Fabric<IInventory> inv, int index) {
        inv.allInventories().stream().filter(inner -> inner instanceof IMixinInventoryPlayer).forEach(inner -> {
            ((IMixinInventoryPlayer) inner).setSelectedItem(index, true);
        });
    }

}
