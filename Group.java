package com.facebook.fullstackbackend.model;

import java.util.List;

public class Group {
    private String groupID;
    private String groupName;
    private String adminID;
    private List<String> memberIDs;

    public Group(String groupID, String groupName, String adminID, List<String> memberIDs) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.adminID = adminID;
        this.memberIDs = memberIDs;
    }

    public String getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getAdminID() {
        return adminID;
    }

    public List<String> getMemberIDs() {
        return memberIDs;
    }
}