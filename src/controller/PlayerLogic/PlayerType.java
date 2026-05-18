package controller.PlayerLogic;

import vehicle.Bus;
import vehicle.Cleaner;

 public sealed interface PlayerType {
     record PCleaner(Cleaner cleaner) implements PlayerType {}
     record PBusDriver(Bus bus) implements PlayerType {}
 }
