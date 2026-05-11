package myapp.mvc.model.entity.path;

import myapp.mvc.model.entity.exam.HasUniqueId;

import java.io.Serializable;
import java.util.UUID;

/**
 * @brief Classe che rappresenta un percorso unico.
 *
 * La classe `UniquePath` implementa l'interfaccia `HasUniqueId` e rappresenta un percorso
 * con un identificativo unico generato automaticamente. Include anche il percorso stesso.
 */
public class UniquePath implements HasUniqueId, Serializable {
    
    /** @brief ID univoco del percorso */
    private final String uniqueID = UUID.randomUUID().toString();
    
    /** @brief Percorso associato */
    private String path;
    
    /**
     * @brief Costruttore della classe `UniquePath`.
     *
     * Inizializza un'istanza di `UniquePath` con il percorso specificato.
     *
     * @param path Percorso da associare all'istanza.
     */
    public UniquePath(String path) {
        this.setPath(path);
    }
    
    /**
     * @brief Restituisce il percorso associato.
     *
     * @return String Percorso associato all'istanza.
     */
    public String getPath() {
        return path;
    }
    
    /**
     * @brief Imposta il percorso associato.
     *
     * Metodo privato per impostare il percorso dell'istanza.
     *
     * @param path Nuovo percorso da associare all'istanza.
     */
    private void setPath(String path) {
        this.path = path;
    }
    
    /**
     * @brief Restituisce l'ID univoco del percorso.
     *
     * @return String ID univoco del percorso.
     */
    @Override
    public String getUniqueId() {
        return this.uniqueID;
    }
}
