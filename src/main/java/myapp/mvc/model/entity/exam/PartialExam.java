package myapp.mvc.model.entity.exam;

import myapp.util.MutablePair;

import java.io.Serializable;
import java.util.UUID;

/**
 * @brief Classe che rappresenta un esame parziale.
 *
 * La classe `PartialExam` implementa l'interfaccia `HasUniqueId` e rappresenta un esame parziale
 * con un voto e un peso associati. Ogni esame parziale ha un ID univoco generato automaticamente.
 */
public class PartialExam implements HasUniqueId, Serializable {
    
    /** @brief ID univoco dell'esame parziale */
    private final String uniqueId = UUID.randomUUID().toString();
    
    /** @brief Coppia di valori: voto e peso */
    private final MutablePair<Integer, Integer> pair;
    
    /**
     * @brief Costruttore dell'esame parziale.
     *
     * Inizializza un'istanza di `PartialExam` con il voto e il peso specificati.
     *
     * @param grade Voto dell'esame parziale.
     * @param weight Peso dell'esame parziale.
     */
    public PartialExam(Integer grade, Integer weight) {
        this.pair = new MutablePair<>(grade, weight);
    }
    
    /**
     * @brief Restituisce l'ID univoco dell'esame parziale.
     *
     * @return String ID univoco dell'esame parziale.
     */
    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }
    
    /**
     * @brief Imposta sia il voto che il peso dell'esame parziale.
     *
     * Aggiorna il voto e il peso dell'esame parziale utilizzando i valori specificati.
     *
     * @param grade Nuovo voto dell'esame parziale.
     * @param weight Nuovo peso dell'esame parziale.
     */
    public void setGradeWeight(Integer grade, Integer weight) {
        this.pair.setValues(grade, weight);
    }
    
    /**
     * @brief Imposta il voto dell'esame parziale.
     *
     * Aggiorna solo il voto dell'esame parziale.
     *
     * @param grade Nuovo voto dell'esame parziale.
     */
    public void setGrade(Integer grade) {
        this.pair.setFirst(grade);
    }
    
    /**
     * @brief Restituisce il voto dell'esame parziale.
     *
     * @return Integer Voto dell'esame parziale.
     */
    public Integer getGrade() {
        return this.pair.getFirst();
    }
    
    /**
     * @brief Imposta il peso dell'esame parziale.
     *
     * Aggiorna solo il peso dell'esame parziale.
     *
     * @param weight Nuovo peso dell'esame parziale.
     */
    public void setWeight(Integer weight) {
        this.pair.setSecond(weight);
    }
    
    /**
     * @brief Restituisce il peso dell'esame parziale.
     *
     * @return Integer Peso dell'esame parziale.
     */
    public Integer getWeight() {
        return this.pair.getSecond();
    }
}
