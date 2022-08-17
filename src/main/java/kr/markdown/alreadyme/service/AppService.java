package kr.markdown.alreadyme.service;

import kr.markdown.alreadyme.domain.dto.GithubUrlDto.Create;
import kr.markdown.alreadyme.utils.JGitUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Log4j2
public class AppService {

    @Value("${github.id}")
    private String id;

    @Value("${github.token}")
    private String token;

    @Value("${github.name}")
    private String name;

    @Value("${github.email}")
    private String email;

    private JGitUtil jGitUtil = new JGitUtil();

    @PostConstruct
    public void init() {
        jGitUtil.init(id, token, name, email);
        log.error("id {} \n token {} \n name {} \n email {} \n", id, token, name, email);
    }

    public String create(Create createDto){
        return "README.md";
    }

    public void pullRequest(Create createDto) throws GitAPIException {
        jGitUtil.cloneRepository(createDto.getGithubUrl());
        jGitUtil.add("C:/Users/KimSuHeon/IdeaProjects/alreadyme/src/main/java/kr/markdown/alreadyme/files/ABC.md");
        jGitUtil.commit("ABC.md");
        jGitUtil.push();
    }
}
