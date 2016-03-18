package hibernate.models.entities;

import main.HexBiController;

public class DictionaryEntity {
	private int id;
	private int codeId;
	private String text;

	public DictionaryEntity() {
		// TODO Auto-generated constructor stub
	}

	public DictionaryEntity(String code, String text) {
		setCodeId(HexBiController.hexBiToInteger(code));
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
		return HexBiController.intToHexBi(codeId) + " : " + text;
	}

	@Override
	public boolean equals(Object obj) {
		boolean res = false;
		DictionaryEntity dictionaryEntity;
		if (obj instanceof DictionaryEntity) {
			dictionaryEntity = (DictionaryEntity) obj;
			res = text.equals(dictionaryEntity.getText());
		}
		return res;
	}

	public int getCodeId() {
		return codeId;
	}

	public void setCodeId(int codeId) {
		this.codeId = codeId;
	}

}
