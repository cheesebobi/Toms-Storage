package com.tom.storagemod.block;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import com.tom.storagemod.Content;
import com.tom.storagemod.StorageModClient;
import com.tom.storagemod.tile.PaintedBlockEntity;

public class PaintedTrimBlock extends BaseEntityBlock implements ITrim, IPaintable {

	public PaintedTrimBlock() {
		super(Block.Properties.of(Material.WOOD).strength(0.5f));
	}

	@Override
	public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip,
			TooltipFlag flagIn) {
		tooltip.add(Component.translatable("tooltip.toms_storage.paintable"));
		StorageModClient.tooltip("trim", tooltip);
	}

	@Override
	public boolean paint(Level world, BlockPos pos, BlockState to) {
		BlockEntity te = world.getBlockEntity(pos);
		if(te != null && te instanceof PaintedBlockEntity)
			return ((PaintedBlockEntity)te).setPaintedBlockState(to);
		return false;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PaintedBlockEntity(pos, state);
	}

	@Override
	public RenderShape getRenderShape(BlockState p_149645_1_) {
		return RenderShape.MODEL;
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
		return new ItemStack(Content.inventoryTrim.get());
	}
}
