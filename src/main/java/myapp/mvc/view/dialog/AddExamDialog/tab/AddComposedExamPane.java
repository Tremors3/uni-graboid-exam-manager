package myapp.mvc.view.dialog.AddExamDialog.tab;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import myapp.mvc.model.manager.ManageExams;
import myapp.mvc.view.dialog.AddExamDialog.tab.common.PartialExamField;
import myapp.mvc.view.dialog.AddExamDialog.tab.common.PartialExamsLayout;
import myapp.mvc.view.dialog.CommonDialogInterface;
import myapp.mvc.view.things.MyHSpacer;
import myapp.mvc.view.things.MySpinner;

/**
 * @brief Pannello per aggiungere un esame composto
 *
 * Questa classe fornisce l'interfaccia grafica per aggiungere un esame composto,
 * permettendo all'utente di inserire i dettagli dell'esame e aggiungere o eliminare
 * prove parziali associate.
 */
public class AddComposedExamPane extends BorderPane {

    // ------------ COMPONENTS
    
    /** @brief Etichetta per il tipo di esame */
    private final Label lblType = new Label("Type");
    /** @brief Etichetta per il nome utente */
    private final Label lblUsername = new Label("Username");
    /** @brief Etichetta per il cognome */
    private final Label lblSurname = new Label("Surname");
    /** @brief Etichetta per l'insegnamento */
    private final Label lblTeaching = new Label("Teaching");
    /** @brief Etichetta per i crediti */
    private final Label lblCredits = new Label("Credits");
    
    /** @brief Campo per il tipo di esame (non modificabile) */
    private final TextField fldType = new TextField("Composed");
    /** @brief Campo per il nome utente */
    private final TextField fldUsername = new TextField();
    /** @brief Campo per il cognome */
    private final TextField fldSurname = new TextField();
    /** @brief Campo per l'insegnamento */
    private final TextField fldTeaching = new TextField();
    /** @brief Spinner per selezionare i crediti */
    private final MySpinner fldCredits = new MySpinner(3, 12, 3, 3);
    
    /** @brief Pulsante per aggiungere una prova parziale */
    private final Button btnAddPartial = new Button("+");
    /** @brief Pulsante per eliminare una prova parziale */
    private final Button btnDelPartial = new Button("-");
    /** @brief Pulsante per annullare l'operazione */
    private final Button btnCancel = new Button("Cancel");
    /** @brief Pulsante per confermare l'aggiunta di un nuovo esame composto */
    private final Button btnAdd = new Button("Add New");
    
    // ------------ INTERFACES
    
    /** @brief Interfaccia per gestire gli esami */
    private final ManageExams manageExams;
    /** @brief Interfaccia per gestire la finestra di dialogo */
    private CommonDialogInterface commonDialogInterface;
    
    // ------------ CONSTRUCTOR
    
    /**
     * @brief Costruttore della classe AddComposedExamPane
     *
     * Inizializza il pannello e configura gli elementi dell'interfaccia grafica.
     *
     * @param manageExams Istanza di ManageExams per la gestione degli esami
     */
    public AddComposedExamPane(ManageExams manageExams) {
        this.manageExams = manageExams;
        this.setupFrame();
        this.setupComponents();
    }
    
    /**
     * @brief Configura il frame
     *
     * Metodo per configurare la struttura base del frame. Attualmente vuoto
     * ma può essere esteso per future personalizzazioni.
     */
    private void setupFrame() {}
    
    /**
     * @brief Configura i componenti grafici
     *
     * Imposta le proprietà e le azioni degli elementi dell'interfaccia grafica,
     * inclusi i campi di testo, pulsanti e layout.
     */
    private void setupComponents() {

        // -----------------------------------

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(50);

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
        
        fldType.setEditable(false);

        grid.getChildren().setAll(
            lblType, fldType,
            lblUsername, fldUsername,
            lblSurname, fldSurname,
            lblTeaching, fldTeaching,
            lblCredits, fldCredits
        );
        
        // -----------------------------------
        
        PartialExamsLayout partialExamsLayout = new PartialExamsLayout();
        
        // -----------------------------------
        
        btnAddPartial.setOnAction(e -> partialExamsLayout.addPartial());
        
        btnDelPartial.setOnAction(e -> partialExamsLayout.delPartial());
        
        btnCancel.setOnAction(e -> commonDialogInterface.CloseWindow());

        btnAdd.setOnAction(e -> {

            String exam_id = manageExams.addComposedExam(
                fldUsername.getText(),
                fldSurname.getText(),
                fldTeaching.getText(),
                fldCredits.getValue()
            );

            for (PartialExamField partial : partialExamsLayout.getPartialExamFields()) {
                manageExams.addIntermediateGrade(
                    exam_id,
                    partial.getPartialGrade(),
                    partial.getWeight()
                );
            }
            
            commonDialogInterface.CloseWindow();
        });
        
        btnAddPartial.setMinWidth(26);
        btnDelPartial.setMinWidth(26);
        
        HBox bot = new HBox();
        bot.setSpacing(10);
        bot.setPadding(new Insets(10, 0, 0, 0));  // OK
        bot.getChildren().addAll(btnAddPartial, btnDelPartial, new MyHSpacer(), btnCancel, btnAdd);
        
        // -----------------------------------
        
        VBox mainLayout = new VBox();
        mainLayout.setPadding(new Insets(10, 10, 10, 10));  // OK
        mainLayout.getChildren().addAll(grid, partialExamsLayout, bot);

        // -----------------------------------

        this.setLeft(mainLayout);
    }
    
    /**
     * @brief Imposta l'interfaccia di dialogo comune
     *
     * Permette di assegnare un'interfaccia di dialogo per la gestione della finestra.
     *
     * @param commonDialogInterface L'interfaccia da assegnare
     */
    public void setCommonDialogInterface(CommonDialogInterface commonDialogInterface) {
        this.commonDialogInterface = commonDialogInterface;
    }
}