package io.github.fozimus.discworkshop.screen;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.screenhandler.DiscWorkshopScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
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
		urlField.setText(handler.getBlockEntity().getUrl());
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
        handler.getBlockEntity().setUrl(url);
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
