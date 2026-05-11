package myapp.mvc.model.entity.exam;

import myapp.util.UniqueList;

import java.io.Serializable;
import java.util.List;

/**
 * @brief Classe che rappresenta un esame composto da esami parziali.
 *
 * La classe `ComposedExam` estende la classe astratta `Exam` e implementa le interfacce
 * `HasFinalGrade` e `HasUniqueId`. Rappresenta un esame composto che aggrega esami parziali
 * con pesi specifici per calcolare il voto finale.
 */
public class ComposedExam extends Exam implements HasFinalGrade, HasUniqueId, Serializable {
    
    /** @brief Peso predefinito degli esami parziali */
    protected static final int DEFAULT_WEIGHT = 50;
    
    /** @brief Limiti per il peso degli esami parziali */
    public static final int[] WEIGHT_BOUNDARIES = {1, 100};
    
    /** @brief Lista degli esami parziali, garantita unica per ID */
    private final UniqueList<PartialExam> partials = new UniqueList<>();
    
    /**
     * @brief Costruttore di default.
     *
     * Inizializza un'istanza di `ComposedExam` con i valori predefiniti.
     */
    public ComposedExam() {
        super();
    }
    
    /**
     * @brief Costruttore con parametri.
     *
     * Inizializza un'istanza di `ComposedExam` con i dati forniti: nome, cognome, materia e crediti.
     *
     * @param username Nome dello studente.
     * @param surname Cognome dello studente.
     * @param teaching Materia dell'esame.
     * @param credits Crediti dell'esame.
     */
    public ComposedExam(String username, String surname, String teaching, int credits) {
        super(username, surname, teaching, credits);
    }
    
    /**
     * @brief Restituisce il voto finale dell'esame composto.
     *
     * Calcola la media ponderata dei voti degli esami parziali, utilizzando i pesi associati.
     *
     * @return int La media ponderata dei voti parziali.
     */
    @Override
    public int getFinalGrade() {
        int numerator = 0;
        int denominator = 0;
        for (PartialExam pe : this.partials) {
            numerator += pe.getGrade() * pe.getWeight();
            denominator += pe.getWeight();
        }
        return (denominator == 0) ? DEFAULT_GRADE : numerator / denominator;
    }
    
    /**
     * @brief Aggiunge un esame parziale all'esame composto.
     *
     * Aggiunge un'istanza di `PartialExam` alla lista di esami parziali, se il peso non eccede il limite.
     *
     * @param exam Istanza di `PartialExam` da aggiungere.
     * @return String ID dell'esame parziale aggiunto, oppure `null` se il peso eccede il limite.
     */
    public String addPartialExam(PartialExam exam) {
        if (this.checkIfWeightExceed(exam.getWeight())) return null;
        return this.partials.addUnique(exam);
    }
    
    /**
     * @brief Aggiunge un nuovo esame parziale specificando voto e peso.
     *
     * Crea un'istanza di `PartialExam` con i valori specificati e la aggiunge alla lista di esami parziali,
     * se il peso non eccede il limite.
     *
     * @param grade Voto dell'esame parziale.
     * @param weight Peso dell'esame parziale.
     * @return String ID dell'esame parziale aggiunto, oppure `null` se il peso eccede il limite.
     */
    public String addPartialExam(int grade, int weight) {
        if (this.checkIfWeightExceed(weight)) return null;
        return this.addPartialExam(new PartialExam(
                (grade >= GRADE_BOUNDARIES[0] && grade <= GRADE_BOUNDARIES[1]) ? grade : DEFAULT_GRADE,
                (weight >= WEIGHT_BOUNDARIES[0] && weight <= WEIGHT_BOUNDARIES[1]) ? weight : DEFAULT_WEIGHT
            )
        );
    }
    
    /**
     * @brief Rimuove un esame parziale dalla lista.
     *
     * Rimuove un'istanza di `PartialExam` dalla lista di esami parziali.
     *
     * @param exam Istanza di `PartialExam` da rimuovere.
     * @return boolean `true` se l'esame è stato rimosso, `false` altrimenti.
     */
    public boolean delPartialExam(PartialExam exam) {
        return this.partials.remove(exam);
    }
    
    /**
     * @brief Rimuove un esame parziale tramite ID.
     *
     * Rimuove un esame parziale dalla lista utilizzando il suo ID univoco.
     *
     * @param uniqueId ID univoco dell'esame parziale da rimuovere.
     * @return boolean `true` se l'esame è stato rimosso, `false` altrimenti.
     */
    public boolean delPartialExam(String uniqueId) {
        return this.delPartialExam(this.getPartialExam(uniqueId));
    }
    
    /**
     * @brief Modifica un esame parziale esistente.
     *
     * Modifica il voto e il peso di un esame parziale esistente, identificato dal suo ID.
     * Se il peso totale supera il limite massimo, l'operazione viene annullata.
     *
     * @param uniqueId ID univoco dell'esame parziale da modificare.
     * @param grade Nuovo voto dell'esame parziale.
     * @param weight Nuovo peso dell'esame parziale.
     */
    public void modPartialExam(String uniqueId, int grade, int weight) {
        PartialExam partialExam = this.getPartialExam(uniqueId);
        if (partialExam != null && !this.checkIfWeightExceed(weight - partialExam.getWeight())) {
            partialExam.setGradeWeight(grade, weight);
        }
    }
    
    /**
     * @brief Modifica un esame parziale specificato.
     *
     * Modifica il voto e il peso di un esame parziale specificato.
     * Se il peso totale supera il limite massimo, l'operazione viene annullata.
     *
     * @param exam Istanza di `PartialExam` da modificare.
     * @param grade Nuovo voto dell'esame parziale.
     * @param weight Nuovo peso dell'esame parziale.
     */
    public void modPartialExam(PartialExam exam, int grade, int weight) {
        if (!this.checkIfWeightExceed(weight - exam.getWeight())) {
            exam.setGradeWeight(grade, weight);
        }
    }
    
    /**
     * @brief Restituisce l'esame parziale corrispondente all'ID specificato.
     *
     * Recupera un'istanza di `PartialExam` dalla lista in base al suo ID univoco.
     *
     * @param uniqueId ID univoco dell'esame parziale.
     * @return PartialExam Istanza dell'esame parziale se trovato, `null` altrimenti.
     */
    public PartialExam getPartialExam(String uniqueId) {
        return this.partials.getUnique(uniqueId);
    }
    
    /**
     * @brief Restituisce la lista degli esami parziali.
     *
     * Recupera una lista contenente tutti gli esami parziali.
     *
     * @return List<PartialExam> Lista degli esami parziali.
     */
    public List<PartialExam> getPartialExams() {
        return this.partials.stream().toList();
    }
    
    /**
     * @brief Restituisce la somma dei pesi degli esami parziali.
     *
     * Calcola la somma dei pesi degli esami parziali presenti nella lista.
     *
     * @return int Somma dei pesi degli esami parziali.
     */
    public int getSumOfCurrentWeights() {
        return partials.stream().mapToInt(PartialExam::getWeight).sum();
    }
    
    /**
     * @brief Verifica se il peso totale eccede il limite massimo.
     *
     * Controlla se l'aggiunta di un nuovo peso supera il limite massimo consentito.
     *
     * @param new_weight Peso dell'esame parziale da aggiungere.
     * @return boolean `true` se il peso complessivo supera il limite, `false` altrimenti.
     */
    public boolean checkIfWeightExceed(int new_weight) {
        return (this.getSumOfCurrentWeights() + new_weight > WEIGHT_BOUNDARIES[1]);
    }
}
