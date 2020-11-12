package com.enderio.core.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface IEnderPacket {
    void toBytes(PacketBuffer buffer);
    void fromBytes(PacketBuffer buffer);
    boolean handle(Supplier<NetworkEvent.Context> context);
}
