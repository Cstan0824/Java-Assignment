package project.global;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import project.modules.item.Item;
import project.modules.schedule.Schedule;
import project.modules.transaction.DeliveryOrder;
import project.modules.transaction.Report;
import project.modules.transaction.Transaction;
import project.modules.user.Retailer;

public class PdfTemplate {
    private String Content;
    private ArrayList<Transaction> purchaseOrders;
    private ArrayList<Transaction> salesOrders;
    private Schedule schedule;


    public String getContent() {
        return this.Content;
    }

    public ArrayList<Transaction> getpurchaseOrder() {
        return this.purchaseOrders;
    }

    public ArrayList<Transaction> getSaleOrder() {
        return this.salesOrders;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    //For Transaction Module
    private String PurchaseOrder() {
        StringBuilder html = new StringBuilder();
        html.append("<head>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; padding: 0; }");
        html.append("h1 { text-align: center; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        html.append("table, th, td { border: 1px solid black; }");
        html.append("th, td { padding: 10px; text-align: left; }");
        html.append(".supplier-details, .po-details { margin-top: 20px; }");
        html.append(".total { text-align: right; margin-top: 20px; font-size: 1.2em; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body style=\"border: 1px solid black; padding: 20px\">");

        html.append("<h1>Purchase Order</h1>");

        html.append("<div class=\"po-details\">");
        html.append("<p><strong>PO Number:</strong> ").append(this.purchaseOrders.get(0).getDoc_No()).append("</p>");
        html.append("<p><strong>Date:</strong> ").append(this.purchaseOrders.get(0).getTransaction_Date())
                .append("</p>");
        html.append("</div>");

        html.append("<div class=\"supplier-details\">");
        html.append("<p><strong>Supplier:</strong> ").append(this.purchaseOrders.get(0).getItem().getVendor().getVendor_Name())
                .append("</p>");
        html.append("<p><strong>Address:</strong> ").append(this.purchaseOrders.get(0).getItem().getVendor().getVendor_Address())
                .append("</p>");
        html.append("<p><strong>Email:</strong> ").append(this.purchaseOrders.get(0).getItem().getVendor().getVendor_Email())
                .append("</p>");
        html.append("</div>");

        html.append("<table>");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>Item #</th>");
        html.append("<th>Description</th>");
        html.append("<th>Quantity</th>");
        html.append("<th>Unit Price</th>");
        html.append("<th>Total</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");
        double totalPrice = 0;
        for (int i = 0; i < this.purchaseOrders.size(); i++) {
            double orderPrice = this.purchaseOrders.get(i).getItem().getItem_Price()
                    * this.purchaseOrders.get(i).getQuantity();
            totalPrice += orderPrice;
            html.append("<tr>");
            html.append("<td>").append(this.purchaseOrders.get(i).getItem().getItem_Name()).append("</td>");
            html.append("<td>").append(this.purchaseOrders.get(i).getItem().getItem_Desc()).append("</td>");
            html.append("<td>").append(this.purchaseOrders.get(i).getQuantity()).append("</td>");
            html.append("<td>$").append(String.format("%.2f", this.purchaseOrders.get(i).getItem().getItem_Price()))
                    .append("</td>");
            html.append("<td>$").append(String.format("%.2f", orderPrice))
                    .append("</td>");
            html.append("</tr>");
        }

        html.append("</tbody>");
        html.append("</table>");

        html.append("<div class=\"total\">");
        html.append("<p><strong>Total Amount:</strong> $").append(String.format("%.2f", totalPrice)).append("</p>");
        html.append("</div>");

        html.append("<p>Thank you for your business!</p>");

        html.append("<div class=\"company-details\">");
        html.append("<p><strong>Inventory Solution Company</strong></p>");
        html.append("<p>Jalan Genting Kelang, Setapak, 53300 Kuala Lumpur, Federal Territory of Kuala Lumpur</p>");
        html.append("<p>Email: tarumtmoviesociety@gmail.com | Phone: +6012-339 6197</p>");
        html.append("<p><a href='#'>Website: www.InventorySolutionCompany.com</a></p>");
        html.append("</div>");
        html.append("</body>");

        return html.toString();
    }

    private String SaleOrder() {
        
        StringBuilder html = new StringBuilder();
        html.append("<head>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; padding: 0; }");
        html.append("h1 { text-align: center; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        html.append("table, th, td { border: 1px solid black; }");
        html.append("th, td { padding: 10px; text-align: left; }");
        html.append(".supplier-details, .po-details { margin-top: 20px; }");
        html.append(".total { text-align: right; margin-top: 20px; font-size: 1.2em; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body style=\"border: 1px solid black; padding: 20px\">");

        html.append("<h1>Sales Order</h1>");

        html.append("<div class=\"po-details\">");
        html.append("<p><strong>SO Number:</strong> ").append(this.salesOrders.get(0).getDoc_No()).append("</p>");
        html.append("<p><strong>Date:</strong> ").append(this.salesOrders.get(0).getTransaction_Date())
                .append("</p>");
        html.append("</div>");

        html.append("<div class=\"supplier-details\">");
        Retailer retailer = new Retailer();
        retailer.setUserId(this.salesOrders.get(0).getTransaction_Recipient());
        retailer.Get();
        html.append("<p><strong>Retailer:</strong> ").append(retailer.getUserName())
                .append("</p>");
        html.append("<p><strong>Address:</strong> ").append(retailer.getRetailerAddress())
                .append("</p>");
        html.append("<p><strong>Email:</strong> ").append(retailer.getUserEmail())
                .append("</p>");
        html.append("</div>");

        html.append("<table>");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>Item #</th>");
        html.append("<th>Description</th>");
        html.append("<th>Quantity</th>");
        html.append("<th>Unit Price</th>");
        html.append("<th>Total</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");
        double totalPrice = 0;
        for (int i = 0; i < this.salesOrders.size(); i++) {
            double orderPrice = this.salesOrders.get(i).getItem().getItem_Price()
                    * this.salesOrders.get(i).getQuantity();
            totalPrice += orderPrice;
            html.append("<tr>");
            html.append("<td>").append(this.salesOrders.get(i).getItem().getItem_Name()).append("</td>");
            html.append("<td>").append(this.salesOrders.get(i).getItem().getItem_Desc()).append("</td>");
            html.append("<td>").append(this.salesOrders.get(i).getQuantity()).append("</td>");
            html.append("<td>$").append(String.format("%.2f", this.salesOrders.get(i).getItem().getItem_Price()))
                    .append("</td>");
            html.append("<td>$").append(String.format("%.2f", orderPrice))
                    .append("</td>");
            html.append("</tr>");
        }

        html.append("</tbody>");
        html.append("</table>");

        html.append("<div class=\"total\">");
        html.append("<p><strong>Total Amount:</strong> $").append(String.format("%.2f", totalPrice)).append("</p>");
        html.append("</div>");

        html.append("<p>Thank you for your business!</p>");

        html.append("<div class=\"company-details\">");
        html.append("<p><strong>Inventory Solution Company</strong></p>");
        html.append("<p>Jalan Genting Kelang, Setapak, 53300 Kuala Lumpur, Federal Territory of Kuala Lumpur</p>");
        html.append("<p>Email: tarumtmoviesociety@gmail.com | Phone: +6012-339 6197</p>");
        html.append("<p><a href='#'>Website: www.InventorySolutionCompany.com</a></p>");
        html.append("</div>");
        html.append("</body>");

        return html.toString();
        }
        
    

    private String SalesReport(Report _report) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>")
                .append("<html lang=\"en\">")
                .append("<head>")
                .append("<style>")
                .append("body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }")
                .append(".container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }")
                .append("h1, h2 { text-align: center; }")
                .append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }")
                .append("table, th, td { border: 1px solid #ddd; }")
                .append("th, td { padding: 12px; text-align: left; }")
                .append("th { background-color: #f1f1f1; }")
                .append(".footer { margin-top: 30px; text-align: center; font-size: 14px; color: #555; }")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<div class=\"container\">")
                .append("<h1>Yearly Sales Report</h1>")
                .append("<h2>Inventory Solution Company</h2>")
                .append("<p>Report generated on ").append(_report.getReportDateTime()).append("</p>")
                .append("<table>")
                .append("<thead>")
                .append("<tr>")
                .append("<th>Item</th>")
                .append("<th>Total Quantity</th>")
                .append("<th>Total Price</th>")
                .append("</tr>")
                .append("</thead>")
                .append("<tbody>");

        double grandTotal = 0; // Initialize total amount

        // Add rows for each item
        for (Map.Entry<Item, Integer> entry : _report.getItemMap().entrySet()) {
            Item item = entry.getKey();
            int totalQuantity = entry.getValue();
            double totalPrice = item.getItem_Price() * totalQuantity;

            // Update the grand total
            grandTotal += totalPrice;

            html.append("<tr>")
                    .append("<td>").append(item.getItem_Name()).append("</td>")
                    .append("<td>").append(totalQuantity).append("</td>")
                    .append("<td>").append(String.format("$%.2f", totalPrice)).append("</td>")
                    .append("</tr>");
        }

        // Add the total amount row
        html.append("<tr>")
                .append("<td colspan=\"2\" style=\"text-align: right; font-weight: bold;\">Total Amount</td>")
                .append("<td>").append(String.format("$%.2f", grandTotal)).append("</td>")
                .append("</tr>");

        html.append("</tbody>")
                .append("</table>")
                .append("<div class=\"footer\">")
                .append("<p>&copy; 2024 Inventory Solutions. All rights reserved.</p>")
                .append("</div>")
                .append("</div>")
                .append("</body>")
                .append("</html>");
        return html.toString();
    }

    private String PurchaseReport(Report _report) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>")
                .append("<html lang=\"en\">")
                .append("<head>")
                .append("<style>")
                .append("body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }")
                .append(".container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }")
                .append("h1, h2 { text-align: center; }")
                .append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }")
                .append("table, th, td { border: 1px solid #ddd; }")
                .append("th, td { padding: 12px; text-align: left; }")
                .append("th { background-color: #f1f1f1; }")
                .append(".footer { margin-top: 30px; text-align: center; font-size: 14px; color: #555; }")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<div class=\"container\">")
                .append("<h1>Yearly Purchase Report</h1>")
                .append("<h2>Inventory Solution Company</h2>")
                .append("<p>Report generated on ").append(_report.getReportDateTime()).append("</p>")
                .append("<table>")
                .append("<thead>")
                .append("<tr>")
                .append("<th>Item</th>")
                .append("<th>Total Quantity</th>")
                .append("<th>Total Price</th>")
                .append("</tr>")
                .append("</thead>")
                .append("<tbody>");

        double grandTotal = 0; // Initialize total amount

        // Add rows for each item
        for (Map.Entry<Item, Integer> entry : _report.getItemMap().entrySet()) {
            Item item = entry.getKey();
            int totalQuantity = entry.getValue();
            double totalPrice = item.getItem_Price() * totalQuantity;

            // Update the grand total
            grandTotal += totalPrice;

            html.append("<tr>")
                    .append("<td>").append(item.getItem_Name()).append("</td>")
                    .append("<td>").append(totalQuantity).append("</td>")
                    .append("<td>").append(String.format("$%.2f", totalPrice)).append("</td>")
                    .append("</tr>");
        }

        // Add the total amount row
        html.append("<tr>")
                .append("<td colspan=\"2\" style=\"text-align: right; font-weight: bold;\">Total Amount</td>")
                .append("<td>").append(String.format("$%.2f", grandTotal)).append("</td>")
                .append("</tr>");

        html.append("</tbody>")
                .append("</table>")
                .append("<div class=\"footer\">")
                .append("<p>&copy; 2024 Inventory Solutions. All rights reserved.</p>")
                .append("</div>")
                .append("</div>")
                .append("</body>")
                .append("</html>");
        return html.toString();
    }

    //For Schedule Module
    private String DeliverySchedule() {

        StringBuilder html = new StringBuilder();
        html.append("<head>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; padding: 0; }");
        html.append("h1 { text-align: center; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        html.append("table, th, td { border: 1px solid black; }");
        html.append("th, td { padding: 10px; text-align: left; }");
        html.append(".supplier-details, .po-details { margin-top: 20px; }");
        html.append(".total { text-align: right; margin-top: 20px; font-size: 1.2em; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body style=\"border: 1px solid black; padding: 20px\">");

        html.append("<h1>Delivery Schedule</h1>");

        html.append("<div class=\"po-details\">");
        html.append("<p><strong>Date:</strong> ").append(LocalDate.now())
                .append("</p>");
        html.append("</div>");

        html.append("<div class=\"supplier-details\">");

        Retailer retailer = new Retailer();
        ArrayList<DeliveryOrder> deliveryOrders = DeliveryOrder.GetAll(this.schedule.getDeliveryOrder().getDoc_No());
        if (deliveryOrders != null && !deliveryOrders.isEmpty()){
            DeliveryOrder deliveryOrder = deliveryOrders.get(0);
            retailer.setUserId(deliveryOrder.getTransaction_Recipient());
            retailer.Get();
        }

        html.append("<p><strong>Retailer:</strong> ").append(retailer.getUserName())
                .append("</p>");
        html.append("<p><strong>Address:</strong> ").append(retailer.getRetailerAddress())
                .append("</p>");
        html.append("<p><strong>Email:</strong> ").append(retailer.getUserEmail())
                .append("</p>");

        html.append("</div>");
        html.append("<table>");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>Doc_No</th>");
        html.append("<th>Vehicle Plate</th>");
        html.append("<th>Driver</th>");
        html.append("<th>Delivery Date</th>");
        html.append("<th>Time Slot</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");
       
        html.append("<tr>");
        html.append("<td>").append(this.schedule.getDeliveryOrder().getDoc_No()).append("</td>");
        html.append("<td>").append(this.schedule.getVehicle().getVehicle_Plate()).append("</td>");
        html.append("<td>").append(this.schedule.getVehicle().getDriver()).append("</td>");
        html.append("<td>").append(this.schedule.getSchedule_Date()).append("</td>");
        html.append("<td>").append(this.schedule.getTime_Slot()).append("</td>");
        html.append("</tr>");
        

        html.append("</tbody>");
        html.append("</table>");

        html.append("<p>Thank you for your business!</p>");

        html.append("<div class=\"company-details\">");
        html.append("<p><strong>Inventory Solution Company</strong></p>");
        html.append("<p>Jalan Genting Kelang, Setapak, 53300 Kuala Lumpur, Federal Territory of Kuala Lumpur</p>");
        html.append("<p>Email: tarumtmoviesociety@gmail.com | Phone: +6012-339 6197</p>");
        html.append("<p><a href='#'>Website: www.InventorySolutionCompany.com</a></p>");
        html.append("</div>");
        html.append("</body>");

        return html.toString();


    }

    public enum TemplateType {
        PURCHASE_ORDER,
        SALES_ORDER,
        DELIVERY_SCHEDULE
    }

    public enum ReportType {
        SALES_REPORT,
        PURCHASE_REPORT
    }

    public PdfTemplate(ArrayList<Transaction> _transaction, TemplateType _templateType) {
        this.purchaseOrders = _transaction;
        this.salesOrders = _transaction;
        switch (_templateType) {
            case PURCHASE_ORDER:
                this.Content = this.PurchaseOrder();
                break;
            case SALES_ORDER:
                this.Content = this.SaleOrder();
                break;
            default:
                break;
        }
    }

    public PdfTemplate(Transaction _transaction, TemplateType _templateType) {
        this.purchaseOrders.add(_transaction);
        switch (_templateType) {
            case PURCHASE_ORDER:
                this.Content = this.PurchaseOrder();
                break;
            case SALES_ORDER:
                this.Content = this.SaleOrder();
                break;
            default:
                break;
        }
    }

    public PdfTemplate(Schedule schedule) {
        this.schedule = schedule;
        this.Content = this.DeliverySchedule();
    }

    public PdfTemplate(Report _report, ReportType _templateType) {
        switch (_templateType) {
            case SALES_REPORT:
                this.Content = this.SalesReport(_report);
                break;
            case PURCHASE_REPORT:
                this.Content = this.PurchaseReport(_report);
                break;
            default:
                break;
        }
    }
}
