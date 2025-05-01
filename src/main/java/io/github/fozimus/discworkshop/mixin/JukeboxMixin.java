package io.github.fozimus.discworkshop.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.init.ItemInit;
import io.github.fozimus.discworkshop.sound.DiscSound;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;


@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxMixin {
	@Shadow
	private ItemStack recordStack;

	@Shadow
	public abstract BlockEntity asBlockEntity();

    private boolean hasMusicDisc = true;

    @Inject(at = @At("HEAD"), method = "onRecordStackChanged")
    private void onRecordStackChanged(boolean hasRecord, CallbackInfo ci) {
        if (!hasMusicDisc || hasRecord) return;

        if (asBlockEntity().getWorld() instanceof ServerWorld serverWorld) {
            DiscSound.stop(serverWorld, asBlockEntity().getPos());
        }
        
        hasMusicDisc = false;
	}
    
	@Inject(at = @At("TAIL"), method = "setStack")    
	public void setStack(ItemStack stack, CallbackInfo cir) {
        if (recordStack.getItem() != ItemInit.MUSIC_DISC) return;
        
        if (asBlockEntity().getWorld() instanceof ServerWorld serverWorld) {
            DiscSound.play(serverWorld, recordStack, asBlockEntity().getPos());
        }

        hasMusicDisc = true;
	}
}
