package project.start;

public class App {
    public static void main(String[] args) 
    {

        //menu
        menu("Add Item", "Add Stock", "Add Stock Flow", "Add Vendor", "Add Category", "Add User",
                "Add Stock In Automation", "Add Stock Out Automation", "Add Stock Flow Automation");
                
        System.out.printf("Enter your choice: %s %s", "testing 123", "testing 12312312312");
    }

    public static void menu(String... values) {
        //print menu based on the arg passed in
        for (int i = 0; i < values.length; i++) {
            System.out.println(i + 1 + ". " + values[i]);
        }
    }

    public static void test() {
        Stock stock = new Stock();
        stock.setStock_Receipent_ID("123");
        stock.setStock_Flow_Category(1);
        stock.setStock_Flow_Date(new Date(2021, 1, 1));
        stock.setStock_Flow_Total_Price(1000);
        stock.setStock_Flow_Created_By("admin");
        stock.setStock_Flow_Modified_By("admin");

        stock.Add();

        for (Object elem : col) {
            new Stock("123").remove();
        }
    }
}
