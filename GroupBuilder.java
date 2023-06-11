package com.facebook.fullstackbackend.model;

import java.util.ArrayList;
import java.util.List;

public class GroupBuilder {
    private String groupID;
    private String groupName;
    private String adminID;
    private List<String> memberIDs;

    public GroupBuilder() {
        this.groupID = null;
        this.groupName = null;
        this.adminID = null;
        this.memberIDs = new ArrayList<>();
    }

    public GroupBuilder setGroupID(String groupID) {
        this.groupID = groupID;
        return this;
    }

    public GroupBuilder setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public GroupBuilder setAdminID(String adminID) {
        this.adminID = adminID;
        return this;
    }

    public GroupBuilder addMemberID(String memberID) {
        this.memberIDs.add(memberID);
        return this;
    }

    public Group build() {
        return new Group(groupID, groupName, adminID, memberIDs);
    }
}