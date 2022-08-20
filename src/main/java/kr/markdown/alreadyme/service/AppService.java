package kr.markdown.alreadyme.service;

import kr.markdown.alreadyme.domain.dto.GithubUrlDto.Create;
import kr.markdown.alreadyme.utils.JGitUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileWriter;

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

    public String create(Create createDto) {
        return "README.md";
    }

    public void pullRequest(Create createDto) throws Exception {
        Git git = jGitUtil.cloneRepository(createDto.getGithubUrl());

        //markdown 코드 생성
        createReadme(git.getRepository().getDirectory().getPath()+"\\..", "test + test");

        jGitUtil.add(git, ".");
        jGitUtil.commit(git, "feat: Add README.md by ALREADYME-BOT", name, email);
        jGitUtil.push(git, id, token);
    }

    private void createReadme(String path, String text) throws Exception {
        try {
            FileWriter output = new FileWriter(path + "/README.md");
            output.write(text);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
