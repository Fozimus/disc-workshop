package io.github.fozimus.discworkshop.screen;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.screenhandler.DiscWorkshopScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DiscWorkshopScreen extends HandledScreen<DiscWorkshopScreenHandler> {
    private static final Identifier TEXTURE = DiscWorkshop.id("textures/gui/container/disc_workshop.png");


    public DiscWorkshopScreen(DiscWorkshopScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        addDrawableChild(ButtonWidget.builder(Text.of("gay"), button -> {
                    button.setX(button.getX() + 1);
                }).build());
    }

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

	}
    
}
