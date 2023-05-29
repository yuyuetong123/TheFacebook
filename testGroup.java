/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Facebook;

import java.util.Scanner;

/**
 *
 * @author 21574
 */
public class testGroup {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AccountManagement manager = new AccountManagement();
        User user = null;

        System.out.println("\tFacebook");
        System.out.println("------------------------------");
        System.out.println("1 - Register");
        System.out.println("2 - Login");
        int choice = sc.nextInt();
        switch(choice){
            case 1: manager.registration();
                    user = manager.login();
                    break;
            case 2: user = manager.login();
                    break;
        }
        if(user!=null){
            int choice1 = 1;
            while(choice1>0){
                System.out.println("0 - Log out");
                System.out.println("1 - Create a new group");
                System.out.println("2 - Invite someone else to join a group");
                System.out.println("3 - To find admin of the group(To check whether the creator of the group is an admin)");
                System.out.println("4 - Show group names on the account page");
                System.out.println("*************************");
                choice1 = sc.nextInt();
                sc.nextLine();
                System.out.println("*************************");
                switch(choice1){
                    case 1: System.out.println("Please enter group name: ");
                            String name=sc.nextLine();
                            manager.createGroup(name);
                            break;
                    case 2: System.out.println("Please enter group name: ");
                            String groupName=sc.nextLine();
                            System.out.println("Please enter invitee's name: ");
                            String userName=sc.nextLine();
                            manager.inviteMemberToAGroup(groupName, userName);
                            break;
                    case 3: System.out.println("Please enter the group's name: ");
                            String s=sc.next();
                            manager.findGroupAdmin(s);
                            break;
                    case 4: manager.displayAllGroupNames();
                            manager.getGroupNamesUserJoined();
                            break;
                }
            }
        }
    }
}
