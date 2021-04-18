package com.enderio.core.common.handlers;

import com.enderio.core.common.enchantment.EnchantmentAutoSmelt;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber
public class AutoSmeltHandler {
    @SubscribeEvent
    public static void handleBlockBreak(BreakEvent event) {
        if (!event.getWorld().isRemote() && EnchantmentHelper.getEnchantmentLevel(EnchantmentAutoSmelt.instance(), event.getPlayer().getActiveItemStack()) > 0) { // Checks if running on server and enchant is on tool
            ServerWorld serverWorld = ((ServerWorld) event.getWorld()); // Casts IWorld to ServerWorld
            LootContext.Builder lootcontext$builder = (new LootContext.Builder(serverWorld)
                    .withRandom(new Random())
                    .withParameter(LootParameters.field_237457_g_, Vector3d.copyCentered(event.getPos()))
                    .withParameter(LootParameters.TOOL, event.getPlayer()
                            .getActiveItemStack())); // Makes lootcontext to calculate drops
            List<ItemStack> drops = event.getState().getDrops(lootcontext$builder); // Calculates drops
            for (ItemStack itemStack : drops) { // Iteration
                if (serverWorld.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(itemStack), serverWorld).isPresent()) { // Checks if recipe is present
                    FurnaceRecipe recipe = serverWorld.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(itemStack), serverWorld).get(); // Recipe as var
                    InventoryHelper.spawnItemStack(serverWorld, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), recipe.getRecipeOutput()); // Spawns Itemstack
                }
            }
            serverWorld.destroyBlock(event.getPos(), false); // Breaks block
            event.setCanceled(true); // Cancels the player breaking the block, shouldn't be required, but there just in case
        }
    }
}