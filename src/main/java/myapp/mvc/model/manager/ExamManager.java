package myapp.mvc.model.manager;

import myapp.mvc.model.Database;
import myapp.mvc.model.entity.exam.ComposedExam;
import myapp.mvc.model.entity.exam.Exam;
import myapp.mvc.model.entity.exam.SimpleExam;

import java.util.List;

/**
 * @brief Classe di controllo per la gestione degli esami.
 *
 * La classe `ControllerExams` gestisce le operazioni sugli esami, inclusi aggiunta, modifica,
 * eliminazione e recupero di esami, sia semplici che composti. Inoltre, gestisce la
 * serializzazione e deserializzazione degli esami da e verso file.
 */
public class ExamManager {
    
    /** @brief Database per memorizzare gli esami */
    private final Database<Exam> database = new Database<>();
    
    /** @brief Flag per indicare se i dati sono stati modificati */
    private boolean modFlag = false;
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Aggiunge un esame semplice al database.
     *
     * @param username Nome dello studente.
     * @param surname Cognome dello studente.
     * @param teaching Materia dell'esame.
     * @param credits Crediti dell'esame.
     * @param grade Voto finale dell'esame.
     * @param honors Lo studente ha ottenuto la lode?
     * @return ID dell'esame aggiunto.
     */
    public String addSimpleExam(String username, String surname, String teaching, int credits, int grade, boolean honors) {
        return database.add(new SimpleExam(username, surname, teaching, credits, grade, honors));
    }
    
    /**
     * @brief Aggiunge un esame composto al database.
     *
     * @param username Nome dello studente.
     * @param surname Cognome dello studente.
     * @param teaching Materia dell'esame.
     * @param credits Crediti dell'esame.
     * @return ID dell'esame aggiunto.
     */
    public String addComposedExam(String username, String surname, String teaching, int credits) {
        return database.add(new ComposedExam(username, surname, teaching, credits));
    }
    
    /**
     * @brief Modifica un esame semplice esistente.
     *
     * @param uniqueId ID univoco dell'esame da modificare.
     * @param username Nuovo nome dello studente.
     * @param surname Nuovo cognome dello studente.
     * @param teaching Nuova materia dell'esame.
     * @param credits Nuovi crediti dell'esame.
     * @param grade Nuovo voto finale dell'esame.
     * @param honors Nuova lode per lo studente.
     */
    public void modSimpleExam(String uniqueId, String username, String surname, String teaching, int credits, int grade, boolean honors) {
        if (database.get(uniqueId) instanceof SimpleExam sExam) {
            sExam.setUsername(username);
            sExam.setSurname(surname);
            sExam.setTeaching(teaching);
            sExam.setCredits(credits);
            sExam.setFinalGrade(grade);
            sExam.setHonors(honors);
        }
    }
    
    /**
     * @brief Modifica un esame composto esistente.
     *
     * @param uniqueId ID univoco dell'esame da modificare.
     * @param username Nuovo nome dello studente.
     * @param surname Nuovo cognome dello studente.
     * @param teaching Nuova materia dell'esame.
     * @param credits Nuovi crediti dell'esame.
     */
    public void modComposedExam(String uniqueId, String username, String surname, String teaching, int credits) {
        if (database.get(uniqueId) instanceof ComposedExam cExam) {
            cExam.setUsername(username);
            cExam.setSurname(surname);
            cExam.setTeaching(teaching);
            cExam.setCredits(credits);
        }
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Recupera un esame dal database.
     *
     * @param uniqueId ID univoco dell'esame da recuperare.
     * @return L'esame corrispondente all'ID specificato.
     */
    public Exam getExam(String uniqueId) {
        return database.get(uniqueId);
    }
    
    /**
     * @brief Recupera tutti gli esami dal database.
     *
     * @return Lista di tutti gli esami presenti nel database.
     */
    public List<Exam> getAllExams() {
        return database.getAll();
    }
    
    /**
     * @brief Elimina un esame dal database.
     *
     * @param uniqueId ID univoco dell'esame da eliminare.
     */
    public void delExam(String uniqueId) {
        database.del(uniqueId);
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Aggiunge un esame parziale a un esame composto.
     *
     * @param examUniqueId ID univoco dell'esame composto.
     * @param grade Voto dell'esame parziale.
     * @param weight Peso dell'esame parziale.
     * @return ID dell'esame parziale aggiunto, o `null` se l'aggiunta fallisce.
     */
    public String addIntermediateGrade(String examUniqueId, int grade, int weight) {
        if (database.get(examUniqueId) instanceof ComposedExam cExam) {
            return cExam.addPartialExam(grade, weight);
        }
        return null;
    }
    
    /**
     * @brief Elimina un esame parziale da un esame composto.
     *
     * @param examUniqueId ID univoco dell'esame composto.
     * @param gradeUniqueId ID univoco dell'esame parziale da eliminare.
     */
    public void delIntermediateGrade(String examUniqueId, String gradeUniqueId) {
        if (database.get(examUniqueId) instanceof ComposedExam cExam) {
            cExam.delPartialExam(gradeUniqueId);
        }
    }
    
    /**
     * @brief Modifica un esame parziale in un esame composto.
     *
     * @param examUniqueId ID univoco dell'esame composto.
     * @param gradeUniqueId ID univoco dell'esame parziale da modificare.
     * @param grade Nuovo voto dell'esame parziale.
     * @param weight Nuovo peso dell'esame parziale.
     */
    public void modIntermediateGrade(String examUniqueId, String gradeUniqueId, int grade, int weight) {
        if (database.get(examUniqueId) instanceof ComposedExam cExam) {
            cExam.modPartialExam(gradeUniqueId, grade, weight);
        }
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Salva tutti gli esami nel file predefinito.
     */
    public synchronized void saveExamsToFile() {
        database.saveToFile();
    }
    
    /**
     * @brief Salva tutti gli esami nel file specificato.
     *
     * @param path Percorso del file in cui salvare gli esami.
     */
    public synchronized void saveExamsToFile(String path) {
        database.saveToFile(path);
    }
    
    /**
     * @brief Carica gli esami dal file predefinito.
     */
    public synchronized void loadExamsFromFile() {
        database.loadFromFile();
    }
    
    /**
     * @brief Carica gli esami dal file specificato.
     *
     * @param path Percorso del file da cui caricare gli esami.
     */
    public synchronized void loadExamsFromFile(String path) {
        database.loadFromFile(path);
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Imposta il flag di modifica.
     *
     * @param isModified Valore del flag di modifica (`true` se modificato, `false` altrimenti).
     */
    public void setModFlag(boolean isModified) {
        this.modFlag = isModified;
    }
    
    /**
     * @brief Restituisce il valore del flag di modifica.
     *
     * @return `true` se i dati sono stati modificati, `false` altrimenti.
     */
    public boolean getModFlag() {
        return this.modFlag;
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Confronta un file con il database attuale per verificare eventuali differenze.
     *
     * @param path Percorso del file da confrontare con il database.
     * @return `true` se il file è uguale al database, `false` altrimenti.
     */
    public boolean compareFileToDatabase(String path) {
        return this.database.compareFileToDatabase(path);
    }
}
