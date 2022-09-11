package kr.markdown.alreadyme.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Log4j2
public class AiService {

    @Value("${ai-server.host}")
    private String aiServerHost;

    public String getReadmeText(String requestJsonData, String githubOriginalUrl) throws IOException {

        //requestJsonData
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("githubOriginalUrl", githubOriginalUrl);

        ObjectNode jsonDataNode = (ObjectNode) new ObjectMapper().readTree(requestJsonData);
        objectNode.set("data", jsonDataNode);

        //httpClient
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(aiServerHost);
        httpPost.setHeader("Content-type", "application/json");

        httpPost.setEntity(new StringEntity(objectNode.toString(), "UTF-8"));
        HttpResponse httpResponse = httpClient.execute(httpPost);

        return EntityUtils.toString(httpResponse.getEntity());
    }
}
