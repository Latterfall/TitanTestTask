package app.model;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.util.Locale;

public class CalculatingFunction {
    // Initial values
    private final byte functionNumber;
    private final String answerType;
    private final long delay;

    // JS parsing objects
    private final Value polyglotFunction;

    // Mutable values
    private int iterationNumber;
    private double inputParameter;

    public CalculatingFunction(byte functionNumber, int initialValue, String function, String answerType, long delay) {
        this.functionNumber = functionNumber;
        this.answerType = answerType;

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

        double result;

        long start = System.nanoTime();
        synchronized (this) {
            result = polyglotFunction.execute(inputParameter).asDouble();
        }
        long evaluationTime = System.nanoTime() - start;

        inputParameter = result;
        iterationNumber++;

        switch (answerType) {
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
