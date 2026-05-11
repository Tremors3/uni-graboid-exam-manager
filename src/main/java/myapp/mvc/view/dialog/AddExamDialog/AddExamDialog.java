package myapp.mvc.view.dialog.AddExamDialog;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import myapp.mvc.model.entity.exam.ComposedExam;
import myapp.mvc.model.entity.exam.Exam;
import myapp.mvc.model.manager.ManageExams;
import myapp.mvc.view.dialog.AddExamDialog.tab.AddComposedExamPane;
import myapp.mvc.view.dialog.AddExamDialog.tab.AddPartialExamPane;
import myapp.mvc.view.dialog.AddExamDialog.tab.AddSimpleExamPane;
import myapp.mvc.view.dialog.CommonDialogInterface;

/**
 * @brief Dialogo per l'aggiunta di un esame.
 *
 * Questa classe gestisce la visualizzazione di un dialogo modale che permette all'utente di aggiungere un nuovo esame.
 * Il dialogo presenta diverse schede a seconda del tipo di esame che si desidera aggiungere.
 */
public class AddExamDialog {
    
    /**
     * @brief Mostra il dialogo per l'aggiunta di un nuovo esame.
     *
     * Questo metodo crea e visualizza una finestra di dialogo modale con diverse schede, ciascuna dedicata a un tipo di esame:
     * semplice, composto e parziale. Se l'esame selezionato è di tipo Composed, viene aggiunta anche una scheda per gli esami parziali.
     *
     * @param manageExams Gestore degli esami
     * @param selectedExam Esame selezionato (può essere di tipo Composed)
     */
    public static void display(ManageExams manageExams, Exam selectedExam) {
        
        final Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Adding a new Exam");
        window.setResizable(false);
        window.sizeToScene();
        
        // -----------------------------------
        
        // Creazione dei pannelli per i tipi di esame
        AddSimpleExamPane simple = new AddSimpleExamPane(manageExams);
        AddComposedExamPane composed = new AddComposedExamPane(manageExams);
        
        // -----------------------------------
        
        // Configurazione dell'interfaccia per chiudere il dialogo
        CommonDialogInterface commonDialogInterface = window::close;
        
        simple.setCommonDialogInterface(commonDialogInterface);
        composed.setCommonDialogInterface(commonDialogInterface);
        
        // -----------------------------------
        
        // Creazione delle schede
        Tab simpleTab = new Tab("Simple"); // Nuovo esame semplice
        simpleTab.setClosable(false);
        simpleTab.setContent(simple);
        
        Tab composedTab = new Tab("Composed"); // Nuovo esame composto
        composedTab.setClosable(false);
        composedTab.setContent(composed);
        
        Tab partialTab = new Tab("Partial"); // Nuovo esame parziale
        partialTab.setClosable(false);
        
        // Creazione del layout principale con le schede
        TabPane mainLayout = new TabPane();
        mainLayout.getTabs().addAll(simpleTab, composedTab);
        
        // Aggiunta della scheda parziale solo se l'esame selezionato è di tipo Composed
        if (selectedExam instanceof ComposedExam) {
            AddPartialExamPane partial = new AddPartialExamPane(manageExams, selectedExam);
            partial.setCommonDialogInterface(commonDialogInterface);
            partialTab.setContent(partial);
            mainLayout.getTabs().add(partialTab);
        }
        
        // -----------------------------------
        
        // Creazione e visualizzazione della scena
        Scene scene = new Scene(mainLayout);
        window.setScene(scene);
        window.showAndWait(); // Mostra la finestra di dialogo e attende che venga chiusa prima di restituire il controllo
    }
}
