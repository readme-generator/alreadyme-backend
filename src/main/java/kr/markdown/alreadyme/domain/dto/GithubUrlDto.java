package kr.markdown.alreadyme.domain.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public interface GithubUrlDto {

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
}
