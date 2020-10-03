package app.handlers;

import app.model.FunctionPair;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class CalculationHandler {
    private static final Logger logger = LogManager.getLogger(CalculationHandler.class);

    public Mono<ServerResponse> runFunction(ServerRequest serverRequest) {
        String functionOneJSON = serverRequest.queryParam("functionOne").get();
        String functionTwo = serverRequest.queryParam("functionTwo").get();
        int numberOfCalculations = Integer.parseInt(serverRequest.queryParam("calcNum").get());
        //String ansType = serverRequest.queryParam("ansType").get();

        FunctionPair functionPair = processJSON(functionOneJSON);
        double result = calculateFunction(functionPair, numberOfCalculations);
        return ServerResponse.ok().body(BodyInserters.fromValue(result));
    }

    private FunctionPair processJSON(String JSON) {
        Gson gson = new Gson();
        return gson.fromJson(JSON, FunctionPair.class);
    }

    private double calculateFunction(FunctionPair functionToCalculate, int numberOfCalculations) {
        Context context = Context.create();
        Value polyglotFunction = context.eval("js", functionToCalculate.function);
        double result = functionToCalculate.initialValue;
        for (int i = 0; i < numberOfCalculations; i++) {
            result = polyglotFunction.execute(result).asDouble();
        }
        return result;
    }
}
