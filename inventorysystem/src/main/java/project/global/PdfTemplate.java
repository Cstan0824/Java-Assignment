package project.global;

import java.util.ArrayList;

import project.modules.transaction.Transaction;

public class PdfTemplate {
    private String Content;
    private ArrayList<Transaction> purchaseOrders;

    public String getContent() {
        return this.Content;
    }

    public ArrayList<Transaction> getpurchaseOrder() {
        return this.purchaseOrders;
    }

    //For Transaction Module
    private String PurchaseOrder() {
        StringBuilder html = new StringBuilder();
        html.append("<head>");
        html.append("<meta charset=\"UTF-8\" />");
        html.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />");
        html.append("<title>Purchase Order</title>");
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
        html.append("<p><strong>Supplier:</strong> ").append(this.purchaseOrders.get(0).getItem().getVendor_ID())
                .append("</p>");
        html.append("<p><strong>Address:</strong> ").append(this.purchaseOrders.get(0).getItem().getVendor_ID())
                .append("</p>");
        html.append("<p><strong>Contact:</strong> ").append(this.purchaseOrders.get(0).getItem().getVendor_ID())
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
        return "Your Sale Order No is " + this.purchaseOrders.get(0).getDoc_No();
    }

    public enum TemplateType {
        PURCHASE_ORDER,
        SALES_ORDER
    }

    public PdfTemplate(ArrayList<Transaction> _transaction, TemplateType _templateType) {
        this.purchaseOrders = _transaction;

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
}
