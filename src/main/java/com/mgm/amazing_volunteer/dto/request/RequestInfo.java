package com.mgm.amazing_volunteer.dto.request;

import com.mgm.amazing_volunteer.util.enums.RequestStatus;

/**
 * A Projection for the {@link com.mgm.amazing_volunteer.model.Request} entity
 */
public interface RequestInfo {
    Boolean isIsDeleted();

    Long getId();

    Integer getQuantity();

    RequestStatus getStatus();

    UserInfo getUser();

    PrizeInfo getPrize();

    /**
     * A Projection for the {@link com.mgm.amazing_volunteer.model.User} entity
     */
    interface UserInfo {
        String getEmail();

        String getUsername();

        Integer getTotalPoint();
    }

    /**
     * A Projection for the {@link com.mgm.amazing_volunteer.model.Prize} entity
     */
    interface PrizeInfo {
        Boolean isIsDeleted();

        Long getId();

        String getPrize_name();

        String getDescription();

        Integer getPoint_archive();
    }
}