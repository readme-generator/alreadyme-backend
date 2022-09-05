package kr.markdown.alreadyme.service;

import kr.markdown.alreadyme.domain.dto.ReadmeItemDto.Create;
import kr.markdown.alreadyme.domain.dto.ReadmeItemDto.Request;
import kr.markdown.alreadyme.domain.model.ReadmeItem;
import kr.markdown.alreadyme.repository.ReadmeItemRepository;
import kr.markdown.alreadyme.utils.FileScanUtil;
import kr.markdown.alreadyme.utils.GithubApiUtil;
import kr.markdown.alreadyme.utils.JGitUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Log4j2
public class AppService {

    private final ReadmeItemRepository readmeItemRepository;

    private final AiService aiService;

    @Value("${github.id}")
    private String id;

    @Value("${github.token}")
    private String token;

    @Value("${github.name}")
    private String name;

    @Value("${github.email}")
    private String email;

    @Transactional
    public ReadmeItem create(Create createDto) throws Exception {

        //GitFork
        String githubBotUrl = GithubApiUtil.gitFork(createDto.getGithubOriginalUrl(), token);

        //GitClone
        Git git = JGitUtil.cloneRepository(githubBotUrl);

        //FileScan
        File gitDirectory = git.getRepository().getDirectory();
        String json = FileScanUtil.createJson(
                new File(gitDirectory.getPath() + File.separator + "..").getCanonicalPath(),
                gitDirectory.getParentFile().getName()
        );

        //Repository Delete
        JGitUtil.close(git);
        FileUtils.deleteDirectory(new File(git.getRepository().getDirectory().getParentFile().getPath()));;
        GithubApiUtil.gitDeleteRemoteRepository(githubBotUrl, token);

        //Get readmeText by ai-server
        String readmeText = aiService.getReadmeText(json);

        //Create ReadmeItem
        ReadmeItem readmeItem = ReadmeItem.builder()
                .githubOriginalUrl(createDto.getGithubOriginalUrl())
                .readmeText(readmeText)
                .createdTime(LocalDateTime.now())
                .build();

        return readmeItemRepository.save(readmeItem);
    }

    @Transactional
    public String download(Request requestDto) throws Exception {

        //Create README.md
        String getDownloadFilePath = createDownloadReadme(
                findReadmeItemThrowException(requestDto.getId()).getReadmeText()
        );

        log.error("download path {}", getDownloadFilePath);

        //Upload S3

        //Create download link

        return null;
    }

    public void pullRequest(Request requestDto) throws Exception {

        //GitFork
        String githubBotUrl = GithubApiUtil.gitFork(requestDto.getGithubOriginalUrl(), token);

        //GitClone
        Git git = JGitUtil.cloneRepository(githubBotUrl);

        //Create Readme
        createReadme(
                git.getRepository().getDirectory().getPath() + File.separator + "..",
                findReadmeItemThrowException(requestDto.getId()).getReadmeText()
        );

        JGitUtil.add(git, ".");
        JGitUtil.commit(git, "feat: Add README.md by ALREADYME-BOT", name, email);
        JGitUtil.push(git, id, token);

        //GitPullRequest
        GithubApiUtil.gitPullRequest(requestDto.getGithubOriginalUrl(), token, git.getRepository().getBranch());

        //Delete Local & Remote Repository
        JGitUtil.close(git);
        FileUtils.deleteDirectory(new File(git.getRepository().getDirectory().getPath() + "\\.."));
//        GithubApiUtil.gitDeleteRemoteRepository(githubBotUrl, token);
    }

    //create README.md
    private void createReadme(String localDirPath, String text) throws Exception {
        try {
            FileWriter output = new FileWriter(localDirPath + File.separator +"README.md");
            output.write(text);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //create download README.md
    private String createDownloadReadme(String text) throws Exception {
        File file = new File(System.getProperty("user.dir") + File.separator + UUID.randomUUID() + File.separator + "README.md");
        try {
            file.getParentFile().mkdirs();
            FileWriter output = new FileWriter(file);
            output.write(text);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    private ReadmeItem findReadmeItemThrowException(Long id) {
        Optional<ReadmeItem> optionalReadmeItem = readmeItemRepository.findById(id);
        return optionalReadmeItem.orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("ReadmeItem id %d was not found", id)));
    }

}
