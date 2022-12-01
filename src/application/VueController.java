package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class VueController {
	@FXML
	private Label billLbl;
	
	@FXML
	private Label tipLbl;
	
	@FXML
	private Label nbLbl;
	
	@FXML
	private TextField billInput;
	
	@FXML
	private TextField tipInput;
	
	@FXML
	private TextField nbInput;
	
	@FXML
	private Button calculateBtn;
	
	@FXML
	private Label tipPerPersonLbl;
	
	@FXML
	private Label totalPerPersonLbl;
	
	@FXML
	private TextField tipPerPersonInput;
	
	@FXML
	private TextField totalPerPersonInput;
	
	@FXML
	private Label errorLbl;
	
	@FXML
	private Label dateLbl;
	
	@FXML
	private DatePicker dateInput;
	
	public void clear() {
		this.errorLbl.setText("");
		this.dateInput.getEditor().clear();
		this.billInput.setText("");
		this.tipInput.setText("");
		this.nbInput.setText("");
		this.tipPerPersonInput.setText("");
		this.totalPerPersonInput.setText("");
	}
	
	public void calculate() throws IOException{
		this.errorLbl.setText("");
		float billValue = 0;
		float tipValue = 0;
		float nbValue = 0;
		LocalDate dateValue = null;
		try {
			billValue = this.isNumber(this.billInput.getText(), "bill");
			tipValue = this.isNumber(this.tipInput.getText(), "tip");
			nbValue = this.isNumber(this.nbInput.getText(), "Nb people");
			dateValue = this.dateFormat(this.dateInput.getValue(), "Date of tip calcul");
			
			this.isZero(billValue, "bill");
			this.isZero(tipValue, "tip");
			this.isZero(nbValue, "Nb people");
			
			this.isNegative(billValue, "bill");
			this.isNegative(tipValue, "tip");
			this.isNegative(nbValue, "Nb people");
			
			float tipPerPersonValue = (billValue * tipValue / 100) / nbValue;
			float totalPerPersonValue = (billValue / nbValue) + tipPerPersonValue;
			
			this.tipPerPersonInput.setText(Float.toString(tipPerPersonValue));
			this.totalPerPersonInput.setText(Float.toString(totalPerPersonValue));
			
			String texte = dateValue + ";" + billValue + ";" + tipValue + ";" + nbValue + "\n";
			FileWriter fw = new FileWriter("archives.txt", true);
			
			String stringDate = dateValue.toString();
			
			if(this.isSameDate(dateValue)) stringDate = "\n" + stringDate + "\n";
				
			for (int i = 0; i < texte.length(); i++) 
			{
				fw.write(texte.charAt(i));
			}
			fw.close();
			
		}catch(NumberFormatException e) {
			this.errorLbl.setText(e.getMessage());
		}catch(IndexOutOfBoundsException e) {
			this.errorLbl.setText(e.getMessage());
		}

	}
	
	private float isNumber(String input, String el) throws NumberFormatException {
		try {
			float number = Float.parseFloat(input);
			return number;
		}catch(NumberFormatException e) {
            throw new NumberFormatException("La valeur du champs " + el + " n'est pas numérique.");
		}
	}
	
	private void isZero(float input, String el) throws IndexOutOfBoundsException {
		if(input == 0) {
            throw new IndexOutOfBoundsException("La valeur du champs " + el + " ne peut pas être égal à zéro");
		}
	}
	
	private void isNegative(float input, String el) throws IndexOutOfBoundsException {
		if(input < 0) {
            throw new IndexOutOfBoundsException("La valeur du champs " + el + " ne peut pas être négative");
		}
	}
	
	private LocalDate dateFormat(LocalDate input, String el) throws IndexOutOfBoundsException {
		if(input == null) {
            throw new IndexOutOfBoundsException("Le champ " + el + " n'est pas au bon format");
		}else {
		return input;
		}
	}
	
	private boolean isSameDate (LocalDate date) throws IOException {
		Scanner scanFile = new Scanner(new File("archives.txt"));
		String lines = "";
		boolean sameDate = false;
		while (scanFile.hasNextLine()) {
			 String line = scanFile.nextLine();
			 
			 if(line.contains(date.toString())){
				 sameDate = true;
			 }else {
				 lines += line ; 
			 }
		 }
		if(sameDate) {
			try(FileWriter buff = new FileWriter("archives.txt")){
				buff.write(lines);
			}
		}
		return sameDate;
	}
	
	public void fillFile() throws IOException {
		Scanner scan = new Scanner(new File("archives.txt"));
		LocalDate date = this.dateFormat(this.dateInput.getValue(), "Date");	
		
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			if(line.contains(date.toString())) {
				String[]value = line.split(";");
				this.billInput.setText(value[1]);
				this.tipInput.setText(value[2]);
				this.nbInput.setText(value[3]);
			}
		}
	
	}
}


