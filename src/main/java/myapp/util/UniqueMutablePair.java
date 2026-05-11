package myapp.util;

import myapp.mvc.model.entity.exam.HasUniqueId;

import java.io.Serializable;
import java.util.UUID;

/**
 * @brief Classe che rappresenta una coppia mutabile con un identificatore unico.
 *
 * Questa classe estende {@link MutablePair} e implementa {@link HasUniqueId}.
 * Fornisce una coppia di oggetti mutabili e un identificatore unico per ogni
 * istanza della coppia.
 *
 * @param <F> Tipo del primo elemento della coppia.
 * @param <S> Tipo del secondo elemento della coppia.
 */
public class UniqueMutablePair<F, S> extends MutablePair<F, S> implements HasUniqueId, Serializable {
    
    /** @brief L'identificatore unico per questa istanza. */
    private final String uniqueId = UUID.randomUUID().toString();
    
    /**
     * Costruttore della coppia unica.
     *
     * @param first Il primo elemento della coppia.
     * @param second Il secondo elemento della coppia.
     */
    public UniqueMutablePair(F first, S second) {
        super(first, second);
    }
    
    /**
     * Restituisce l'ID unico di questa coppia.
     *
     * @return L'ID unico della coppia.
     */
    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }
}
