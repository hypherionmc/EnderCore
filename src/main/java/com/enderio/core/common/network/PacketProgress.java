package com.enderio.core.common.network;

import javax.annotation.Nonnull;

import com.enderio.core.api.common.util.IProgressTile;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketProgress extends PacketTileEntity<TileEntity> {

  float progress;

  public PacketProgress() {
  }

  public PacketProgress(@Nonnull IProgressTile tile) {
    super(tile.getTileEntity());
    progress = tile.getProgress();
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.writeFloat(progress);
  }

  @Override
  public void read(PacketBuffer buffer) {
    progress = buffer.readFloat();
  }

  @Override
  public boolean onReceived(@Nonnull TileEntity te, @Nonnull Supplier<NetworkEvent.Context> context) {
    if (te instanceof IProgressTile) {
      ((IProgressTile) te).setProgress(progress);
    }
    return true;
  }
}
