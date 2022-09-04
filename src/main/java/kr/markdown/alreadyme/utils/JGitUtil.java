package kr.markdown.alreadyme.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.UUID;

public class JGitUtil {

    public static Git cloneRepository(String gitUrl) throws GitAPIException {

        String directory = "/" + UUID.nameUUIDFromBytes(gitUrl.getBytes()).toString();

        Git git = Git.cloneRepository()
                .setURI(gitUrl)
                .setDirectory(new File(System.getProperty("user.dir") + directory))
                .call();
        return git;
    }

    public static void add(Git git, String filePattern) throws GitAPIException {
        git.add().addFilepattern(filePattern)
                .call();
    }

    public static void commit(Git git, String msg, String name, String email) throws GitAPIException {
        git.commit()
                .setAuthor(name, email)
                .setMessage(msg)
                .call();
    }

    public static void push(Git git, String id, String token) throws GitAPIException {
        PushCommand pushCommand = git.push();
        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(id, token));
        pushCommand.setForce(true);
        pushCommand.call();
    }

    public static void close(Git git) {
        git.close();
    }
}
