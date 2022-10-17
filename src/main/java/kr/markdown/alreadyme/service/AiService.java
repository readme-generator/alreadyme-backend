package kr.markdown.alreadyme.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.markdown.alreadyme.domain.dto.ReadmeItemDto.UpdateReadmeText;
import kr.markdown.alreadyme.domain.model.ReadmeItem;
import kr.markdown.alreadyme.repository.ReadmeItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AiService {

    private final ReadmeItemRepository readmeItemRepository;

    @Value("${ai-server.host}")
    private String aiServerHost;

    public void requestReadmeText(Long id, String githubOriginalUrl, String requestJsonData) throws IOException {

        //requestJsonData
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("requestId", id);
        objectNode.put("githubOriginalUrl", githubOriginalUrl);

        ObjectNode jsonDataNode = (ObjectNode) new ObjectMapper().readTree(requestJsonData);
        objectNode.set("data", jsonDataNode);

        //httpClient
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(aiServerHost);
        httpPost.setHeader("Content-type", "application/json");

        httpPost.setEntity(new StringEntity(objectNode.toString(), "UTF-8"));
        httpClient.execute(httpPost);
    }

    public ReadmeItem responseReadmeText(UpdateReadmeText updateDto) {
        ReadmeItem readmeItem = findReadmeItemThrowException(updateDto.getId());
        readmeItem.setReadmeText(updateDto.getReadmeText());

        return readmeItemRepository.save(readmeItem);
    }

    private ReadmeItem findReadmeItemThrowException(Long id) {
        Optional<ReadmeItem> optionalReadmeItem = readmeItemRepository.findById(id);
        return optionalReadmeItem.orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("ReadmeItem id %d was not found", id)));
    }
}
