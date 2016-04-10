public class Member {
    private Integer groupId;
    private Integer userId;

    public Member() {}

    public Member(Integer groupId, Integer userId) {
        setGroupId(groupId);
        setUserId(userId);
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
