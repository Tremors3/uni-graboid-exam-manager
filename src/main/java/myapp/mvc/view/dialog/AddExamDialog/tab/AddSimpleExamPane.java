package myapp.mvc.view.dialog.AddExamDialog.tab;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import myapp.mvc.model.manager.ManageExams;
import myapp.mvc.view.dialog.CommonDialogInterface;
import myapp.mvc.view.things.MyHSpacer;
import myapp.mvc.view.things.MySpinner;

/**
 * @brief Pannello per l'aggiunta di un esame semplice.
 *
 * Questa classe rappresenta il pannello che permette di inserire le informazioni relative a un esame semplice.
 * Il pannello include campi per il tipo, il nome, il cognome, l'insegnamento, i crediti, il voto finale e la lode.
 */
public class AddSimpleExamPane extends BorderPane {

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
    
    /** @brief Etichetta per il voto finale */
    private final Label lblFinalGrade = new Label("Grade");
    
    /** @brief Etichetta per la lode */
    private final Label lblHonors = new Label("Honors");
    
    /** @brief Campo di testo per il tipo di esame */
    private final TextField fldType = new TextField("Simple");
    
    /** @brief Campo di testo per il nome utente dello studente */
    private final TextField fldUsername = new TextField();
    
    /** @brief Campo di testo per il cognome dello studente */
    private final TextField fldSurname = new TextField();
    
    /** @brief Campo di testo per l'insegnamento */
    private final TextField fldTeaching = new TextField();
    
    /** @brief Spinner per i crediti dell'esame */
    private final MySpinner fldCredits = new MySpinner(3, 12, 3, 3);
    
    /** @brief Spinner per il voto finale dell'esame */
    private final MySpinner fldFinalGrade = new MySpinner(18, 30, 18, 1);
    
    /** @brief CheckBox per la lode */
    private final CheckBox chkHonors = new CheckBox();
    
    /** @brief Pulsante per annullare l'operazione */
    private final Button btnCancel = new Button("Cancel");
    
    /** @brief Pulsante per aggiungere un nuovo esame */
    private final Button btnAdd = new Button("Add New");

    // ------------ INTERFACES
    
    /** @brief Gestore degli esami */
    private final ManageExams manageExams;
    
    /** @brief Interfaccia per la chiusura del dialogo */
    private CommonDialogInterface commonDialogInterface;

    // ------------ CONSTRUCTOR
    
    /**
     * @brief Costruttore del pannello per l'aggiunta di un esame semplice.
     *
     * Inizializza il pannello e configura i componenti per l'aggiunta di un esame semplice.
     *
     * @param manageExams Gestore degli esami
     */
    public AddSimpleExamPane(ManageExams manageExams) {
        this.manageExams = manageExams;
        this.setupFrame();
        this.setupComponents();
    }
    
    /**
     * @brief Configura il layout del frame.
     */
    private void setupFrame() {}
    
    /**
     * @brief Configura i componenti del pannello.
     *
     * Questo metodo posiziona e configura i componenti del pannello per l'inserimento delle informazioni dell'esame.
     */
    private void setupComponents() {

        // -----------------------------------

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(50);
        
        // Imposta le posizioni dei componenti nella griglia
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

        fldType.setEditable(false);
        chkHonors.setSelected(false);

        grid.getChildren().setAll(
                lblType, fldType,
                lblUsername, fldUsername,
                lblSurname, fldSurname,
                lblTeaching, fldTeaching,
                lblCredits, fldCredits,
                lblFinalGrade, fldFinalGrade,
                lblHonors, chkHonors
        );

        // -----------------------------------
        
        // Configurazione del pulsante per annullare l'operazione
        btnCancel.setOnAction(e -> commonDialogInterface.CloseWindow());
        
        // Configurazione del pulsante per aggiungere un nuovo esame
        btnAdd.setOnAction(e -> {
            manageExams.addSimpleExam(
                    fldUsername.getText(),
                    fldSurname.getText(),
                    fldTeaching.getText(),
                    fldCredits.getValue(),
                    fldFinalGrade.getValue(),
                    chkHonors.isSelected()
            );
            commonDialogInterface.CloseWindow();
        });
        
        // Creazione della parte inferiore del pannello con i pulsanti
        HBox bot = new HBox();
        bot.setSpacing(10);
        bot.setPadding(new Insets(10, 0, 0, 0));
        bot.getChildren().addAll(new MyHSpacer(), btnCancel, btnAdd);

        // -----------------------------------
        
        // Creazione del layout principale e aggiunta dei componenti
        VBox mainLayout = new VBox();
        mainLayout.setPadding(new Insets(10, 10, 10, 10));
        mainLayout.getChildren().addAll(grid, bot);

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