package myapp.mvc.model.manager;

import myapp.mvc.model.Database;
import myapp.mvc.model.entity.path.UniquePath;

import java.util.Iterator;
import java.util.List;

/**
 * @brief Classe di controllo per la gestione dei percorsi unici.
 *
 * La classe `PathManager` gestisce le operazioni sui percorsi unici, inclusi aggiunta,
 * modifica, eliminazione e recupero dei percorsi. Inoltre, gestisce la serializzazione e
 * deserializzazione dei percorsi da e verso file.
 */
public class PathManager {
    
    /** @brief Database per memorizzare i percorsi unici */
    private final Database<UniquePath> database = new Database<>(".paths");
    
    /** @brief ID univoco del percorso attualmente selezionato */
    private String selectedUniqueID = null;
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Restituisce l'ID univoco del percorso attualmente selezionato.
     *
     * @return ID univoco del percorso selezionato.
     */
    public String getSelectedUniqueID() {
        return this.selectedUniqueID;
    }
    
    /**
     * @brief Deseleziona l'ID univoco del percorso attualmente selezionato.
     */
    public void delSelectedUniqueID() {
        this.selectedUniqueID = null;
    }
    
    /**
     * @brief Imposta l'ID univoco del percorso selezionato, se esiste nel database.
     *
     * @param selectedUniqueID ID univoco del percorso da selezionare.
     */
    public void setSelectedUniqueID(String selectedUniqueID) {
        if (selectedUniqueID == null) return;
        
        boolean setted = false;
        
        Iterator<UniquePath> it = this.getAllPaths().listIterator();
        while (!setted && it.hasNext()) {
            if (selectedUniqueID.equals(it.next().getUniqueId())) {
                this.selectedUniqueID = selectedUniqueID;
                setted = true;
            }
        }
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Aggiunge un nuovo percorso al database.
     *
     * @param path Percorso da aggiungere.
     * @return ID del percorso aggiunto.
     */
    public String addPath(String path) {
        return database.add(new UniquePath(path));
    }
    
    /**
     * @brief Recupera un percorso dal database utilizzando l'ID univoco.
     *
     * @param uniqueId ID univoco del percorso da recuperare.
     * @return Il percorso corrispondente all'ID specificato.
     */
    public UniquePath getPath(String uniqueId) {
        return database.get(uniqueId);
    }
    
    /**
     * @brief Recupera tutti i percorsi dal database.
     *
     * @return Lista di tutti i percorsi presenti nel database.
     */
    public List<UniquePath> getAllPaths() {
        return database.getAll();
    }
    
    /**
     * @brief Elimina un percorso dal database.
     *
     * Se il percorso da eliminare è anche quello selezionato, viene deselezionato.
     *
     * @param uniqueId ID univoco del percorso da eliminare.
     */
    public void delPath(String uniqueId) {
        if (uniqueId == null) return;
        
        // Se il percorso da eliminare è quello selezionato, deselezionalo
        if (this.getSelectedUniqueID() != null && uniqueId.equals(getPath(this.getSelectedUniqueID()).getUniqueId())) {
            this.delSelectedUniqueID();
        }
        
        database.del(uniqueId);
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Salva tutti i percorsi nel file predefinito.
     */
    public synchronized void savePathsToFile() {
        database.saveToFile();
    }
    
    /**
     * @brief Salva tutti i percorsi nel file specificato.
     *
     * @param path Percorso del file in cui salvare i percorsi.
     */
    public synchronized void savePathsToFile(String path) {
        if (path == null) return;
        database.saveToFile(path);
    }
}
