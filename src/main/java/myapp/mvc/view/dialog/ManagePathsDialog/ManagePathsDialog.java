package myapp.mvc.view.dialog.ManagePathsDialog;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import myapp.mvc.model.entity.path.UniquePath;
import myapp.mvc.model.manager.ManageExams;
import myapp.mvc.model.manager.ManagePaths;
import myapp.mvc.view.dialog.CommonDialogInterface;

/**
 * @brief Gestisce la visualizzazione della finestra di dialogo per la gestione dei percorsi.
 *
 * Questa classe fornisce un metodo statico per visualizzare una finestra di dialogo che permette di gestire
 * i percorsi dei file. Utilizza la classe {@link ManagePathsPane} per visualizzare e manipolare i percorsi.
 */
public class ManagePathsDialog {
    
    /** @brief Pannello di gestione dei percorsi */
    private static ManagePathsPane pathsPane;
    
    /**
     * @brief Mostra la finestra di dialogo per la gestione dei percorsi.
     *
     * Questo metodo crea e mostra una finestra di dialogo modale che permette all'utente di gestire i percorsi
     * dei file. La finestra di dialogo include un pannello {@link ManagePathsPane} per la visualizzazione e la
     * manipolazione dei percorsi.
     *
     * @param managePaths Gestore dei percorsi
     * @param manageExams Gestore degli esami
     */
    public static void display(ManagePaths managePaths, ManageExams manageExams) {
        // Creazione della finestra di dialogo
        final Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Manage File Paths");
        window.setResizable(false);
        window.sizeToScene();
        
        // Configurazione del pannello di gestione dei percorsi
        pathsPane = new ManagePathsPane(managePaths, manageExams);
        pathsPane.setData(managePaths.getAllPaths());
        
        // Imposta l'interfaccia per chiudere la finestra di dialogo
        CommonDialogInterface commonDialogInterface = window::close;
        pathsPane.setCommonDialogInterface(commonDialogInterface);
        
        // Configura e mostra la scena
        Scene scene = new Scene(pathsPane);
        window.setScene(scene);
        window.showAndWait(); // Mostra la finestra e attende che venga chiusa prima di ritornare al chiamante
    }
    
    /**
     * @brief Imposta i dati dei percorsi nella finestra di dialogo.
     *
     * Questo metodo aggiorna i dati visualizzati nel pannello di gestione dei percorsi.
     *
     * @param data Lista osservabile di percorsi da visualizzare
     */
    public static void setPathData(ObservableList<UniquePath> data) {
        pathsPane.setData(data);
    }
}
