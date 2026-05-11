package myapp.mvc.view.pane;

import javafx.stage.Stage;
import myapp.mvc.model.entity.exam.Exam;

import java.util.function.Function;

/**
 * @brief Interfaccia per le operazioni del menu superiore.
 *
 * Questa interfaccia definisce i metodi per gestire le operazioni del menu superiore,
 * come la gestione della sensibilità al caso, il salvataggio dei dati, la chiusura
 * dell'applicazione, la stampa delle tabelle e la visualizzazione di grafici.
 */
public interface TopMenuInterface {
    
    /**
     * @brief Imposta la sensibilità al caso per le operazioni di ricerca.
     *
     * Questo metodo abilita o disabilita la ricerca sensibile al caso basata sul valore
     * del parametro booleano fornito.
     *
     * @param check Se true, la ricerca sarà sensibile al caso; se false, sarà insensibile.
     */
    void setCaseSensitive(boolean check);
    
    /**
     * @brief Salva i dati correnti.
     *
     * Questo metodo salva i dati correnti. La modalità di salvataggio deve essere
     * implementata nella classe che utilizza questa interfaccia.
     */
    void Save();
    
    /**
     * @brief Chiude l'applicazione.
     *
     * Questo metodo chiude l'applicazione. La logica di chiusura deve essere
     * implementata nella classe che utilizza questa interfaccia.
     */
    void Close();
    
    /**
     * @brief Salva i dati e chiude l'applicazione.
     *
     * Questo metodo salva i dati correnti e poi chiude l'applicazione.
     */
    void SaveAndClose();
    
    /**
     * @brief Stampa le tabelle visualizzate.
     *
     * Questo metodo gestisce la stampa delle tabelle visualizzate nella finestra principale.
     *
     * @param primaryStage La finestra principale dell'applicazione, necessaria per
     * gestire la stampa.
     */
    void printTables(Stage primaryStage);
    
    /**
     * @brief Visualizza un grafico.
     *
     * Questo metodo visualizza un grafico basato sui dati disponibili. La modalità di
     * visualizzazione del grafico deve essere implementata nella classe che utilizza
     * questa interfaccia.
     */
    void showGraph();
    
    /**
     * @brief Configura il grafico da visualizzare.
     *
     * Questo metodo configura il grafico che verrà visualizzato, utilizzando una funzione
     * per ottenere una chiave unica per ciascun esame e un titolo per il grafico.
     *
     * @param getKey Funzione che estrae una chiave unica da un oggetto di tipo Exam.
     * @param title Titolo del grafico da visualizzare.
     */
    void setupGraph(Function<Exam, String> getKey, String title);
}
