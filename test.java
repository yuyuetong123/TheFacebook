package Facebook;


import java.util.Scanner;

import Facebook.AccountManagement;

public class test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AccountManagement managerAdmin = new AccountManagement();
        AccountManagement managerNormal = new AccountManagement();
        AccountManagement managerNormal2 = new AccountManagement();
       
        managerAdmin.registration();
        managerNormal.registration();
        managerNormal2.registration();
        managerNormal2.createGroup("ABC");

        
        if(managerNormal.user!=null){
            int choice1 = 0;
            while(choice1>-1){
                System.out.println("0 - Create Post on Personal Page");
                System.out.println("1 - Create Group");
                System.out.println("2 - Show the name list of groups that the user joined");
                System.out.println("3 - Get remaining time for being banned");
                System.out.println("4 - Invite someone to a certain group");
                System.out.println("5 - Join a certain group by entering creator and the name of the group");
                System.out.println("6 - View a user's personal page");
                System.out.println("7 - Post content on a group");
                System.out.println("*************************");
                choice1 = sc.nextInt();
                sc.nextLine();
                System.out.println("*************************");
                switch(choice1){
                    case 0: managerNormal.createPost("hello world");
                            break;
                    case 1: managerNormal.createGroup("Java");//1
                            break;
                    case 2: System.out.println("Names of all groups the user has joined:"+managerNormal.getGroupNames());//3
                            break;
                    case 3: System.out.println("Remaining time for being banned: "+managerNormal.getRemainingBannedTime());
                            break;
                    case 4: managerNormal.inviteMemberToAGroup("Java", managerAdmin.user);//2
                            break;
                    case 5: managerNormal.joinGroup(managerNormal2.user,"ABC");
                            break;
                    case 6: managerNormal.viewPage(managerNormal.user);
                            break;
                    case 7: managerNormal.getGroup(managerNormal2.user, "ABC").postContent(managerNormal.user, "Good Morning");//4
                            break;
                }
            }
        }
        
        if(managerAdmin.user!=null){
            int choice1 = 0;
            while(choice1>-1){
                System.out.println("0 - Set Admin");
                System.out.println("1 - Delete Account");
                System.out.println("2 - Automatically Remove Inappropriate Content in User's Page");
                System.out.println("3 - Manually Remove Inappropriate Content");
                System.out.println("4 - Ban a User");
                System.out.println("5 - Update the Banned Words List");
                System.out.println("*************************");
                choice1 = sc.nextInt();
                sc.nextLine();
                System.out.println("*************************");
                switch(choice1){
                    case 0: managerAdmin.setAdmin(managerAdmin.user);
                            break;
                    case 1: managerAdmin.deleteAccount(managerNormal.user.getAccountID());
                            break;
                    case 2: managerAdmin.automaticallyRemoveInappropriateContentInUserPage(managerNormal.user);
                            break;
                    case 3: managerAdmin.manuallyRemoveInappropriateContent(managerNormal.user,managerNormal.user.getPage().get(1));
                            break;
                    case 4: managerAdmin.banUser(managerNormal.user, User.BanDuration.PERMANENT);
                            break;
                    case 5: managerAdmin.addNewProhibitedWords("python");
                            break;
                }
            }
        }
    }
}
