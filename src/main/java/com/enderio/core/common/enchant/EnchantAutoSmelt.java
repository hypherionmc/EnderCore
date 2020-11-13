package com.enderio.core.common.enchant;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.enderio.core.EnderCore;
import com.enderio.core.api.common.enchant.IAdvancedEnchant;
import com.enderio.core.common.util.NullHelper;
import com.google.common.base.Predicate;

import com.google.common.base.Supplier;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EnderCore.MODID)
public class EnchantAutoSmelt extends Enchantment implements IAdvancedEnchant {

  private static EnchantAutoSmelt INSTANCE;

  private static final EnchantmentType ENCH_TYPE = EnchantmentType.create("EC_AUTOSMELT", new Predicate<Item>() {

    @Override
    public boolean apply(@Nullable Item item) {
      return NullHelper.notnullM(item, "EnumEnchantmentType.canEnchantItem(null)").isDamageable() && !(item instanceof ArmorItem)
          && !(item instanceof SwordItem) && !(item instanceof BowItem) && !(item instanceof FishingRodItem);
    }

    @Override
    public int hashCode() {
      return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
      return super.equals(obj);
    }

  });

  /**
   * Can be null prior to registration, or if disabled
   */
  @Nullable
  public static EnchantAutoSmelt instance() {
    return INSTANCE;
  }

  private EnchantAutoSmelt() {
    super(Rarity.RARE, ENCH_TYPE, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND });
    setRegistryName("autosmelt");
  }

  @Override
  public int getMaxEnchantability(int level) {
    return 60;
  }

  @Override
  public int getMinEnchantability(int level) {
    return 15;
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @Override
  public @Nonnull String getName() {
    return "enchantment.autosmelt";
  }

  @Override
  public boolean isAllowedOnBooks() {
    return ConfigHandler.allowAutoSmelt;
  }

  @Override
  public boolean canApply(@Nonnull ItemStack stack) {
    return ConfigHandler.allowAutoSmelt && super.canApply(stack);
  }

  @Override
  public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
    return ConfigHandler.allowAutoSmelt && super.canApplyAtEnchantingTable(stack);
  }

  @Override
  public @Nonnull String[] getTooltipDetails(@Nonnull ItemStack stack) {
    return new String[] { EnderCore.lang.localize("enchantment.autosmelt.tooltip", false) };
  }

  @SubscribeEvent
  public static void register(@Nonnull RegistryEvent.Register<Enchantment> event) {
    if (ConfigHandler.allowAutoSmelt) {
      INSTANCE = new EnchantAutoSmelt();
      event.getRegistry().register(INSTANCE);
      Supplier<String> supplier = new Supplier<String>() {
        @Override
        public String get() {
          return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><recipes>"
                  + "<recipe name=\"Enchanter: endercore:autosmelt\" required=\"true\" disabled=\"false\"><enchanting>"
                  + "<input name=\"oredict:blockCoal\" amount=\"32\"/><enchantment name=\"" + EnderCore.DOMAIN
                  + ":autosmelt\" costMultiplier=\"1\"/></enchanting></recipe></recipes>";
        }
      };
      InterModComms.sendTo("enderio", "recipe:xml",supplier);

    }
  }

  @Override
  public boolean canApplyTogether(@Nonnull Enchantment ench) {
    return super.canApplyTogether(ench) && ench != Enchantments.SILK_TOUCH;
  }

}
