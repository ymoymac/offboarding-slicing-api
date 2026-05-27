package mx.izzi.offboarding.modules.users.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleUserUnion {

    private Long id;
    private Role role;
    private User user;
    private LocalDateTime assignmentDate;
    private String assignedBy;
    private Boolean isActive;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof RoleUserUnion that)) return false;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(role, that.role)
                .append(user, that.user)
                .append(assignmentDate, that.assignmentDate)
                .append(assignedBy, that.assignedBy)
                .append(isActive, that.isActive)
                .append(updatedAt, that.updatedAt)
                .append(createdBy, that.createdBy)
                .append(updatedBy, that.updatedBy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(role)
                .append(user)
                .append(assignmentDate)
                .append(assignedBy)
                .append(isActive)
                .append(updatedAt)
                .append(createdBy)
                .append(updatedBy)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("role", role)
                .append("user", user)
                .append("assignmentDate", assignmentDate)
                .append("assignedBy", assignedBy)
                .append("isActive", isActive)
                .append("updatedAt", updatedAt)
                .append("createdBy", createdBy)
                .append("updatedBy", updatedBy)
                .toString();
    }
}
