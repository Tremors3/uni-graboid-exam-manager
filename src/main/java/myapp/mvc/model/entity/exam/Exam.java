package myapp.mvc.model.entity.exam;

import java.io.Serializable;
import java.util.UUID;

/**
 * @brief Classe astratta che rappresenta un esame.
 *
 * Questa classe definisce le proprietà generali di un esame, inclusi nome dello studente,
 * materia, crediti, e un identificativo univoco. Fornisce metodi per accedere e modificare
 * queste proprietà e impone la presenza di un metodo astratto per ottenere il voto finale.
 */
public abstract class Exam implements HasFinalGrade, HasUniqueId, Serializable {
    
    /** @brief Username predefinito per lo studente. */
    protected static final String DEFAULT_USER = "Unnamed";
    
    /** @brief Materia predefinita dell'esame. */
    protected static final String DEFAULT_TEACHING = "Undefined";
    
    /** @brief Numero di crediti predefiniti per l'esame. */
    public static final int DEFAULT_CREDITS = 6;
    
    /** @brief Voto minimo predefinito per l'esame. */
    public static final int DEFAULT_GRADE = 18;
    
    /** @brief Limiti per il voto valido. */
    public static final int[] GRADE_BOUNDARIES = {18, 30};
    
    /** @brief Limiti per i crediti validi. */
    public static final int[] CREDITS_BOUNDARIES = {0, 12};
    
    /** @brief Username dello studente. */
    private String username;
    
    /** @brief Cognome dello studente. */
    private String surname;
    
    /** @brief Materia dell'esame. */
    private String teaching;
    
    /** @brief Numero di crediti dell'esame. */
    private int credits;
    
    /** @brief Identificativo univoco dell'esame. */
    private String uniqueId;
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Costruttore di default.
     *
     * Inizializza un esame con valori predefiniti.
     */
    public Exam() {
        this(Exam.DEFAULT_USER, Exam.DEFAULT_USER, Exam.DEFAULT_TEACHING, Exam.DEFAULT_CREDITS);
    }
    
    /**
     * @brief Costruttore con parametri.
     *
     * Inizializza un esame con i valori forniti.
     *
     * @param username Username dello studente.
     * @param surname Cognome dello studente.
     * @param teaching Materia dell'esame.
     * @param credits Numero di crediti dell'esame.
     */
    public Exam(String username, String surname, String teaching, int credits) {
        this.setUniqueId();
        this.setUsername(username);
        this.setSurname(surname);
        this.setTeaching(teaching);
        this.setCredits(credits);
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // Unique ID
    
    /**
     * @brief Imposta automaticamente un ID univoco per l'esame.
     *
     * Usa UUID per generare un identificativo univoco.
     */
    private void setUniqueId() {
        this.uniqueId = UUID.randomUUID().toString();
    }
    
    /**
     * @brief Ritorna l'ID univoco dell'esame.
     *
     * @return String ID univoco dell'esame.
     */
    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // Username
    
    /**
     * @brief Imposta lo username dello studente.
     *
     * Se lo username è nullo, viene impostato il valore predefinito.
     *
     * @param username Username dello studente.
     */
    public void setUsername(String username) {
        this.username = (username != null) ? username : Exam.DEFAULT_USER;
    }
    
    /**
     * @brief Ritorna lo username dello studente.
     *
     * @return String Username dello studente.
     */
    public String getUsername() {
        return this.username;
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // Surname
    
    /**
     * @brief Imposta il cognome dello studente.
     *
     * Se il cognome è nullo, viene impostato il valore predefinito.
     *
     * @param surname Cognome dello studente.
     */
    public void setSurname(String surname) {
        this.surname = (surname != null) ? surname : Exam.DEFAULT_USER;
    }
    
    /**
     * @brief Ritorna il cognome dello studente.
     *
     * @return String Cognome dello studente.
     */
    public String getSurname() {
        return this.surname;
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // Teaching (Materia)
    
    /**
     * @brief Imposta la materia dell'esame.
     *
     * Se la materia è nulla, viene impostato il valore predefinito.
     *
     * @param teaching Materia dell'esame.
     */
    public void setTeaching(String teaching) {
        this.teaching = (teaching != null) ? teaching : Exam.DEFAULT_TEACHING;
    }
    
    /**
     * @brief Ritorna la materia dell'esame.
     *
     * @return String Materia dell'esame.
     */
    public String getTeaching() {
        return this.teaching;
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // Credits (Crediti)
    
    /**
     * @brief Imposta il numero di crediti dell'esame.
     *
     * Il numero di crediti deve essere compreso nei limiti definiti da {@link Exam#CREDITS_BOUNDARIES}.
     * Se non lo è, viene impostato il valore predefinito.
     *
     * @param credits Numero di crediti dell'esame.
     */
    public void setCredits(int credits) {
        this.credits = (credits >= Exam.CREDITS_BOUNDARIES[0] && credits <= Exam.CREDITS_BOUNDARIES[1]) ? credits : Exam.DEFAULT_CREDITS;
    }
    
    /**
     * @brief Ritorna il numero di crediti dell'esame.
     *
     * @return int Numero di crediti dell'esame.
     */
    public int getCredits() {
        return this.credits;
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // Grade (Voto)
    
    /**
     * @brief Metodo astratto che deve essere implementato per ottenere il voto finale.
     *
     * @return int Il voto finale dell'esame.
     */
    @Override
    public abstract int getFinalGrade();
}
