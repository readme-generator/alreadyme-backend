package kr.markdown.alreadyme.controller;

import kr.markdown.alreadyme.domain.dto.ReadmeItemDto;
import kr.markdown.alreadyme.domain.model.ReadmeItem;
import kr.markdown.alreadyme.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/ai")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AiController {
    private final AiService service;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ReadmeItem post(@Valid @RequestBody ReadmeItemDto.UpdateReadmeText updateDto) throws Exception {
        return service.responseReadmeText(updateDto);
    }
}
