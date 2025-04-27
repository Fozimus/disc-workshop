package io.github.fozimus.discworkshop.block.entity;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.init.BlockEntityTypeInit;
import io.github.fozimus.discworkshop.network.BlockPosPayload;
import io.github.fozimus.discworkshop.screenhandler.DiscWorkshopScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class DiscWorkshopBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPosPayload> {
    public static final Text TITLE = Text.translatable("container." + DiscWorkshop.MOD_ID + ".disc_workshop");
    
    public DiscWorkshopBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypeInit.DISC_WORKSHOP_BLOCK_ENTITY, pos, state);
	}

	@Override
	public Text getDisplayName() {
        return TITLE;
	}

	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new DiscWorkshopScreenHandler(syncId, playerInventory, this);
	}

	@Override
	public BlockPosPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new BlockPosPayload(this.pos);
	}

}
