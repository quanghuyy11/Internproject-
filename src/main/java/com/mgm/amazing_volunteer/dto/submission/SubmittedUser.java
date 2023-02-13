package com.mgm.amazing_volunteer.dto.submission;

import org.hibernate.annotations.NamedNativeQuery;

import java.util.Objects;

public class SubmittedUser {
    private String email;
    private Boolean isParticipated;

    public SubmittedUser(final String email, final Boolean isParticipated) {
        this.email = email;
        this.isParticipated = isParticipated;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setParticipated(final boolean participated) {
        isParticipated = participated;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getIsParticipated() {
        return isParticipated;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SubmittedUser that = (SubmittedUser) o;
        return getEmail().equals(that.getEmail()) && Objects.equals(isParticipated, that.isParticipated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), isParticipated);
    }
}
