package dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ViewStatsDto {
    private String app;
    private String uri;
    private long hits;
}
