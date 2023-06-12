package com.facebook.fullstackbackend.model;

import java.util.Scanner;

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
                System.out.println("3 - Show group names on the account page");
                System.out.println("*************************");
                choice1 = sc.nextInt();
                sc.nextLine();
                System.out.println("*************************");
                switch(choice1){
                    case 1: System.out.println("Please enter group name: ");
                            String name=sc.nextLine();
                            manager.createGroup(name);
                            break;
                    case 2: System.out.println("Please enter group ID: ");
                            String groupId=sc.nextLine();
                            System.out.println("Please enter invitee's name: ");
                            String userName=sc.nextLine();
                            manager.inviteToGroup(manager.database.getProfile(userName), manager.database.getGroupByID(groupId));
                            break;
                    case 3: manager.showGroupNamesUserJoined();
                            break;
                }
            }
        }
    }
}