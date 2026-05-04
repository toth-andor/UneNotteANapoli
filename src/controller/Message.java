package controller;

import map.Lane;

/**
 * A View/Proto és a Controller közötti kommunikációért felelős algebrai adattípus.
 * Java sealed interface és record típusok segítségével biztosítja a típusbiztos
 * üzenetkezelést és a kimerítő mintaillesztést.
 */
public sealed interface Message {

    // --- Setup üzenetek ---

    /** Új játékos regisztrálása (pl. role: "bus", "cleaner") **/
    record AddCleaner(String name) implements Message {}
    record AddBusDriver(String name) implements Message {}
    record AddNPCCar(int count) implements Message {}

    /** Meghatározott számú csomópont hozzáadása a térképhez **/
    record AddJunction(int count) implements Message {}

    /** Út hozzáadása két létező csomópont azonosítója között **/
    record AddRoad(String j1, String j2) implements Message {}

    record SaveMap() implements Message {}

    /** A világ felépítésének lezárása és a játék indítása **/
    record StartGame() implements Message {}

    // --- Játékos és jármű akciók ---

    /** Új hókotró vásárlása a takarító játékos számára (AwaitingPurchaseState) **/
    record BuySnowPlow() implements Message {}

    /** Új fej vásárlása az aktuálisan irányított hókotróhoz (SnowPlowActionState) **/
    record BuyAttachment(AttachmentType type) implements Message {}

    /** Az aktuális hókotró aktív fejének cseréje (SnowPlowActionState) **/
    record SwapAttachment(AttachmentType type) implements Message {}

    /** Az aktuális hókotró aktív fejének újratöltése (SnowPlowActionState) **/
    record RefillAttachment() implements Message {}

    /** Mozgás a cél sávra. Ez az akció minden esetben lezárja a jármű körét. **/
    record PickLane(Lane lane) implements Message {}

    // --- Rendszer és általános üzenetek ---

    /** Játékállapot mentése **/
    record SaveGame(String path) implements Message {}

    /** Mentett állapot betöltése **/
    record LoadGame(String path) implements Message {}

    /** Kilépés a programból **/
    record ExitGame() implements Message {}

    record RandomOn() implements Message {}

    record RandomOff(int seed) implements Message {}

    /** Súgó kérése az adott témához **/
    record RequestHelp(String topic) implements Message {}
}
