package project.modules.transaction;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import project.global.PdfConverter;
import project.global.PdfTemplate;
import project.modules.item.Item;

public class Report {
    private String reportName;
    private String reportDescription;
    private LocalDateTime reportDateTime;
    private File file;
    private PdfConverter pdf;
    private final Map<Item, Integer> itemMap = new HashMap<>();

    public enum ReportType {
        YEARLY_SALES,
        YEARLY_PURCHASE
    }

    //getter and setter
    public String getReportName() {
        return reportName;
    }

    public void setReportName(String _reportName) {
        this.reportName = _reportName;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String _reportDescription) {
        this.reportDescription = _reportDescription;
    }

    public LocalDateTime getReportDateTime() {
        return this.reportDateTime;
    }

    public void setReportDateTime(LocalDateTime _reportDateTime) {
        this.reportDateTime = _reportDateTime;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File _file) {
        this.file = _file;
    }

    public Map<Item, Integer> getItemMap() {
        return this.itemMap;
    }

    //Constructor
    public Report(String _reportName, String _reportDescription,
            LocalDateTime _reportDateTime) {
        this.reportName = _reportName;
        this.reportDescription = _reportDescription;
        this.reportDateTime = _reportDateTime;
    }

    //Methods
    public boolean generateReport(ReportType _reportType) {
        switch (_reportType) {
            case YEARLY_SALES:
                return SalesReport();
            case YEARLY_PURCHASE:
                return PurchaseReport();
            default:
                return false;
        }
    }

    private boolean SalesReport() {
        ArrayList<SalesOrder> salesOrders = SalesOrder.GetAll();
        if (salesOrders == null || salesOrders.isEmpty()) {
            return false;
        }
        for (SalesOrder salesOrder : salesOrders) {
            salesOrder.getItem().Get();
            if (itemMap.containsKey(salesOrder.getItem())) {
                // Update the quantity of the existing item
                int existingQuantity = itemMap.get(salesOrder.getItem());
                itemMap.put(salesOrder.getItem(), existingQuantity + salesOrder.getQuantity());
            } else {
                // Add the new item with its quantity
                itemMap.put(salesOrder.getItem(), salesOrder.getQuantity());
            }
        }
        try {
            URL resource = getClass().getClassLoader()
                    .getResource("project/Report");
            this.file = new File(resource.getPath().replace("%20", " "), "YearlySalesReport.pdf");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        this.pdf = new PdfConverter(this.file, new PdfTemplate(this, PdfTemplate.ReportType.SALES_REPORT));

        return this.pdf.Save();
    }

    private boolean PurchaseReport() {

        ArrayList<PurchaseOrder> purchaseOrders = PurchaseOrder.GetAll();
        if (purchaseOrders == null || purchaseOrders.isEmpty()) {
            return false;
        }

        //get ordered items quantity
        for (PurchaseOrder purchaseOrder : purchaseOrders) {
            if (itemMap.containsKey(purchaseOrder.getItem())) {
                // Update the quantity of the existing item
                int existingQuantity = itemMap.get(purchaseOrder.getItem());
                itemMap.put(purchaseOrder.getItem(), existingQuantity + purchaseOrder.getQuantity());
            } else {
                // Add the new item with its quantity
                itemMap.put(purchaseOrder.getItem(), purchaseOrder.getQuantity());
            }
        }
        try {
            URL resource = getClass().getClassLoader()
                    .getResource("project/Report");
            this.file = new File(resource.getPath().replace("%20", " "), "YearlyPurchaseReport.pdf");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

        this.pdf = new PdfConverter(this.file, new PdfTemplate(this, PdfTemplate.ReportType.PURCHASE_REPORT));

        return this.pdf.Save();
    }
}
