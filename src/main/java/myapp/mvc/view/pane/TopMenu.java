package myapp.mvc.view.pane;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;
import myapp.autosaver.ManageAutosaver;
import myapp.mvc.model.entity.path.UniquePath;
import myapp.mvc.model.manager.ManageExams;
import myapp.mvc.model.manager.ManagePaths;
import myapp.mvc.view.dialog.ManagePathsDialog.ManagePathsDialog;

/**
 * @brief Classe che estende {@link MenuBar} per fornire una barra dei menu personalizzata.
 *
 * La classe {@link TopMenu} crea una barra dei menu con voci per la gestione dei file, tabelle, grafici e opzioni.
 * Include anche metodi per configurare e gestire i diversi componenti della barra dei menu.
 */
public class TopMenu extends MenuBar {
    
    /** @brief Directory di esportazione predefinita. */
    public static final String DEFAULT_EXPORT_DIRECTORY = System.getProperty("user.home") + "/Downloads";
    
    /** @brief Gestore per il salvataggio automatico. */
    private ManageAutosaver manageAutosaver;
    
    /** @brief Interfaccia per le azioni della barra dei menu. */
    private TopMenuInterface topMenuInterface;
    
    /** @brief Gestore per gli esami. */
    private ManageExams manageExams;
    
    /** @brief Gestore per i percorsi. */
    private ManagePaths managePaths;
    
    /**
     * @brief Costruttore per {@link TopMenu}.
     *
     * Questo costruttore inizializza la barra dei menu configurando il pannello e i componenti.
     */
    public TopMenu() {
        this.setupPane();
        this.setupComponents();
    }
    
    /**
     * @brief Configura il pannello della barra dei menu.
     *
     * Questo metodo può essere utilizzato per impostare le proprietà iniziali del pannello della barra dei menu.
     */
    private void setupPane() {
        // Metodo vuoto; può essere utilizzato per configurazioni future del pannello
    }
    
    /**
     * @brief Configura i componenti della barra dei menu.
     *
     * Questo metodo crea e aggiunge i menu e gli elementi di menu alla barra dei menu.
     */
    private void setupComponents() {
        
        // ------ File -------------------------------------------------------------------------------------------------
        
        MenuItem openFile = new MenuItem("Open...");
        openFile.setOnAction(e -> manageExams.openExamsAndSetSelected());
        
        MenuItem chooseFile = new MenuItem("Choose...");
        chooseFile.setOnAction(e -> managePaths.displayManagePathDialog());
        
        MenuItem exportFile = new MenuItem("Export...");
        exportFile.setOnAction(e -> manageExams.exportExams());
        
        MenuItem saveAndExit = new MenuItem("Save and Exit");
        saveAndExit.setOnAction(e -> this.topMenuInterface.SaveAndClose());
        
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> this.topMenuInterface.Close());
        
        Menu fileMenu = new Menu("_File");
        fileMenu.getItems().addAll(
            openFile, chooseFile, exportFile,
            new SeparatorMenuItem(),
            saveAndExit, exit
        );
        
        // ------ Table ------------------------------------------------------------------------------------------------
        
        MenuItem printMenuItem = new MenuItem("Print...");
        printMenuItem.setOnAction(e -> topMenuInterface.printTables((Stage) this.getScene().getWindow()));
        
        Menu tableMenu = new Menu("_Table");
        tableMenu.getItems().addAll(printMenuItem);
        
        // ------ Graphs -----------------------------------------------------------------------------------------------
        
        MenuItem showGraph = new MenuItem("Show Graph...");
        showGraph.setOnAction(e -> topMenuInterface.showGraph());
        
        Menu graphMenu = new Menu("_Graph");
        graphMenu.getItems().addAll(showGraph);
        
        // ------ Options ----------------------------------------------------------------------------------------------
        
        MenuItem paths = new MenuItem("Manage Paths...");
        paths.setOnAction(e -> managePaths.displayManagePathDialog());
        
        CheckMenuItem autoSave = new CheckMenuItem("Auto Save");
        autoSave.setOnAction(e -> {
            if (autoSave.isSelected()) manageAutosaver.resumeAutosaver();
            else manageAutosaver.pauseAutosaver();
        });
        autoSave.setSelected(true);
        
        CheckMenuItem caseSearch = new CheckMenuItem("Case Sensitive Search");
        caseSearch.setOnAction(e -> {
            if (caseSearch.isSelected()) topMenuInterface.setCaseSensitive(true);
            else topMenuInterface.setCaseSensitive(false);
        });
        caseSearch.setSelected(false);
        
        Menu optionsMenu = new Menu("_Options");
        optionsMenu.getItems().addAll(paths, new SeparatorMenuItem(), autoSave, caseSearch);
        
        // -------------------------------------------------------------------------------------------------------------
        
        this.getMenus().addAll(fileMenu, tableMenu, graphMenu, optionsMenu);
    }
    
    /**
     * @brief Imposta il gestore per il salvataggio automatico.
     *
     * @param manageAutosaver Il gestore per il salvataggio automatico.
     */
    public void setManageAutosaver(ManageAutosaver manageAutosaver) {
        this.manageAutosaver = manageAutosaver;
    }
    
    /**
     * @brief Imposta il gestore per gli esami.
     *
     * @param manageExams Il gestore per gli esami.
     */
    public void setManageExams(ManageExams manageExams) {
        this.manageExams = manageExams;
    }
    
    /**
     * @brief Imposta l'interfaccia per le azioni della barra dei menu.
     *
     * @param topMenuInterface L'interfaccia per le azioni della barra dei menu.
     */
    public void setTopMenuInterface(TopMenuInterface topMenuInterface) {
        this.topMenuInterface = topMenuInterface;
    }
    
    /**
     * @brief Imposta il gestore per i percorsi.
     *
     * @param managePaths Il gestore per i percorsi.
     */
    public void setManagePaths(ManagePaths managePaths) {
        this.managePaths = managePaths;
    }
    
    /**
     * @brief Imposta i dati dei percorsi per il dialogo di gestione percorsi.
     *
     * @param data Lista dei percorsi unici da impostare.
     */
    public void setPathData(ObservableList<UniquePath> data) {
        ManagePathsDialog.setPathData(data);
    }
}
