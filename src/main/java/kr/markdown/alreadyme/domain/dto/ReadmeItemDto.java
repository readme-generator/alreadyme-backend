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
        @Pattern(regexp = "(?:https://)github.com[:/](.*).git")
        String githubUrl;
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
        private String githubUrl;

        @NotBlank
        private String githubBotUrl;

        @NotBlank
        private String readmeText;

        private String objectUrl;

        @DateTimeFormat
        private LocalDateTime createdTime;
    }
}