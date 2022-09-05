package kr.markdown.alreadyme.domain.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReadmeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
