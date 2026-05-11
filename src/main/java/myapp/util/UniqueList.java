package myapp.util;

import myapp.mvc.model.entity.exam.HasUniqueId;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @brief Lista di oggetti unici basata su un identificatore unico.
 *
 * Questa classe estende {@link ArrayList} per gestire una lista di oggetti
 * che implementano l'interfaccia {@link HasUniqueId}. Fornisce metodi per
 * aggiungere, rimuovere e recuperare oggetti dalla lista basandosi su un
 * identificatore unico.
 *
 * @param <T> Tipo degli oggetti nella lista, che deve implementare {@link HasUniqueId}.
 */
public class UniqueList<T extends HasUniqueId> extends ArrayList<T> implements Serializable {
    
    /**
     * @brief Costruttore della lista unica.
     *
     * Crea una nuova istanza di {@link UniqueList}.
     */
    public UniqueList() {
        super();
    }
    
    /**
     * @brief Aggiunge un nuovo record alla lista e restituisce il suo ID univoco.
     *
     * @param record Istanza del record da inserire nella lista.
     * @return L'ID univoco del record aggiunto.
     */
    public String addUnique(T record) {
        this.add(record);
        return record.getUniqueId();
    }
    
    /**
     * @brief Cancella un record dalla lista fornendo il suo ID univoco.
     *
     * @param uniqueId L'ID univoco del record da cancellare.
     * @return true se il record è stato cancellato, false altrimenti.
     */
    public boolean delUnique(String uniqueId) {
        return this.removeIf(record -> record.getUniqueId().equals(uniqueId));
    }
    
    /**
     * @brief Cancella un record dalla lista.
     *
     * @param record Istanza del record da cancellare.
     * @return true se il record è stato cancellato, false altrimenti.
     */
    public boolean delUnique(T record) {
        return this.remove(record);
    }
    
    /**
     * @brief Restituisce l'elemento associato all'ID specificato.
     *
     * @param uniqueId L'ID univoco del record da cercare.
     * @return Il record se trovato, null se non trovato.
     */
    public T getUnique(String uniqueId) {
        for (T record : this)
            if (record.getUniqueId().equals(uniqueId))
                return record;
        return null;
    }
}
