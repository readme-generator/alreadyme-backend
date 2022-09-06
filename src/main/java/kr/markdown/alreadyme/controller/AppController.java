package kr.markdown.alreadyme.controller;

import kr.markdown.alreadyme.domain.dto.ReadmeItemDto.Create;
import kr.markdown.alreadyme.domain.dto.ReadmeItemDto.Request;
import kr.markdown.alreadyme.domain.model.ReadmeItem;
import kr.markdown.alreadyme.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/app")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AppController {
    public final AppService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReadmeItem post(@Valid @RequestBody Create createDto) throws Exception {
        return service.create(createDto);
    }

    @PostMapping("get-branch-list")
    public List<String> getBranchList() {
        return null;
    }

    @PostMapping("download")
    public ReadmeItem download(@Valid @RequestBody Request requestDto) throws Exception {
        return service.download(requestDto);
    }

    @PostMapping("pull-request")
    @ResponseStatus(HttpStatus.CREATED)
    public void pullRequest(@Valid @RequestBody Request requestDto) throws Exception {
        service.pullRequest(requestDto);
    }
}

