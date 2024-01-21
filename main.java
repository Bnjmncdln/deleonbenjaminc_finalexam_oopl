import java.util.*;

class User {
    String username;
    String role;

    User(String username, String role) {
        this.username = username;
        this.role = role;
    }
}

class AuctionItem {
    String itemName;
    double currentBid;
    String highestBidder;

    int startingBids;

    AuctionItem(String itemName, int startingBids) {
        this.itemName = itemName;
        this.currentBid = startingBids;
        this.highestBidder = "";
        this.startingBids = startingBids;
    }

    void displayBiddingHistory() {
        System.out.println("Bidding History for " + itemName + ":");
        if (!highestBidder.isEmpty()) {
            System.out.println("Highest Bidder: " + highestBidder);
            System.out.println("Winning Bid: $" + currentBid);
            System.out.println("Starting Bids: " + startingBids);
        } else {
            System.out.println("No bids for this item.");
        }
    }
}

class Seller {
    String username;
    List<AuctionItem> itemsToSell;

    Seller(String username) {
        this.username = username;
        this.itemsToSell = new ArrayList<>();
    }

    void addItemToSell(String itemName, int startingBids) {
        itemsToSell.add(new AuctionItem(itemName, startingBids));
    }

    List<AuctionItem> getItemsToSell() {
        return itemsToSell;
    }
}

public class Main {
    private static final List<User> users = new ArrayList<>();
    private static final List<AuctionItem> generalItems = new ArrayList<>();
    private static final List<Seller> sellers = new ArrayList<>();
    private static User loggedInUser;
    private static boolean biddingOpen = false;

    public static void main(String[] args) {
        initializeData();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMainMenu();

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    register(scanner);
                    break;
                case 3:
                    System.out.println("Exiting the auction system. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void initializeData() {
        users.add(new User("admin", "admin"));
        users.add(new User("seller", "seller"));
        users.add(new User("buyer", "buyer"));

        Seller seller = new Seller("seller");
        sellers.add(seller);

        seller.addItemToSell("Nike", 150);
    }

    private static void showMainMenu() {
        System.out.println("\n===== Auction System =====");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter role (admin/seller/buyer): ");
        String role = scanner.nextLine();

        for (User user : users) {
            if (user.username.equals(username) && user.role.equals(role)) {
                loggedInUser = user;
                System.out.println("Login successful as " + role + ": " + username);
                showRoleMenu(scanner);
                return;
            }
        }

        System.out.println("Login failed. Invalid credentials.");
    }

    private static void showRoleMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n===== " + loggedInUser.role.toUpperCase() + " Menu =====");
            System.out.println("1. View Auction Items");
            System.out.println("2. Place Bid");
            if (loggedInUser.role.equals("admin")) {
                System.out.println("3. Start/Close Bidding");
            } else if (loggedInUser.role.equals("seller")) {
                System.out.println("3. Add Item to Bidding List");
            } else if (loggedInUser.role.equals("buyer")) {
                System.out.println("3. Buy Items");
            }
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAuctionItems();
                    break;
                case 2:
                    placeBid(scanner);
                    break;
                case 3:
                    performRoleAction(scanner);
                    break;
                case 4:
                    loggedInUser = null;
                    System.out.println("Logout successful.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void performRoleAction(Scanner scanner) {
        if (loggedInUser.role.equals("admin")) {
            adminActions(scanner);
        } else if (loggedInUser.role.equals("seller")) {
            addItemToBiddingList(scanner);
        } else if (loggedInUser.role.equals("buyer")) {
            buyItems(scanner);
        }
    }

    private static void adminActions(Scanner scanner) {
        System.out.println("\n===== Admin Actions =====");
        System.out.println("1. Start Bidding");
        System.out.println("2. Close Bidding");
        System.out.print("Enter your choice: ");

        int adminChoice = scanner.nextInt();
        scanner.nextLine();

        switch (adminChoice) {
            case 1:
                startBidding();
                break;
            case 2:
                closeBidding();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void startBidding() {
        if (!biddingOpen) {
            System.out.println("Bidding started.");
            biddingOpen = true;
        } else {
            System.out.println("Bidding is already open.");
        }
    }

    private static void closeBidding() {
        if (biddingOpen) {
            System.out.println("Bidding closed.");
            displayBiddingHistory();
            biddingOpen = false;
        } else {
            System.out.println("Bidding is not open.");
        }
    }

    private static void addItemToBiddingList(Scanner scanner) {
        if (biddingOpen) {
            System.out.println("Bidding is currently open. Cannot add items.");
            return;
        }
    
        Seller currentSeller = getSeller(loggedInUser.username);
    
        if (currentSeller == null) {
            System.out.println("Error: Seller not found.");
            return;
        }
    
        System.out.println("\n===== Add Item to Bidding List =====");
        System.out.print("Enter the name of the item to add to the bidding list: ");
        String itemName = scanner.nextLine();
    
        System.out.print("Enter the number of starting bids for the item: ");
        int startingBids = scanner.nextInt();
    
        if (startingBids <= 0) {
            System.out.println("Starting bids must be greater than 0.");
            return;
        }
    
        currentSeller.addItemToSell(itemName, startingBids);
    
        System.out.println("Item added to the bidding list successfully with starting bids: " + startingBids);
        }

    private static void buyItems(Scanner scanner) {
        if (!biddingOpen) {
            viewAuctionItems();

            System.out.print("Enter the item number to buy: ");
            int itemNumber = scanner.nextInt();
            scanner.nextLine();

            if (isValidItemNumber(itemNumber)) {
                AuctionItem selectedItem = getItemByNumber(itemNumber);

                if (selectedItem.highestBidder.equals(loggedInUser.username)) {
                    System.out.println("Item purchased successfully.");
                } else {
                    System.out.println("You are not the highest bidder for this item.");
                }
            } else {
                System.out.println("Invalid item number.");
            }
        } else {
            System.out.println("Bidding is currently open. Cannot buy items.");
        }
    }

    private static void viewAuctionItems() {
        System.out.println("\n===== Auction Items =====");
        int itemNumber = 1;
    
        for (AuctionItem item : generalItems) {
            displayItemInformation(item, itemNumber);
            itemNumber++;
        }
    
        for (Seller seller : sellers) {
            List<AuctionItem> sellerItems = seller.getItemsToSell();
            for (AuctionItem sellerItem : sellerItems) {
                displayItemInformation(sellerItem, itemNumber, seller.username);
                itemNumber++;
            }
        }
    }

    private static void displayItemInformation(AuctionItem item, int itemNumber) {
        System.out.println(itemNumber + ". " + item.itemName +
                " - Starting Bids: " + item.startingBids +
                " - Current Bid: $" + item.currentBid +
                " - Highest Bidder: " + item.highestBidder);
    }

    private static void displayItemInformation(AuctionItem item, int itemNumber, String sellerUsername) {
        System.out.println(itemNumber + ". " + item.itemName +
                " - Starting Bids: " + item.startingBids +
                " - Current Bid: $" + item.currentBid +
                " - Highest Bidder: " + item.highestBidder +
                " - Seller: " + sellerUsername);
    }

    private static void placeBid(Scanner scanner) {
        if (biddingOpen) {
            viewAuctionItems();

            System.out.print("Enter the item number to place a bid: ");
            int itemNumber = scanner.nextInt();
            scanner.nextLine();

            if (isValidItemNumber(itemNumber)) {
                AuctionItem selectedItem = getItemByNumber(itemNumber);

                if (loggedInUser.role.equals("buyer")) {
                    placeBidForItem(scanner, selectedItem);
                } else {
                    System.out.println("Only buyers can place bids.");
                }
            } else {
                System.out.println("Invalid item number.");
            }
        } else {
            System.out.println("Bidding is not currently open. Cannot place bids.");
        }
    }

    private static boolean isValidItemNumber(int itemNumber) {
        return itemNumber >= 1 && itemNumber <= generalItems.size() + getSellerItemCount();
    }

    private static AuctionItem getItemByNumber(int itemNumber) {
        int count = 0;

        for (AuctionItem item : generalItems) {
            if (++count == itemNumber) {
                return item;
            }
        }

        for (Seller seller : sellers) {
            List<AuctionItem> sellerItems = seller.getItemsToSell();
            for (AuctionItem sellerItem : sellerItems) {
                if (++count == itemNumber) {
                    return sellerItem;
                }
            }
        }

        return null;
    }

    private static void placeBidForItem(Scanner scanner, AuctionItem item) {
        System.out.print("Enter your bid for " + item.itemName + ": $");
        double bidAmount = scanner.nextDouble();

        if (bidAmount > item.currentBid) {
            item.currentBid = bidAmount;
            item.highestBidder = loggedInUser.username;
            System.out.println("Bid placed successfully.");
        } else {
            System.out.println("Bid amount must be higher than the current bid.");
        }
    }

    private static void displayBiddingHistory() {
        System.out.println("\n===== Bidding History =====");
        for (AuctionItem item : generalItems) {
            item.displayBiddingHistory();
        }
        for (Seller seller : sellers) {
            List<AuctionItem> sellerItems = seller.getItemsToSell();
            for (AuctionItem sellerItem : sellerItems) {
                sellerItem.displayBiddingHistory();
            }
        }
    }

    private static void register(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        for (User user : users) {
            if (user.username.equals(username)) {
                System.out.println("Username already taken. Please choose another one.");
                return;
            }
        }

        System.out.print("Enter role (admin/seller/buyer): ");
        String role = scanner.nextLine();

        if (!role.equals("admin") && !role.equals("seller") && !role.equals("buyer")) {
            System.out.println("Invalid role. Please choose 'admin', 'seller', or 'buyer'.");
            return;
        }

        users.add(new User(username, role));
        if (role.equals("seller")) {
            sellers.add(new Seller(username));
        }
        System.out.println("Registration successful.");
    }

    private static Seller getSeller(String username) {
        for (Seller seller : sellers) {
            if (seller.username.equals(username)) {
                return seller;
            }
        }
        return null;
    }

    private static int getSellerItemCount() {
        int count = 0;
        for (Seller seller : sellers) {
            count += seller.getItemsToSell().size();
        }
        return count;
    }
}