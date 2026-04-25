package controller;

import Vehicle.Bus;
import Vehicle.Cleaner;

 public sealed interface PlayerType {
     record PCleaner(Cleaner cleaner) implements PlayerType {}
     record PBusDriver(Bus bus) implements PlayerType {}
 }
