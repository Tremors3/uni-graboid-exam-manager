package myapp.mvc.view.pane;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import myapp.mvc.model.entity.exam.ComposedExam;
import myapp.mvc.model.entity.exam.Exam;
import myapp.mvc.model.entity.exam.SimpleExam;
import myapp.mvc.model.entity.path.UniquePath;
import myapp.mvc.model.manager.ManageExams;
import myapp.mvc.model.manager.ManagePaths;
import myapp.mvc.view.dialog.AddExamDialog.AddExamDialog;
import myapp.mvc.view.things.MyHSpacer;
import myapp.mvc.view.things.MySpinner;

import java.util.StringTokenizer;

/**
 * @brief Rappresenta il pannello centrale dell'interfaccia utente.
 *
 * Questo pannello gestisce la visualizzazione e la manipolazione delle informazioni sugli esami,
 * inclusi la ricerca, la visualizzazione delle informazioni dettagliate e la gestione dei file.
 */
public class CenterPane extends BorderPane {
    
    // ------------ COMPONENTS
    /** Etichetta per il tipo di esame */
    private final Label lblType = new Label("Type");
    /** Etichetta per il nome utente */
    private final Label lblUsername = new Label("Username");
    /** Etichetta per il cognome */
    private final Label lblSurname = new Label("Surname");
    /** Etichetta per l'insegnamento */
    private final Label lblTeaching = new Label("Teaching");
    /** Etichetta per i crediti */
    private final Label lblCredits = new Label("Credits");
    /** Etichetta per il voto finale */
    private final Label lblFinalGrade = new Label("Final Grade");
    /** Etichetta per gli onori */
    private final Label lblHonors = new Label("Honors");
    
    /** Campo di testo per il tipo di esame */
    private final TextField fldType = new TextField();
    /** Campo di testo per il nome utente */
    private final TextField fldUsername = new TextField();
    /** Campo di testo per il cognome */
    private final TextField fldSurname = new TextField();
    /** Campo di testo per l'insegnamento */
    private final TextField fldTeaching = new TextField();
    /** Spinner per i crediti */
    private final MySpinner fldCredits = new MySpinner(3, 12, 3, 3);
    /** Spinner per il voto finale */
    private final MySpinner fldFinalGrade = new MySpinner(18, 30, 18, 1);
    /** Checkbox per gli onori */
    private final CheckBox chkHonors = new CheckBox();
    /** Pulsante per eliminare un esame */
    private final Button btnDelete = new Button("Delete");
    /** Pulsante per annullare le modifiche */
    private final Button btnCancel = new Button("Cancel");
    /** Pulsante per aggiornare le informazioni dell'esame */
    private final Button btnUpdate = new Button("Update");
    /** Spaziatore orizzontale */
    private final MyHSpacer spacer = new MyHSpacer();
    
    // ------------ COMPONENTS SEARCH BAR (BAR)
    /** Etichetta per la ricerca */
    private final Label lblSearch = new Label("Search");
    /** Campo di testo per la ricerca */
    private final TextField fldSearch = new TextField();
    /** ComboBox per i filtri di ricerca */
    private final ComboBox<String> cmbFilter = new ComboBox<>();
    
    // ------------ COMPONENTS MANAGE PATH (BAR)
    /** Etichetta per il percorso aperto */
    private final Label lblPath = new Label("Opened");
    /** Campo di testo per visualizzare il percorso del file */
    private final TextField fldPath = new TextField();
    /** Etichetta per il flag di modifica */
    private final Label lblFlag = new Label("*");
    /** Pulsante per salvare i dati */
    private final Button btnSave = new Button("Save");
    /** Pulsante per caricare i dati */
    private final Button btnLoad = new Button("Load");
    
    // ------------ COMPONENTS MANAGE TABLE (BAR)
    /** Pulsante per aggiungere un nuovo esame */
    private final Button btnAddExam = new Button("Add New");
    
    // ------------ TABLE VIEW
    /** Tabella per visualizzare gli esami */
    private final TableView<Exam> table = new TableView<>();
    /** Lista filtrata di esami */
    private FilteredList<Exam> filteredExams;
    
    // ------------ INTERFACES
    /** Interfaccia per passare le informazioni degli esami parziali */
    private CenterPaneInterface centerPaneInterface;
    /** Gestore degli esami */
    private ManageExams manageExams;
    /** Gestore dei percorsi */
    private ManagePaths managePaths;
    
    // ------------ OTHER
    /** Indica se la ricerca è sensibile al maiuscolo/minuscolo */
    private boolean caseSensitivity = false;
    /** Opzioni di filtro per la ricerca */
    private static final String[] filterOptions = {"None", "Student", "Teaching"};
    
    /**
     * @brief Costruttore della classe CenterPane.
     *
     * Inizializza il pannello centrale impostando il layout e configurando la tabella degli esami.
     */
    public CenterPane() {
        this.setupPane();
        this.setupTable();
    }
    
    /**
     * @brief Configura il layout del pannello.
     *
     * Questo metodo configura il layout del pannello sinistro e del pannello centrale,
     * inclusi gli elementi di ricerca e gestione della tabella degli esami.
     */
    private void setupPane() {}
    
    /**
     * @brief Configura la tabella degli esami e i relativi controlli.
     *
     * Questo metodo imposta le colonne della tabella, le azioni dei pulsanti e il layout
     * per la visualizzazione e la gestione degli esami.
     */
    private void setupTable() {

        /* ------------ LEFT TOP LAYOUT ----------------------------------------------------------------------------- */

        VBox leftLayout = new VBox();
        //leftLayout.setStyle("-fx-background-color: #fdf9eb");
        leftLayout.setPadding(new Insets(10, 10, 10, 10));

        // ------------ VERTICAL 1
        //leftLayout.getChildren().add(lblSelectedExam);

        // ------------ VERTICAL 2
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        GridPane.setConstraints(lblType, 0, 0);
        GridPane.setConstraints(fldType, 1, 0);

        GridPane.setConstraints(lblUsername, 0, 1);
        GridPane.setConstraints(fldUsername, 1, 1);

        GridPane.setConstraints(lblSurname, 0, 2);
        GridPane.setConstraints(fldSurname, 1, 2);

        GridPane.setConstraints(lblTeaching, 0, 3);
        GridPane.setConstraints(fldTeaching, 1, 3);

        GridPane.setConstraints(lblCredits, 0, 4);
        GridPane.setConstraints(fldCredits, 1, 4);

        GridPane.setConstraints(lblFinalGrade, 0, 5);
        GridPane.setConstraints(fldFinalGrade, 1, 5);

        GridPane.setConstraints(lblHonors, 0, 6);
        GridPane.setConstraints(chkHonors, 1, 6);

        grid.getChildren().setAll(
                lblType, fldType,
                lblUsername, fldUsername,
                lblSurname, fldSurname,
                lblTeaching, fldTeaching,
                lblCredits, fldCredits,
                lblFinalGrade, fldFinalGrade,
                lblHonors, chkHonors
        );

        fldType.setEditable(false);
        updateMenuWithSelectedExam(null);

        leftLayout.getChildren().add(grid);

        // ------------ VERTICAL 3
        HBox bottomLayout = new HBox();
        bottomLayout.setSpacing(10);
        bottomLayout.setPadding(new Insets(10, 0, 10, 10));
        bottomLayout.getChildren().setAll(spacer, btnDelete, btnCancel, btnUpdate);

        btnDelete.setOnAction(e -> {
            Exam selectedExam = table.getSelectionModel().getSelectedItem();
            if (selectedExam == null) return;
            manageExams.delExam(selectedExam.getUniqueId());
        });

        btnCancel.setOnAction(e -> {
            Exam selectedExam = table.getSelectionModel().getSelectedItem();
            if (selectedExam == null) return;
            updateMenuWithSelectedExam(selectedExam);
        });

        btnUpdate.setOnAction(e -> {
            Exam selectedExam = table.getSelectionModel().getSelectedItem();
            if (selectedExam == null) return;
            if (selectedExam instanceof SimpleExam)
                manageExams.modSimpleExam(
                        selectedExam.getUniqueId(),
                        fldUsername.getText(),
                        fldSurname.getText(),
                        fldTeaching.getText(),
                        fldCredits.getValue(),
                        fldFinalGrade.getValue(),
                        chkHonors.isSelected()
                );
            else
                manageExams.modComposedExam(
                        selectedExam.getUniqueId(),
                        fldUsername.getText(),
                        fldSurname.getText(),
                        fldTeaching.getText(),
                        fldCredits.getValue()
                );
        });

        leftLayout.getChildren().add(bottomLayout);

        /* ------------ CENTER TOP ---------------------------------------------------------------------------------- */
        
        HBox centerTopLayout = new HBox();
        //centerTopLayout.setStyle("-fx-background-color: #fdf9eb");
        centerTopLayout.setPadding(new Insets(10, 10, 10, 10));
        centerTopLayout.setSpacing(10);
        centerTopLayout.setAlignment(Pos.CENTER_LEFT);

        lblSearch.setMaxHeight(Double.MAX_VALUE);
        lblSearch.setAlignment(Pos.CENTER);

        fldSearch.setPrefWidth(180);
        fldSearch.setPromptText("Search");
        fldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            // Cambiamo il predicato di matching della lista filtrata.
            setFilteredExamsPredicate(newValue);
        });

        cmbFilter.setPrefWidth(105);
        cmbFilter.setVisibleRowCount(3);
        cmbFilter.setItems(FXCollections.observableArrayList(
            filterOptions
        ));
        cmbFilter.setValue(filterOptions[0]);
        cmbFilter.setOnAction(e -> updFilteredExamsPredicate());

        // -----------------------------------

        lblPath.setMaxHeight(Double.MAX_VALUE);
        lblPath.setAlignment(Pos.CENTER);

        fldPath.setPrefWidth(180);
        fldPath.setEditable(false);
        fldPath.setPromptText("No File Selected!");
        
        lblFlag.setMaxHeight(Double.MAX_VALUE);
        lblFlag.setAlignment(Pos.TOP_LEFT);
        
        // ToolTip for the Path
        fldPath.hoverProperty().addListener((observable) -> {
            final String path = fldPath.getText();
            if (fldPath.isHover() && path != null && !path.isEmpty()) {
                Tooltip t = new Tooltip(path);
                t.setShowDelay(Duration.seconds(0.7));
                fldPath.setTooltip(t);
            }
        });

        btnLoad.setOnAction(e -> {
            UniquePath selectedPath = managePaths.getPath(managePaths.getSelected());
            if (selectedPath != null) {
                manageExams.loadExams(selectedPath.getPath());
            }
        });

        btnSave.setOnAction(e -> {
            UniquePath selectedPath = managePaths.getPath(managePaths.getSelected());
            if (selectedPath != null) {
                manageExams.saveExams(selectedPath.getPath());
            }
        });

        // -----------------------------------

        btnAddExam.setOnAction(e -> AddExamDialog.display(manageExams, table.getSelectionModel().getSelectedItem()) );

        // -----------------------------------

        centerTopLayout.getChildren().addAll(lblSearch, fldSearch, cmbFilter, new MyHSpacer(), lblPath, fldPath, lblFlag, btnLoad, btnSave, new MyHSpacer(), btnAddExam);

        /* ------------ CENTER MID ---------------------------------------------------------------------------------- */

        // ID Column
        TableColumn<Exam, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(100); idColumn.setMaxWidth(150);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("uniqueId"));

        // Username Column
        TableColumn<Exam, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setMinWidth(100); usernameColumn.setMaxWidth(300);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Surname Column
        TableColumn<Exam, String> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setMinWidth(100); surnameColumn.setMaxWidth(300);
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        // Teaching Column
        TableColumn<Exam, String> teachingColumn = new TableColumn<>("Teaching");
        teachingColumn.setMinWidth(100); teachingColumn.setMaxWidth(300);
        teachingColumn.setCellValueFactory(new PropertyValueFactory<>("teaching"));

        // Credits Column
        TableColumn<Exam, Integer> creditsColumn = new TableColumn<>("Credits");
        creditsColumn.setMinWidth(100); creditsColumn.setMaxWidth(150);
        creditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));

        // Grade Column
        TableColumn<Exam, Integer> gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setMinWidth(100); gradeColumn.setMaxWidth(150);
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("finalGrade"));

        // Honors Column
        TableColumn<Exam, Boolean> honorsColumn = new TableColumn<>("Honors");
        honorsColumn.setMinWidth(100); honorsColumn.setMaxWidth(150);
        honorsColumn.setCellValueFactory(c -> new SimpleBooleanProperty(
                /* Se l'istanza di c.getValue() è  un SimpleExam allora posso ritornare il valore di getHonors().
                 * Se l'istanza di c.getValue() NON è SimpleExam allora posso ritornare il valore false.
                 */
                c.getValue() instanceof SimpleExam se && se.getHonors()
                //(c.getValue() instanceof SimpleExam se) ? se.getHonors() : false
        ));

        table.getColumns().addAll(idColumn, usernameColumn, surnameColumn, teachingColumn, creditsColumn, gradeColumn, honorsColumn);

        /* Aggiungo un listener alla tabella.
          Ogni volta che un elemento dalla tabella viene selezionato:
          1. Se l'elemento è un esame composto allora vengono spedite le informazini
          relative allo stesso esame (uniqueID) e ai parziali (List<PartialExam>) alla tabella
          del pannello inferiore attraverso l'interfaccia passPartialExamInfo.
         */
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            Exam selectedExam = table.getSelectionModel().getSelectedItem();

            if (selectedExam instanceof ComposedExam ce)
                this.centerPaneInterface.passPartialExam(ce.getUniqueId());
            else
                this.centerPaneInterface.hidePartialExam();
            
            // Updating the menu
            updateMenuWithSelectedExam(selectedExam);
        });
        
        table.setEditable(false);
        table.setStyle("""
        .table-view:focused .table-row-cell:focused {
            -fx-border-color: derive(-fx-base, 20%);
            -fx-background-insets: 0 0 0 0;
            -fx-table-cell-border-color: transparent;
        }
        """);

        /* ------------ CENTER -------------------------------------------------------------------------------------- */

        VBox centerLayout = new VBox();
        centerLayout.getChildren().addAll(centerTopLayout, table);

        /* ---------------------------------------------------------------------------------------------------------- */
        
        this.setLeft(leftLayout);
        this.setCenter(centerLayout);
    }

    // ------------ INTERFACES
    
    /**
     * @brief Imposta l'interfaccia per passare le informazioni sugli esami parziali.
     *
     * @param centerPaneInterface Interfaccia per la gestione degli esami parziali.
     */
    public void setPassPartialExamInfo(CenterPaneInterface centerPaneInterface) {
        this.centerPaneInterface = centerPaneInterface;
    }
    
    /**
     * @brief Imposta il gestore degli esami.
     *
     * @param manageExams Gestore degli esami.
     */
    public void setManageExams(ManageExams manageExams) {
        this.manageExams = manageExams;
    }
    
    /**
     * @brief Imposta il gestore dei percorsi.
     *
     * @param managePaths Gestore dei percorsi.
     */
    public void setManagePaths(ManagePaths managePaths) {
        this.managePaths = managePaths;
    }

    // ------------ TABLE
    
    /**
     * @brief Imposta i dati da visualizzare nella tabella degli esami.
     *
     * @param data Lista di esami da visualizzare.
     */
    public void setData(ObservableList<Exam> data) {
        filteredExams = new FilteredList<>(data, b -> true);
        updFilteredExamsPredicate();
        table.setItems(filteredExams);
        table.refresh();
    }

    // ------------ OTHERS
    
    /**
     * @brief Aggiorna il menu con le informazioni dell'esame selezionato.
     *
     * Se non è selezionato alcun esame, tutti i campi vengono svuotati e disabilitati.
     * Se è selezionato un esame, i campi vengono abilitati e aggiornati con le informazioni dell'esame.
     *
     * @param selectedExam Esame selezionato.
     */
    private void updateMenuWithSelectedExam(Exam selectedExam) {
        if (null == selectedExam) {
            
            // Pulizia dei componenti
            fldType.setText("No Exam Selected!");
            fldUsername.setText("");
            fldSurname.setText("");
            fldTeaching.setText("");
            fldCredits.getValueFactory().setValue(18);
            fldFinalGrade.getValueFactory().setValue(18);
            
            // Disabilitazione dei componenti
            chkHonors.setSelected(false);
            chkHonors.setDisable(true);
            fldUsername.setDisable(true);
            fldSurname.setDisable(true);
            fldTeaching.setDisable(true);
            fldCredits.setDisable(true);
            fldFinalGrade.setDisable(true);

        } else {
            
            // Preparazione dei componenti
            if (selectedExam instanceof SimpleExam se) {
                fldType.setText("Simple");
                fldFinalGrade.setDisable(false);
                chkHonors.setDisable(false);
                chkHonors.setSelected(se.getHonors());
            } else {
                fldType.setText("Composed");
                fldFinalGrade.setDisable(true);
                chkHonors.setDisable(true);
                chkHonors.setSelected(false);
            }
            fldUsername.setText(selectedExam.getUsername());
            fldSurname.setText(selectedExam.getSurname());
            fldTeaching.setText(selectedExam.getTeaching());
            fldCredits.getValueFactory().setValue(selectedExam.getCredits());
            fldFinalGrade.getValueFactory().setValue(selectedExam.getFinalGrade());
            
            // Abilitazione dei componenti
            fldUsername.setDisable(false);
            fldSurname.setDisable(false);
            fldTeaching.setDisable(false);
            fldCredits.setDisable(false);
        }
    }
    
    /**
     * @brief Imposta il flag di modifica in base allo stato degli esami.
     */
    public void setModifiedFlag() {
        lblFlag.setVisible(manageExams.getModFlag());
    }
    
    /**
     * @brief Imposta il predicato per filtrare gli esami in base al testo di ricerca e al filtro selezionato.
     *
     * @param newValue Testo di ricerca.
     */
    private void setFilteredExamsPredicate(String newValue) {
        filteredExams.setPredicate(exam -> {

            if (newValue == null || newValue.isEmpty()) return true;

            String selectedFilter = cmbFilter.getValue();
            String match = (caseSensitivity) ? newValue : newValue.toLowerCase();
            String[] values = {exam.getUsername(), exam.getSurname(), exam.getTeaching()};

            if (!caseSensitivity)
                for (int i = 0; i < values.length; i++)
                    values[i] = values[i].toLowerCase();
            
            // Filtro per Studente selezionato
            if (selectedFilter.equals(filterOptions[1]))
                /* Per studiare il calendario degli esami di un singolo studente devo identificarlo in modo univoco.
                   Devo tokenizzare la corrispondenza e assicurarmi che ciascuna sottostringa corrisponda ad almeno
                   uno dei valori in Nome utente o Cognome. */
                return areUsernameAndSurnameMatched(match, values);
            
            // Filtro per Username, Surname o Materia
            return areUsernameOrSurnameOrTeachingMatched(match, selectedFilter, values);
        });
    }
    
    /**
     * @brief Verifica se Username e Surname corrispondono al testo di ricerca.
     *
     * @param match Testo di ricerca.
     * @param values Array contenente Username e Surname.
     * @return TRUE se entrambi corrispondono, FALSE altrimenti.
     */
    private static boolean areUsernameAndSurnameMatched(String match, String[] values) {
        boolean matched = true;

        StringTokenizer tokenizer = new StringTokenizer(match, " ");
        while (tokenizer.hasMoreTokens() && matched) {
            String token = tokenizer.nextToken();
            
            /* Non appena la sottostringa corrente non corrisponde né al nome utente né al cognome, la corrispondenza viene considerata fallita. */
            //matched = matched && ( values[0].contains(token) || values[1].contains(token) );
            matched = ( values[0].contains(token) || values[1].contains(token) );
        }

        return matched;
    }
    
    /**
     * @brief Verifica se il testo di ricerca corrisponde a Username, Surname o Materia.
     *
     * @param match Testo di ricerca.
     * @param filter Filtro selezionato.
     * @param values Array contenente Username, Surname e Materia.
     * @return TRUE se corrisponde, FALSE altrimenti.
     */
    private static boolean areUsernameOrSurnameOrTeachingMatched(String match, String filter, String[] values) {
        boolean isMatch = false;

        for (int i = 0; i < values.length; i++) {
            if (values[i].contains(match)) {

                // Username or Surname
                if ((i == 0 || i == 1) && filter.equals(filterOptions[0]))
                    isMatch = true;

                // Teaching
                else if (i == 2 && (filter.equals(filterOptions[2]) || filter.equals(filterOptions[0])))
                    isMatch = true;
            }
        }
        return isMatch;
    }
    
    /**
     * @brief Aggiorna il predicato di filtro in base al testo di ricerca corrente.
     */
    private void updFilteredExamsPredicate() {
        setFilteredExamsPredicate(
            fldSearch.getText()
        );
    }
    
    /**
     * @brief Imposta la sensibilità al caso nella ricerca.
     *
     * @param check TRUE se la ricerca è sensibile al caso, FALSE altrimenti.
     */
    public void setCaseSensitivity(boolean check) {
        this.caseSensitivity = check;

        /* Appena cambiamo lo stato della variabile "caseSensitivity" controlliamo
           che il valore già presente nel field di recerca rispatti il nuovo stato. */
        updFilteredExamsPredicate();
    }
    
    /**
     * @brief Aggiorna il campo del percorso con il percorso del file selezionato.
     */
    public void setCurrentSelectedFile() {
        if (managePaths.getSelected() != null)
            fldPath.setText(managePaths.getPath(managePaths.getSelected()).getPath());
        else
            fldPath.setText(null);
    }
    
    /**
     * @brief Restituisce la tabella degli esami.
     *
     * @return Tabella degli esami.
     */
    public TableView<?> getTableToPrint() {
        return this.table;
    }
    
    /**
     * @brief Restituisce la lista filtrata degli esami.
     *
     * @return Lista filtrata degli esami.
     */
    public FilteredList<Exam> getFilteredExams() {
        return this.filteredExams;
    }
    
    /**
     * @brief Restituisce l'opzione di filtro selezionata.
     *
     * @return Opzione di filtro selezionata.
     */
    public String getSelectedFilterOption() {
        return cmbFilter.getValue();
    }
    
    /**
     * @brief Restituisce le opzioni di filtro disponibili.
     *
     * @return Array di opzioni di filtro.
     */
    public String[] getFilterOptions() {
        return filterOptions;
    }
}