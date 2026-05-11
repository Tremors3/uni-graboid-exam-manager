package myapp.mvc.model.manager;

import myapp.mvc.model.entity.exam.Exam;

import java.util.List;

/**
 * @brief Interfaccia per la gestione degli esami.
 *
 * Questa interfaccia definisce i metodi per aggiungere, modificare, eliminare e gestire esami e voti intermedi.
 * Include anche metodi per salvare e caricare esami da e verso file, e per confrontare i file con il database.
 */
public interface ManageExams {
    
    /**
     * @brief Aggiunge un esame semplice.
     *
     * @param username Nome dell'utente.
     * @param surname Cognome dell'utente.
     * @param teaching Materia dell'esame.
     * @param credits Crediti dell'esame.
     * @param grade Voto dell'esame.
     * @param honors Indica se l'esame è stato superato con lode.
     * @return ID dell'esame aggiunto.
     */
    String addSimpleExam(String username, String surname, String teaching, int credits, int grade, boolean honors);
    
    /**
     * @brief Aggiunge un esame composto.
     *
     * @param username Nome dell'utente.
     * @param surname Cognome dell'utente.
     * @param teaching Materia dell'esame.
     * @param credits Crediti dell'esame.
     * @return ID dell'esame aggiunto.
     */
    String addComposedExam(String username, String surname, String teaching, int credits);
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Modifica un esame semplice.
     *
     * @param uniqueId ID univoco dell'esame.
     * @param username Nome dell'utente.
     * @param surname Cognome dell'utente.
     * @param teaching Materia dell'esame.
     * @param credits Crediti dell'esame.
     * @param grade Voto dell'esame.
     * @param honors Indica se l'esame è stato superato con lode.
     */
    void modSimpleExam(String uniqueId, String username, String surname, String teaching, int credits, int grade, boolean honors);
    
    /**
     * @brief Modifica un esame composto.
     *
     * @param uniqueId ID univoco dell'esame.
     * @param username Nome dell'utente.
     * @param surname Cognome dell'utente.
     * @param teaching Materia dell'esame.
     * @param credits Crediti dell'esame.
     */
    void modComposedExam(String uniqueId, String username, String surname, String teaching, int credits);
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Aggiunge un voto intermedio a un esame composto.
     *
     * @param examUniqueId ID univoco dell'esame composto.
     * @param grade Voto dell'esame parziale.
     * @param weight Peso dell'esame parziale.
     * @return ID dell'esame parziale aggiunto.
     */
    String addIntermediateGrade(String examUniqueId, int grade, int weight);
    
    /**
     * @brief Elimina un voto intermedio da un esame composto.
     *
     * @param examUniqueId ID univoco dell'esame composto.
     * @param gradeUniqueId ID univoco del voto intermedio da eliminare.
     */
    void delIntermediateGrade(String examUniqueId, String gradeUniqueId);
    
    /**
     * @brief Modifica un voto intermedio di un esame composto.
     *
     * @param examUniqueId ID univoco dell'esame composto.
     * @param gradeUniqueId ID univoco del voto intermedio da modificare.
     * @param grade Nuovo voto.
     * @param weight Nuovo peso.
     */
    void modIntermediateGrade(String examUniqueId, String gradeUniqueId, int grade, int weight);
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Recupera un esame utilizzando il suo ID univoco.
     *
     * @param uniqueId ID univoco dell'esame.
     * @return L'esame corrispondente all'ID specificato.
     */
    Exam getExam(String uniqueId);
    
    /**
     * @brief Recupera tutti gli esami.
     *
     * @return Lista di tutti gli esami.
     */
    List<Exam> getAllExams();
    
    /**
     * @brief Elimina un esame utilizzando il suo ID univoco.
     *
     * @param uniqueId ID univoco dell'esame da eliminare.
     */
    void delExam(String uniqueId);
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Salva gli esami nel file predefinito.
     */
    void saveExams();
    
    /**
     * @brief Salva gli esami in un percorso specificato.
     *
     * @param path Percorso del file in cui salvare gli esami.
     */
    void saveExams(String path);
    
    /**
     * @brief Salva gli esami nel file selezionato.
     */
    void saveExamsToSelectedFile();
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Carica gli esami dal file predefinito.
     */
    void loadExams();
    
    /**
     * @brief Carica gli esami da un percorso specificato.
     *
     * @param path Percorso del file da cui caricare gli esami.
     */
    void loadExams(String path);
    
    /**
     * @brief Carica gli esami dal file selezionato.
     */
    void loadExamsFromSelectedFile();
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Apre gli esami dal file.
     *
     * @return ID del file degli esami aperto.
     */
    String openExams();
    
    /**
     * @brief Apre gli esami dal file e imposta l'esame selezionato.
     */
    void openExamsAndSetSelected();
    
    /**
     * @brief Esporta gli esami.
     */
    void exportExams();
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Aggiorna il flag di modifica.
     */
    void updModFlag();
    
    /**
     * @brief Restituisce il valore del flag di modifica.
     *
     * @return `true` se sono state effettuate modifiche, `false` altrimenti.
     */
    boolean getModFlag();
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Confronta un file con il database degli esami.
     *
     * @param path Percorso del file da confrontare.
     * @return `true` se il file e il database sono uguali, `false` altrimenti.
     */
    boolean compareFileToDatabase(String path);
}
