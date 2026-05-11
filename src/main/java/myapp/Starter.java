package myapp;

import myapp.mvc.controller.MainPane;

/**
 * @brief Classe di avvio dell'applicazione.
 *
 * Questa classe contiene il metodo `main` che avvia l'applicazione JavaFX.
 */
public class Starter {
    
    /**
     * @brief Punto di ingresso principale dell'applicazione.
     *
     * Questo metodo avvia l'applicazione JavaFX in un thread separato.
     *
     * @param args Argomenti della riga di comando passati all'applicazione.
     */
    public static void main(String[] args) {
        // Crea e avvia un nuovo thread per l'applicazione JavaFX
        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(MainPane.class, args);
            }
        }.start();
    }
}
