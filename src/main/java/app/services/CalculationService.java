package app.services;

import app.model.CalculatingFunction;
import app.model.JSON.CalculatingFunctionJSON;
import app.model.JSON.CalculatingFunctionJSONPair;

import org.graalvm.polyglot.PolyglotException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CalculationService {
    @Value("${function-one-calculations-delay}")
    private long delayOne;

    @Value("${function-two-calculations-delay}")
    private long delayTwo;

    public Flux<String> calculateFunctions (
            CalculatingFunctionJSONPair calculatingFunctionJSONPair,
            int numberOfCalculations,
            String answerType
    ) {
        CalculatingFunction calculatingFunctionOne;
        CalculatingFunction calculatingFunctionTwo;
        try {
            calculatingFunctionOne = transformJSONFunctionToObject((byte) 1, calculatingFunctionJSONPair.getFunctionOne(), answerType, delayOne * 1000);
            calculatingFunctionTwo = transformJSONFunctionToObject((byte) 2, calculatingFunctionJSONPair.getFunctionTwo(), answerType, delayTwo * 1000);
        } catch (PolyglotException exception) {
            return Flux.just("Ошибка в функции JS!");
        }

        if (numberOfCalculations < 1) return Flux.just("Количество подсчётов меньше одного!");

        if (!answerType.equals("sorted") && !answerType.equals("unsorted")) return Flux.just("Неподдерживаемый тип ответа!");

        Flux<String> fluxOne = createFluxFromFunction(calculatingFunctionOne, numberOfCalculations);
        Flux<String> fluxTwo = createFluxFromFunction(calculatingFunctionTwo, numberOfCalculations);
        switch (answerType) {
            case "sorted":
                AtomicInteger currentIteration = new AtomicInteger(1);
                return Flux
                        .zip(
                            fluxOne,
                            fluxTwo,
                            (one, two) -> {
                                int numberOfCalculatedAheadFunctionsOne = calculatingFunctionOne.getIterationNumber() - currentIteration.get();
                                int numberOfCalculatedAheadFunctionsTwo = calculatingFunctionTwo.getIterationNumber() - currentIteration.get();

                                return
                                        currentIteration.getAndIncrement() + "," +
                                        one + "," + numberOfCalculatedAheadFunctionsOne + "," +
                                        two + "," + numberOfCalculatedAheadFunctionsTwo;
                            })
                        .defaultIfEmpty("Ошибка в функции JS!");
            case "unsorted":
                return Flux.merge(fluxOne, fluxTwo);
            default:
                return null;
        }
    }

    private CalculatingFunction transformJSONFunctionToObject(byte functionNumber, CalculatingFunctionJSON calculatingFunctionJSON, String isOutputSorted, long delay) {
        return new CalculatingFunction(
                functionNumber,
                calculatingFunctionJSON.getInitialValue(),
                calculatingFunctionJSON.getFunction(),
                isOutputSorted,
                delay
        );
    }

    private Flux<String> createFluxFromFunction(CalculatingFunction calculatingFunction, int numberOfCalculations)  {
        return Flux
                .just(calculatingFunction)
                .flatMap(
                        function -> Mono.fromFuture(
                                CompletableFuture
                                        .supplyAsync(function::calculate)
                                        .exceptionally(exception -> null)
                        )
                )
                .repeat(numberOfCalculations - 1);
    }
}