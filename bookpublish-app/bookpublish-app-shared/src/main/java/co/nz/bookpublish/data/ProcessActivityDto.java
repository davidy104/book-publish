package co.nz.bookpublish.data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class ProcessActivityDto implements Serializable {

	private String activityId;

	private String name;

	private String type;

	private String assignee;

	private Set<String> candidateUsers;

	private Set<String> candidateGroups;

	private String duration;

	private Date startTime;

	private Date endTime;

	private Map<String, ProcessActivityDto> outgoingActivityMap;

	private Map<String, ProcessActivityDto> incomeActivityMap;

	public static Builder getBuilder(String activityId, String name, String type) {
		return new Builder(activityId, name, type);
	}

	public static Builder getBuilder(String activityId, String name,
			String type, String assignee, Date startTime, Date endTime) {
		return new Builder(activityId, name, type, assignee, startTime, endTime);
	}

	public static class Builder {

		private ProcessActivityDto built;

		public Builder(String activityId, String name, String type) {
			built = new ProcessActivityDto();
			built.activityId = activityId;
			built.name = name;
			built.type = type;
		}

		public Builder(String activityId, String name, String type,
				String assignee, Date startTime, Date endTime) {
			built = new ProcessActivityDto();
			built.activityId = activityId;
			built.name = name;
			built.type = type;
			built.assignee = assignee;
			built.startTime = startTime;
			built.endTime = endTime;
		}

		public ProcessActivityDto build() {
			return built;
		}
	}

	public void addOutgoingProcessActivity(String transition,
			ProcessActivityDto outgoingActivity) {
		if (outgoingActivityMap == null) {
			outgoingActivityMap = new HashMap<String, ProcessActivityDto>();
		}
		outgoingActivityMap.put(transition, outgoingActivity);
	}

	public void addIncomingProcessActivity(String transition,
			ProcessActivityDto incomingActivity) {
		if (incomeActivityMap == null) {
			incomeActivityMap = new HashMap<String, ProcessActivityDto>();
		}
		incomeActivityMap.put(transition, incomingActivity);
	}

	public Set<String> getCandidateUsers() {
		return candidateUsers;
	}

	public void setCandidateUsers(Set<String> candidateUsers) {
		this.candidateUsers = candidateUsers;
	}

	public Set<String> getCandidateGroups() {
		return candidateGroups;
	}

	public void setCandidateGroups(Set<String> candidateGroups) {
		this.candidateGroups = candidateGroups;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Map<String, ProcessActivityDto> getOutgoingActivityMap() {
		return outgoingActivityMap;
	}

	public void setOutgoingActivityMap(
			Map<String, ProcessActivityDto> outgoingActivityMap) {
		this.outgoingActivityMap = outgoingActivityMap;
	}

	public Map<String, ProcessActivityDto> getIncomeActivityMap() {
		return incomeActivityMap;
	}

	public void setIncomeActivityMap(
			Map<String, ProcessActivityDto> incomeActivityMap) {
		this.incomeActivityMap = incomeActivityMap;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder
				.append(this.activityId, ((ProcessActivityDto) obj).activityId)
				.append(this.name, ((ProcessActivityDto) obj).name).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.activityId).append(this.name).toHashCode();
	}
}
