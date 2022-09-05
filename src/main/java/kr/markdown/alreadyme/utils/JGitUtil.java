package kr.markdown.alreadyme.utils;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class JGitUtil {

    public static Git cloneRepository(String gitUrl) throws GitAPIException, IOException {

        String directory = File.separator + UUID.nameUUIDFromBytes(gitUrl.getBytes());
        Git git;
        try {
            git = Git.cloneRepository()
                    .setURI(gitUrl)
                    .setDirectory(new File(System.getProperty("user.dir") + directory))
                    .setTimeout(30)
                    .call();

        } catch (JGitInternalException e) {
            git = Git.open(new File(System.getProperty("user.dir") + directory));
        }
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
