package main.model.entities;

import main.MainClass;

public class DictionaryEntity {
	private int id;
	private String text;

	public DictionaryEntity() {
		// TODO Auto-generated constructor stub
	}

	public DictionaryEntity(String text) {
		this.text = text.toUpperCase();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text.toUpperCase();
	}
	
	@Override
	public String toString() {
		return id + " | " + MainClass.dictionaryController.getCodeOf(id) + " : " + text;
	}

}
