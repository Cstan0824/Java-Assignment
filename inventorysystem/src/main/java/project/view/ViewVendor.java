package project.view;
import java.util.ArrayList;

import project.global.UserInputHandler;
import project.modules.user.User;
import project.modules.vendor.Vendor;

public class ViewVendor {
    private ArrayList<Vendor> vendors;
    private static User user;

    public void setVendors() {
        this.vendors = Vendor.GetAll();
    }

    public void setVendors(ArrayList<Vendor> vendors) {
        this.vendors = vendors;
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
        //display
        System.out.println("\n============= Vendor Menu===============");

        System.out.println("1. Add Vendor\n2. Modify Vendor\n3. Delete Vendor\n4. Display Vendor\n5. Exit");

        System.out.println("=========================================");

        //switch case
        switch (UserInputHandler.getInteger("Enter choice", 1, 5)) {
            case 1:
                //call add vendor function
                addVendor();

                System.out.println("\n\n**Redirecting To Main Menu**");
                menu();
                break;
            case 2:
                //call modify vendor function
                displayVendors();
                Vendor vendorToModify = selectVendorFromList();
                modifyVendor(vendorToModify);
                System.out.println("\n\n**Redirecting To Main Menu**");
                menu();
                break;
            case 3:
                //call delete vendor function
                displayVendors();
                Vendor vendorToRemove = selectVendorFromList();
                deleteVendor(vendorToRemove);

                System.out.println("\n\n**Redirecting To Main Menu**");
                menu();
                break;
            case 4:
                //call display vendor fucntion
                displayVendors();
                UserInputHandler.systemPause("Press any key to continue...");
                System.out.println("\n\n**Redirecting To Main Menu**");
                menu();
                break;
            case 5:
                System.out.println("Byebye, see you next time!\n");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    public void displayVendors() {
        //display headers
        System.out.println(" ================= Vendor List ==================== ");
        System.out.println(String.format("| %-10s | %-20s | %-20s | %-20s |", "Vendor ID", "Vendor Name",
                "Vendor Address", "Vendor Email"));
        System.out.println(" ================================================== ");

        //display vendors
        for (Vendor vendor : vendors) {
            System.out.println(vendor.toString());
        }
    }

    public void addVendor() {
        //display
        System.out.println(
                "\n\n\n=================================================================================\nADD VENDOR\n");

        do {
            Vendor vendor = new Vendor();
            vendor.getItemCategory().setItem_Category_ID(
                    UserInputHandler.getInteger(String.format("%-40s", "Please enter item category ID: "), 1, 10));
            vendor.setVendor_Name(
                    UserInputHandler.getString(String.format("%-40s", "Please enter vendor name: "), 4, 50));
            vendor.setVendor_Address(
                    UserInputHandler.getString(String.format("%-40s", "Please enter vendor address: "), 4, 100));
            vendor.setVendorEmail(
                    UserInputHandler.getString(String.format("%-40s", "Please enter vendor email: "), ""));
            vendor.setVendor_Created_By(user.getUserId());
            vendor.setVendor_Modified_By(user.getUserId());
            vendor.setVendor_ID(vendor.GenerateVendorID());

            //add to database
            if (vendor.Add()) {
                System.out.println("Vendor " + vendor.getVendor_ID() + " has been added successfully.");
            } else {
                System.out.println("Unable to add vendor: " + vendor.getVendor_ID());
            }

        } while (UserInputHandler
                .getConfirmation(
                        String.format("%-40s", "\nContinue to add vendor?"))
                .equalsIgnoreCase("Y"));
    }

    public void deleteVendor(Vendor _vendor) 
    {
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

    public void modifyVendor(Vendor _vendor)
    {
        String column;
        String value;
        //display
        System.out.println(
                "\n\n\n=================================================================================\nMODIFY VENDOR\n");

        //search for vendor
        _vendor.setVendor_ID(UserInputHandler.getString(String.format("%-40s", "Please enter vendor ID: "), 6,
                "^V[0-9]{5}"));

        do {
            //select what to modify
            System.out.println("1. Vendor Name\n2. Vendor Address\n3. Vendor Email");

            switch (UserInputHandler.getInteger("Enter choice ", 1, 3)) {
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
                    value =_vendor.getVendor_Address();
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
        String vendorId = UserInputHandler.getString("Enter Vendor ID", "V[0-9]{3}");

        //search for vendor
        for (Vendor vendor : vendors) {
            if (vendor.getVendor_ID().equals(vendorId)) {
                return vendor;
            }
        }
        //if no vendor are founded in the list
        return null;
    }
}