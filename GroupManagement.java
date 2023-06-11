package com.facebook.fullstackbackend.model;

import com.facebook.fullstackbackend.repository.DatabaseSql;

public class GroupManagement {
    private DatabaseSql<String> database;

    public GroupManagement(DatabaseSql<String> database) {
        this.database = database;
    }

    public void createGroup(User creator, String groupName) {
        String groupID = generateGroupID();
        Group group = new GroupBuilder()
                .setGroupID(groupID)
                .setGroupName(groupName)
                .setAdminID(creator.getAccountID())
                .addMemberID(creator.getAccountID())
                .build();

        //Store group information in the database
        database.createGroup(group);
    }

    public void inviteToGroup(Group group, User inviter, User invitee) {
        if (!group.getMemberIDs().contains(inviter.getAccountID())) {
            System.out.println("You must be a member of the group to invite others.");
            return;
        }

        //Check if the invitee is already a member of the group
        if (group.getMemberIDs().contains(invitee.getAccountID())) {
            System.out.println(invitee.getUsername() + " is already a member of the group.");
            return;
        }

        //Invite the user to join the group
        group.getMemberIDs().add(invitee.getAccountID());

        //Update the group information in the database
        database.updateGroup(group);
    }

    //Helper method to generate a unique group ID
    private String generateGroupID() {
        // Generate a random group ID
        String groupID = "GROUP-" + System.currentTimeMillis() + "-" + (int) (Math.random() * 1000);

        while(!database.isGroupIDUnique(groupID)){
            groupID = "GROUP-" + System.currentTimeMillis() + "-" + (int) (Math.random() * 1000);
        }

        return groupID;
    }
}
