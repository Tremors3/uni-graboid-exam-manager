package myapp.mvc.view.dialog.GraphDialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import myapp.mvc.model.entity.exam.Exam;
import myapp.mvc.view.dialog.CommonDialogInterface;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @brief Pannello per la visualizzazione di un grafico a barre.
 *
 * Questo pannello visualizza un grafico a barre che rappresenta le valutazioni degli studenti. Include anche
 * un pannello inferiore che mostra la media totale delle valutazioni.
 */
public class GraphPane extends BorderPane {
    
    // ------------ COMPONENTI
    
    /** Etichetta per la media totale */
    private final Label lblAverage = new Label("Total average:");
    
    /** Valore della media totale */
    private final Label valAverage = new Label();
    
    // ------------ INTERFACCE
    
    /** Interfaccia per la chiusura della finestra di dialogo */
    private CommonDialogInterface commonDialogInterface;
    
    // ------------ ALTRO
    
    /**
     * @brief Costruisce un pannello per il grafico a barre.
     *
     * Questo costruttore inizializza il pannello configurando il layout e il grafico a barre in base ai dati forniti.
     *
     * @param title Titolo del grafico
     * @param studentGrades Mappa dei voti degli studenti, con chiavi come nomi degli studenti e valori come array di voti
     * @param size Dimensione del grafico
     */
    public GraphPane(String title, Map<String, int[]> studentGrades, int size) {
        this.setupPane();
        this.setupComponents(title, studentGrades, size);
    }
    
    /**
     * @brief Configura il layout del pannello.
     *
     * Imposta il padding del pannello per garantire uno spazio uniforme tra i bordi e i componenti.
     */
    private void setupPane() {
        this.setPadding(new Insets(10, 10, 10, 10));
    }
    
    /**
     * @brief Configura i componenti del pannello.
     *
     * Imposta il grafico a barre e il pannello delle statistiche basati sui dati forniti.
     *
     * @param title Titolo del grafico
     * @param studentGrades Mappa dei voti degli studenti
     * @param size Dimensione del grafico
     */
    private void setupComponents(String title, Map<String, int[]> studentGrades, int size) {
        // Creazione delle categorie per l'asse X
        ObservableList<String> categories = FXCollections.observableArrayList();
        for (int i = Exam.GRADE_BOUNDARIES[0]; i <= Exam.GRADE_BOUNDARIES[1]; i++) {
            categories.add("" + i);
        }
        
        // Definizione degli assi
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(categories);
        xAxis.setLabel("Grades");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Occurrences");
        yAxis.setTickLabelFormatter(new StringConverter<Number>() {
            
            private final DecimalFormat formatter = new DecimalFormat("0");
            
            @Override
            public String toString(Number number) {
                if (number.intValue() != number.doubleValue()) return "";
                return formatter.format(number);
            }
            
            @Override
            public Number fromString(String string) {
                return Double.parseDouble(string);
            }
        });
        
        // Creazione del grafico a barre
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(title);
        
        // Creazione delle serie di dati
        List<XYChart.Series<String, Number>> series = new ArrayList<>();
        
        for (String student : studentGrades.keySet()) {
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName(student);
            
            for (int i = Exam.GRADE_BOUNDARIES[0]; i <= Exam.GRADE_BOUNDARIES[1]; i++) {
                serie.getData().add(new XYChart.Data<>("" + i, studentGrades.get(student)[i - Exam.GRADE_BOUNDARIES[0]]));
            }
            
            series.add(serie);
        }
        
        // Imposta i dati nel grafico a barre
        barChart.getData().addAll(series);
        this.setCenter(barChart);
        
        // -----------------------------------
        
        // Calcolo e visualizzazione della media totale
        valAverage.setText(
            String.format("%.1f", getTotalAverage(studentGrades.values()))
        );
        
        // Configurazione del pannello delle statistiche
        GridPane statPane = new GridPane();
        statPane.setPadding(new Insets(5, 0, 0, 0));
        statPane.setVgap(10);
        statPane.setHgap(10);
        
        GridPane.setConstraints(lblAverage, 0, 0);
        GridPane.setConstraints(valAverage, 1, 0);
        
        statPane.getChildren().addAll(lblAverage, valAverage);
        
        this.setBottom(statPane);
    }
    
    /**
     * @brief Calcola la media totale delle valutazioni.
     *
     * Questo metodo calcola la media ponderata delle valutazioni basata sui dati forniti.
     *
     * @param onlyGrades Collezione di array di voti
     * @return La media totale delle valutazioni
     */
    private float getTotalAverage(Collection<int[]> onlyGrades) {
        int sum = 0;
        int counter = 0;
        
        for (int[] grades : onlyGrades) {
            for (int i = 0; i <= Exam.GRADE_BOUNDARIES[1] - Exam.GRADE_BOUNDARIES[0]; i++) {
                sum += grades[i] * (Exam.GRADE_BOUNDARIES[0] + i);
                counter += grades[i];
            }
        }
        
        return (float) sum / counter;
    }
    
    /**
     * @brief Imposta l'interfaccia per la chiusura della finestra di dialogo.
     *
     * @param commonDialogInterface Interfaccia per la chiusura della finestra di dialogo
     */
    public void setCommonDialogInterface(CommonDialogInterface commonDialogInterface) {
        this.commonDialogInterface = commonDialogInterface;
    }
}
