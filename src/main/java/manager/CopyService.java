package manager;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class CopyService extends Service<File> {

	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	protected Task<File> createTask() {
		return new Task<>() {

            @Override
            protected File call() {
                File temp = new File(System.getProperty("java.io.tmpdir") + "/p&p-manager/" + file.getName());

                try {
                    FileUtils.copyFile(getFile(), temp);
                } catch (IOException e) {
                    return null;
                }

                return temp;
            }

        };
	}

}