package com.enderio.core.client;

import java.lang.reflect.Field;

import com.enderio.core.common.util.Log;

import net.minecraft.client.particle.Particle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientUtil {
  public static void setParticleVelocity(Particle p, double x, double y, double z) {
    if (p == null) {
      return;
    }
    p.motionX = x;
    p.motionY = y;
    p.motionZ = z;
  }

  public static void setParticleVelocityY(Particle p, double y) {
    if (p == null) {
      return;
    }
    p.motionY = y;
  }

  public static double getParticleVelocityY(Particle p) {
    if (p == null) {
      return 0;
    }
    return p.motionY;
  }
}
