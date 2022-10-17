package kr.markdown.alreadyme.domain.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public interface ReadmeItemDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Create {
        @NotBlank
        @Pattern(regexp = "(?:https://)github.com[:/](.*)")
        private String githubOriginalUrl;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Request {
        @NotNull
        private Long id;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class ObjectUrl {
        @NotBlank
        private String objectUrl;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class UpdateReadmeText {
        @NotNull
        private Long id;

        @NotBlank
        private String readmeText;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class PullRequest {
        @NotBlank
        private String pullRequestUrl;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Result {
        @NotNull
        private Long id;

        @NotBlank
        @Pattern(regexp = "(?:https://)github.com[:/](.*).git")
        private String githubOriginalUrl;

        @NotBlank
        private String readmeText;

        private String objectUrl;

        @DateTimeFormat
        private LocalDateTime createdTime;
    }
}
