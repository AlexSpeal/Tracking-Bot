package edu.java.controller;

import edu.java.errors.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.net.URISyntaxException;
import org.example.dto.request.AddLinkRequest;
import org.example.dto.response.ApiErrorResponse;
import org.example.dto.response.LinkResponse;
import org.example.dto.response.ListLinksResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/links")
public class ScrapperLinkController {
    @Operation(summary = "Получить все отслеживаемые ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылки успешно получены",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation
                    = ListLinksResponse.class))}
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ListLinksResponse getLinks(@RequestParam @Positive Long tgChatId)
        throws URISyntaxException {
        LinkResponse[] link =
            new LinkResponse[] {new LinkResponse(tgChatId, new URI("https://github.com/AlexSpeal/zxc")),
                new LinkResponse(tgChatId, new URI("https://github.com/AlexSpeal/qwerty"))};
        return new ListLinksResponse(link, 2);
    }

    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылки успешно добавлены",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation
                    = LinkResponse.class))}
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
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public LinkResponse setLink(
        @RequestParam @Positive Long tgChatId,
        @RequestBody @Valid AddLinkRequest addLinkRequest
    ) throws URISyntaxException {
        return new LinkResponse(tgChatId, new URI(addLinkRequest.link()));
    }

    @Operation(summary = "Убрать отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылка успешно убрана",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation
                    = LinkResponse.class))}
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
            description = "Ссылка не найдена",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation
                        = ApiErrorResponse.class))
            })
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public LinkResponse deleteLink(
        @RequestParam @Positive Long tgChatId,
        @RequestBody @Valid AddLinkRequest addLinkRequest
    )
        throws NotFoundException, URISyntaxException {
        // пока подготовил почву
        if (false) {
            throw new NotFoundException("Not Found");
        }
        return new LinkResponse(tgChatId, new URI(addLinkRequest.link()));
    }
}