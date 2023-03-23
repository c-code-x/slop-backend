package com.slop.slopbackend.dto.request.event;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
public class EventCreateReqDTO {
    @NotEmpty
    @Size(min=3)
    private String name;
    @NotEmpty
    @Size(min=3)
    private String slug;
    private String poster;
    @NotEmpty
    @Size(min=3)
    private String descriptionMd;
    @NotEmpty
    @Size(min=3)
    private String briefDescription;
    private Timestamp startTime;
    private Timestamp endTime;
}
