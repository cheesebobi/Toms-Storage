package com.tom.storagemod.tile;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import com.tom.storagemod.StorageMod;
import com.tom.storagemod.block.BlockInventoryHopperBasic;
import com.tom.storagemod.util.EmptyHandler;
import com.tom.storagemod.util.IItemHandler;
import com.tom.storagemod.util.ItemHandlerHelper;

public class TileEntityInventoryHopperBasic extends TileEntityInventoryHopperBase {
	private ItemStack filter = ItemStack.EMPTY;
	private int cooldown, lastItemSlot = -1;
	public TileEntityInventoryHopperBasic() {
		super(StorageMod.invHopperBasicTile);
	}

	@Override
	protected void update() {
		if(topNet && getFilter().isEmpty())return;
		if(cooldown > 0) {
			cooldown--;
			return;
		}
		if(!this.getCachedState().get(BlockInventoryHopperBasic.ENABLED))return;
		boolean hasFilter = !getFilter().isEmpty();
		IItemHandler top = this.top == null ? EmptyHandler.INSTANCE : this.top.wrap();
		IItemHandler bot = this.bottom == null ? EmptyHandler.INSTANCE : this.bottom.wrap();

		if(lastItemSlot != -1 && lastItemSlot < top.getSlots()) {
			if(hasFilter) {
				ItemStack inSlot = top.getStackInSlot(lastItemSlot);
				if(!ItemStack.areItemsEqual(inSlot, getFilter()) || !ItemStack.areTagsEqual(inSlot, getFilter())) {
					lastItemSlot = -1;
				}
			}
		}
		if(lastItemSlot == -1) {
			for (int i = 0; i < top.getSlots(); i++) {
				if(hasFilter) {
					ItemStack inSlot = top.getStackInSlot(i);
					if(!ItemStack.areItemsEqual(inSlot, getFilter()) || !ItemStack.areTagsEqual(inSlot, getFilter())) {
						continue;
					}
				}
				ItemStack extractItem = top.extractItem(i, 1, true);
				if (!extractItem.isEmpty()) {
					lastItemSlot = i;
					break;
				}
			}
			cooldown = 10;
		}
		if(lastItemSlot != -1) {
			ItemStack extractItem = top.extractItem(lastItemSlot, 1, true);
			if (!extractItem.isEmpty()) {
				ItemStack is = ItemHandlerHelper.insertItemStacked(bot, extractItem, true);
				if(is.isEmpty()) {
					is = ItemHandlerHelper.insertItemStacked(bot, top.extractItem(lastItemSlot, 1, false), false);
					cooldown = 10;
					if(!is.isEmpty()) {
						//Never?
					}
					return;
				}
			}
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound compound) {
		compound.put("Filter", getFilter().writeNbt(new NbtCompound()));
		return super.writeNbt(compound);
	}

	@Override
	public void fromTag(BlockState stateIn, NbtCompound nbtIn) {
		super.fromTag(stateIn, nbtIn);
		setFilter(ItemStack.fromNbt(nbtIn.getCompound("Filter")));
	}

	public void setFilter(ItemStack filter) {
		this.filter = filter;
	}

	public ItemStack getFilter() {
		return filter;
	}
}
