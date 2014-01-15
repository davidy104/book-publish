package co.nz.bookpublish.data;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class UserDto implements Serializable {
	private Long userId;
	private String username;
	private String password;
	private String createTime;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public static Builder getBuilder(String username, String password,
			String createTime) {
		return new Builder(username, password, createTime);
	}

	public static Builder getBuilder(String username, String password) {
		return new Builder(username, password);
	}

	public static class Builder {

		private UserDto built;

		public Builder(String username, String password, String createTime) {
			built = new UserDto();
			built.username = username;
			built.password = password;
			built.createTime = createTime;
		}

		public Builder(String username, String password) {
			built = new UserDto();
			built.username = username;
			built.password = password;
		}

		public UserDto build() {
			return built;
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.username, ((UserDto) obj).username)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.username).toHashCode();
	}
}
