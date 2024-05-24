package com.iafenvoy.avaritia.item.block.entity;

import com.iafenvoy.avaritia.data.recipe.ExtremeCraftingShapedRecipe;
import com.iafenvoy.avaritia.gui.ExtremeCraftingTableScreenHandler;
import com.iafenvoy.avaritia.registry.ModBlockEntities;
import com.iafenvoy.avaritia.util.RecipeUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExtremeCraftingTableBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(82, ItemStack.EMPTY);


    public ExtremeCraftingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EXTREME_CRAFTING_TABLE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, ExtremeCraftingTableBlockEntity entity) {
        entity.inventory.set(81, ItemStack.EMPTY);
        List<List<ItemStack>> table = RecipeUtil.toTable(entity.inventory, 9, 9);
        for (ExtremeCraftingShapedRecipe recipe : ExtremeCraftingShapedRecipe.recipes.values())
            if (recipe.matches(table)) {
                entity.inventory.set(81, recipe.output());
                break;
            }
        entity.markDirty();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(this.getCachedState().getBlock().getTranslationKey());
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ExtremeCraftingTableScreenHandler(syncId, inv, this);
    }
}
