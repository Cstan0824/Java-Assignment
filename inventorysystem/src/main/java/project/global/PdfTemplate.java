package project.global;

import project.modules.transaction.Transaction;

public class PdfTemplate {
    private String Content;
    private final Transaction transaction;

    public String getContent() {
        return this.Content;
    }

    //For Transaction Module
    private String PurchaseOrder() {
        return "Your Purchase Order No is " + this.transaction.getDoc_No();
    }

    private String GoodReceivedNotes() {
        return "Your Good Received Notes No is " + this.transaction.getDoc_No();
    }

    private String SaleOrder() {
        return "Your Sale Order No is " + this.transaction.getDoc_No();
    }

    
    public enum TemplateType {
        PURCHASE_ORDER,
        GOOD_RECEIVED_NOTES,
        SALES_ORDER
    }

    public PdfTemplate(Transaction _transaction, TemplateType _templateType) {
        this.transaction = _transaction;
        
        switch (_templateType) {
            case PURCHASE_ORDER:
                this.Content = this.PurchaseOrder();
                break;
            case GOOD_RECEIVED_NOTES:
                this.Content = this.GoodReceivedNotes();
                break;
            case SALES_ORDER:
                this.Content = this.SaleOrder();
                break;
            
            default:
                break;
        }
    }


}
