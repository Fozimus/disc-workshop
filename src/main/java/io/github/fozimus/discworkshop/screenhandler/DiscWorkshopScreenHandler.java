package io.github.fozimus.discworkshop.screenhandler;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.block.entity.DiscWorkshopBlockEntity;
import io.github.fozimus.discworkshop.init.BlockInit;
import io.github.fozimus.discworkshop.init.ScreenHandlerTypeInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class DiscWorkshopScreenHandler extends ScreenHandler {
    private final DiscWorkshopBlockEntity blockEntity;
    private final ScreenHandlerContext context;
    
    public DiscWorkshopScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, (DiscWorkshopBlockEntity)playerInventory.player.getWorld().getBlockEntity(pos));

    }
    
    public DiscWorkshopScreenHandler(int syncId, PlayerInventory playerInventory, DiscWorkshopBlockEntity blockEntity) {
        super(ScreenHandlerTypeInit.DISC_WORKSHOP, syncId);        
        this.blockEntity = blockEntity;
        this.context = ScreenHandlerContext.create(this.blockEntity.getWorld(), this.blockEntity.getPos());
        checkSize(blockEntity, 11);
        blockEntity.onOpen(playerInventory.player);
        
        addBlockInventory();
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
	}

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        blockEntity.onClose(player);

        blockEntity.setEditor(null);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, 9 + col + row * 9, 24 + col * 18, 105 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 24 + col * 18, 163));
        }
    }

    private void addBlockInventory() {
        int i = 0;
        
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 5; col++) {
                if ((col == 0 || col == 4) && (row == 0 || row == 2)) {
                    continue;
                }
                
                addSlot(new DiscWorkshopSlot(blockEntity, i, 104 + col * 19, 18 + row * 19));
                i += 1;
            }
        }

    }

	@Override
	public ItemStack quickMove(PlayerEntity player, int slotIndex) {

        int invSize = blockEntity.size();
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.getSlot(slotIndex);

        if (slot != null && slot.hasStack()) {
            ItemStack invSlot = slot.getStack();
            newStack = invSlot.copy();

            if (slotIndex == 5) {
                if (blockEntity.canExtract(slotIndex, invSlot, null)) {
                    ItemStack disc = blockEntity.removeStack(slotIndex);
                    if (!insertItem(disc, invSize, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                }
            }            
            else if (slotIndex < invSize) {
                if (!blockEntity.canExtract(slotIndex, invSlot, null)) {
                    return ItemStack.EMPTY;
                }
                
                if (!insertItem(invSlot, invSize, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else {
                if (!insertItem(invSlot, 0, invSize, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (invSlot.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            }
            else {
                slot.markDirty();
            }
        }
            
        return newStack;
	}
    
	@Override
	public boolean canUse(PlayerEntity player) {
        return canUse(context, player, BlockInit.DISC_WORKSHOP_BE_BLOCK);
	}

	public DiscWorkshopBlockEntity getBlockEntity() {
		return blockEntity;
	}

    public BlockPos getPos() {
        return blockEntity.getPos();
    }
}
