package kr.markdown.alreadyme.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class GithubApiUtil {

    public static String gitFork(String githubUrl, String token) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost("https://api.github.com/repos" + exportGitRepoPath(githubUrl) + "/forks");

            httpPost.setHeader("Accept", "application/vnd.github.v3+json");
            httpPost.setHeader("Authorization", "token " + token);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(httpResponse.getEntity());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonStr);
            return node.get("clone_url").asText();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String gitPullRequest(String githubUrl, String token, String branchName, String masterBranchName, String githubId) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost("https://api.github.com/repos"+ exportGitRepoPath(githubUrl)+"/pulls");

            httpPost.setHeader("Accept", "application/vnd.github.v3+json");
            httpPost.setHeader("Authorization", "token " + token);

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("title", "Add README.md by " + githubId);
            node.put("body", "This text was created by " + githubId + "\n" + "https://github.com/" + githubId);
            node.put("head", githubId + ":" + branchName);
            node.put("base", masterBranchName);

            httpPost.setEntity(new StringEntity(node.toString(), "UTF-8"));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(httpResponse.getEntity());

            ObjectMapper resMapper = new ObjectMapper();
            JsonNode resNode = resMapper.readTree(jsonStr);
            return resNode.get("html_url").asText();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void gitDeleteRemoteRepository(String githubUrl, String token) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpDelete httpDelete = new HttpDelete("https://api.github.com/repos"+ exportGitRepoPath(githubUrl));

            httpDelete.setHeader("Accept", "application/vnd.github.v3+json");
            httpDelete.setHeader("Authorization", "token " + token);

            httpClient.execute(httpDelete);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String exportGitRepoPath(String githubUrl) {
        int startIdx = githubUrl.indexOf("https://github.com");
        return githubUrl.substring(startIdx + 18, (githubUrl.substring(startIdx).indexOf(".git")+startIdx));
    }
}
