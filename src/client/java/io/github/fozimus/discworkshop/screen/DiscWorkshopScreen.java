package io.github.fozimus.discworkshop.screen;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.network.UrlPayload;
import io.github.fozimus.discworkshop.screenhandler.DiscWorkshopScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DiscWorkshopScreen extends HandledScreen<DiscWorkshopScreenHandler> {
    private static final Identifier TEXTURE = DiscWorkshop.id("textures/gui/container/disc_workshop.png");

    private TextFieldWidget urlField;


    public DiscWorkshopScreen(DiscWorkshopScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 209;
        this.backgroundHeight = 184;
        this.playerInventoryTitleY = 93;

    }

    @Override
    protected void init() {
        super.init();
        urlField = new TextFieldWidget(textRenderer, x + 8, y + 81, 80, 8, Text.literal("Url"));
        urlField.setFocusUnlocked(false);
		urlField.setEditableColor(-1);
		urlField.setUneditableColor(-1);
		urlField.setDrawsBackground(false);
		urlField.setMaxLength(1000);
		urlField.setChangedListener(this::onUrlChange);
		urlField.setText(handler.getUrl());
		addSelectableChild(urlField);
		urlField.setEditable(true);

        addDrawable(urlField);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    void onUrlChange(String url) {
        ClientPlayNetworking.send(new UrlPayload(url, handler.getPos()));
    }
    
	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight); 
	}
    
}
