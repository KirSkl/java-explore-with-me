package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*@Data
@NoArgsConstructor
@Builder*/
@AllArgsConstructor
public class AllStatsDto {
    private String app;
    private String uri;
    private long hits;
}
