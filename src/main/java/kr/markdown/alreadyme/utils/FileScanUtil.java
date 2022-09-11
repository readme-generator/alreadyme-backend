package kr.markdown.alreadyme.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileScanUtil {
    private static final Set<String> extSet = new HashSet<>(Arrays.asList(
            ".c", ".h", ".cs", ".cpp", ".hpp", ".c++", ".h++", ".cc", ".hh", ".C", ".H", ".java", ".js", ".lua", ".md", ".markdown", ".php", ".php3", ".php4", ".php5", ".phps", ".phpt", ".pl", ".pm", ".pod", ".perl", ".py", ".rb", ".rs", ".ts", ".tsx", ".vb", ".kt"
    ));

    public static String createJson(ObjectNode objectNode, String localDirPath, String directoryName) throws IOException {
        String userDirPath = System.getProperty("user.dir");

        File path = new File(localDirPath);
        File[] fileList = path.listFiles();

        for (int i = 0; i < fileList.length; i++) {
            if(fileList[i].isFile() && isExtension(fileList[i]) && !fileList[i].isHidden()) {
                objectNode.put(
                        fileList[i].getPath()
                                .replace(userDirPath, "")
                                .replace(File.separator + directoryName + File.separator, "")
                                .replace("\\", "/"),
                        FileUtils.readFileToString(fileList[i], "UTF-8")
                );
            } else if (fileList[i].isDirectory()) {
                createJson(objectNode, fileList[i].getPath(), directoryName);
            }
        }

        return objectNode.toString();
    }

    private static Boolean isExtension(File file) {
        return extSet.contains("." + FilenameUtils.getExtension(file.getName())) ? true : false;
    }
}
