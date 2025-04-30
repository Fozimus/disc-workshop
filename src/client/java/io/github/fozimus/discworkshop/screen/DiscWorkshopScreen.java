package io.github.fozimus.discworkshop.screen;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.network.UrlPayload;
import io.github.fozimus.discworkshop.screenhandler.DiscWorkshopScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

public class DiscWorkshopScreen extends HandledScreen<DiscWorkshopScreenHandler> {
    private static final Identifier TEXTURE = DiscWorkshop.id("textures/gui/container/disc_workshop.png");

    private TextFieldWidget urlField;


    public DiscWorkshopScreen(DiscWorkshopScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundWidth = 209;
        backgroundHeight = 184;
        playerInventoryTitleY = 93;       
    }

    @Override
    protected void init() {
        super.init();
        urlField = new TextFieldWidget(textRenderer, x + 8, y + 81, 80, 8, Text.literal("Url"));
        urlField.setFocusUnlocked(false);
		urlField.setEditableColor(-1);
		urlField.setUneditableColor(-1);
		urlField.setDrawsBackground(false);
        urlField.setText(handler.getBlockEntity().getUrl());
		urlField.setMaxLength(1000);
		urlField.setChangedListener(this::onUrlChange);
		addSelectableChild(urlField);
		urlField.setEditable(true);
        addDrawable(urlField);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!urlField.getText().equals(handler.getBlockEntity().getUrl())) {
            urlField.setText(handler.getBlockEntity().getUrl());            
        }
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
		context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 0x6f5f58, false);
		context.drawText(this.textRenderer, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY, 0x6f5f58, false);
	}

    void onUrlChange(String url) {
         ClientPlayNetworking.send(new UrlPayload(url, handler.getPos()));
         handler.getBlockEntity().setUrlFromClient(url);
    }
    
	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        ItemStack item = handler.getBlockEntity().getCraftingResult();

        if (item.isEmpty()) return;

        MatrixStack matrices = context.getMatrices();
        
        matrices.push();
        matrices.translate(x + 10, y + 5, 0);
        matrices.scale(5, 5, 1);
        context.drawItem(item, 0, 0);
        matrices.pop();
	}
}
