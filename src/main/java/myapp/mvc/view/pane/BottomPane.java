package myapp.mvc.view.pane;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import myapp.mvc.model.entity.exam.PartialExam;
import myapp.mvc.model.manager.ManageExams;
import myapp.mvc.view.things.MyHSpacer;
import myapp.mvc.view.things.MySpinner;

/**
 * @brief Classe che estende {@link BorderPane} per fornire un pannello inferiore personalizzato.
 *
 * La classe {@link BottomPane} gestisce l'interfaccia utente per la visualizzazione e modifica dei voti parziali degli esami,
 * inclusa una tabella per la visualizzazione dei dati e controlli per la modifica dei valori. Permette anche di
 * eseguire operazioni di aggiornamento e cancellazione dei dati.
 */
public class BottomPane extends BorderPane {
    
    // ------------ INTERFACES
    /** @brief Gestore per le operazioni sugli esami. */
    private ManageExams manageExams;
    
    // ------------ COMPONENTS
    /** @brief Etichetta per il grado parziale. */
    private final Label lblPartialGrade = new Label("Final Grade");
    
    /** @brief Etichetta per il peso. */
    private final Label lblWeight = new Label("Weight");
    
    /** @brief Spinner per il peso. */
    private final MySpinner fldWeight = new MySpinner(1, 100, 1, 1);
    
    /** @brief Spinner per il grado parziale. */
    private final MySpinner fldPartialGrade = new MySpinner(18, 30, 18, 1);
    
    /** @brief Pulsante per eliminare. */
    private final Button btnDelete = new Button("Delete");
    
    /** @brief Pulsante per annullare. */
    private final Button btnCancel = new Button("Cancel");
    
    /** @brief Pulsante per aggiornare. */
    private final Button btnUpdate = new Button("Update");
    
    /** @brief Spaziatore orizzontale. */
    private final MyHSpacer spacer = new MyHSpacer();
    
    // ------------ TABLE VIEW
    /** @brief Tabella per visualizzare gli esami parziali. */
    private final TableView<PartialExam> table = new TableView<>();
    
    // ------------ VARIABLES
    /** @brief ID unico dell'esame corrente. */
    private String uniqueExamId;
    
    /**
     * @brief Costruttore per {@link BottomPane}.
     *
     * Questo costruttore inizializza il pannello inferiore configurando il layout e i componenti.
     */
    public BottomPane() {
        this.setupPane();
        this.setupComponents();
    }
    
    /**
     * @brief Configura il pannello inferiore.
     *
     * Imposta la visibilità del pannello a nascosta all'inizio.
     */
    private void setupPane() {
        this.hide();
    }
    
    /**
     * @brief Configura i componenti del pannello inferiore.
     *
     * Crea e aggiunge i layout e i controlli alla vista, configurando la tabella e i pulsanti.
     */
    private void setupComponents() {
        
        /* ------------ LEFT BOTTOM LAYOUT -------------------------------------------------------------------------- */
        
        VBox leftLayout = new VBox();
        leftLayout.setPadding(new Insets(10, 10, 10, 10));
        leftLayout.setMinWidth(240);
        
        // ------------ VERTICAL 2
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        GridPane.setConstraints(lblPartialGrade, 0, 0);
        GridPane.setConstraints(fldPartialGrade, 1, 0);
        GridPane.setConstraints(lblWeight, 0, 1);
        GridPane.setConstraints(fldWeight, 1, 1);
        grid.getChildren().setAll(lblWeight, fldWeight, lblPartialGrade, fldPartialGrade);
        
        fldWeight.setDisable(true);
        fldPartialGrade.setDisable(true);
        
        leftLayout.getChildren().add(grid);
        
        // ------------ VERTICAL 3
        HBox bottomLayout = new HBox();
        bottomLayout.setSpacing(10);
        bottomLayout.setPadding(new Insets(10, 0, 10, 10));
        bottomLayout.getChildren().setAll(spacer, btnDelete, btnCancel, btnUpdate);
        
        // Configura azioni per i pulsanti
        btnDelete.setOnAction(e -> {
            PartialExam partialExam = table.getSelectionModel().getSelectedItem();
            if (partialExam != null) {
                manageExams.delIntermediateGrade(uniqueExamId, partialExam.getUniqueId());
            }
        });
        
        btnCancel.setOnAction(e -> {
            PartialExam partialExam = table.getSelectionModel().getSelectedItem();
            if (partialExam != null) {
                updateMenuWithSelectedExam(partialExam);
            }
        });
        
        btnUpdate.setOnAction(e -> {
            PartialExam partialExam = table.getSelectionModel().getSelectedItem();
            if (partialExam != null) {
                manageExams.modIntermediateGrade(
                    uniqueExamId,
                    partialExam.getUniqueId(),
                    fldPartialGrade.getValue(),
                    fldWeight.getValue()
                );
            }
        });
        
        leftLayout.getChildren().add(bottomLayout);
        
        /* ------------ CENTER BOTTOM LAYOUT ------------------------------------------------------------------------ */
        
        // Colonna ID
        TableColumn<PartialExam, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(100);
        idColumn.setMaxWidth(150);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("uniqueId"));
        
        // Colonna Grado
        TableColumn<PartialExam, Integer> gradeColumn = new TableColumn<>("Partial Grade");
        gradeColumn.setMinWidth(100);
        gradeColumn.setMaxWidth(150);
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        
        // Colonna Peso
        TableColumn<PartialExam, Integer> weightColumn = new TableColumn<>("Weight");
        weightColumn.setMinWidth(100);
        weightColumn.setMaxWidth(150);
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        
        table.getColumns().addAll(idColumn, gradeColumn, weightColumn);
        
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            PartialExam partialExam = table.getSelectionModel().getSelectedItem();
            updateMenuWithSelectedExam(partialExam);
        });
        
        VBox.setVgrow(table, Priority.ALWAYS);  // Permette alla TableView di crescere
        table.setMaxHeight(Double.MAX_VALUE);
        table.setEditable(false);
        table.setStyle("""
        .table-view:focused .table-row-cell:focused {
            -fx-border-color: derive(-fx-base, 20%);
            -fx-background-insets: 0 0 0 0;
            -fx-table-cell-border-color: transparent;
        }
        """);
        
        /* ---------------------------------------------------------------------------------------------------------- */
        
        this.setLeft(leftLayout);
        this.setCenter(table);
    }
    
    /**
     * @brief Aggiorna i campi di input con i dati dell'esame parziale selezionato.
     *
     * Se l'esame parziale è null, i campi di input sono disabilitati e impostati ai valori predefiniti.
     * Altrimenti, i campi vengono abilitati e popolati con i dati dell'esame parziale selezionato.
     *
     * @param partialExam L'esame parziale selezionato.
     */
    private void updateMenuWithSelectedExam(PartialExam partialExam) {
        if (partialExam == null) {
            fldWeight.getValueFactory().setValue(1);
            fldPartialGrade.getValueFactory().setValue(18);
            
            fldWeight.setDisable(true);
            fldPartialGrade.setDisable(true);
            
        } else {
            fldWeight.getValueFactory().setValue(partialExam.getWeight());
            fldPartialGrade.getValueFactory().setValue(partialExam.getGrade());
            
            fldWeight.setDisable(false);
            fldPartialGrade.setDisable(false);
        }
    }
    
    /**
     * @brief Imposta i dati da visualizzare nella tabella e l'ID dell'esame corrente.
     *
     * @param data Lista degli esami parziali da visualizzare nella tabella.
     * @param uniqueExamId ID unico dell'esame corrente.
     */
    public void setData(ObservableList<PartialExam> data, String uniqueExamId) {
        table.setItems(data);
        this.uniqueExamId = uniqueExamId;
        table.refresh();
    }
    
    /**
     * @brief Imposta il gestore per le operazioni sugli esami.
     *
     * @param manageExams Il gestore per le operazioni sugli esami.
     */
    public void setManageExams(ManageExams manageExams) {
        this.manageExams = manageExams;
    }
    
    /**
     * @brief Nasconde il pannello.
     *
     * Il pannello non sarà visibile e non sarà gestito dal layout.
     */
    public void hide() {
        this.setVisible(false);
        this.setManaged(false);
    }
    
    /**
     * @brief Mostra il pannello.
     *
     * Il pannello sarà visibile e sarà gestito dal layout.
     */
    public void show() {
        this.setVisible(true);
        this.setManaged(true);
    }
    
    /**
     * @brief Ottiene la tabella da stampare.
     *
     * @return La tabella {@link TableView} da stampare.
     */
    public TableView<?> getTableToPrint() {
        return this.table;
    }
}
