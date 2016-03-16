package main.model.entities;

import main.MainClassFromGUI;

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
		return MainClassFromGUI.dictionaryController.getCodeOf(id) + " : " + text;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean res = false;
		DictionaryEntity dictionaryEntity;
		if(obj instanceof DictionaryEntity){
			dictionaryEntity = (DictionaryEntity) obj;
			res = text.equals(dictionaryEntity.getText());
		}
		return res;
	}

}
