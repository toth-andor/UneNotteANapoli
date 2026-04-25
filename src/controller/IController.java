package controller;

/**
 * A Controller interfész, amely meghatározza a külső felületek
 * (View/Proto) számára elérhető metódusokat.
 */
public interface IController {
    
    /**
     * A külső felületekről (View/Proto) érkező üzenetek fogadása.
     * 
     * @param msg a feldolgozandó üzenet
     */
    void receive(Message msg);
    
    /**
     * Konfigurációs fájl betöltése és feldolgozása.
     * 
     * @param cfg a konfigurációs fájl tartalma
     */
    void loadConfig(String cfg);
}