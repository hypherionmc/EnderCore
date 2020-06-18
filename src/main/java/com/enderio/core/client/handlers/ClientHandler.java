package com.enderio.core.client.handlers;

import javax.annotation.Nonnull;

import com.enderio.core.common.Handlers.Handler;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


@Handler
public class ClientHandler {

  private static int ticksElapsed;

  public static int getTicksElapsed() {
    return ticksElapsed;
  }

  @SubscribeEvent
  public static void onClientTick(@Nonnull TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      ticksElapsed++;
    }
  }

  private ClientHandler() {
  }

}
