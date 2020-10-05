package app.model;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

import java.util.Locale;

public class CalculatingFunction {
    // Initial values
    private final byte functionNumber;
    private final String isOutputSorted;

    // JS parsing objects
    private final Value polyglotFunction;

    // Mutable values
    private int iterationNumber;

    // Transitional values
    private double inputParameter;

    // DEBUG
    private final long delay;

    public CalculatingFunction(byte functionNumber, int initialValue, String function, String isOutputSorted, long delay) throws PolyglotException {
        this.functionNumber = functionNumber;
        this.isOutputSorted = isOutputSorted;

        Context context = Context.create();
        polyglotFunction = context.eval("js", function);

        iterationNumber = 0;
        inputParameter = initialValue;

        this.delay = delay;
    }

    public String calculate() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long start = System.nanoTime();

        double result;
        synchronized (this) {
            result = polyglotFunction.execute(inputParameter).asDouble();
        }
        inputParameter = result;
        iterationNumber++;

        long evaluationTime = System.nanoTime() - start;

        switch (isOutputSorted) {
            case "sorted":
                return
                        String.format(Locale.US, "%.2f", result) + "," +
                                evaluationTime;
            case "unsorted":
                return
                        iterationNumber + "," +
                        functionNumber + "," +
                        String.format(Locale.US, "%.2f", result) + "," +
                                evaluationTime;
                        //evaluationTime/1000000 + "мс " + evaluationTime%1000000/1000 + "мкс ]";
            default:
                return null;
        }
    }

    public int getIterationNumber() {
        return iterationNumber;
    }
}
