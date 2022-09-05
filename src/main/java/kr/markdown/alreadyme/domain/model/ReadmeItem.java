package kr.markdown.alreadyme.domain.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
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
    private String githubOriginalUrl;

    @NotBlank
    @Column(columnDefinition = "MEDIUMTEXT")
    private String readmeText;

    private String objectUrl;

    @DateTimeFormat
    private LocalDateTime createdTime;
}
