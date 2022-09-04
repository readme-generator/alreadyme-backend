package kr.markdown.alreadyme.controller;

import kr.markdown.alreadyme.domain.dto.GithubUrlDto.Create;
import kr.markdown.alreadyme.service.AppService;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
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
    public String post(@Valid @RequestBody Create createDto){
        return service.create(createDto);
    }

    @PostMapping("get-branch-list")
    public List<String> getBranchList() {
        return null;
    }

    @PostMapping("download")
    public String download() {
        return null;
    }

    @PostMapping("pull-request")
    @ResponseStatus(HttpStatus.CREATED)
    public void pullRequest(@Valid @RequestBody Create createDto) throws Exception {
        service.pullRequest(createDto);
    }
}

