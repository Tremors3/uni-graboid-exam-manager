package myapp.mvc.view.dialog.ManagePathsDialog;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import myapp.mvc.model.entity.path.UniquePath;
import myapp.mvc.model.manager.ManageExams;
import myapp.mvc.model.manager.ManagePaths;
import myapp.mvc.view.dialog.CommonDialogInterface;
import myapp.mvc.view.things.MyHSpacer;
import myapp.util.MutablePair;

import java.util.List;

/**
 * @brief Gestisce la visualizzazione e la manipolazione dei percorsi.
 *
 * Questa classe estende {@link BorderPane} e fornisce un'interfaccia per gestire i percorsi. Include
 * pulsanti per selezionare, eliminare, aprire, salvare e caricare percorsi, e una tabella per visualizzare
 * i percorsi disponibili.
 */
public class ManagePathsPane extends BorderPane {

    // ------------ COMPONENTS
    
    /** @brief Pulsante per selezionare un percorso */
    private final Button btnSelect = new Button("Select");
    
    /** @brief Pulsante per eliminare un percorso */
    private final Button btnDelete = new Button("Delete");
    
    /** @brief Pulsante per aggiungere un nuovo percorso */
    private final Button btnOpen = new Button("Add New...");
    
    /** @brief Spaziatore orizzontale */
    private final MyHSpacer spacer1 = new MyHSpacer();
    
    /** @brief Pulsante per salvare i percorsi */
    private final Button btnSave = new Button("Save");
    
    /** @brief Pulsante per caricare i percorsi */
    private final Button btnLoad = new Button("Load");
    
    /** @brief Checkbox per abilitare il caricamento automatico dei file */
    private final CheckBox chkAutoLoad = new CheckBox("When Selecting Load File Automatically");
    
    /** @brief Spaziatore orizzontale */
    private final MyHSpacer spacer2 = new MyHSpacer();
    
    /** @brief Pulsante per chiudere la finestra di dialogo */
    private final Button btnClose = new Button("Close");
    
    // ------------ TABLE VIEW
    
    /** @brief Tabella per visualizzare i percorsi */
    private final TableView<UniquePath> table = new TableView<>();

    // ------------ INTERFACES
    
    /** @brief Interfaccia per gestire i percorsi */
    private ManagePaths managePaths;
    
    /** @brief Interfaccia per gestire gli esami */
    private ManageExams manageExams;
    
    /** @brief Interfaccia per i dialog comuni */
    private CommonDialogInterface commonDialogInterface;

    // ------------ OTHER
    
    /** @brief Dimensioni predefinite della tabella */
    private static final MutablePair<Integer, Integer> TABLE_DIMENSIONS = new MutablePair<>(500, 150);
    
    /**
     * @brief Costruttore della classe ManagePathsPane.
     *
     * Inizializza la classe con i controller necessari e imposta il layout della finestra di dialogo.
     *
     * @param managePaths Gestore dei percorsi
     * @param manageExams Gestore degli esami
     */
    public ManagePathsPane(ManagePaths managePaths, ManageExams manageExams) {
        this.setManagePaths(managePaths);
        this.setManageExams(manageExams);
        this.setupPane();
        this.setupTable();
    }
    
    /**
     * @brief Configura il layout della finestra di dialogo.
     *
     * Questo metodo imposta i componenti dell'interfaccia utente e i loro gestori di eventi.
     */
    private void setupPane() {}
    
    /**
     * @brief Configura la tabella per la visualizzazione dei percorsi.
     *
     * Questo metodo imposta le colonne della tabella, i pulsanti e i loro gestori di eventi, e la visualizzazione
     * dei percorsi. Gestisce anche la visibilità degli header e la creazione dei ToolTips per le righe della tabella.
     */
    private void setupTable() {

        /* ------------ TOP ----------------------------------------------------------------------------------------- */

        HBox centerTopLayout = new HBox();
        centerTopLayout.setPadding(new Insets(10, 10, 10, 10));
        centerTopLayout.setSpacing(10);
        centerTopLayout.setAlignment(Pos.CENTER_LEFT);
        centerTopLayout.getChildren().addAll(btnSelect, btnDelete, btnOpen, spacer1, btnLoad, btnSave);

        btnSelect.setOnAction(e -> {
            UniquePath selectedPath = table.getSelectionModel().getSelectedItem();
            if (selectedPath == null) return;
            managePaths.setSelected(selectedPath.getUniqueId());
            managePaths.updateTheTable();
            if (chkAutoLoad.isSelected())
                manageExams.loadExamsFromSelectedFile();
            else
                manageExams.updModFlag();
        });

        btnDelete.setOnAction(e -> {
            UniquePath selectedPath = table.getSelectionModel().getSelectedItem();
            if (selectedPath == null) return;
            managePaths.delPath(selectedPath.getUniqueId());
            managePaths.updateTheTable();
        });

        btnOpen.setOnAction(e -> {
            manageExams.openExams();
            managePaths.updateTheTable();
        });

        btnSave.setOnAction(e -> manageExams.saveExamsToSelectedFile());

        btnLoad.setOnAction(e -> manageExams.loadExamsFromSelectedFile());

        /* ------------ CENTER -------------------------------------------------------------------------------------- */

        // ID Column
        TableColumn<UniquePath, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(0); idColumn.setMaxWidth(0);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("uniqueId"));
        idColumn.setVisible(false);

        // Username Column
        TableColumn<UniquePath, String> pathColumn = new TableColumn<>();
        pathColumn.setMinWidth(TABLE_DIMENSIONS.getFirst() - 50); pathColumn.setMaxWidth(TABLE_DIMENSIONS.getFirst() - 50);
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));

        // Is Selected Column
        TableColumn<UniquePath, String> isSelectedColumn = new TableColumn<>();
        isSelectedColumn.setMinWidth(50); isSelectedColumn.setMaxWidth(50);
        isSelectedColumn.setCellValueFactory(c -> {
            // Controllo che il valore di ID del percorso primario non sia NULL
            // In quel caso lo confronto con il percorso della riga corrente per sapere se stampare false (percorso normale) o true (percorso primario)
            if (managePaths.getSelected() != null && c.getValue().getUniqueId().equals(managePaths.getPath(managePaths.getSelected()).getUniqueId()))
                return new SimpleStringProperty("[ x ]"); else
                return new SimpleStringProperty("[   ]");
        });

        table.getColumns().addAll(idColumn, pathColumn, isSelectedColumn);

        this.hideHeaders();
        table.setEditable(false);
        table.setMinSize(TABLE_DIMENSIONS.getFirst() + 2, TABLE_DIMENSIONS.getSecond());
        table.setMaxSize(TABLE_DIMENSIONS.getFirst() + 2, TABLE_DIMENSIONS.getSecond());
        table.setStyle("""
            .table-view:focused .table-row-cell:focused {
            -fx-border-color: derive(-fx-base, 20%);
            -fx-background-insets: 0 0 0 0;
            -fx-table-cell-border-color: transparent;
        }
        """);

        // Aggiunge un listener a ciascuna riga della tabella per mostrare il ToolTip del percorso
        table.setRowFactory(tableView -> {
            final TableRow<UniquePath> row = new TableRow<>();

            row.hoverProperty().addListener((observable) -> {
                final UniquePath path = row.getItem();

                if (row.isHover() && path != null) {

                    Tooltip t = new Tooltip(path.getPath());
                    t.setShowDelay(Duration.seconds(0.7));
                    row.setTooltip(t);

                }
            });

            return row;
        });

        /* ------------ BOTTOM -------------------------------------------------------------------------------------- */
        
        chkAutoLoad.setOnAction(e -> hideLoadSaveButtons());
        chkAutoLoad.setSelected(true);
        hideLoadSaveButtons();
        
        btnClose.setOnAction(e -> commonDialogInterface.CloseWindow());
        
        HBox bottomLayout = new HBox();
        bottomLayout.setPadding(new Insets(10, 10, 10, 10));
        bottomLayout.setSpacing(10);
        bottomLayout.setAlignment(Pos.CENTER_LEFT);
        bottomLayout.getChildren().addAll(chkAutoLoad, spacer2, btnClose);

        /* ---------------------------------------------------------------------------------------------------------- */

        this.setTop(centerTopLayout);
        this.setCenter(table);
        this.setBottom(bottomLayout);
    }

    // ------------ INTERFACES
    
    /**
     * @brief Imposta il gestore dei percorsi.
     *
     * Questo metodo imposta il gestore dei percorsi da utilizzare per le operazioni sui percorsi.
     *
     * @param managePaths Gestore dei percorsi
     */
    public void setManagePaths(ManagePaths managePaths) {
        this.managePaths = managePaths;
    }
    
    /**
     * @brief Imposta il gestore degli esami.
     *
     * Questo metodo imposta il gestore degli esami da utilizzare per le operazioni sugli esami.
     *
     * @param manageExams Gestore degli esami
     */
    public void setManageExams(ManageExams manageExams) {
        this.manageExams = manageExams;
    }
    
    /**
     * @brief Imposta l'interfaccia per i dialog comuni.
     *
     * Questo metodo imposta l'interfaccia per i dialog comuni da utilizzare per la chiusura della finestra di dialogo.
     *
     * @param commonDialogInterface Interfaccia per i dialog comuni
     */
    public void setCommonDialogInterface(CommonDialogInterface commonDialogInterface) {
        this.commonDialogInterface = commonDialogInterface;
    }
    
    // ------------ TABLE
    
    /**
     * @brief Imposta i dati nella tabella.
     *
     * Questo metodo imposta i dati nella tabella e aggiorna la visualizzazione.
     *
     * @param data Lista di percorsi da visualizzare
     */
    public void setData(List<UniquePath> data) {
        table.setItems(
            FXCollections.observableList(data)
        );
        table.refresh();
    }
    
    /**
     * @brief Nasconde gli header della tabella.
     *
     * Questo metodo nasconde gli header della tabella impostando altezza e visibilità a zero.
     */
    private void hideHeaders() {
        table.skinProperty().addListener((a, b, newSkin) ->
        {
            Pane header = (Pane) table.lookup("TableHeaderRow");
            header.setMinHeight(0);
            header.setPrefHeight(0);
            header.setMaxHeight(0);
            header.setVisible(false);
        });
    }
    
    // ------------ OTHER
    
    /**
     * @brief Nasconde i pulsanti di caricamento e salvataggio.
     *
     * Questo metodo disabilita i pulsanti di caricamento e salvataggio se la checkbox di caricamento automatico è selezionata.
     */
    private void hideLoadSaveButtons() {
        if (chkAutoLoad.isSelected()) {
            btnLoad.setDisable(true);
            btnSave.setDisable(true);
        } else {
            btnLoad.setDisable(false);
            btnSave.setDisable(false);
        }
    }
}
