package mx.izzi.offboarding.modules.users.domain.models;

import mx.izzi.offboarding.modules.workcenter.domain.models.WorkCenter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long userId;
    private Long idssff;
    private String name;
    private String firstSurname;
    private String secondSurname;
    private String username;
    private String email;
    private String password;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private List<Role> roles;
    private WorkCenter workCenter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof User user)) return false;

        return new EqualsBuilder()
                .append(userId, user.userId)
                .append(idssff, user.idssff)
                .append(name, user.name)
                .append(firstSurname, user.firstSurname)
                .append(secondSurname, user.secondSurname)
                .append(username, user.username)
                .append(email, user.email)
                .append(password, user.password)
                .append(isActive, user.isActive)
                .append(createdAt, user.createdAt)
                .append(updatedAt, user.updatedAt)
                .append(createdBy, user.createdBy)
                .append(updatedBy, user.updatedBy)
                .append(roles, user.roles)
                .append(workCenter, user.workCenter)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(userId)
                .append(idssff)
                .append(name)
                .append(firstSurname)
                .append(secondSurname)
                .append(username)
                .append(email)
                .append(password)
                .append(isActive)
                .append(createdAt)
                .append(updatedAt)
                .append(createdBy)
                .append(updatedBy)
                .append(roles)
                .append(workCenter)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", userId)
                .append("idssff", idssff)
                .append("name", name)
                .append("firstSurname", firstSurname)
                .append("secondSurname", secondSurname)
                .append("username", username)
                .append("email", email)
                .append("password", password)
                .append("isActive", isActive)
                .append("createdAt", createdAt)
                .append("updatedAt", updatedAt)
                .append("createdBy", createdBy)
                .append("updatedBy", updatedBy)
                .append("roles", roles)
                .append("workCenter", workCenter)
                .toString();
    }
}
