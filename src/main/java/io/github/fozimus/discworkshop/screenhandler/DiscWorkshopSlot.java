package io.github.fozimus.discworkshop.screenhandler;

import io.github.fozimus.discworkshop.init.ItemInit;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class DiscWorkshopSlot extends Slot {

	public DiscWorkshopSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

    @Override
    public boolean canInsert(ItemStack stack) {
        return getIndex() == 5
            ? stack.getItem().equals(ItemInit.BLANK_DISC)
            : stack.getItem() instanceof DyeItem;
    }
}
