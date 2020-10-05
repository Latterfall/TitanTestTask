package app.model.JSON;

public class CalculatingFunctionJSON {
    private int initialValue;
    private String function;

    public CalculatingFunctionJSON(int initialValue, String function) {
        this.initialValue = initialValue;
        this.function = function;
    }

    public int getInitialValue() {
        return initialValue;
    }

    public String getFunction() {
        return function;
    }
}
