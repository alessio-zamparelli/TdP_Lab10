package it.polito.tdp.porto;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {

	private Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ComboBox<Author> boxPrimo;

	@FXML
	private ComboBox<Author> boxSecondo;

	@FXML
	private TextArea txtResult;

	@FXML
	void handleCoautori(ActionEvent event) {
		Author autore = boxPrimo.getSelectionModel().getSelectedItem();
		if (autore == null) {
			// TODO;
			return;
		}
		List<Author> coAutori = model.getCoAutoriOf(autore);
		txtResult.setText("Coautori di " + autore + "\n");
		coAutori.forEach(a->txtResult.appendText(a.toString() + "\n"));
		List<Author> nonCoAutori = model.getAutori();
		nonCoAutori.removeAll(model.getCoAutoriOf(autore));
		boxSecondo.getItems().setAll(nonCoAutori);
	}

	@FXML
	void handleSequenza(ActionEvent event) {
		Author autore1 = boxPrimo.getSelectionModel().getSelectedItem();
		Author autore2 = boxSecondo.getSelectionModel().getSelectedItem();
		List<Paper> res = model.collegaAutori(autore1, autore2);
		txtResult.appendText("Lista di articoli da " + autore1.getId() + " a " + autore2.getId() + "\n\n");
		res.forEach(a->txtResult.appendText(a.toString() + "\n"));
	}

	@FXML
	void initialize() {
		assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
		assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;
		List<Author> autori = model.getAutori();
		this.boxPrimo.getItems().setAll(autori);
//		this.boxSecondo.getItems().setAll(autori);
	}
}
