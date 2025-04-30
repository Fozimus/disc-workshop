package io.github.fozimus.discworkshop.block.entity;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.IntStream;

import io.github.fozimus.discworkshop.DiscWorkshop;
import io.github.fozimus.discworkshop.init.BlockEntityTypeInit;
import io.github.fozimus.discworkshop.init.ComponentTypesInit;
import io.github.fozimus.discworkshop.init.ItemInit;
import io.github.fozimus.discworkshop.network.UrlPayload;
import io.github.fozimus.discworkshop.screenhandler.DiscWorkshopScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentChanges;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class DiscWorkshopBlockEntity extends BlockEntity implements SidedInventory,  ExtendedScreenHandlerFactory<BlockPos> {
    public static final Text TITLE = Text.translatable("container." + DiscWorkshop.MOD_ID + ".disc_workshop");
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(11, ItemStack.EMPTY);
    private String url = "";

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
	public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return pos;
	}

	@Override
	public int size() {
        return 11;
	}

	@Override
	public boolean isEmpty() {
        for (int i = 0; i < size(); ++i) {
            if (!getStack(i).isEmpty()) {
                return false;
            }
        }
        return true;
	}

	@Override
	public ItemStack getStack(int slot) {
        return inventory.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
        return removeStack(slot);
	}

	@Override
	public ItemStack removeStack(int slot) {
        if (!canExtract(slot, getStack(slot), null)) return ItemStack.EMPTY;

        ItemStack result;
        if (slot == 5 && canCraft()) {
            result = getCraftingResult();
            inventory.clear();
            url = "";
        }
        else {
            result = getStack(slot).copyAndEmpty();
        }

        markDirty();
        return result;
    }

    public ItemStack getCraftingResult() {
        if (!canCraft()) return ItemStack.EMPTY;
        return new ItemStack(RegistryEntry.of(ItemInit.MUSIC_DISC), 1,
                             ComponentChanges
                             .builder()
                             .add(ComponentTypesInit.DISC_URL, url)
                             .add(ComponentTypesInit.DISC_PATTERN,
                                  IntStream
                                  .range(0, inventory.size())
                                  .filter(i -> i != 5)
                                  .mapToObj(inventory::get)
                                  .map(stack -> stack.isEmpty()
                                       ? (DyeItem)Items.WHITE_DYE
                                       : (DyeItem)stack.getItem())
                                  .map(dye -> dye.getColor().getEntityColor())
                                  .toList())
                             .build());
    }

    private boolean isValidURL(String url)  {
        try {
            URI.create(url).toURL().toURI();
            return true;
        } catch (MalformedURLException | IllegalArgumentException | URISyntaxException e) {
            return false;
        }
    }
    
    private boolean canCraft() {
        if (inventory.get(5).isEmpty()) return false;
        return !url.isBlank() && isValidURL(url);
    }
    
	@Override
	public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        markDirty();
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
        return true;
	}

	@Override
	public void clear() {
        inventory.clear();
        markDirty();
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
        return IntStream.range(0, size())
            .filter((i) -> getStack(i).isEmpty())
            .toArray();
	}


    @Override
    public int getMaxCountPerStack() {
        return 1;
    }

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        return slot == 5
            ? stack.getItem().equals(ItemInit.BLANK_DISC)
            : stack.getItem() instanceof DyeItem;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
	}

    @Override
    public NbtCompound toInitialChunkDataNbt(WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    
    @Override
    protected void writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putString("url", url);
    }

    @Override
    protected void readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, inventory, registryLookup);
        url = nbt.getString("url");
    }

    public void setUrlFromClient(String url) {
        this.url = url;       
        markDirty();

        DiscWorkshop.LOGGER.info("{} {}", world.isClient ? "Client" : "Server", url);
    }
    
    public void setUrl(String url) {
        this.url = url;       
        markDirty();

        DiscWorkshop.LOGGER.info("{} {}", world.isClient ? "Client" : "Server", url);
        
        if (world instanceof ServerWorld serverWorld) {
            for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                ServerPlayNetworking.send(player, new UrlPayload(url, pos));            
            }            
        }
    }

    public String getUrl() {
        return url;
    }
}
