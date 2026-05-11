package myapp.mvc.model.entity.exam;

/**
 * @brief Interfaccia funzionale per ottenere il voto finale.
 *
 * Questa interfaccia definisce un metodo per ottenere il voto finale di un esame.
 * È annotata come interfaccia funzionale, il che significa che può essere utilizzata
 * come espressione lambda o riferimento a un metodo.
 */
@FunctionalInterface
public interface HasFinalGrade {
    
    /**
     * @brief Metodo per ottenere il voto finale.
     *
     * Questo metodo deve essere implementato da qualsiasi classe che rappresenti un esame
     * o una valutazione, restituendo il voto finale.
     *
     * @return int Il voto finale dell'esame.
     */
    int getFinalGrade();
}
