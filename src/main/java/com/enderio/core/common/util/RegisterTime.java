//package com.enderio.core.common.util;
//
//
//public enum RegisterTime {
//  PREINIT(FMLPreInitializationEvent.class),
//  INIT(FMLInitializationEvent.class),
//  POSTINIT(FMLPostInitializationEvent.class);
//
//  private Class<? extends FMLStateEvent> clazz;
//
//  private RegisterTime(Class<? extends FMLStateEvent> clazz) {
//    this.clazz = clazz;
//  }
//
//  public static RegisterTime timeFor(FMLStateEvent event) {
//    for (RegisterTime time : values()) {
//      if (time.clazz == event.getClass()) {
//        return time;
//      }
//    }
//    return null;
//  }
//}
