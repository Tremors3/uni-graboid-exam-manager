package myapp.mvc.model.entity.exam;

import java.io.Serializable;

/**
 * @brief Classe che rappresenta un esame semplice.
 *
 * La classe `SimpleExam` estende la classe astratta `Exam` e implementa le interfacce
 * `HasFinalGrade` e `HasUniqueId`. Rappresenta un esame con un voto finale e la possibilità di
 * ottenere la lode.
 */
public class SimpleExam extends Exam implements HasFinalGrade, HasUniqueId, Serializable {
    
    /** @brief Stato predefinito per la lode (false significa senza lode) */
    protected static final boolean DEFAULT_HONORS = false;
    
    /** @brief Voto finale dello studente */
    private int grade;
    
    /** @brief Lo studente ha ottenuto la lode? */
    private boolean honors;
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Costruttore di default.
     *
     * Inizializza un'istanza di `SimpleExam` con i valori predefiniti per il voto e la lode.
     */
    public SimpleExam() {
        super();
        this.setSimpleExam(DEFAULT_GRADE, DEFAULT_HONORS);
    }
    
    /**
     * @brief Costruttore con parametri.
     *
     * Inizializza un'istanza di `SimpleExam` con i dati forniti: nome, cognome, materia, crediti,
     * voto finale e stato della lode.
     *
     * @param username Nome dello studente.
     * @param surname Cognome dello studente.
     * @param teaching Materia d'esame.
     * @param credits Crediti dell'esame.
     * @param grade Voto finale dello studente.
     * @param honors Indica se lo studente ha ottenuto la lode.
     */
    public SimpleExam(String username, String surname, String teaching, int credits, int grade, boolean honors) {
        super(username, surname, teaching, credits);
        this.setSimpleExam(grade, honors);
    }
    
    /**
     * @brief Imposta il voto finale e lo stato della lode.
     *
     * Metodo privato per configurare il voto finale e se lo studente ha ottenuto la lode.
     *
     * @param grade Voto finale dello studente.
     * @param honors Indica se lo studente ha ottenuto la lode.
     */
    private void setSimpleExam(int grade, boolean honors) {
        this.setFinalGrade(grade);
        this.setHonors(honors);
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // Honors
    
    /**
     * @brief Imposta lo stato della lode.
     *
     * La lode può essere assegnata solo se il voto finale è il massimo (30). Se il voto non è il massimo,
     * la lode non può essere impostata.
     *
     * @param honors Indica se lo studente ha ottenuto la lode.
     */
    public void setHonors(boolean honors) {
        if (honors && getFinalGrade() != Exam.GRADE_BOUNDARIES[1]) {
            // La lode può essere assegnata solo se il voto è 30
            return;
        }
        this.honors = honors;
    }
    
    /**
     * @brief Restituisce lo stato della lode.
     *
     * @return boolean `true` se lo studente ha ottenuto la lode, altrimenti `false`.
     */
    public boolean getHonors() {
        return this.honors;
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // Grade
    
    /**
     * @brief Imposta il voto finale dell'esame.
     *
     * Il voto deve essere compreso tra i limiti definiti. Se il voto non è valido, viene impostato al valore di default.
     * Se il voto cambia e non è più il massimo (30), la lode viene revocata.
     *
     * @param grade Voto finale dello studente.
     */
    public void setFinalGrade(int grade) {
        this.grade = (grade >= GRADE_BOUNDARIES[0] && grade <= GRADE_BOUNDARIES[1]) ?
            grade : DEFAULT_GRADE;
        
        // Se il voto finale non è più 30, la lode viene revocata
        if (this.getHonors() && getFinalGrade() != Exam.GRADE_BOUNDARIES[1]) {
            this.setHonors(false);
        }
    }
    
    /**
     * @brief Restituisce il voto finale dell'esame.
     *
     * @return int Il voto finale dello studente.
     */
    @Override
    public int getFinalGrade() {
        return this.grade;
    }
}
