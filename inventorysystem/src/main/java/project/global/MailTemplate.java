package project.global;

public class MailTemplate {
    private String Content;
    //getter 
    public String getTemplate() {
        return this.Content;
    }

    //For user Module
    private String OTPMail(String _OTP) {
        return "Your OTP is " + _OTP;
    }

    private String PasswordResetMail(String _OTP) {
        return "Your password reset OTP is " + _OTP;
    }

    //For Transaction Module
    private String PurchaseOrderMail(String _DocNo) {
        return "Your Purchase Order No is " + _DocNo;
    }

    private String GoodReceivedNotesMail(String _DocNo) {
        return "Your Good Received Notes No is " + _DocNo;
    }

    private String SaleOrderMail(String _DocNo) {
        return "Your Sale Order No is " + _DocNo;
    }

    public static class TemplateType {
        public static final int OTP = 0;
        public static final int RESET_PASS = 1;
        public static final int PURCHASE_ORDER = 2;
        public static final int GOOD_RECEIVED_NOTES = 3;
        public static final int SALES_ORDER = 4;
    }

    public MailTemplate(String _Content, int _templateType) {
        switch (_templateType) {
            case 0:
                this.Content = this.OTPMail(_Content);
                break;
            case 1:
                this.Content = this.PasswordResetMail(_Content);
                break;
            case 2:
                this.Content = this.PurchaseOrderMail(_Content);
                break;
            case 3:
                this.Content = this.GoodReceivedNotesMail(_Content);
                break;
            case 4:
                this.Content = this.SaleOrderMail(_Content);
                break;
            default:
                break;
        }
    }
}
