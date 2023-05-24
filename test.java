package Facebook;


import java.util.Scanner;


public class test {
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
                System.out.println("1 - Post on my page");
                System.out.println("2 - View my page");
                System.out.println("3 - View others page");
                System.out.println("4 - Set your role as an admin");
                System.out.println("5 - Check if an user is an admin or not");
                System.out.println("6 - Ban an user");
                System.out.println("7 - Get remaining banned time of an user");
                System.out.println("8 - Manually remove inappropriate content");
                System.out.println("9 - Automatically remove inappropriate content in an user's page");
                System.out.println("10 - Update Prohibited Words List");
                System.out.println("11 - Delete Account");
                System.out.println("*************************");
                choice1 = sc.nextInt();
                sc.nextLine();
                System.out.println("*************************");
                switch(choice1){
                    case 1: System.out.println("Please enter your content for post: ");
                            String content=sc.nextLine();
                            manager.createPost(content);
                            break;
                    case 2: manager.viewMyPage();
                            break;
                    case 3: System.out.println("Please enter username of the user you want to view: ");
                            String username=sc.next();
                            manager.viewOthersPage(username);
                            break;
                    case 4: manager.setAdmin();
                            break;
                    case 5: System.out.println("Please enter username of the user you want to check: ");
                            String username2=sc.next();
                            manager.isAdmin(username2);
                            break;
                    case 6: System.out.println("Please select banned duration: \n1 for 1 month; 2 for 1 year");
                            int select=sc.nextInt();
                            System.out.println("Please enter username of the user: ");
                            String username3=sc.next();
                            if(select==1){
                                manager.banUser(username3, User.BanDuration.ONE_MONTH);
                            }else if(select==2){
                                manager.banUser(username3, User.BanDuration.ONE_YEAR);
                            }
                            break;
                    case 7: System.out.println("Please enter username of the user you want to check: ");
                            String username4=sc.next();
                            manager.getRemainingBannedTime(username4);
                            break;
                    case 8: System.out.println("Please enter username of the user: ");
                            String username5=sc.next();
                            System.out.println();
                            System.out.println("Please enter the number of the post you want to remove: ");
                            int postNo=sc.nextInt();
                            manager.manuallyRemoveInappropriateContent(username5, postNo);
                            break;
                    case 9: System.out.println("Please enter username of the user: ");
                            String username6=sc.next();
                            manager.automaticallyRemoveInappropriateContentInUserPage(username6);
                            break;
                    case 10: System.out.println("Please enter the new word: ");
                            String word=sc.nextLine();
                            manager.addNewProhibitedWords(word);
                            break;
                    case 11: System.out.println("Please enter accID of the user you want to delete: ");
                            String accId=sc.next();
                            manager.deleteAccount(accId);
                            break;
                }
            }
        }
    }
}
