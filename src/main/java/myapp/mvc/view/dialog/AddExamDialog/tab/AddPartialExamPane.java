package myapp.mvc.view.dialog.AddExamDialog.tab;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import myapp.mvc.model.entity.exam.Exam;
import myapp.mvc.model.manager.ManageExams;
import myapp.mvc.view.dialog.AddExamDialog.tab.common.PartialExamField;
import myapp.mvc.view.dialog.AddExamDialog.tab.common.PartialExamsLayout;
import myapp.mvc.view.dialog.CommonDialogInterface;
import myapp.mvc.view.things.MyHSpacer;

/**
 * @brief Pannello per l'aggiunta di un esame parziale.
 *
 * Questa classe rappresenta l'interfaccia grafica per l'inserimento di esami composti da più prove parziali.
 * Permette di visualizzare le informazioni dell'esame selezionato e di aggiungere o eliminare prove parziali.
 */
public class AddPartialExamPane extends BorderPane {

    // ------------ COMPONENTS
    
    /** @brief Etichetta per il tipo di esame */
    private final Label lblType = new Label("Type");
    
    /** @brief Etichetta per il nome utente dello studente */
    private final Label lblUsername = new Label("Username");
    
    /** @brief Etichetta per il cognome dello studente */
    private final Label lblSurname = new Label("Surname");
    
    /** @brief Etichetta per l'insegnamento */
    private final Label lblTeaching = new Label("Teaching");
    
    /** @brief Etichetta per i crediti */
    private final Label lblCredits = new Label("Credits");
    
    /** @brief Campo di testo per il tipo di esame */
    private final TextField fldType = new TextField("Composed");
    
    /** @brief Campo di testo per il nome utente dello studente */
    private final TextField fldUsername = new TextField();
    
    /** @brief Campo di testo per il cognome dello studente */
    private final TextField fldSurname = new TextField();
    
    /** @brief Campo di testo per l'insegnamento */
    private final TextField fldTeaching = new TextField();
    
    /** @brief Campo di testo per i crediti */
    private final TextField fldCredits = new TextField();
    
    /** @brief Pulsante per aggiungere una prova parziale */
    private final Button btnAddPartial = new Button("+");
    
    /** @brief Pulsante per eliminare una prova parziale */
    private final Button btnDelPartial = new Button("-");
    
    /** @brief Pulsante per annullare l'operazione */
    private final Button btnCancel = new Button("Cancel");
    
    /** @brief Pulsante per aggiungere un nuovo esame */
    private final Button btnAdd = new Button("Add New");
    
    // ------------ INTERFACES
    
    /** @brief Gestore degli esami */
    private final ManageExams manageExams;
    
    /** @brief Interfaccia per la chiusura del dialogo */
    private CommonDialogInterface commonDialogInterface;
    
    // ------------ VARIABLES
    
    /** @brief L'esame selezionato a cui aggiungere le prove parziali */
    private Exam selectedExam;
    
    // ------------ CONSTRUCTOR
    
    /**
     * @brief Costruttore del pannello per l'aggiunta di prove parziali a un esame composto.
     *
     * Inizializza i campi e le interfacce con i dati dell'esame selezionato.
     *
     * @param manageExams Il gestore degli esami
     * @param selectedExam L'esame selezionato
     */
    public AddPartialExamPane(ManageExams manageExams, Exam selectedExam) {

        this.manageExams = manageExams;
        this.selectedExam = selectedExam;

        this.setupFrame();
        this.setupComponents();
    }
    
    /**
     * @brief Configura il layout del frame.
     *
     * Metodo placeholder per future configurazioni del layout del frame.
     */
    private void setupFrame() {}
    
    /**
     * @brief Configura i componenti del pannello.
     *
     * Questo metodo configura i campi e i layout per l'inserimento e la gestione delle prove parziali.
     */
    private void setupComponents() {

        // -----------------------------------

        fldUsername.setText(selectedExam.getUsername());
        fldUsername.setEditable(false);

        fldSurname.setText(selectedExam.getSurname());
        fldSurname.setEditable(false);

        fldTeaching.setText(selectedExam.getTeaching());
        fldTeaching.setEditable(false);

        fldCredits.setText(String.valueOf(selectedExam.getCredits()));
        fldCredits.setEditable(false);

        fldType.setEditable(false);

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
        
        grid.getChildren().setAll(
            lblType, fldType,
            lblUsername, fldUsername,
            lblSurname, fldSurname,
            lblTeaching, fldTeaching,
            lblCredits, fldCredits
        );
        
        // -----------------------------------
        
        // Layout per la gestione delle prove parziali
        PartialExamsLayout partialExamsLayout = new PartialExamsLayout(1, 5);
        
        // -----------------------------------
        
        btnAddPartial.setOnAction(e -> partialExamsLayout.addPartial());
        
        btnDelPartial.setOnAction(e -> partialExamsLayout.delPartial());
        
        btnCancel.setOnAction(e -> commonDialogInterface.CloseWindow());

        btnAdd.setOnAction(e -> {
            
            for (PartialExamField partial : partialExamsLayout.getPartialExamFields()) {
                manageExams.addIntermediateGrade(
                    selectedExam.getUniqueId(),
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
        bot.setPadding(new Insets(10, 0, 0, 0));
        bot.getChildren().addAll(btnAddPartial, btnDelPartial, new MyHSpacer(), btnCancel, btnAdd);

        // -----------------------------------

        VBox mainLayout = new VBox();
        mainLayout.setPadding(new Insets(10, 10, 10, 10));
        mainLayout.getChildren().addAll(grid, partialExamsLayout, bot);

        // -----------------------------------

        this.setLeft(mainLayout);
    }
    
    /**
     * @brief Imposta l'interfaccia per la chiusura del dialogo.
     *
     * Questo metodo consente di impostare l'interfaccia per chiudere la finestra di dialogo.
     *
     * @param commonDialogInterface Interfaccia per la chiusura del dialogo
     */
    public void setCommonDialogInterface(CommonDialogInterface commonDialogInterface) {
        this.commonDialogInterface = commonDialogInterface;
    }

}
