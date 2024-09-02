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
        String html = "<!DOCTYPE html><br/>" +
                "<html lang=\"en\"><br/>" +
                "  <head><br/>" +
                "    <link<br/>" +
                "      href=\"https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap\"<br/>" +
                "      rel=\"stylesheet\"<br/>" +
                "    /><br/>" +
                "  </head><br/>" +
                "  <body<br/>" +
                "    style=\"<br/>" +
                "      margin: 0;<br/>" +
                "      font-family: 'Poppins', sans-serif;<br/>" +
                "      background: #ffffff;<br/>" +
                "      font-size: 14px;<br/>" +
                "    \"<br/>" +
                "  ><br/>" +
                "    <div<br/>" +
                "      style=\"<br/>" +
                "        max-width: 680px;<br/>" +
                "        margin: 0 auto;<br/>" +
                "        padding: 45px 30px 60px;<br/>" +
                "        background: #f4f7ff;<br/>" +
                "        background-image: url(https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661497957196_595865/email-template-background-banner);<br/>"
                +
                "        background-repeat: no-repeat;<br/>" +
                "        background-size: 800px 452px;<br/>" +
                "        background-position: top center;<br/>" +
                "        font-size: 14px;<br/>" +
                "        color: #434343;<br/>" +
                "      \"<br/>" +
                "    ><br/>" +
                "      <header><br/>" +
                "        <table style=\"width: 100%;\"><br/>" +
                "          <tbody><br/>" +
                "            <tr style=\"height: 0;\"><br/>" +
                "              <td><br/>" +
                "                <img<br/>" +
                "                  alt=\"\"<br/>" +
                "                  src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1663574980688_114990/archisketch-logo\"<br/>"
                +
                "                  height=\"30px\"<br/>" +
                "                /><br/>" +
                "              </td><br/>" +
                "              <td style=\"text-align: right;\"><br/>" +
                "                <span<br/>" +
                "                  style=\"font-size: 16px; line-height: 30px; color: #ffffff;\"<br/>" +
                "                  ><br/>" +
                currDate +
                "</span<br/>" +
                "                ><br/>" +
                "              </td><br/>" +
                "            </tr><br/>" +
                "          </tbody><br/>" +
                "        </table><br/>" +
                "      </header><br/>" +
                "<br/>" +
                "      <main><br/>" +
                "        <div<br/>" +
                "          style=\"<br/>" +
                "            margin: 0;<br/>" +
                "            margin-top: 70px;<br/>" +
                "            padding: 92px 30px 115px;<br/>" +
                "            background: #ffffff;<br/>" +
                "            border-radius: 30px;<br/>" +
                "            text-align: center;<br/>" +
                "          \"<br/>" +
                "        ><br/>" +
                "          <div style=\"width: 100%; max-width: 489px; margin: 0 auto;\"><br/>" +
                "            <h1<br/>" +
                "              style=\"<br/>" +
                "                margin: 0;<br/>" +
                "                font-size: 24px;<br/>" +
                "                font-weight: 500;<br/>" +
                "                color: #1f1f1f;<br/>" +
                "              \"<br/>" +
                "            ><br/>" +
                "OTP<br/>" +
                "            </h1><br/>" +
                "            <p<br/>" +
                "              style=\"<br/>" +
                "                margin: 0;<br/>" +
                "                margin-top: 17px;<br/>" +
                "                font-size: 16px;<br/>" +
                "                font-weight: 500;<br/>" +
                "              \"<br/>" +
                "            ><br/>" +
                "              Hey Tomy,<br/>" +
                "            </p><br/>" +
                "            <p<br/>" +
                "              style=\"<br/>" +
                "                margin: 0;<br/>" +
                "                margin-top: 17px;<br/>" +
                "                font-weight: 500;<br/>" +
                "                letter-spacing: 0.56px;<br/>" +
                "              \"<br/>" +
                "            ><br/>" +
                "              Thank you for choosing Inventory Solution Company. Use the following OTP<br/>" +
                "              to complete the procedure to change your email address. OTP is <br/>" +
                "              valid for<br/>" +
                "              <span style=\"font-weight: 600; color: #1f1f1f;\">5 minutes</span>.<br/>" +
                "              Do not share this code with others, including Inventory Solution<br/>" +
                "              employees.<br/>" +
                "            </p><br/>" +
                "            <p<br/>" +
                "              style=\"<br/>" +
                "                margin: 0;<br/>" +
                "                margin-top: 60px;<br/>" +
                "                font-size: 40px;<br/>" +
                "                font-weight: 600;<br/>" +
                "                letter-spacing: 25px;<br/>" +
                "                color: #ba3d4f;<br/>" +
                "              \"<br/>" +
                "            ><br/>" +
                _OTP +
                "            </p><br/>" +
                "          </div><br/>" +
                "        </div><br/>" +
                "<br/>" +
                "        <p<br/>" +
                "          style=\"<br/>" +
                "            max-width: 400px;<br/>" +
                "            margin: 0 auto;<br/>" +
                "            margin-top: 90px;<br/>" +
                "            text-align: center;<br/>" +
                "            font-weight: 500;<br/>" +
                "            color: #8c8c8c;<br/>" +
                "          \"<br/>" +
                "        ><br/>" +
                "          Need help? Ask at<br/>" +
                "          <a<br/>" +
                "            href=\"mailto:archisketch@gmail.com\"<br/>" +
                "            style=\"color: #499fb6; text-decoration: none;\"<br/>" +
                "            >tarumtmoviesociety@gmail.com</a<br/>" +
                "          ><br/>" +
                "          or visit our<br/>" +
                "          <a<br/>" +
                "            href=\"\"<br/>" +
                "            target=\"_blank\"<br/>" +
                "            style=\"color: #499fb6; text-decoration: none;\"<br/>" +
                "            >Help Center</a<br/>" +
                "          ><br/>" +
                "        </p><br/>" +
                "      </main><br/>" +
                "<br/>" +
                "      <footer<br/>" +
                "        style=\"<br/>" +
                "          width: 100%;<br/>" +
                "          max-width: 490px;<br/>" +
                "          margin: 20px auto 0;<br/>" +
                "          text-align: center;<br/>" +
                "          border-top: 1px solid #e6ebf1;<br/>" +
                "        \"<br/>" +
                "      ><br/>" +
                "        <p<br/>" +
                "          style=\"<br/>" +
                "            margin: 0;<br/>" +
                "            margin-top: 40px;<br/>" +
                "            font-size: 16px;<br/>" +
                "            font-weight: 600;<br/>" +
                "            color: #434343;<br/>" +
                "          \"<br/>" +
                "        ><br/>" +
                "          Inventory Solution Company<br/>" +
                "        </p><br/>" +
                "        <p style=\"margin: 0; margin-top: 8px; color: #434343;\"><br/>" +
                "          Address 540, City, State.<br/>" +
                "        </p><br/>" +
                "        <div style=\"margin: 0; margin-top: 16px;\"><br/>" +
                "          <a href=\"\" target=\"_blank\" style=\"display: inline-block;\"><br/>" +
                "            <img<br/>" +
                "              width=\"36px\"<br/>" +
                "              alt=\"Facebook\"<br/>" +
                "              src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661502815169_682499/email-template-icon-facebook\"<br/>"
                +
                "            /><br/>" +
                "          </a><br/>" +
                "          <a<br/>" +
                "            href=\"\"<br/>" +
                "            target=\"_blank\"<br/>" +
                "            style=\"display: inline-block; margin-left: 8px;\"<br/>" +
                "          ><br/>" +
                "            <img<br/>" +
                "              width=\"36px\"<br/>" +
                "              alt=\"Instagram\"<br/>" +
                "              src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661504218208_684135/email-template-icon-instagram\"<br/>"
                +
                "          /></a><br/>" +
                "          <a<br/>" +
                "            href=\"\"<br/>" +
                "            target=\"_blank\"<br/>" +
                "            style=\"display: inline-block; margin-left: 8px;\"<br/>" +
                "          ><br/>" +
                "            <img<br/>" +
                "              width=\"36px\"<br/>" +
                "              alt=\"Twitter\"<br/>" +
                "              src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661503043040_372004/email-template-icon-twitter\"<br/>"
                +
                "            /><br/>" +
                "          </a><br/>" +
                "          <a<br/>" +
                "            href=\"\"<br/>" +
                "            target=\"_blank\"<br/>" +
                "            style=\"display: inline-block; margin-left: 8px;\"<br/>" +
                "          ><br/>" +
                "            <img<br/>" +
                "              width=\"36px\"<br/>" +
                "              alt=\"Youtube\"<br/>" +
                "              src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661503195931_210869/email-template-icon-youtube\"<br/>"
                +
                "          /></a><br/>" +
                "        </div><br/>" +
                "        <p style=\"margin: 0; margin-top: 16px; color: #434343;\"><br/>" +
                "          Copyright © 2024 Inventory System. All rights reserved.<br/>" +
                "        </p><br/>" +
                "      </footer><br/>" +
                "    </div><br/>" +
                "  </body><br/>" +
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

    public enum TemplateType {
        OTP,
        PURCHASE_ORDER,
        SALES_ORDER,
        ORDER_CANCELLATION,
        REORDERING,
        ORDER_CONFIRMATION,
        FOLLOW_ORDER_STATUS
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
            case REORDERING:
                this.Content = this.Reordering(_Content);
                break;
            case ORDER_CONFIRMATION:
                this.Content = this.OrderConfirmation(_Content);
                break;
            case FOLLOW_ORDER_STATUS:
                this.Content = this.FollowOrderStatus(_Content);
                break;
            default:
                break;
        }
    }
}
