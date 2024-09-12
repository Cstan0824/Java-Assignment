package project.global;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MailTemplate {
    private String Content;

    //getter 
    public String getTemplate() {
        return this.Content;
    }

    //For user Module
    private String OTPMail(String _OTP) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currDate = dateFormat.format(date);
        String html = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "  <head>" +
                "    <link" +
                "      href=\"https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap\"" +
                "      rel=\"stylesheet\"" +
                "    />" +
                "  </head>" +
                "  <body" +
                "    style=\"" +
                "      margin: 0;" +
                "      font-family: 'Poppins', sans-serif;" +
                "      background: #ffffff;" +
                "      font-size: 14px;" +
                "    \"" +
                "  >" +
                "    <div" +
                "      style=\"" +
                "        max-width: 680px;" +
                "        margin: 0 auto;" +
                "        padding: 45px 30px 60px;" +
                "        background: #f4f7ff;" +
                "        background-image: url(https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661497957196_595865/email-template-background-banner);"
                +
                "        background-repeat: no-repeat;" +
                "        background-size: 800px 452px;" +
                "        background-position: top center;" +
                "        font-size: 14px;" +
                "        color: #434343;" +
                "      \"" +
                "    >" +
                "      <header>" +
                "        <table style=\"width: 100%;\">" +
                "          <tbody>" +
                "            <tr style=\"height: 0;\">" +
                "              <td>" +
                "                <img" +
                "                  alt=\"\"" +
                "                  src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1663574980688_114990/archisketch-logo\""
                +
                "                  height=\"30px\"" +
                "                />" +
                "              </td>" +
                "              <td style=\"text-align: right;\">" +
                "                <span" +
                "                  style=\"font-size: 16px; line-height: 30px; color: #ffffff;\"" +
                "                  >" +
                currDate +
                "</span" +
                "                >" +
                "              </td>" +
                "            </tr>" +
                "          </tbody>" +
                "        </table>" +
                "      </header>" +
                "" +
                "      <main>" +
                "        <div" +
                "          style=\"" +
                "            margin: 0;" +
                "            margin-top: 70px;" +
                "            padding: 92px 30px 115px;" +
                "            background: #ffffff;" +
                "            border-radius: 30px;" +
                "            text-align: center;" +
                "          \"" +
                "        >" +
                "          <div style=\"width: 100%; max-width: 489px; margin: 0 auto;\">" +
                "            <h1" +
                "              style=\"" +
                "                margin: 0;" +
                "                font-size: 24px;" +
                "                font-weight: 500;" +
                "                color: #1f1f1f;" +
                "              \"" +
                "            >" +
                "OTP" +
                "            </h1>" +
                "            <p" +
                "              style=\"" +
                "                margin: 0;" +
                "                margin-top: 17px;" +
                "                font-size: 16px;" +
                "                font-weight: 500;" +
                "              \"" +
                "            >" +
                "              Hey Tomy," +
                "            </p>" +
                "            <p" +
                "              style=\"" +
                "                margin: 0;" +
                "                margin-top: 17px;" +
                "                font-weight: 500;" +
                "                letter-spacing: 0.56px;" +
                "              \"" +
                "            >" +
                "              Thank you for choosing Inventory Solution Company. Use the following OTP" +
                "              to complete the procedure to change your email address. OTP is " +
                "              valid for" +
                "              <span style=\"font-weight: 600; color: #1f1f1f;\">5 minutes</span>." +
                "              Do not share this code with others, including Inventory Solution" +
                "              employees." +
                "            </p>" +
                "            <p" +
                "              style=\"" +
                "                margin: 0;" +
                "                margin-top: 60px;" +
                "                font-size: 40px;" +
                "                font-weight: 600;" +
                "                letter-spacing: 25px;" +
                "                color: #ba3d4f;" +
                "              \"" +
                "            >" +
                _OTP +
                "            </p>" +
                "          </div>" +
                "        </div>" +
                "" +
                "        <p" +
                "          style=\"" +
                "            max-width: 400px;" +
                "            margin: 0 auto;" +
                "            margin-top: 90px;" +
                "            text-align: center;" +
                "            font-weight: 500;" +
                "            color: #8c8c8c;" +
                "          \"" +
                "        >" +
                "          Need help? Ask at" +
                "          <a" +
                "            href=\"mailto:archisketch@gmail.com\"" +
                "            style=\"color: #499fb6; text-decoration: none;\"" +
                "            >tarumtmoviesociety@gmail.com</a" +
                "          >" +
                "          or visit our" +
                "          <a" +
                "            href=\"\"" +
                "            target=\"_blank\"" +
                "            style=\"color: #499fb6; text-decoration: none;\"" +
                "            >Help Center</a" +
                "          >" +
                "        </p>" +
                "      </main>" +
                "" +
                "      <footer" +
                "        style=\"" +
                "          width: 100%;" +
                "          max-width: 490px;" +
                "          margin: 20px auto 0;" +
                "          text-align: center;" +
                "          border-top: 1px solid #e6ebf1;" +
                "        \"" +
                "      >" +
                "        <p" +
                "          style=\"" +
                "            margin: 0;" +
                "            margin-top: 40px;" +
                "            font-size: 16px;" +
                "            font-weight: 600;" +
                "            color: #434343;" +
                "          \"" +
                "        >" +
                "          Inventory Solution Company" +
                "        </p>" +
                "        <p style=\"margin: 0; margin-top: 8px; color: #434343;\">" +
                "          Address 540, City, State." +
                "        </p>" +
                "        <div style=\"margin: 0; margin-top: 16px;\">" +
                "          <a href=\"\" target=\"_blank\" style=\"display: inline-block;\">" +
                "            <img" +
                "              width=\"36px\"" +
                "              alt=\"Facebook\"" +
                "              src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661502815169_682499/email-template-icon-facebook\""
                +
                "            />" +
                "          </a>" +
                "          <a" +
                "            href=\"\"" +
                "            target=\"_blank\"" +
                "            style=\"display: inline-block; margin-left: 8px;\"" +
                "          >" +
                "            <img" +
                "              width=\"36px\"" +
                "              alt=\"Instagram\"" +
                "              src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661504218208_684135/email-template-icon-instagram\""
                +
                "          /></a>" +
                "          <a" +
                "            href=\"\"" +
                "            target=\"_blank\"" +
                "            style=\"display: inline-block; margin-left: 8px;\"" +
                "          >" +
                "            <img" +
                "              width=\"36px\"" +
                "              alt=\"Twitter\"" +
                "              src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661503043040_372004/email-template-icon-twitter\""
                +
                "            />" +
                "          </a>" +
                "          <a" +
                "            href=\"\"" +
                "            target=\"_blank\"" +
                "            style=\"display: inline-block; margin-left: 8px;\"" +
                "          >" +
                "            <img" +
                "              width=\"36px\"" +
                "              alt=\"Youtube\"" +
                "              src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661503195931_210869/email-template-icon-youtube\""
                +
                "          /></a>" +
                "        </div>" +
                "        <p style=\"margin: 0; margin-top: 16px; color: #434343;\">" +
                "          Copyright © 2024 Inventory System. All rights reserved." +
                "        </p>" +
                "      </footer>" +
                "    </div>" +
                "  </body>" +
                "</html>";
        return html;
    }

    //For Transaction Module
    private String PurchaseOrder(String _DocNo) {
        return "Dear Customer,<br/><br/>" +
                "We are pleased to inform you that your Purchase Order No is " + _DocNo + ".<br/>" +
                "Thank you for choosing our services.<br/><br/>" +
                "Best regards,<br/>" +
                "Inventory Solution Company";
    }

    private String SaleOrder(String _DocNo) {
        return "Dear Customer,<br/><br/>" +
                "We are glad to confirm that your Sale Order No is " + _DocNo + ".<br/>" +
                "Thank you for doing business with us.<br/><br/>" +
                "Best regards,<br/>" +
                "Inventory Solution Company";
    }

    private String OrderCancellation(String _DocNo) {
        return "Dear Customer,<br/>" +
                "We acknowledge the cancellation of your order. Your Order Cancellation No is " + _DocNo + ".<br/>" +
                "We regret any inconvenience caused.<br/><br/>" +
                "Best regards,<br/>" +
                "Inventory Solution Company";
    }

    private String SalesOrderCancellation(String _DocNo) {
        return "Dear Customer,<br/>" +
                "We acknowledge the cancellation of your order. Your order for " + _DocNo + " has been cancelled.<br/>" +
                "We regret any inconvenience caused.<br/><br/>" +
                "Best regards,<br/>" +
                "Inventory Solution Company";
    }

    private String Reordering(String _DocNo) {
        return "Dear Customer,<br/><br/>" +
                "We have processed your reorder request. The Reordering No is " + _DocNo + ".<br/>" +
                "Thank you for your continued business.<br/><br/>" +
                "Best regards,<br/>" +
                "Inventory Solution Company";
    }

    private String OrderConfirmation(String _DocNo) {
        return "Dear Customer,<br/><br/>" +
                "We are happy to confirm that the order for " + _DocNo + " is successfully placed and confirmed.<br/>" +
                "Thank you for your trust in us.<br/><br/>" +
                "Best regards,<br/>" +
                "Inventory Solution Company";
    }

    private String FollowOrderStatus(String _DocNo) {
        return "Dear Customer,<br/><br/>" +
                "This is a follow-up regarding the status of your Purchase Order " + _DocNo + ".<br/>" +
                "Please let us know if you need any further assistance.<br/><br/>" +
                "Best regards,<br/>" +
                "Inventory Solution Company";
    }

    private String OrderMofification(String _DocNo) {

        return "Dear Customer,<br/><br/>" +
                "We are happy to inform that the order for " + _DocNo + " is successfully modified.<br/>"  + "Please let us know if you need any further assistance.<br/><br/>" +
                "Best regards,<br/>" +
                "Inventory Solution Company";
    }

    //For Schedule Module
    private String ScheduleCreation(String _DocNo) {
        return "Dear Customer,<br/><br/>" +
                "We are happy to inform that the order for " + _DocNo + " is successfully confirmed and scheduled for delivery.<br/>" + "Please refer the attached document for the delivery schedule information.<br/>" +
                "Please let us know if you need any further assistance.<br/><br/>" +
                "Best regards,<br/>" +
                "Inventory Solution Company";
    }

    private String ScheduleCancellation(String _DocNo){
        return "Dear Customer,<br/><br/>" +
                "We acknowledge the cancellation of your delivery schedule for " + _DocNo + ".<br/>" +
                "We regret any inconvenience caused.<br/><br/>" +
                "Best regards,<br/>" +
                "Inventory Solution Company";
    }

    private String ScheduleModification(String _DocNo) {

        return "Dear Customer,<br/><br/>" +
                "We are happy to inform that the delivery schedule for " + _DocNo + " has been modified.<br/>"  + "Please refer the attached document for the new delivery schedule information.<br/>" + "Please let us know if you need any further assistance.<br/><br/>" +
                "Best regards,<br/>" +
                "Inventory Solution Company";
    }

    private String SalesOrderDelivered(String _DocNo) {

        return "Dear Customer,<br/><br/>" +
                "We are happy to inform that the your Order - " + _DocNo + " has been delivered successfully.<br/>"  + "Please check the receive goods and let us know if you need any further assistance.<br/>" + "Thank you for ordering from us. <br/><br/>" +
                "Best regards,<br/>" +
                "Inventory Solution Company";

    }
    //For User Module
    private String REGISTRATION_APPROVAL(String RetailerID) {

        return "Dear Customer,<br/><br/>" +
                "We are happy to inform that your registration request has been approved.<br/>"  + "This is your Retailer ID : " + RetailerID + " <br> Please let us know if you need any further assistance.<br/><br/>" +
                "Best regards,<br/>" +
                "Inventory Solution Company";
    }

    private String REGISTRATION_REJECTION(){
        return "Dear Customer,<br/><br/>" +
                "We regret to inform that your registration request has been rejected.<br/>" +
                "Please let us know if you need any further assistance.<br/><br/>" +
                "Best regards,<br/>" +
                "Inventory Solution Company";
    }

    private String RETAILER_CREATED(String RetailerID) {

        return "Dear Customer,<br/><br/>" +
                "We are happy to inform that you have been registered as a retailer.<br/>"  + "This is your Retailer ID : " + RetailerID + " <br> Please let us know if you need any further assistance.<br/><br/>" +
                "Best regards,<br/>" +
                "Inventory Solution Company";

    }


    public enum TemplateType {
        OTP,
        PURCHASE_ORDER,
        SALES_ORDER,
        ORDER_CANCELLATION,
        SALES_ORDER_CANCELLATION,
        ORDER_MODIFICATION,
        REORDERING,
        ORDER_CONFIRMATION,
        FOLLOW_ORDER_STATUS,
        REGISTRATION_APPROVAL,
        REGISTRATION_REJECTION,
        SCHEDULE_CREATION,
        SCHEDULE_CANCELLATION,
        SCHEDULE_MODIFICATION,
        SO_DELIVERED,
        RETAILER_CREATED
    }

    public MailTemplate(String _Content, TemplateType _templateType) {
        switch (_templateType) {
            case OTP:
                this.Content = this.OTPMail(_Content);
                break;
            case PURCHASE_ORDER:
                this.Content = this.PurchaseOrder(_Content);
                break;
            case SALES_ORDER:
                this.Content = this.SaleOrder(_Content);
                break;
            case ORDER_CANCELLATION:
                this.Content = this.OrderCancellation(_Content);
                break;
            case SALES_ORDER_CANCELLATION:
                this.Content = this.SalesOrderCancellation(_Content);
                break;
            case ORDER_MODIFICATION:
                this.Content = this.OrderMofification(_Content);
                break;
            case REORDERING:
                this.Content = this.Reordering(_Content);
                break;
            case ORDER_CONFIRMATION:
                this.Content = this.OrderConfirmation(_Content);
                break;
            case FOLLOW_ORDER_STATUS:
                this.Content = this.FollowOrderStatus(_Content);
                break;
            case SCHEDULE_CREATION:
                this.Content = this.ScheduleCreation(_Content);
                break;
            case SCHEDULE_CANCELLATION:
                this.Content = this.ScheduleCancellation(_Content);
                break;
            case SCHEDULE_MODIFICATION:
                this.Content = this.ScheduleModification(_Content);
                break;
            case REGISTRATION_APPROVAL:
                this.Content = this.REGISTRATION_APPROVAL(_Content);
                break;
            case REGISTRATION_REJECTION:
                this.Content = this.REGISTRATION_REJECTION();
                break;
            case RETAILER_CREATED:
                this.Content = this.RETAILER_CREATED(_Content);
                break;
            case SO_DELIVERED:
                this.Content = this.SalesOrderDelivered(_Content);
                break;
            default:
                break;
        }

        
    }
}
