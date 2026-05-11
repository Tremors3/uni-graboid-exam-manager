package myapp.mvc.model.manager;

import myapp.mvc.model.entity.path.UniquePath;

import java.util.List;

/**
 * @brief Interfaccia per la gestione dei percorsi.
 *
 * Questa interfaccia definisce i metodi per aggiungere, ottenere, eliminare percorsi e gestire la selezione dei percorsi.
 * Include anche metodi per salvare i percorsi e aggiornare la visualizzazione della tabella.
 */
public interface ManagePaths {
    
    /**
     * @brief Aggiunge un nuovo percorso.
     *
     * @param path Percorso da aggiungere.
     * @return ID del percorso aggiunto.
     */
    String addPath(String path);
    
    /**
     * @brief Recupera un percorso utilizzando il suo ID univoco.
     *
     * @param uniqueId ID univoco del percorso.
     * @return Il percorso corrispondente all'ID specificato.
     */
    UniquePath getPath(String uniqueId);
    
    /**
     * @brief Recupera tutti i percorsi.
     *
     * @return Lista di tutti i percorsi.
     */
    List<UniquePath> getAllPaths();
    
    /**
     * @brief Elimina un percorso utilizzando il suo ID univoco.
     *
     * @param uniqueId ID univoco del percorso da eliminare.
     */
    void delPath(String uniqueId);
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Aggiorna la visualizzazione della tabella dei percorsi.
     */
    void updateTheTable();
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Salva i percorsi nel file predefinito.
     */
    void savePaths();
    
    /**
     * @brief Salva i percorsi in un percorso specificato.
     *
     * @param path Percorso del file in cui salvare i percorsi.
     */
    void savePaths(String path);
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Elimina il percorso selezionato.
     */
    void delSelected();
    
    /**
     * @brief Recupera l'ID del percorso selezionato.
     *
     * @return ID del percorso selezionato.
     */
    String getSelected();
    
    /**
     * @brief Imposta il percorso selezionato.
     *
     * @param uniqueId ID del percorso da selezionare.
     */
    void setSelected(String uniqueId);
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Mostra il dialogo per la gestione dei percorsi.
     */
    void displayManagePathDialog();
}
