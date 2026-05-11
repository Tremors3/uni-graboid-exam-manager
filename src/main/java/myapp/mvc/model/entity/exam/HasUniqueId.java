package myapp.mvc.model.entity.exam;

/**
 * @brief Interfaccia funzionale per ottenere un identificativo univoco.
 *
 * Questa interfaccia definisce un metodo per ottenere un identificativo univoco
 * per un oggetto. È annotata come interfaccia funzionale, quindi può essere utilizzata
 * come espressione lambda o riferimento a un metodo.
 */
@FunctionalInterface
public interface HasUniqueId {
    
    /**
     * @brief Metodo per ottenere l'identificativo univoco.
     *
     * Questo metodo deve essere implementato da classi che necessitano di un
     * identificativo univoco per rappresentare i propri oggetti.
     *
     * @return String Identificativo univoco dell'oggetto.
     */
    String getUniqueId();
}
