package kr.markdown.alreadyme.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.markdown.alreadyme.domain.dto.ReadmeItemDto.Create;
import kr.markdown.alreadyme.domain.dto.ReadmeItemDto.Request;
import kr.markdown.alreadyme.domain.dto.ReadmeItemDto.Download;
import kr.markdown.alreadyme.domain.dto.ReadmeItemDto.PullRequest;
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
    private final S3Service s3Service;

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

        String gitUrl = createDto.getGithubOriginalUrl();
        gitUrl += (createDto.getGithubOriginalUrl().contains(".git")) ? "" : ".git";

        //GitClone
        Git git = JGitUtil.cloneRepository(gitUrl);

        //FileScan
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        File gitDirectory = git.getRepository().getDirectory();
        String requestJsonData = FileScanUtil.createJson(
                objectNode,
                new File(gitDirectory.getPath() + File.separator + "..").getCanonicalPath(),
                gitDirectory.getParentFile().getName()
        );

        //Delete Repository
        JGitUtil.close(git);
        FileUtils.deleteDirectory(new File(git.getRepository().getDirectory().getParentFile().getPath()));;

        //Create ReadmeItem
        ReadmeItem readmeItem = ReadmeItem.builder()
                .githubOriginalUrl(gitUrl)
                .createdTime(LocalDateTime.now())
                .build();
        ReadmeItem newReadmeItem = readmeItemRepository.save(readmeItem);
        newReadmeItem = findReadmeItemThrowException(newReadmeItem.getId());

        //POST call readmeText by ai-server
        aiService.requestReadmeText(newReadmeItem.getId(), gitUrl, requestJsonData);

        return readmeItemRepository.save(newReadmeItem);
    }

    @Transactional
    public Download download(Request requestDto) throws Exception {

        Download downloadDto;
        ReadmeItem readmeItem = findReadmeItemThrowException(requestDto.getId());

        //If S3 link already exists
        if(readmeItem.getObjectUrl() != null) {
            downloadDto = Download.builder()
                    .objectUrl(readmeItem.getObjectUrl())
                    .build();
            return downloadDto;
        }

        //Create README.md File
        File uploadFile = createReadmeInDownload(readmeItem.getReadmeText());

        //Upload README.md to S3-bucket
        String objectUrl = s3Service.upload(uploadFile, uploadFile.getParentFile().getName());
        downloadDto = Download.builder()
                .objectUrl(objectUrl)
                .build();

        readmeItem.setObjectUrl(objectUrl);
        readmeItemRepository.save(readmeItem);

        //Delete Folder
        FileUtils.deleteDirectory(new File(uploadFile.getParentFile().getPath()));

        return downloadDto;
    }

    public PullRequest pullRequest(Request requestDto) throws Exception {

        ReadmeItem readmeItem = findReadmeItemThrowException(requestDto.getId());

        //GitFork
        String githubBotUrl = GithubApiUtil.gitFork(readmeItem.getGithubOriginalUrl(), token);

        //GitClone
        Git git = JGitUtil.cloneRepository(githubBotUrl);

        //Create README.md
        createReadmeInPullRequest(
                git.getRepository().getDirectory().getPath() + File.separator + "..",
                readmeItem.getReadmeText()
        );
        String masterBranchName = git.getRepository().getBranch();
        JGitUtil.checkout(git, String.valueOf(requestDto.getId()));
        JGitUtil.add(git, ".");
        JGitUtil.commit(git, "feat: Add README.md by " + id, name, email);
        JGitUtil.push(git, id, token);

        //GitPullRequest
        String pullRequestUrl = GithubApiUtil.gitPullRequest(readmeItem.getGithubOriginalUrl(), token, git.getRepository().getBranch(), masterBranchName, id);

        //Delete Local & Remote Repository
        JGitUtil.close(git);
        FileUtils.deleteDirectory(new File(git.getRepository().getDirectory().getParentFile().getPath()));

        //Create pullRequestDto
        PullRequest pullRequestDto = PullRequest.builder()
                .pullRequestUrl(pullRequestUrl)
                .build();

        return pullRequestDto;
    }

    //create README.md in pull-request
    private void createReadmeInPullRequest(String localDirPath, String text) throws Exception {
        try {
            FileWriter output = new FileWriter(localDirPath + File.separator +"README.md");
            output.write(text);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //create README.md in download
    private File createReadmeInDownload(String text) throws Exception {
        File file = new File(System.getProperty("user.dir") + File.separator + UUID.randomUUID() + File.separator + "README.md");
        try {
            file.getParentFile().mkdirs();
            FileWriter output = new FileWriter(file);
            output.write(text);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    private ReadmeItem findReadmeItemThrowException(Long id) {
        Optional<ReadmeItem> optionalReadmeItem = readmeItemRepository.findById(id);
        return optionalReadmeItem.orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("ReadmeItem id %d was not found", id)));
    }

}
