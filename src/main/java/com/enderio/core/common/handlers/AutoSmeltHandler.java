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
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber
public class AutoSmeltHandler {
    @SubscribeEvent
    public static void handleBlockBreak(BreakEvent event) {
        if (!event.getWorld().isRemote() && EnchantmentHelper.getEnchantmentLevel(EnchantmentAutoSmelt.instance(), event.getPlayer().getHeldItem(Hand.MAIN_HAND)) > 0) { // Checks if running on server and enchant is on tool
            ServerWorld serverWorld = ((ServerWorld) event.getWorld()); // Casts IWorld to ServerWorld
            LootContext.Builder lootcontext$builder = (new LootContext.Builder(serverWorld)
                    .withRandom(serverWorld.rand)
                    .withParameter(LootParameters.field_237457_g_, Vector3d.copyCentered(event.getPos()))
                    .withParameter(LootParameters.TOOL, event.getPlayer().getHeldItem(Hand.MAIN_HAND))); // Makes lootcontext to calculate drops
            List<ItemStack> drops = event.getState().getDrops(lootcontext$builder); // Calculates drops
            for (ItemStack item : drops) { // Iteration
                ItemStack stack = serverWorld.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(item), serverWorld)
                        .map(FurnaceRecipe::getRecipeOutput)
                        .filter(itemStack -> !itemStack.isEmpty())
                        .map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, item.getCount() * itemStack.getCount()))
                        .orElse(item); // Recipe as var
                InventoryHelper.spawnItemStack(event.getPlayer().world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), stack); // Spawns Itemstack
            }
            event.getPlayer().world.destroyBlock(event.getPos(), false); // Breaks block
            event.setResult(Event.Result.DENY);
        }
    }
}