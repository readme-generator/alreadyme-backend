package kr.markdown.alreadyme.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AiService {

    public String getReadmeText(String requestJsonData, String githubOriginalUrl) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("githubOriginalUrl", githubOriginalUrl);

        ObjectNode jsonDataNode = (ObjectNode) new ObjectMapper().readTree(requestJsonData);
        objectNode.set("data", jsonDataNode);

        //httpClient

        //response.toString()

        return "create readme.md success";
    }
}
