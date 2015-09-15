
public class RowData {

	private String name;
	private String value;
	private String contentFromJavaFile;
	private String contentFromXmlFile;
	
	private boolean highLight = false;
	private boolean firstRow;

	public RowData(String name, String value, String contentFromJavaFile, String contentFromXmlFile) {
		this.name = name;
		this.value = value;
		this.contentFromJavaFile = contentFromJavaFile;
		this.contentFromXmlFile = contentFromXmlFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getContentFromJavaFile() {
		return contentFromJavaFile;
	}

	public void setContentFromJavaFile(String contentFromJavaFile) {
		this.contentFromJavaFile = contentFromJavaFile;
	}

	public String getContentFromXmlFile() {
		return contentFromXmlFile;
	}

	public void setContentFromXmlFile(String contentFromXmlFile) {
		this.contentFromXmlFile = contentFromXmlFile;
	}

	public boolean isHighLight() {
		return highLight;
	}

	public void setHighLight(boolean highLight) {
		this.highLight = highLight;
	}

	public boolean isFirstRow() {
		return firstRow;
	}

	public void setFirstRow(boolean firstRow) {
		this.firstRow = firstRow;
	}
	
}
