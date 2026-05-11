package myapp.mvc.controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import myapp.autosaver.Autosaver;
import myapp.autosaver.ManageAutosaver;
import myapp.mvc.model.entity.exam.ComposedExam;
import myapp.mvc.model.entity.exam.Exam;
import myapp.mvc.model.entity.path.UniquePath;
import myapp.mvc.model.manager.ExamManager;
import myapp.mvc.model.manager.ManageExams;
import myapp.mvc.model.manager.ManagePaths;
import myapp.mvc.model.manager.PathManager;
import myapp.mvc.view.dialog.GraphDialog.GraphDialog;
import myapp.mvc.view.dialog.ManagePathsDialog.ManagePathsDialog;
import myapp.mvc.view.dialog.SaveOnExitDialog;
import myapp.mvc.view.pane.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @brief Classe principale per la gestione dell'interfaccia utente dell'applicazione.
 * Estende la classe {@link Application} di JavaFX.
 */
public class MainPane extends Application {
    
    // ------------ Finestra principale
    /** @brief La finestra principale dell'applicazione JavaFX. */
    private static Stage window;

    // ------------ Layouts principali
    /** @brief Layout principale della finestra. */
    VBox windowLayout = new VBox();
    /** @brief Menu nella parte superiore della finestra. */
    TopMenu topLayout = new TopMenu();
    /** @brief Layout centrale della finestra. */
    CenterPane centerLayout = new CenterPane();
    /** @brief Layout nella parte inferiore della finestra. */
    BottomPane bottomLayout = new BottomPane();

    // ------------ Controller
    /** @brief Controllore per la gestione degli esami. */
    private static final ExamManager examManager = new ExamManager();
    /** @brief Controllore per la gestione dei percorsi. */
    private static final PathManager pathManager = new PathManager();

    // ------------ Interfacce
    /** @brief Interfaccia per la gestione degli esami. */
    private final ManageExams manageExams = getManageExamsInterface();
    /** @brief Interfaccia per la gestione dei percorsi. */
    private final ManagePaths managePaths = getManagePathsInterface();

    // ------------ Thread di autosalvataggio
    /** @brief Thread di autosalvataggio per il salvataggio automatico dei dati. */
    private static final Autosaver saver = new Autosaver();

    // ------------ Dimensioni predefinite della finestra
    /** @brief Larghezza predefinita della finestra. */
    private static final double DEFAULT_WIDTH = 1100;
    /** @brief Altezza estesa della finestra. */
    private static final double EXTENDED_HEIGHT = 480;
    /** @brief Altezza predefinita della finestra. */
    private static final double DEFAULT_HEIGHT = 355;
    
    /**
     * @brief Metodo che avvia l'applicazione JavaFX.
     *
     * Questo metodo viene chiamato all'avvio dell'applicazione JavaFX. Imposta il principale
     * Stage fornito da JavaFX e avvia i thread necessari, configurando successivamente
     * la finestra e i componenti dell'applicazione.
     *
     * @param primaryStage Il principale Stage fornito da JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        
        // Avvia i thread necessari e configura la finestra e i componenti.
        this.startThreads();
        this.setupFrame();
        this.setupComponents();
    }
    
    /**
     * @brief Avvia il thread di autosalvataggio.
     *
     * Questo metodo configura e avvia un thread dedicato all'autosalvataggio dei dati.
     * Il thread esegue il salvataggio dei dati delle esami su file a intervalli regolari.
     */
    private void startThreads() {
        saver.setAutosaverJob(() -> {
            System.out.print("Salvataggio... "); // DEBUG
            synchronized (examManager) {
                examManager.saveExamsToFile();
            }
            System.out.println("Salvataggio Avvenuto!"); //DEBUG
        });
        
        if (!saver.isAlive()) saver.start();
    }
    
    /**
     * @brief Configura il frame principale della finestra.
     *
     * Questo metodo imposta le opzioni di configurazione per la finestra principale dell'applicazione.
     * Include la gestione della chiusura della finestra, l'impostazione del titolo, delle dimensioni
     * e della scena.
     */
    private void setupFrame() {
        // Gestione chiusura finestra
        window.setOnCloseRequest(e -> {
            e.consume();
            closeRequestHandler();
        });
        
        // Imposta opzioni finestra
        window.setTitle("Graboid Manager");
        window.setMinWidth(DEFAULT_WIDTH);
        window.setMinHeight(EXTENDED_HEIGHT);
        window.setWidth(DEFAULT_WIDTH);
        window.setHeight(EXTENDED_HEIGHT);
        window.setResizable(true);
        
        // Imposta scena
        window.setScene(new Scene(windowLayout, DEFAULT_WIDTH, DEFAULT_HEIGHT));
        window.show();
    }
    
    /**
     * @brief Gestisce la richiesta di chiusura della finestra.
     *
     * Questo metodo viene chiamato quando l'utente tenta di chiudere la finestra. Verifica se i dati
     * sono stati salvati e, se necessario, mostra un dialogo per confermare se si desidera salvare
     * prima di uscire. A seconda della risposta dell'utente, può salvare i dati e chiudere l'applicazione.
     */
    private void closeRequestHandler() {
        // Chiudi subito se il file selezionato è salvato
        if (!manageExams.getModFlag()) {
            CloseProgram();
            return;
        }
        
        // Mostra il dialogo di conferma salvataggio
        SaveOnExitDialog.VALUES result = SaveOnExitDialog.display(
            "Exiting the program", "Do you want to save before exiting?"
        );
        if (result != SaveOnExitDialog.VALUES.CANCEL) {
            if (result == SaveOnExitDialog.VALUES.SAVE_AND_EXIT)
                SaveProgram();
            CloseProgram();
        }
    }
    
    /**
     * @brief Salva lo stato corrente dei dati presenti in tabella.
     *
     * Questo metodo esegue il salvataggio dello stato corrente dei dati delle esami. Viene
     * chiamato quando l'utente sceglie di salvare i dati prima di chiudere l'applicazione.
     */
    private void SaveProgram() {
        System.out.println("Saving Application!"); // DEBUG
        manageExams.saveExamsToSelectedFile();
    }
    
    /**
     * @brief Chiude il programma e termina i thread attivi.
     *
     * Questo metodo chiude l'applicazione e ferma eventuali thread di autosalvataggio attivi.
     * Interrompe il thread di autosalvataggio se è in esecuzione e chiude la finestra principale
     * dell'applicazione.
     */
    private void CloseProgram() {
        System.out.println("Terminating Threads!"); // DEBUG
        if (saver.isAlive()) {
            saver.myStop();     // Ferma il thread se in attesa
            saver.interrupt();  // Ferma il thread se in pausa
        }
        System.out.println("Closing Application!"); // DEBUG
        window.close();
    }
    
    /**
     * @brief Configura i componenti dell'interfaccia utente.
     *
     * Questo metodo imposta i vari layout, interfacce e operazioni per la finestra principale dell'applicazione.
     */
    private void setupComponents() {
        
        // -------------------------------------------------------------------------------------------------------------

        windowLayout.getChildren().addAll(topLayout, centerLayout, bottomLayout);
        
        // -------------------------------------------------------------------------------------------------------------
        
        // Interfacce di pannelli specifici
        
        // Configura l'interfaccia utilizzata dal pannello superiore
        topLayout.setTopMenuInterface(new TopMenuInterface() {
            
            /**
             * @brief Imposta la sensibilità al caso nella vista centrale.
             *
             * Questa funzione aggiorna la sensibilità al caso della vista centrale basata sul parametro fornito.
             *
             * @param check Il flag che indica se la sensibilità al caso deve essere abilitata.
             */
            @Override
            public void setCaseSensitive(boolean check) {
                centerLayout.setCaseSensitivity(check);
            }
            
            /**
             * @brief Salva lo stato corrente dell'applicazione.
             *
             * Questa funzione richiama il metodo {@link SaveProgram} per salvare i dati dell'applicazione.
             */
            @Override
            public void Save() {
                SaveProgram();
            }
            
            /**
             * @brief Chiude l'applicazione.
             *
             * Questa funzione richiama il metodo {@link CloseProgram} per chiudere l'applicazione.
             */
            @Override
            public void Close() {
                CloseProgram();
            }
            
            /**
             * @brief Salva e poi chiude l'applicazione.
             *
             * Questa funzione prima richiama il metodo {@link SaveProgram} per salvare i dati dell'applicazione
             * e successivamente chiama il metodo {@link CloseProgram} per chiudere l'applicazione.
             */
            @Override
            public void SaveAndClose() {
                SaveProgram();
                CloseProgram();
            }
            
            /**
             * @brief Funzione per stampare le tabelle attive.
             *
             * Questa funzione raccoglie le tabelle da stampare, le scala per adattarle alla pagina e poi le stampa.
             *
             * @param primaryStage La finestra principale utilizzata per mostrare la finestra di dialogo di stampa.
             */
            @Override
            public void printTables(Stage primaryStage) { // , TableView<?> table

                List<TableView<?>> tablesToPrint = new ArrayList<>();
                
                ObservableList<TableView<?>> allTables = FXCollections.observableArrayList(
                    centerLayout.getTableToPrint(), bottomLayout.getTableToPrint()
                );
                
                for (TableView<?> table : allTables) {
                    if (!table.getItems().isEmpty()) {
                        tablesToPrint.add(table);
                    }
                }
                
                PrinterJob job = PrinterJob.createPrinterJob();
                
                if (job != null) {
                    
                    boolean showDialog = job.showPrintDialog(primaryStage); // Mostra la finestra di dialogo di stampa
                    
                    if (showDialog) {
                        
                        // Scala le tabelle per la stampa
                        for (TableView<?> table : tablesToPrint) {
                            
                            double scaleX = job.getJobSettings().getPageLayout().getPrintableWidth() / table.getWidth();
                            double scaleY = job.getJobSettings().getPageLayout().getPrintableHeight() / table.getHeight();
                            double scale = Math.min(scaleX, scaleY);
                            
                            table.getTransforms().add(new Scale(scale, scale));
                        }
                        
                        boolean success = true;
                        for (TableView<?> table : tablesToPrint) {
                            success = success && job.printPage(table);
                        }
                        
                        if (success) {
                            job.endJob();
                        }
                        
                        // Rimuove la scala dopo la stampa
                        for (TableView<?> table : tablesToPrint) {
                            table.getTransforms().clear();
                        }
                    }
                }
            }
            
            /**
             * @brief Mostra un grafico basato sui dati degli esami.
             *
             * Questa funzione mostra un grafico che rappresenta i voti degli studenti o delle materie a seconda del filtro selezionato.
             */
            @Override
            public void showGraph() {
                
                Boolean is_filter_set_to_teaching = centerLayout.getSelectedFilterOption().equals(
                    centerLayout.getFilterOptions()[2]
                );
                
                String title = is_filter_set_to_teaching
                    ? "Istogramma voti delle Materie"
                    : "Istogramma voti degli Studenti";
                
                setupGraph(e -> is_filter_set_to_teaching
                    ? e.getTeaching()
                    : e.getUsername() + " " + e.getSurname()
                    , title);
            }
            
            /**
             * @brief Configura e mostra un grafico basato sui dati degli esami.
             *
             * Questo metodo calcola la distribuzione dei voti degli esami e mostra un grafico in base ai dati raccolti.
             *
             * @param getKey Funzione per ottenere la chiave di raggruppamento per il grafico (nome dell'insegnante o dello studente).
             * @param title Titolo del grafico da mostrare.
             */
            @Override
            public void setupGraph(Function<Exam, String> getKey, String title) {
                
                // Calcolo la cardinalità dei voti
                int arraySize = Exam.GRADE_BOUNDARIES[1] - Exam.GRADE_BOUNDARIES[0] + 1;
                
                // Mappa che contiene il numero di voti da 1 a 18 di ciascuno studente selezionato.
                Map<String, int[]> studentGrades = new HashMap<>();
                
                // Iterazione di tutti gli esami filtrati in tabella
                for (Exam exam : centerLayout.getFilteredExams()) {
                    
                    int offset = exam.getFinalGrade() - Exam.GRADE_BOUNDARIES[0];
                    
                    // Gli studenti sono identificati in modo univoco dal Surname e dal Username
                    String keyValue = getKey.apply(exam);

                    // Per lo studente corrente una voce viene inizializzata se non lo è già
                    studentGrades.putIfAbsent(keyValue, new int[arraySize]);
                    
                    // Il contatore dello studente corrente del voto corrente viene incrementato di 1
                    studentGrades.get(keyValue)[offset] = studentGrades.get(keyValue)[offset] + 1;
                }
                
                // Mostra la finestra che contiene il grafico
                GraphDialog.display(title, studentGrades, arraySize);
            }
        });
        // Configura l'interfaccia utilizzata dal pannello centrale
        centerLayout.setPassPartialExamInfo(new CenterPaneInterface() {
            /**
             * @brief Passa un esame parziale e aggiorna la visualizzazione.
             *
             * Questa funzione aggiorna la tabella degli esami parziali utilizzando l'ID dell'esame fornito
             * e mostra il pannello inferiore della vista. Utilizza {@link updatePartialExamTable} per
             * aggiornare i dati.
             *
             * @param uniqueExamId L'ID univoco dell'esame parziale.
             */
            @Override
            public void passPartialExam(String uniqueExamId) {
                updatePartialExamTable(uniqueExamId);
                bottomLayout.show();
            }
            
            /**
             * @brief Nasconde il pannello degli esami parziali.
             *
             * Questa funzione nasconde il pannello inferiore della vista utilizzando {@link bottomLayout#hide()}.
             */
            @Override
            public void hidePartialExam() {
                bottomLayout.hide();
            }
        });bottomLayout.hide();
        
        // Interfacce di scopi specifici
        
        // Configura l'interfaccia utilizzata dal pannello superiore per interagire con l'autosaver
        topLayout.setManageAutosaver(new ManageAutosaver() {
            @Override
            public void startAutosaver() {
                if (!saver.isAlive()) saver.start();
            }
            
            @Override
            public void stopAutosaver() {
                if (saver.isAlive()) saver.myStop();
            }
            
            @Override
            public void resumeAutosaver() {
                if (saver.isAlive()) saver.myResume();
            }
            
            @Override
            public void pauseAutosaver() {
                if (saver.isAlive()) saver.myPause();
            }
        });
        // Configura l'interfaccia utilizzata dal pannello superiore per gestire i percorsi
        topLayout.setManagePaths(managePaths);
        // Configura l'interfaccia utilizzata dal pannello superiore per gestire gli esami
        topLayout.setManageExams(manageExams);
        // Configura l'interfaccia utilizzata dal pannello centrale per gestire i percorsi
        centerLayout.setManagePaths(managePaths);
        // Configura l'interfaccia utilizzata dal pannello centrale per gestire gli esami
        centerLayout.setManageExams(manageExams);
        // Configura l'interfaccia utilizzata dal pannello inferiore per gestire gli esami
        bottomLayout.setManageExams(manageExams);
        
        // -------------------------------------------------------------------------------------------------------------

        // Imposta il flag di modifica
        centerLayout.setModifiedFlag();
        
        //  Aggiorna la tabella degli esami
        this.updateExamTable();
    }
    
    /**
     * @brief Restituisce l'interfaccia per la gestione degli esami.
     *
     * Questo metodo crea e restituisce un'istanza dell'interfaccia {@link ManageExams},
     * implementando i metodi per gestire l'aggiunta, modifica, eliminazione e salvataggio degli esami.
     *
     * @return Un'istanza dell'interfaccia {@link ManageExams}.
     */
    private ManageExams getManageExamsInterface() {
        return new ManageExams() {
            
            /**
             * @brief Aggiunge un esame semplice.
             *
             * Questo metodo aggiunge un esame semplice e aggiorna la tabella degli esami e il flag di modifica.
             *
             * @param username Il nome dell'utente che ha effettuato l'esame.
             * @param surname Il cognome dell'utente che ha effettuato l'esame.
             * @param teaching Il nome dell'insegnante.
             * @param credits I crediti dell'esame.
             * @param grade Il voto dell'esame.
             * @param honors Indica se l'esame ha ottenuto lode.
             * @return L'ID univoco dell'esame aggiunto.
             */
            @Override
            public String addSimpleExam(String username, String surname, String teaching, int credits, int grade, boolean honors) {
                String uniqueId = MainPane.examManager.addSimpleExam(username, surname, teaching, credits, grade, honors);
                updateExamTable();
                updModFlag();
                return uniqueId;
            }
            
            /**
             * @brief Aggiunge un esame composto.
             *
             * Questo metodo aggiunge un esame composto e aggiorna la tabella degli esami e il flag di modifica.
             *
             * @param username Il nome dell'utente che ha effettuato l'esame.
             * @param surname Il cognome dell'utente che ha effettuato l'esame.
             * @param teaching Il nome dell'insegnante.
             * @param credits I crediti dell'esame.
             * @return L'ID univoco dell'esame aggiunto.
             */
            @Override
            public String addComposedExam(String username, String surname, String teaching, int credits) {
                String uniqueId = MainPane.examManager.addComposedExam(username, surname, teaching, credits);
                updateExamTable();
                updModFlag();
                return uniqueId;
            }
            
            /**
             * @brief Modifica un esame semplice esistente.
             *
             * Questo metodo aggiorna le informazioni di un esame semplice e poi aggiorna la tabella degli esami e il flag di modifica.
             *
             * @param uniqueId L'ID univoco dell'esame da modificare.
             * @param username Il nome dell'utente che ha effettuato l'esame.
             * @param surname Il cognome dell'utente che ha effettuato l'esame.
             * @param teaching Il nome dell'insegnante.
             * @param credits I crediti dell'esame.
             * @param grade Il voto dell'esame.
             * @param honors Indica se l'esame ha ottenuto lode.
             */
            @Override
            public void modSimpleExam(String uniqueId, String username, String surname, String teaching, int credits, int grade, boolean honors) {
                MainPane.examManager.modSimpleExam(uniqueId, username, surname, teaching, credits, grade, honors);
                updateExamTable();
                updModFlag();
            }
            
            /**
             * @brief Modifica un esame composto esistente.
             *
             * Questo metodo aggiorna le informazioni di un esame composto e poi aggiorna la tabella degli esami e il flag di modifica.
             *
             * @param uniqueId L'ID univoco dell'esame da modificare.
             * @param username Il nome dell'utente che ha effettuato l'esame.
             * @param surname Il cognome dell'utente che ha effettuato l'esame.
             * @param teaching Il nome dell'insegnante.
             * @param credits I crediti dell'esame.
             */
            @Override
            public void modComposedExam(String uniqueId, String username, String surname, String teaching, int credits) {
                MainPane.examManager.modComposedExam(uniqueId, username, surname, teaching, credits);
                updateExamTable();
                updModFlag();
            }
            
            /**
             * @brief Aggiunge un voto intermedio a un esame.
             *
             * Questo metodo aggiunge un voto intermedio a un esame e aggiorna la tabella degli esami parziali, la tabella degli esami e il flag di modifica.
             *
             * @param examUniqueId L'ID univoco dell'esame a cui aggiungere il voto intermedio.
             * @param grade Il voto intermedio.
             * @param weight Il peso del voto intermedio.
             * @return L'ID univoco del voto intermedio aggiunto.
             */
            @Override
            public String addIntermediateGrade(String examUniqueId, int grade, int weight) {
                String uniqueId = MainPane.examManager.addIntermediateGrade(examUniqueId, grade, weight);
                updatePartialExamTable(examUniqueId);
                updateExamTable();
                updModFlag();
                return uniqueId;
            }
            
            /**
             * @brief Elimina un voto intermedio da un esame.
             *
             * Questo metodo elimina un voto intermedio e, se necessario, elimina anche l'esame composto associato.
             *
             * @param examUniqueId L'ID univoco dell'esame da cui eliminare il voto intermedio.
             * @param gradeUniqueId L'ID univoco del voto intermedio da eliminare.
             */
            @Override
            public void delIntermediateGrade(String examUniqueId, String gradeUniqueId) {
                MainPane.examManager.delIntermediateGrade(examUniqueId, gradeUniqueId);
                
                // Controlla se l'esame parziale eliminato era l'ultimo. Se sì, elimina anche l'esame composto associato.
                if (MainPane.examManager.getExam(examUniqueId) instanceof ComposedExam ce && (ce.getPartialExams().isEmpty() || ce.getPartialExams().size() < 2)) {
                    delExam(examUniqueId);
                }

                updatePartialExamTable(examUniqueId);
                updateExamTable();
                updModFlag();
            }
            
            /**
             * @brief Modifica un voto intermedio esistente.
             *
             * Questo metodo aggiorna un voto intermedio e poi aggiorna la tabella degli esami parziali, la tabella degli esami e il flag di modifica.
             *
             * @param examUniqueId L'ID univoco dell'esame a cui appartiene il voto intermedio.
             * @param gradeUniqueId L'ID univoco del voto intermedio da modificare.
             * @param grade Il nuovo voto intermedio.
             * @param weight Il nuovo peso del voto intermedio.
             */
            @Override
            public void modIntermediateGrade(String examUniqueId, String gradeUniqueId, int grade, int weight) {
                MainPane.examManager.modIntermediateGrade(examUniqueId, gradeUniqueId, grade, weight);
                updatePartialExamTable(examUniqueId);
                updateExamTable();
                updModFlag();
            }
            
            /**
             * @brief Restituisce l'esame con l'ID specificato.
             *
             * @param uniqueId L'ID univoco dell'esame.
             * @return L'oggetto {@link Exam} corrispondente all'ID fornito.
             */
            @Override
            public Exam getExam(String uniqueId) {
                return MainPane.examManager.getExam(uniqueId);
            }
            
            /**
             * @brief Restituisce tutti gli esami.
             *
             * @return Una lista di tutti gli oggetti {@link Exam}.
             */
            @Override
            public List<Exam> getAllExams() {
                return MainPane.examManager.getAllExams();
            }
            
            /**
             * @brief Elimina un esame.
             *
             * Questo metodo elimina un esame e aggiorna la tabella degli esami e il flag di modifica.
             *
             * @param uniqueId L'ID univoco dell'esame da eliminare.
             */
            @Override
            public void delExam(String uniqueId) {
                MainPane.examManager.delExam(uniqueId);
                updateExamTable();
                updModFlag();
            }
            
            /**
             * @brief Salva gli esami nel file predefinito.
             *
             * Questo metodo salva gli esami nel file predefinito e aggiorna il flag di modifica.
             */
            @Override
            public void saveExams() {
                MainPane.examManager.saveExamsToFile();
                updModFlag();
            }
            
            /**
             * @brief Salva gli esami in un percorso specificato.
             *
             * Questo metodo salva gli esami in un file specificato dal percorso e aggiorna il flag di modifica.
             *
             * @param path Il percorso del file dove salvare gli esami.
             */
            @Override
            public void saveExams(String path) {
                MainPane.examManager.saveExamsToFile(path);
                updModFlag();
            }
            
            /**
             * @brief Salva gli esami nel file selezionato.
             *
             * Questo metodo salva gli esami nel file selezionato tramite l'interfaccia di gestione dei percorsi e aggiorna il flag di modifica.
             */
            @Override
            public void saveExamsToSelectedFile() {
                UniquePath selectedPath = managePaths.getPath(managePaths.getSelected());
                if (selectedPath != null) {
                    manageExams.saveExams(selectedPath.getPath());
                }
            }
            
            /**
             * @brief Carica gli esami dal file predefinito.
             *
             * Questo metodo carica gli esami dal file predefinito e aggiorna la tabella degli esami e il flag di modifica.
             */
            @Override
            public void loadExams() {
                MainPane.examManager.loadExamsFromFile();
                updateExamTable();
                updModFlag();
            }
            
            /**
             * @brief Carica gli esami da un percorso specificato.
             *
             * Questo metodo carica gli esami da un file specificato dal percorso e aggiorna la tabella degli esami e il flag di modifica.
             *
             * @param path Il percorso del file da cui caricare gli esami.
             */
            @Override
            public void loadExams(String path) {
                MainPane.examManager.loadExamsFromFile(path);
                updateExamTable();
                updModFlag();
            }
            
            /**
             * @brief Carica gli esami dal file selezionato.
             *
             * Questo metodo carica gli esami dal file selezionato tramite l'interfaccia di gestione dei percorsi e aggiorna il flag di modifica.
             */
            @Override
            public void loadExamsFromSelectedFile() {
                UniquePath selectedPath = managePaths.getPath(managePaths.getSelected());
                if (selectedPath != null) {
                    manageExams.loadExams(selectedPath.getPath());
                }
                updModFlag();
            }
            
            /**
             * @brief Apre un file di esami e lo aggiunge ai percorsi gestiti.
             *
             * Questo metodo mostra una finestra di dialogo per selezionare un file di esami e aggiunge il percorso ai percorsi gestiti.
             *
             * @return L'ID univoco del percorso aggiunto, oppure {@code null} se nessun file è stato selezionato.
             */
            @Override
            public String openExams() {
                FileChooser fileChooser = new FileChooser();

                File projectDir = new File(System.getProperty("user.dir"));
                if (projectDir.exists())
                    fileChooser.setInitialDirectory(projectDir);

                fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("select your data file (*.data)", "*.data")
                );
                
                // Ottiene il percorso del file
                File uPath = fileChooser.showOpenDialog(new Stage());
                
                // Aggiunge il percorso
                if (uPath != null)
                    return managePaths.addPath(uPath.getAbsolutePath());
                return null;
            }
            
            /**
             * @brief Apre un file di esami e lo imposta come selezionato.
             *
             * Questo metodo apre un file di esami utilizzando l'interfaccia di gestione degli esami e imposta il percorso come selezionato.
             */
            @Override
            public void openExamsAndSetSelected() {
                String uniqueId = manageExams.openExams();
                if (uniqueId != null)
                    managePaths.setSelected(uniqueId);
                updModFlag();
            }
            
            /**
             * @brief Esporta gli esami in un file selezionato.
             *
             * Questo metodo mostra una finestra di dialogo per selezionare un file e salva gli esami nel file selezionato.
             */
            @Override
            public void exportExams() {

                FileChooser fileChooser = new FileChooser();

                File projectDir = new File(TopMenu.DEFAULT_EXPORT_DIRECTORY);
                if (projectDir.exists())
                    fileChooser.setInitialDirectory(projectDir);

                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Graboid Manager Files", "*.data"));

                File file = fileChooser.showSaveDialog(new Stage());

                if (file != null) {
                    manageExams.saveExams(file.getAbsolutePath());
                }
            }
            
            /**
             * @brief Aggiorna il flag di modifica.
             *
             * Questo metodo aggiorna il flag di modifica in base alla comparazione tra il file e il database.
             */
            @Override
            public void updModFlag() {
                if (null == managePaths.getSelected()) return;
                examManager.setModFlag(!compareFileToDatabase(managePaths.getPath(managePaths.getSelected()).getPath()));
                updateModifiedFlag();
            }
            
            /**
             * @brief Restituisce il flag di modifica.
             *
             * @return {@code true} se ci sono modifiche non salvate, altrimenti {@code false}.
             */
            @Override
            public boolean getModFlag() {
                return examManager.getModFlag();
            }
            
            /**
             * @brief Confronta il file con il database.
             *
             * Questo metodo confronta il contenuto del file con il database per determinare se ci sono modifiche.
             *
             * @param path Il percorso del file da confrontare.
             * @return {@code true} se il file è uguale al database, altrimenti {@code false}.
             */
            @Override
            public boolean compareFileToDatabase(String path) {
                return examManager.compareFileToDatabase(path);
            }
        };
    }
    
    /**
     * @brief Restituisce l'interfaccia per la gestione dei percorsi.
     *
     * Questo metodo crea e restituisce un'istanza dell'interfaccia {@link ManagePaths},
     * implementando i metodi per gestire l'aggiunta, eliminazione, salvataggio e aggiornamento dei percorsi.
     *
     * @return Un'istanza dell'interfaccia {@link ManagePaths}.
     */
    private ManagePaths getManagePathsInterface() {
        return new ManagePaths() {
            
            /**
             * @brief Aggiunge un percorso.
             *
             * Questo metodo aggiunge un percorso e poi salva i percorsi aggiornati.
             *
             * @param path Il percorso da aggiungere.
             * @return L'ID univoco del percorso aggiunto.
             */
            @Override
            public String addPath(String path) {
                String uniqueId = pathManager.addPath(path);
                savePaths();
                return uniqueId;
            }
            
            /**
             * @brief Restituisce un percorso basato sull'ID fornito.
             *
             * Questo metodo restituisce un oggetto {@link UniquePath} associato all'ID specificato.
             *
             * @param uniqueId L'ID univoco del percorso.
             * @return L'oggetto {@link UniquePath} corrispondente all'ID fornito.
             */
            @Override
            public UniquePath getPath(String uniqueId) {
                return pathManager.getPath(uniqueId);
            }
            
            /**
             * @brief Restituisce tutti i percorsi.
             *
             * Questo metodo restituisce una lista di tutti gli oggetti {@link UniquePath}.
             *
             * @return Una lista di tutti gli oggetti {@link UniquePath}.
             */
            @Override
            public List<UniquePath> getAllPaths() {
                return pathManager.getAllPaths();
            }
            
            /**
             * @brief Elimina un percorso basato sull'ID fornito.
             *
             * Questo metodo elimina il percorso associato all'ID specificato, salva i percorsi aggiornati,
             * e aggiorna la tabella dei percorsi e il file selezionato.
             *
             * @param uniqueId L'ID univoco del percorso da eliminare.
             */
            @Override
            public void delPath(String uniqueId) {
                pathManager.delPath(uniqueId);
                savePaths();
                updatePathTable();
                updateSelectedFile();
            }
            
            /**
             * @brief Aggiorna la tabella dei percorsi.
             *
             * Questo metodo aggiorna la visualizzazione della tabella dei percorsi.
             */
            @Override
            public void updateTheTable() {
                updatePathTable();
            }
            
            /**
             * @brief Salva i percorsi nel file predefinito.
             *
             * Questo metodo salva i percorsi nel file predefinito.
             */
            @Override
            public void savePaths() {
                pathManager.savePathsToFile();
            }
            
            /**
             * @brief Salva i percorsi in un percorso specificato.
             *
             * Questo metodo salva i percorsi in un file specificato dal percorso.
             *
             * @param path Il percorso del file dove salvare i percorsi.
             */
            @Override
            public void savePaths(String path) {
                pathManager.savePathsToFile(path);
            }
            
            /**
             * @brief Elimina il percorso selezionato.
             *
             * Questo metodo elimina il percorso attualmente selezionato e aggiorna il file selezionato.
             */
            @Override
            public void delSelected() {
                pathManager.delSelectedUniqueID();
                updateSelectedFile();
            }
            
            /**
             * @brief Restituisce l'ID del percorso selezionato.
             *
             * Questo metodo restituisce l'ID univoco del percorso attualmente selezionato.
             *
             * @return L'ID univoco del percorso selezionato.
             */
            @Override
            public String getSelected() {
                return pathManager.getSelectedUniqueID();
            }
            
            /**
             * @brief Imposta il percorso selezionato.
             *
             * Questo metodo imposta un percorso come selezionato e aggiorna il file selezionato.
             *
             * @param uniqueId L'ID univoco del percorso da selezionare.
             */
            @Override
            public void setSelected(String uniqueId) {
                pathManager.setSelectedUniqueID(uniqueId);
                updateSelectedFile();
            }
            
            /**
             * @brief Mostra il dialogo di gestione dei percorsi.
             *
             * Questo metodo visualizza una finestra di dialogo per la gestione dei percorsi utilizzando
             * gli oggetti {@link ManagePaths} e {@link ManageExams}.
             */
            @Override
            public void displayManagePathDialog() {
                ManagePathsDialog.display(managePaths, manageExams);
            }
        };
    }
    
    /**
     * @brief Aggiorna la tabella degli esami visualizzati nella vista centrale.
     *
     * Questa funzione imposta i dati della tabella degli esami nella vista centrale con
     * la lista di tutti gli esami ottenuti dal controller degli esami.
     */
    private void updateExamTable() {
        this.centerLayout.setData(
            FXCollections.observableList(
                MainPane.examManager.getAllExams()
            ) // Conversione di List<Exam> in ObservableList<Exam>
        );
    }
    
    /**
     * @brief Aggiorna la tabella degli esami parziali per un esame composto specifico.
     *
     * Questa funzione imposta i dati della tabella degli esami parziali nella vista inferiore
     * per un esame composto identificato dall'ID unico specificato.
     *
     * @param uniqueExamId ID univoco dell'esame composto per il quale aggiornare la tabella degli esami parziali.
     */
    private void updatePartialExamTable(String uniqueExamId) {
        if (MainPane.examManager.getExam(uniqueExamId) instanceof ComposedExam ce) {
            this.bottomLayout.setData(
                FXCollections.observableList(ce.getPartialExams()), uniqueExamId
            );
        }
    }
    
    /**
     * @brief Aggiorna la tabella dei percorsi nella vista superiore.
     *
     * Questa funzione imposta i dati della tabella dei percorsi nella vista superiore con
     * la lista di tutti i percorsi ottenuti dal controller dei percorsi.
     */
    private void updatePathTable() {
        this.topLayout.setPathData(
            FXCollections.observableList(
                MainPane.pathManager.getAllPaths()
            ) // Conversione di List<UniquePath> in ObservableList<UniquePath>
        );
    }
    
    /**
     * @brief Aggiorna il file selezionato nella vista centrale.
     *
     * Questa funzione aggiorna la vista centrale per riflettere il file attualmente selezionato.
     */
    private void updateSelectedFile() {
        this.centerLayout.setCurrentSelectedFile();
    }
    
    /**
     * @brief Aggiorna il flag di modificazione nella vista centrale.
     *
     * Questa funzione aggiorna il flag di modificazione nella vista centrale per riflettere
     * lo stato corrente delle modifiche.
     */
    private void updateModifiedFlag() {
        this.centerLayout.setModifiedFlag();
    }
}
