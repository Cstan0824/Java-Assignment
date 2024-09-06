package project.view;
import java.util.ArrayList;
import java.util.Scanner;

import project.global.UserInputHandler;
import project.modules.user.User;
import project.modules.vendor.Vendor;

public class ViewVendor {
    private static final Scanner sc = new Scanner(System.in);
    private ArrayList<Vendor> vendors;
    private static User user;

    public void setVendors() {
        this.vendors = Vendor.GetAll();
    }

    public void setVendors(ArrayList<Vendor> vendors) {
        this.vendors = vendors;
    }

    public ArrayList<Vendor> getVendors() {
        return vendors;
    }

    public User getUser() {
        return user;
    }

    public ViewVendor() {
    }

    public ViewVendor(User _user) {
        user = _user;
        this.vendors = Vendor.GetAll();
    }

    public void menu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n============= Vendor Menu===============");
            System.out.println(
                    "1. Add Vendor\n2. Modify Vendor\n3. Delete Vendor\n4. Display Vendor\n5. Search Vendor\n6. Exit");

            System.out.println("=========================================");

            //switch case
            switch (UserInputHandler.validateInteger(sc, "Enter choice", 1, 6)) {
                case 1:
                    //call add vendor function
                    addVendor();
                    break;
                case 2:
                    //call modify vendor function
                    this.setVendors();
                    displayVendors();
                    Vendor vendorToModify = selectVendorFromList();
                    modifyVendor(vendorToModify);
                    break;
                case 3:
                    //call delete vendor function
                    this.setVendors();
                    displayVendors();
                    Vendor vendorToRemove = selectVendorFromList();
                    deleteVendor(vendorToRemove);
                    break;
                case 4:
                    //call display vendor fucntion
                    this.setVendors();
                    displayVendors();
                    UserInputHandler.systemPause("Press any key to continue...");
                    break;
                case 5:
                    //call search vendor function
                    searchVendor();
                    displayVendors();
                    UserInputHandler.systemPause("Press any key to continue...");
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try Vagain.");
                    break;
            }
        }
    }

    private void displayVendors() {
        //display headers
        System.out.println(" ================= Vendor List ==================== ");
        System.out.println(String.format("| %-10s | %-20s | %-20s | %-20s |", "Vendor ID", "Vendor Name",
                "Vendor Address", "Vendor Email"));
        System.out.println(" ================================================== ");

        //display vendors and item they handled
        for (Vendor vendor : this.vendors) {
            System.out.println(vendor.toString());
        }
    }

    //add multiple item while adding particular vendor
    private void addVendor() {
        //display
        System.out.println(
                "\n\n\n=================================================================================\nADD VENDOR\n");

        do {
            Vendor vendor = new Vendor();
            //able to input multiple item
            vendor.setVendor_Name(
                    UserInputHandler.getString(String.format("%-40s", "Please enter vendor name: "), 4, 50));
            vendor.setVendor_Address(
                    UserInputHandler.getString(String.format("%-40s", "Please enter vendor address: "), 4, 100));
            vendor.setVendorEmail(
                    UserInputHandler.getString(String.format("%-40s", "Please enter vendor email: "),
                            "^[a-zA-Z0-9]{4,24}@gmail.com"));
            vendor.setVendor_Created_By(user.getUserId());
            vendor.setVendor_Modified_By(user.getUserId());
            vendor.setVendor_ID(vendor.GenerateVendorID());

            //add to database
            if (vendor.Add()) {
                System.out.println("Vendor " + vendor.getVendor_Name() + " has been added successfully.");
            } else {
                System.out.println("Unable to add vendor: " + vendor.getVendor_ID());
            }
        } while (UserInputHandler
                .getConfirmation(
                        String.format("%-40s", "\nContinue to add vendor?"))
                .equalsIgnoreCase("Y"));
    }

    private void deleteVendor(Vendor _vendor) {
        if (!UserInputHandler
                .getConfirmation(
                        String.format("%-40s",
                                "Are you sure you want to delete vendor " + _vendor.getVendor_ID() + "?"))
                .equalsIgnoreCase("Y")) {
            return;
        }
        if (_vendor.Remove()) {
            System.out.println("Vendor " + _vendor.getVendor_ID() + " has been removed successfully.");
        } else {
            System.out.println("Unable to remove vendor: " + _vendor.getVendor_ID());
        }
    }

    private void modifyVendor(Vendor _vendor) {
        String column;
        String value;
        //display
        System.out.println(
                "\n\n\n=================================================================================\nMODIFY VENDOR\n");

        do {
            //select what to modify including the item they handle? - maybe no need to modify the item they handle
            System.out.println("1. Vendor Name\n2. Vendor Address\n3. Vendor Email");

            switch (UserInputHandler.validateInteger(sc, "Enter choice ", 1, 3)) {
                case 1:
                    System.out.println("===Update Vendor Name===");
                    _vendor.setVendor_Name(
                            UserInputHandler.getString(String.format("%-40s", "Please enter vendor name: "), 4, 50));

                    column = ("Vendor_Name");
                    value = _vendor.getVendor_Name();

                    break;

                case 2:
                    System.out.println("===Update Vendor Address===");
                    _vendor.setVendor_Address(
                            UserInputHandler.getString(String.format("%-40s", "Please enter vendor address: "), 4,
                                    100));

                    column = "Vendor_Address";
                    value = _vendor.getVendor_Address();
                    break;

                default:
                    System.out.println("===Update Vendor Email===");
                    _vendor.setVendorEmail(
                            UserInputHandler.getString(String.format("%-40s", "Please enter vendor email: "), ""));

                    column = "Vendor_Email";
                    value = _vendor.getVendor_Email();
                    break;
            }
            //add to database

            _vendor.Update(column, value);
        } while (UserInputHandler.getConfirmation(String.format("%-40s", "\nContinue to edit vendor?"))
                .equalsIgnoreCase("Y"));
    }

    public Vendor selectVendorFromList() {
        //get vendor id
        String vendorId = UserInputHandler.getString("Enter Vendor ID", "V[0-9]{5}");

        //search for vendor
        for (Vendor vendor : vendors) {
            if (vendor.getVendor_ID().equals(vendorId)) {
                return vendor;
            }
        }
        //if no vendor are founded in the list
        return null;
    }

    private void searchVendor() {
        boolean backToVendorManagement = false;
        while (!backToVendorManagement) {
            //search for vendor
            System.out.println("======== Search Vendor ========");
            System.out.println("1. Search by Vendor Name");
            System.out.println("2. Search by Vendor ID");
            System.out.println("3. Back to Vendor Management");
            System.out.println("===============================");

            switch (UserInputHandler.validateInteger(sc, "Select an option", 1, 3)) {
                case 1:
                    this.vendors = Vendor.Get("Vendor_Name",
                            UserInputHandler.getString("Enter Vendor Name", 4,50));
                    break;
                case 2:
                    this.vendors = Vendor.Get("Vendor_ID",
                            UserInputHandler.getString("Enter Vendor ID", 6, "^V[0-9]{5}"));
                    break;
                case 3:
                    backToVendorManagement = true;
                    break;
                default:
                    break;

            }
            if (backToVendorManagement != true) {
                displayVendors();
            }
        }
    }
}

     

