package edu.java.controller;

import edu.java.servises.interfaces.TgChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.example.dto.response.ApiErrorResponse;
import org.example.dto.response.StateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
public class ScrapperChatController {
    @Autowired
    private TgChatService tgChatService;

    @Operation(summary = "Зарегистрировать чат")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Чат зарегистрирован"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation
                        = ApiErrorResponse.class))
            })
    })

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String registerChat(@PathVariable @Valid @Positive Long id, @RequestBody String username) {
        tgChatService.register(id, username);
        return "Удачно!";
    }

    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Чат успешно удалён"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation
                        = ApiErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Чат не существует",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation
                        = ApiErrorResponse.class))
            })
    })
    @DeleteMapping("/{id}")
    public String deleteChat(@PathVariable @Valid @Positive Long id) {
        tgChatService.unregister(id);
        return "Чат удалён";
    }

    @Operation(summary = "Обновить статус")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Статус успешно обновлен"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation
                        = ApiErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Чат не существует",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation
                        = ApiErrorResponse.class))
            })
    })
    @PostMapping("/state")
    @ResponseStatus(HttpStatus.OK)
    public void setState(
        @RequestHeader(name = "Tg-Chat-Id") @Valid @Positive Long id,
        @RequestBody String state
    ) {
        tgChatService.setState(id, state);
    }

    @Operation(summary = "Получить статус")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Статус успешно получен"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation
                        = ApiErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Чат не существует",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation
                        = ApiErrorResponse.class))
            })
    })
    @GetMapping("/state/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StateResponse getState(@PathVariable @Valid @Positive Long id) {
        return tgChatService.getState(id);
    }

    @Operation(summary = "Проверка существования чата")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Статус успешно получен"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation
                        = ApiErrorResponse.class))
            }),
    })
    @GetMapping("/check-reg/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean isRegister(@PathVariable @Valid @Positive Long id) {
        return tgChatService.isRegister(id);
    }

}
