/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Facebook;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 21574
 */
public class Group {
    private String name;
    private User groupAdmin;
    private List<User> members;
    private List<Post> groupPosts;

    public Group(String name, User groupAdmin ) {
        this.name = name;
        this.groupAdmin = groupAdmin;
        this.members = new ArrayList<>();
        this.members.add(groupAdmin);
        this.groupPosts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public User getgroupAdmin() {
        return groupAdmin;
    }

    public List<User> getMembers() {
        return members;
    }

    public List<Post> getGroupPosts() {
        return groupPosts;
    }

    public void addMember(User user) {
        members.add(user);
    }

    public void removeMember(User user) {
        members.remove(user);
    }

    public void postContent(User user, String content) {
        if (this.members.contains(user)) {
            Post newPost=new Post(user,content);
            this.groupPosts.add(newPost);
            System.out.println(user.getUsername() + " has posted content to the group.");
        } else {
            System.out.println(user.getUsername() + " is not a member of this group.");
        }
    }
    
    public void viewAllGroupPosts(){
        for (int i = 0; i < this.groupPosts.size(); i++) {
            System.out.println("Post No."+(i+1)+" in the Group Posts Channel: "+this.groupPosts.get(i).getContent());
        }
    }
    
    public void inviteMember(User inviter, User invitee) {
        //Check if the group contains the inviter
        if (this.getMembers().contains(inviter)) {
            this.addMember(invitee);
            invitee.getGroupsList().add(this);
        } else {
            System.out.println(inviter.getUsername() + " does not have permission to invite members to this group.");
        }
    }
    
}