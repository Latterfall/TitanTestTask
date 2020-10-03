package app.routers;

import app.handlers.CalculationHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CalculationRouter {
    @Bean
    @RouterOperation(
            path = "/api/calculator/run",
            operation = @Operation(
                    operationId = "runCalculator",
                    summary = "Принимает на вход js функции, количество итераций рассчётов, которые необходимо выполнить, вид выдачи ответа (по мере поступления, либо целиком в CSV-формате).",
                    parameters = {
                            @Parameter(
                                    in = ParameterIn.QUERY,
                                    name = "functionOne",
                                    required = true,
                                    description = "Первая функция."
                            ),
                            @Parameter(
                                    in = ParameterIn.QUERY,
                                    name = "functionTwo",
                                    description = "Вторая функция."
                            ),
                            @Parameter(
                                    in = ParameterIn.QUERY,
                                    name = "calcNum",
                                    required = true,
                                    description = "Количество итераций рассчетов."
                            ),
                            @Parameter(
                                    in = ParameterIn.QUERY,
                                    name = "ansType",
                                    description = "Тип ответа."
                            )
                    }
            )
    )
    public RouterFunction<ServerResponse> runCalculator(CalculationHandler calculationHandler) {
        return RouterFunctions.route(RequestPredicates.POST("/api/calculator/run"), calculationHandler::runFunction);
    }
}
