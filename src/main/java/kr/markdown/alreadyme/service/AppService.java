package kr.markdown.alreadyme.service;

import kr.markdown.alreadyme.domain.dto.GithubUrlDto.Create;
import kr.markdown.alreadyme.utils.GithubApiUtil;
import kr.markdown.alreadyme.utils.JGitUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
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

    public String create(Create createDto) {
        return "README.md";
    }

    public void pullRequest(Create createDto) throws Exception {

        String githubUrl = GithubApiUtil.gitFork(createDto.getGithubUrl(), token);

        Git git = JGitUtil.cloneRepository(githubUrl);
        createReadme(git.getRepository().getDirectory().getPath()+"\\..", "test + test");
        JGitUtil.add(git, ".");
        JGitUtil.commit(git, "feat: Add README.md by ALREADYME-BOT", name, email);
        JGitUtil.push(git, id, token);

        GithubApiUtil.gitPullRequest(createDto.getGithubUrl(), token, git.getRepository().getBranch());

        JGitUtil.close(git);
        FileUtils.deleteDirectory(new File(git.getRepository().getDirectory().getPath()+"\\.."));
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
