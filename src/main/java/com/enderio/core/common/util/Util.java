package com.enderio.core.common.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.enderio.core.api.common.util.IProgressTile;
import com.enderio.core.common.vecmath.Vector3d;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Util {

  public static @Nullable Block getBlockFromItemId(@Nonnull ItemStack itemId) {
    Item item = itemId.getItem();
    if (item instanceof BlockItem) {
      return ((BlockItem) item).getBlock();
    }
    return null;
  }

  public static @Nonnull ItemStack consumeItem(@Nonnull ItemStack stack) {
    if (stack.getItem() instanceof PotionItem) {
      if (stack.getCount() == 1) {
        return new ItemStack(Items.GLASS_BOTTLE);
      } else {
        stack.split(1);
        return stack;
      }
    }
    if (stack.getCount() == 1) {
      if (stack.getItem().hasContainerItem(stack)) {
        return stack.getItem().getContainerItem(stack);
      } else {
        return ItemStack.EMPTY;
      }
    } else {
      stack.split(1);
      return stack;
    }
  }

  public static void giveExperience(@Nonnull PlayerEntity thePlayer, float experience) {
    int intExp = (int) experience;
    float fractional = experience - intExp;
    if (fractional > 0.0F) {
      if ((float) Math.random() < fractional) {
        ++intExp;
      }
    }
    while (intExp > 0) {
      int j = ExperienceOrbEntity.getXPSplit(intExp);
      intExp -= j;
      thePlayer.world.spawnEntity(new ExperienceOrbEntity(thePlayer.world, thePlayer.getPosX(), thePlayer.getPosY() + 0.5D, thePlayer.getPosZ() + 0.5D, j));
    }
  }

  public static ItemEntity createDrop(@Nonnull World world, @Nonnull ItemStack stack, double x, double y, double z, boolean doRandomSpread) {
    if (stack.isEmpty()) {
      return null;
    }
    if (doRandomSpread) {
      float f1 = 0.7F;
      double d = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
      double d1 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
      double d2 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
      ItemEntity entityitem = new ItemEntity(world, x + d, y + d1, z + d2, stack);
      entityitem.setDefaultPickupDelay();
      return entityitem;
    } else {
      ItemEntity entityitem = new ItemEntity(world, x, y, z, stack);
      entityitem.setMotion(0, 0, 0);
      entityitem.setNoPickupDelay();
      return entityitem;
    }
  }

  public static void dropItems(@Nonnull World world, @Nonnull ItemStack stack, @Nonnull BlockPos pos, boolean doRandomSpread) {
    dropItems(world, stack, pos.getX(), pos.getY(), pos.getZ(), doRandomSpread);
  }

  public static void dropItems(@Nonnull World world, @Nonnull ItemStack stack, double x, double y, double z, boolean doRandomSpread) {
    if (stack.isEmpty()) {
      return;
    }

    ItemEntity entityitem = createEntityItem(world, stack, x, y, z, doRandomSpread);
    world.spawnEntity(entityitem);
  }

  public static ItemEntity createEntityItem(@Nonnull World world, @Nonnull ItemStack stack, double x, double y, double z) {
    return createEntityItem(world, stack, x, y, z, true);
  }

  public static @Nonnull ItemEntity createEntityItem(@Nonnull World world, @Nonnull ItemStack stack, double x, double y, double z, boolean doRandomSpread) {
    ItemEntity entityitem;
    if (doRandomSpread) {
      float f1 = 0.7F;
      double d = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
      double d1 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
      double d2 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
      entityitem = new ItemEntity(world, x + d, y + d1, z + d2, stack);
      entityitem.setDefaultPickupDelay();
    } else {
      entityitem = new ItemEntity(world, x, y, z, stack);
      entityitem.setMotion(0, 0, 0);
      entityitem.setNoPickupDelay();
    }
    return entityitem;
  }

  public static void dropItems(@Nonnull World world, @Nonnull ItemStack stack, int x, int y, int z, boolean doRandomSpread) {
    if (stack.isEmpty()) {
      return;
    }

    if (doRandomSpread) {
      float f1 = 0.7F;
      double d = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
      double d1 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
      double d2 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
      ItemEntity entityitem = new ItemEntity(world, x + d, y + d1, z + d2, stack);
      entityitem.setDefaultPickupDelay();
      world.spawnEntity(entityitem);
    } else {
      ItemEntity entityitem = new ItemEntity(world, x + 0.5, y + 0.5, z + 0.5, stack);
      entityitem.setMotion(0,0,0);
      entityitem.setNoPickupDelay();
      world.spawnEntity(entityitem);
    }
  }

  public static void dropItems(@Nonnull World world, ItemStack[] inventory, int x, int y, int z, boolean doRandomSpread) {
    if (inventory == null) {
      return;
    }
    for (ItemStack stack : inventory) {
      if (!stack.isEmpty()) {
        dropItems(world, stack.copy(), x, y, z, doRandomSpread);
      }
    }
  }

  public static void dropItems(@Nonnull World world, @Nonnull IInventory inventory, int x, int y, int z, boolean doRandomSpread) {
    for (int l = 0; l < inventory.getSizeInventory(); ++l) {
      ItemStack items = inventory.getStackInSlot(l);

      if (!items.isEmpty()) {
        dropItems(world, items.copy(), x, y, z, doRandomSpread);
      }
    }
  }

  public static boolean dumpModObjects(@Nonnull File file) {

    StringBuilder sb = new StringBuilder();
    for (Object key : Block.REGISTRY.getKeys()) {
      if (key != null) {
        sb.append(key.toString());
        sb.append("\n");
      }
    }
    for (Object key : Item.REGISTRY.getKeys()) {
      if (key != null) {
        sb.append(key.toString());
        sb.append("\n");
      }
    }

    try {
      Files.write(sb, file, Charsets.UTF_8);
      return true;
    } catch (IOException e) {
      Log.warn("Error dumping ore dictionary entries: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  public static boolean dumpOreNames(@Nonnull File file) {

    try {
      String[] oreNames = OreDictionary.getOreNames();
      Files.write(Joiner.on("\n").join(oreNames), file, Charsets.UTF_8);
      return true;
    } catch (IOException e) {
      Log.warn("Error dumping ore dictionary entries: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  public static @Nonnull ItemStack decrStackSize(@Nonnull IInventory inventory, int slot, int size) {
    ItemStack item = inventory.getStackInSlot(slot);
    if (!item.isEmpty()) {
      if (item.getCount() <= size) {
        ItemStack result = item;
        inventory.setInventorySlotContents(slot, ItemStack.EMPTY);
        inventory.markDirty();
        return result;
      }
      ItemStack split = item.split(size);
      inventory.markDirty();
      return split;
    }
    return ItemStack.EMPTY;
  }

  public static @Nonnull Vec3d getEyePosition(@Nonnull PlayerEntity player) {
    double y = player.getPosY();
    y += player.getEyeHeight();
    return new Vec3d(player.getPosX(), y, player.getPosZ());
  }

  public static @Nonnull Vector3d getEyePositionEio(@Nonnull PlayerEntity player) {
    Vector3d res = new Vector3d(player.getPosX(), player.getPosY(), player.getPosZ());
    res.y += player.getEyeHeight();
    return res;
  }

  public static @Nonnull Vector3d getLookVecEio(@Nonnull PlayerEntity player) {
    Vec3d lv = player.getLookVec();
    return new Vector3d(lv.x, lv.y, lv.z);
  }

  // Code adapted from World.rayTraceBlocks to return all
  // collided blocks
  public static @Nonnull List<RayTraceResult> raytraceAll(@Nonnull World world, @Nonnull Vec3d startVector, @Nonnull Vec3d endVec, boolean includeLiquids) {
    boolean ignoreBlockWithoutBoundingBox = true;
    Vec3d startVec = startVector;

    List<RayTraceResult> result = new ArrayList<RayTraceResult>();

    if (!Double.isNaN(startVec.x) && !Double.isNaN(startVec.y) && !Double.isNaN(startVec.z)) {
      if (!Double.isNaN(endVec.x) && !Double.isNaN(endVec.y) && !Double.isNaN(endVec.z)) {
        int i = MathHelper.floor(endVec.x);
        int j = MathHelper.floor(endVec.y);
        int k = MathHelper.floor(endVec.z);
        int l = MathHelper.floor(startVec.x);
        int i1 = MathHelper.floor(startVec.y);
        int j1 = MathHelper.floor(startVec.z);
        BlockPos blockpos = new BlockPos(l, i1, j1);
        BlockState iblockstate = world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB)
            && block.canCollideCheck(iblockstate, includeLiquids)) {
          @Nonnull
          RayTraceResult raytraceresult = iblockstate.collisionRayTrace(world, blockpos, startVec, endVec);
          result.add(raytraceresult);
        }

        int k1 = 200;

        while (k1-- >= 0) {
          if (Double.isNaN(startVec.x) || Double.isNaN(startVec.y) || Double.isNaN(startVec.z)) {
            return new ArrayList<RayTraceResult>();
          }

          if (l == i && i1 == j && j1 == k) {
            return result;
          }

          boolean flag2 = true;
          boolean flag = true;
          boolean flag1 = true;
          double d0 = 999.0D;
          double d1 = 999.0D;
          double d2 = 999.0D;

          if (i > l) {
            d0 = l + 1.0D;
          } else if (i < l) {
            d0 = l + 0.0D;
          } else {
            flag2 = false;
          }

          if (j > i1) {
            d1 = i1 + 1.0D;
          } else if (j < i1) {
            d1 = i1 + 0.0D;
          } else {
            flag = false;
          }

          if (k > j1) {
            d2 = j1 + 1.0D;
          } else if (k < j1) {
            d2 = j1 + 0.0D;
          } else {
            flag1 = false;
          }

          double d3 = 999.0D;
          double d4 = 999.0D;
          double d5 = 999.0D;
          double d6 = endVec.x - startVec.x;
          double d7 = endVec.y - startVec.y;
          double d8 = endVec.z - startVec.z;

          if (flag2) {
            d3 = (d0 - startVec.x) / d6;
          }

          if (flag) {
            d4 = (d1 - startVec.y) / d7;
          }

          if (flag1) {
            d5 = (d2 - startVec.z) / d8;
          }

          if (d3 == -0.0D) {
            d3 = -1.0E-4D;
          }

          if (d4 == -0.0D) {
            d4 = -1.0E-4D;
          }

          if (d5 == -0.0D) {
            d5 = -1.0E-4D;
          }

          Direction enumfacing;

          if (d3 < d4 && d3 < d5) {
            enumfacing = i > l ? Direction.WEST : Direction.EAST;
            startVec = new Vec3d(d0, startVec.y + d7 * d3, startVec.z + d8 * d3);
          } else if (d4 < d5) {
            enumfacing = j > i1 ? Direction.DOWN : Direction.UP;
            startVec = new Vec3d(startVec.x + d6 * d4, d1, startVec.z + d8 * d4);
          } else {
            enumfacing = k > j1 ? Direction.NORTH : Direction.SOUTH;
            startVec = new Vec3d(startVec.x + d6 * d5, startVec.y + d7 * d5, d2);
          }

          l = MathHelper.floor(startVec.x) - (enumfacing == Direction.EAST ? 1 : 0);
          i1 = MathHelper.floor(startVec.y) - (enumfacing == Direction.UP ? 1 : 0);
          j1 = MathHelper.floor(startVec.z) - (enumfacing == Direction.SOUTH ? 1 : 0);
          blockpos = new BlockPos(l, i1, j1);
          BlockState iblockstate1 = world.getBlockState(blockpos);
          Block block1 = iblockstate1.getBlock();

          if (!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL
              || iblockstate1.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB) {
            if (block1.canCollideCheck(iblockstate1, includeLiquids)) {
              @Nonnull
              RayTraceResult raytraceresult1 = iblockstate1.collisionRayTrace(world, blockpos, startVec, endVec);
              result.add(raytraceresult1);
            }
          }
        }

        return result;
      } else {
        return result;
      }
    } else {
      return result;
    }
  }

  public static @Nullable Direction getDirFromOffset(int xOff, int yOff, int zOff) {
    if (xOff != 0 && yOff == 0 && zOff == 0) {
      return xOff < 0 ? Direction.WEST : Direction.EAST;
    }
    if (zOff != 0 && yOff == 0 && xOff == 0) {
      return zOff < 0 ? Direction.NORTH : Direction.SOUTH;
    }
    if (yOff != 0 && xOff == 0 && zOff == 0) {
      return yOff < 0 ? Direction.DOWN : Direction.UP;
    }
    return null;
  }

  public static @Nonnull Direction getFacingFromEntity(@Nonnull LivingEntity entity) {
    int heading = MathHelper.floor(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
    switch (heading) {
    case 0:
      return Direction.NORTH;
    case 1:
      return Direction.EAST;
    case 2:
      return Direction.SOUTH;
    case 3:
    default:
      return Direction.WEST;
    }

  }

  public static int getProgressScaled(int scale, @Nonnull IProgressTile tile) {
    return (int) (tile.getProgress() * scale);
  }

  public static void writeFacingToNBT(@Nonnull CompoundNBT nbtRoot, @Nonnull String name, @Nonnull Direction dir) {
    short val = -1;
    val = (short) dir.ordinal();
    nbtRoot.putShort(name, val);
  }

  public static @Nullable Direction readFacingFromNBT(@Nonnull CompoundNBT nbtRoot, @Nonnull String name) {
    short val = -1;
    if (nbtRoot.contains(name)) {
      val = nbtRoot.getShort(name);
    }
    if (val > 0) {
      return Direction.values()[val];
    }
    return null;
  }

}
