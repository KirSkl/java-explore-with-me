package dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EndpointHitDto {
    private Long id;
    @NotBlank(message = "Не указано название приложения")
    private String app;
    @NotBlank(message = "Не указан URL")
    private String uri;
    @NotBlank(message = "Не указан IP")
    private String ip;
    @NotNull(message = "Не указано время")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
