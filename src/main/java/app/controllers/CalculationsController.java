package app.controllers;

import app.model.JSON.CalculatingFunctionJSONPair;
import app.services.CalculationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/api/calculations")
public class CalculationsController {
    private final CalculationService calculationService;

    @Autowired
    public CalculationsController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @PostMapping(path = "/run", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> calculateFunctions (
            @RequestBody CalculatingFunctionJSONPair functions,
            @RequestParam(name = "numberOfCalculations") int numberOfCalculations,
            @RequestParam(name = "answerType") String answerType
    ) {
/*        return calculationService.calculateFunctions(
                new CalculatingFunctionJSONPair(
                        new CalculatingFunctionJSON(2, "x => x * 2"),
                        new CalculatingFunctionJSON(2, "x => x + 7")
                ), numberOfCalculations, answerType);*/
        return calculationService.calculateFunctions(functions, numberOfCalculations, answerType);
    }
}
