package manager;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;

public class WorkbookService extends Service<Workbook> {

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    protected Task<Workbook> createTask() {
        return new Task<>() {

            @Override
            protected Workbook call() {
                try {
                    return WorkbookFactory.create(getFile());
                } catch (EncryptedDocumentException | IOException e) {
                    return null;
                }
            }

        };
    }

}
