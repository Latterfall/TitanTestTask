package app.model.JSON;

public class CalculatingFunctionJSONPair {
    private CalculatingFunctionJSON functionOne;
    private CalculatingFunctionJSON functionTwo;

    public CalculatingFunctionJSONPair(CalculatingFunctionJSON functionOne, CalculatingFunctionJSON functionTwo) {
        this.functionOne = functionOne;
        this.functionTwo = functionTwo;
    }

    public CalculatingFunctionJSON getFunctionOne() {
        return functionOne;
    }

    public CalculatingFunctionJSON getFunctionTwo() {
        return functionTwo;
    }
}
