package cn.sh.ideal.jdbc.datasource.router;

public enum DataSourceType {
	MASTER("master"), SALVE("salve");
	private final String value;

	private DataSourceType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getValue();
	}

}
