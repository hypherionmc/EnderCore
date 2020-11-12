package com.enderio.core.common.network;

import com.enderio.core.common.config.PacketConfigSync;
import com.enderio.core.common.util.ChatUtil.PacketNoSpamChat;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class EnderPacketHandler {

  private static SimpleChannel INSTANCE;

  public static void init() {
    INSTANCE.registerMessage(PacketConfigSync.Handler.class, PacketConfigSync.class, 0, Side.CLIENT);
    INSTANCE.registerMessage(PacketProgress.Handler.class, PacketProgress.class, 1, Side.CLIENT);
    INSTANCE.registerMessage(PacketNoSpamChat.Handler.class, PacketNoSpamChat.class, 2, Side.CLIENT);
    INSTANCE.registerMessage(PacketGhostSlot.Handler.class, PacketGhostSlot.class, 3, Side.SERVER);
  }

  public static void sendToAllTracking(IPacket<?> message, TileEntity te) {
    sendToAllTracking(message, te.getWorld(), te.getPos());
  }

  // Credit: https://github.com/mekanism/Mekanism/blob/0287e5fd48a02dd8fe0b7a474c766d6c3a8d3f01/src/main/java/mekanism/common/network/BasePacketHandler.java#L150
  public static void sendToAllTracking(IPacket<?> packet, World world, BlockPos pos) {
    if (world instanceof ServerWorld) {
      ((ServerWorld) world).getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(pos), false).forEach(p -> sendTo(packet, p));
    } else{
      INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunk(pos.getX() >> 4, pos.getZ() >> 4)), packet);
    }
  }

  public static void sendTo(IEnderPacket packet, ServerPlayerEntity player) {
    INSTANCE.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
  }

  public static void sendToServer(IEnderPacket packet) {
    INSTANCE.sendToServer(packet);
  }
}
