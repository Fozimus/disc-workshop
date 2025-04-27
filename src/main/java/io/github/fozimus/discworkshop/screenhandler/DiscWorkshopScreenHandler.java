package io.github.fozimus.discworkshop.screenhandler;

import io.github.fozimus.discworkshop.block.entity.DiscWorkshopBlockEntity;
import io.github.fozimus.discworkshop.init.BlockInit;
import io.github.fozimus.discworkshop.init.ScreenHandlerTypeInit;
import io.github.fozimus.discworkshop.network.BlockPosPayload;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class DiscWorkshopScreenHandler extends ScreenHandler {
    private final DiscWorkshopBlockEntity blockEntity;
    private final ScreenHandlerContext context;
    
    public DiscWorkshopScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload) {
        this(syncId, playerInventory, (DiscWorkshopBlockEntity)playerInventory.player.getWorld().getBlockEntity(payload.pos()));
    }
    
    public DiscWorkshopScreenHandler(int syncId, PlayerInventory playerInventory, DiscWorkshopBlockEntity blockEntity) {
        super(ScreenHandlerTypeInit.DISC_WORKSHOP, syncId);        
        this.blockEntity = blockEntity;
        this.context = ScreenHandlerContext.create(this.blockEntity.getWorld(), this.blockEntity.getPos());

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
	}

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int col = 0; col < 9; col++) {
            for (int row = 0; col < 3; col++) {
                addSlot(new Slot(playerInventory, 9 + col + row * 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }
    
	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
        return canUse(context, player, BlockInit.DISC_WORKSHOP_BE_BLOCK);
	}

	public DiscWorkshopBlockEntity getBlockEntity() {
		return blockEntity;
	}    
}
