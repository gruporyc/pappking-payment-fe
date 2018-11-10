package co.ppk;

public class ModelEntry {

    private final String key;
    private final Object value;

    private ModelEntry(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static ModelEntry withModel(String key, Object value) {
        return new ModelEntry(key, value);
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
