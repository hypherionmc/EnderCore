package com.enderio.core.client.handlers;

import javax.annotation.Nonnull;

import com.enderio.core.common.OreDict;
import com.enderio.core.common.util.OreDictionaryHelper;
import net.java.games.input.Keyboard;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.enderio.core.EnderCore;
import com.enderio.core.common.Handlers.Handler;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.BooleanUtils;
import org.lwjgl.glfw.GLFW;

import static com.enderio.core.common.config.ConfigHandler.showOredictTooltips;
import static com.enderio.core.common.config.ConfigHandler.showRegistryNameTooltips;

@Handler
public class OreDictTooltipHandler {

  @SubscribeEvent
  public static void onItemTooltip(@Nonnull ItemTooltipEvent event) {
    boolean shiftDown = BooleanUtils.toBoolean(GLFW.glfwGetKey(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) || BooleanUtils.toBoolean(GLFW.glfwGetKey(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT));
    //boolean shiftDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    boolean debugMode = Minecraft.getInstance().gameSettings.advancedItemTooltips;
    boolean doRegistry = showRegistryNameTooltips == 3 ? debugMode : showRegistryNameTooltips == 2 ? shiftDown : showRegistryNameTooltips == 1;
    boolean doOredict = showOredictTooltips == 3 ? debugMode : showOredictTooltips == 2 ? shiftDown : showOredictTooltips == 1;

    if (doRegistry) {

      final ResourceLocation nameForObject = ForgeRegistries.ITEMS.getKey(event.getItemStack().getItem()); //Item.REGISTRY.getNameForObject(event.getItemStack().getItem());
      if (nameForObject != null) {
        event.getToolTip().add(new StringTextComponent(nameForObject.toString()));
      } else {
        event.getToolTip().add(new StringTextComponent("ERROR: non-existing item"));
      }
    }

    if (doOredict) {
      if (event.getItemStack().isEmpty()) {
        event.getToolTip().add(new StringTextComponent("ERROR: empty item stack"));
      } else {
        //int[] ids = OreDictionary.getOreIDs(event.getItemStack());
        String[] names = OreDictionaryHelper.getOreNames(event.getItemStack());
        if (names.length > 0) {
          event.getToolTip().add(new StringTextComponent(EnderCore.lang.localize("tooltip.oreDictNames")));
          for (String name : names) {
            event.getToolTip().add(new StringTextComponent("  - " + name));
          }
        }
      }
    }
  }

  private OreDictTooltipHandler() {
  }

}
