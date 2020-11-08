package ui;

import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public abstract class View implements IView {
	
	protected static Image ICON_IMAGE = new Image("Icon.png");
	
	protected Stage stage;
	protected StringProperty fileName;
	
	public View() {
		this(new Stage(), null);
	}

	public View(Stage stage){
		this(stage, null);
	}

	public View(StringProperty fileName){
		this(new Stage(), fileName);
	}

	public View(Stage stage, StringProperty fileName) {
		this.stage = stage;
		this.stage.getIcons().add(ICON_IMAGE);
		this.stage.setOnCloseRequest(ev -> onClose());
		this.fileName = fileName;
	}

	protected void onClose() {}

	public void show(){
		stage.show();
	}

	public Stage getStage() {
		return stage;
	}
}
