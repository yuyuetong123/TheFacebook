
package Facebook;

import java.util.ArrayList;
import java.util.Map;


public class Group {
    private String groupName;
    private String admin;
    private ArrayList<String> members;

    public Group(String groupName, String userNameOfCreator ) {
        this.groupName = groupName;
        this.admin = userNameOfCreator;
        this.members=new ArrayList<>();
        this.members.add(admin);//The group admin is the first member of the group.
    }
    
    public String getGroupName() {
        return this.groupName;
    }
    
    public String getGroupAdmin() {
        return this.admin;
    }

    public ArrayList<String> getMembers() {
        return this.members;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }
    
}