package myapp.mvc.model;

import myapp.mvc.model.entity.exam.HasUniqueId;
import myapp.util.UniqueList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @brief Classe che rappresenta un database generico basato su file.
 *
 * Questa classe fornisce funzionalità per gestire un insieme di oggetti univoci
 * che implementano l'interfaccia HasUniqueId. Permette di salvare e caricare i dati
 * su file, aggiungere e rimuovere record, e comparare file.
 *
 * @tparam T Il tipo di oggetto che estende l'interfaccia HasUniqueId.
 */
public class Database<T extends HasUniqueId> {
    
    /** @brief Nome predefinito del file del database. */
    protected static final String DEFAULT_DATABASE_FILE_NAME = "session_recovery.tmp";
    
    /** @brief Nome predefinito del file temporaneo utilizzato per confronti. */
    protected static final String DEFAULT_TEMPORARY_FILE_NAME = "compare_buffer.tmp";
    
    /** @brief Percorso predefinito del file del database. */
    protected static final String DEFAULT_DATABASE_FILE_PATH = System.getProperty("user.dir") + '/' + DEFAULT_DATABASE_FILE_NAME;
    
    /** @brief Percorso predefinito del file temporaneo. */
    protected static final String DEFAULT_TEMPORARY_FILE_PATH = System.getProperty("user.dir") + '/' + DEFAULT_TEMPORARY_FILE_NAME;
    
    /** @brief Nome del file corrente usato per il database. */
    protected String CURRENT_FILE_NAME;
    
    /** @brief Struttura dati che contiene tutti i record univoci. */
    private final UniqueList<T> structure = new UniqueList<>();
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Costruttore di default.
     *
     * Inizializza il database con il file predefinito.
     */
    public Database() {
        this(Database.DEFAULT_DATABASE_FILE_PATH);
    }
    
    /**
     * @brief Costruttore che accetta un file specifico.
     *
     * @param fileName Nome del file da usare per caricare e salvare i dati.
     */
    public Database(String fileName) {
        this.CURRENT_FILE_NAME = fileName;
        this.loadFromFile(this.CURRENT_FILE_NAME);
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Aggiungi un record al database.
     *
     * @param record Il record da aggiungere.
     * @return L'ID univoco del record aggiunto.
     */
    public synchronized String add(T record) {
        return this.structure.addUnique(record);
    }
    
    /**
     * @brief Rimuovi un record dal database.
     *
     * @param uniqueId L'ID univoco del record da rimuovere.
     * @return true se il record è stato rimosso, false altrimenti.
     */
    public synchronized boolean del(String uniqueId) {
        return this.structure.delUnique(uniqueId);
    }
    
    /**
     * @brief Ottieni un record dal database.
     *
     * @param uniqueId L'ID univoco del record da ottenere.
     * @return Il record associato all'ID univoco, null se non esiste.
     */
    public synchronized T get(String uniqueId) {
        return this.structure.getUnique(uniqueId);
    }
    
    /**
     * @brief Ottieni tutti i record presenti nel database.
     *
     * @return Una lista contenente tutti i record.
     */
    public synchronized List<T> getAll() {
        return this.structure.stream().toList();
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Salva i record del database sul file corrente.
     */
    public synchronized void saveToFile() {
        this.saveToFile(this.CURRENT_FILE_NAME);
    }
    
    /**
     * @brief Salva i record del database su un file specificato.
     *
     * @param path Percorso del file dove salvare i dati.
     */
    public synchronized void saveToFile(String path) {
        try {
            this.writeToFile(new FileOutputStream(path), this.getAll());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @brief Salva i record del database su un file specificato.
     *
     * @param file Oggetto File dove salvare i dati.
     */
    public synchronized void saveToFile(File file) {
        try {
            this.writeToFile(new FileOutputStream(file), this.getAll());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Carica i record dal file corrente nel database.
     */
    public synchronized void loadFromFile() {
        this.loadFromFile(this.CURRENT_FILE_NAME);
    }
    
    /**
     * @brief Carica i record da un file specificato nel database.
     *
     * @param path Percorso del file da cui caricare i dati.
     */
    public synchronized void loadFromFile(String path) {
        try {
            List<T> data = this.readFromFile(new FileInputStream(path));
            if (data != null) {
                this.structure.clear();
                this.structure.addAll(data);
            }
        } catch (IOException ignored) {
            // Ignora l'eccezione per evitare il crash.
        }
    }
    
    /**
     * @brief Carica i record da un file specificato nel database.
     *
     * @param file Oggetto File da cui caricare i dati.
     */
    public synchronized void loadFromFile(File file) {
        try {
            List<T> data = this.readFromFile(new FileInputStream(file));
            if (data != null) {
                this.structure.clear();
                this.structure.addAll(data);
            }
        } catch (IOException ignored) {
            // Ignora l'eccezione per evitare il crash.
        }
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Scrive la lista dei record su un file specificato.
     *
     * @param fOut FileOutputStream su cui scrivere i dati.
     * @param data Lista dei record da scrivere.
     */
    private synchronized void writeToFile(FileOutputStream fOut, List<T> data) {
        try (ObjectOutputStream oOut = new ObjectOutputStream(fOut)) {
            oOut.writeObject(data);
            oOut.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @brief Legge la lista dei record da un file specificato.
     *
     * @param fIn FileInputStream da cui leggere i dati.
     * @return La lista dei record letti dal file.
     */
    private synchronized List<T> readFromFile(FileInputStream fIn) {
        try (ObjectInputStream oIn = new ObjectInputStream(fIn)) {
            return (List<T>) oIn.readObject();
        } catch (IOException | ClassNotFoundException ignored) {
            // Ignora l'eccezione per evitare il crash.
        }
        return null;
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @brief Confronta i record del database corrente con quelli di un file specificato.
     *
     * Salva il database corrente in un file temporaneo e lo confronta con il file specificato.
     *
     * @param path Percorso del file da confrontare.
     * @return true se i file sono identici, false altrimenti.
     */
    public synchronized boolean compareFileToDatabase(String path) {
        if (null == path) return false;
        saveToFile(DEFAULT_TEMPORARY_FILE_PATH);
        return compareTwoFiles(DEFAULT_TEMPORARY_FILE_PATH, path);
    }
    
    /**
     * @brief Confronta due file per verificarne l'uguaglianza.
     *
     * @param file1Path Percorso del primo file.
     * @param file2Path Percorso del secondo file.
     * @return true se i file sono uguali, false altrimenti.
     */
    private synchronized boolean compareTwoFiles(String file1Path, String file2Path) {
        try {
            return Files.mismatch(Path.of(file1Path), Path.of(file2Path)) == -1L;
        } catch (IOException ignored) {
            // Ignora l'eccezione per evitare il crash.
        }
        return false;
    }
    
}
