package com.mgm.amazing_volunteer.common;


import com.mgm.amazing_volunteer.dto.event.EventDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {
    private List<EventDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
