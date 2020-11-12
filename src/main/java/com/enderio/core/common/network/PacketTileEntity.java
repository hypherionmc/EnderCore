package com.enderio.core.common.network;

import java.lang.reflect.TypeVariable;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.enderio.core.EnderCore;
import com.google.common.reflect.TypeToken;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public abstract class PacketTileEntity<T extends TileEntity> implements IEnderPacket {

  private long pos;

  protected PacketTileEntity() {
  }

  protected PacketTileEntity(@Nonnull T tile) {
    pos = tile.getPos().toLong();
  }

  public PacketTileEntity(PacketBuffer buffer) {
    pos = buffer.readLong();
  }

  @Override
  public final void toBytes(PacketBuffer buffer) {
    buffer.writeLong(pos);
    write(buffer);
  }

  public void write(PacketBuffer buffer) {
  }

  @Override
  public final void fromBytes(PacketBuffer buffer) {
    pos = buffer.readLong();
    read(buffer);
  }

  public void read(PacketBuffer buffer) {
  }

  public @Nonnull BlockPos getPos() {
    return BlockPos.fromLong(pos);
  }

  @SuppressWarnings("unchecked")
  protected T getTileEntity(World worldObj) {
    // Sanity check, and prevent malicious packets from loading chunks
    if (worldObj == null || !worldObj.isBlockLoaded(getPos())) {
      return null;
    }
    TileEntity te = worldObj.getTileEntity(getPos());
    if (te == null) {
      return null;
    }
    @SuppressWarnings("rawtypes")
    final Class<? extends PacketTileEntity> ourClass = getClass();
    @SuppressWarnings("rawtypes")
    final TypeVariable<Class<PacketTileEntity>>[] typeParameters = PacketTileEntity.class.getTypeParameters();
    if (typeParameters.length > 0) {
      @SuppressWarnings("rawtypes")
      final TypeVariable<Class<PacketTileEntity>> typeParam0 = typeParameters[0];
      if (typeParam0 != null) {
        TypeToken<?> teType = TypeToken.of(ourClass).resolveType(typeParam0);
        final Class<? extends TileEntity> teClass = te.getClass();
        if (teType.isSupertypeOf(teClass)) {
          return (T) te;
        }
      }
    }
    return null;
  }

  @Override
  public boolean handle(Supplier<NetworkEvent.Context> context) {
    if (context.get() != null) {
      T te = getTileEntity(getWorld(context));
      if (te != null) {
        return onReceived(te, context);
      }
    }
    return true;
  }

  public boolean onReceived(@Nonnull T te, @Nonnull Supplier<NetworkEvent.Context> context) {
    return true;
  }

  protected @Nonnull World getWorld(Supplier<NetworkEvent.Context> context) {
    if (context.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
      return context.get().getSender().world;
    } else {
      final World clientWorld = EnderCore.proxy.getClientWorld();
      if (clientWorld == null) {
        throw new NullPointerException("Recieved network packet ouside any world!");
      }
      return clientWorld;
    }
  }
}
