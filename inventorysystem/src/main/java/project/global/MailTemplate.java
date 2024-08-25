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
        String html =  "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <link\n" +
                "      href=\"https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap\"\n" +
                "      rel=\"stylesheet\"\n" +
                "    />\n" +
                "  </head>\n" +
                "  <body\n" +
                "    style=\"\n" +
                "      margin: 0;\n" +
                "      font-family: 'Poppins', sans-serif;\n" +
                "      background: #ffffff;\n" +
                "      font-size: 14px;\n" +
                "    \"\n" +
                "  >\n" +
                "    <div\n" +
                "      style=\"\n" +
                "        max-width: 680px;\n" +
                "        margin: 0 auto;\n" +
                "        padding: 45px 30px 60px;\n" +
                "        background: #f4f7ff;\n" +
                "        background-image: url(https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661497957196_595865/email-template-background-banner);\n"
                +
                "        background-repeat: no-repeat;\n" +
                "        background-size: 800px 452px;\n" +
                "        background-position: top center;\n" +
                "        font-size: 14px;\n" +
                "        color: #434343;\n" +
                "      \"\n" +
                "    >\n" +
                "      <header>\n" +
                "        <table style=\"width: 100%;\">\n" +
                "          <tbody>\n" +
                "            <tr style=\"height: 0;\">\n" +
                "              <td>\n" +
                "                <img\n" +
                "                  alt=\"\"\n" +
                "                  src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1663574980688_114990/archisketch-logo\"\n"
                +
                "                  height=\"30px\"\n" +
                "                />\n" +
                "              </td>\n" +
                "              <td style=\"text-align: right;\">\n" +
                "                <span\n" +
                "                  style=\"font-size: 16px; line-height: 30px; color: #ffffff;\"\n" +
                "                  >\n" +
                currDate +
                "</span\n" +
                "                >\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </tbody>\n" +
                "        </table>\n" +
                "      </header>\n" +
                "\n" +
                "      <main>\n" +
                "        <div\n" +
                "          style=\"\n" +
                "            margin: 0;\n" +
                "            margin-top: 70px;\n" +
                "            padding: 92px 30px 115px;\n" +
                "            background: #ffffff;\n" +
                "            border-radius: 30px;\n" +
                "            text-align: center;\n" +
                "          \"\n" +
                "        >\n" +
                "          <div style=\"width: 100%; max-width: 489px; margin: 0 auto;\">\n" +
                "            <h1\n" +
                "              style=\"\n" +
                "                margin: 0;\n" +
                "                font-size: 24px;\n" +
                "                font-weight: 500;\n" +
                "                color: #1f1f1f;\n" +
                "              \"\n" +
                "            >\n" +
                            "OTP\n" +
                "            </h1>\n" +
                "            <p\n" +
                "              style=\"\n" +
                "                margin: 0;\n" +
                "                margin-top: 17px;\n" +
                "                font-size: 16px;\n" +
                "                font-weight: 500;\n" +
                "              \"\n" +
                "            >\n" +
                "              Hey Tomy,\n" +
                "            </p>\n" +
                "            <p\n" +
                "              style=\"\n" +
                "                margin: 0;\n" +
                "                margin-top: 17px;\n" +
                "                font-weight: 500;\n" +
                "                letter-spacing: 0.56px;\n" +
                "              \"\n" +
                "            >\n" +
                "              Thank you for choosing Inventory Solution Company. Use the following OTP\n" +
                "              to complete the procedure to change your email address. OTP is \n" +
                "              valid for\n" +
                "              <span style=\"font-weight: 600; color: #1f1f1f;\">5 minutes</span>.\n" +
                "              Do not share this code with others, including Inventory Solution\n" +
                "              employees.\n" +
                "            </p>\n" +
                "            <p\n" +
                "              style=\"\n" +
                "                margin: 0;\n" +
                "                margin-top: 60px;\n" +
                "                font-size: 40px;\n" +
                "                font-weight: 600;\n" +
                "                letter-spacing: 25px;\n" +
                "                color: #ba3d4f;\n" +
                "              \"\n" +
                "            >\n" +
                            _OTP +
                "            </p>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "\n" +
                "        <p\n" +
                "          style=\"\n" +
                "            max-width: 400px;\n" +
                "            margin: 0 auto;\n" +
                "            margin-top: 90px;\n" +
                "            text-align: center;\n" +
                "            font-weight: 500;\n" +
                "            color: #8c8c8c;\n" +
                "          \"\n" +
                "        >\n" +
                "          Need help? Ask at\n" +
                "          <a\n" +
                "            href=\"mailto:archisketch@gmail.com\"\n" +
                "            style=\"color: #499fb6; text-decoration: none;\"\n" +
                "            >tarumtmoviesociety@gmail.com</a\n" +
                "          >\n" +
                "          or visit our\n" +
                "          <a\n" +
                "            href=\"\"\n" +
                "            target=\"_blank\"\n" +
                "            style=\"color: #499fb6; text-decoration: none;\"\n" +
                "            >Help Center</a\n" +
                "          >\n" +
                "        </p>\n" +
                "      </main>\n" +
                "\n" +
                "      <footer\n" +
                "        style=\"\n" +
                "          width: 100%;\n" +
                "          max-width: 490px;\n" +
                "          margin: 20px auto 0;\n" +
                "          text-align: center;\n" +
                "          border-top: 1px solid #e6ebf1;\n" +
                "        \"\n" +
                "      >\n" +
                "        <p\n" +
                "          style=\"\n" +
                "            margin: 0;\n" +
                "            margin-top: 40px;\n" +
                "            font-size: 16px;\n" +
                "            font-weight: 600;\n" +
                "            color: #434343;\n" +
                "          \"\n" +
                "        >\n" +
                "          Inventory Solution Company\n" +
                "        </p>\n" +
                "        <p style=\"margin: 0; margin-top: 8px; color: #434343;\">\n" +
                "          Address 540, City, State.\n" +
                "        </p>\n" +
                "        <div style=\"margin: 0; margin-top: 16px;\">\n" +
                "          <a href=\"\" target=\"_blank\" style=\"display: inline-block;\">\n" +
                "            <img\n" +
                "              width=\"36px\"\n" +
                "              alt=\"Facebook\"\n" +
                "              src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661502815169_682499/email-template-icon-facebook\"\n"
                +
                "            />\n" +
                "          </a>\n" +
                "          <a\n" +
                "            href=\"\"\n" +
                "            target=\"_blank\"\n" +
                "            style=\"display: inline-block; margin-left: 8px;\"\n" +
                "          >\n" +
                "            <img\n" +
                "              width=\"36px\"\n" +
                "              alt=\"Instagram\"\n" +
                "              src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661504218208_684135/email-template-icon-instagram\"\n"
                +
                "          /></a>\n" +
                "          <a\n" +
                "            href=\"\"\n" +
                "            target=\"_blank\"\n" +
                "            style=\"display: inline-block; margin-left: 8px;\"\n" +
                "          >\n" +
                "            <img\n" +
                "              width=\"36px\"\n" +
                "              alt=\"Twitter\"\n" +
                "              src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661503043040_372004/email-template-icon-twitter\"\n"
                +
                "            />\n" +
                "          </a>\n" +
                "          <a\n" +
                "            href=\"\"\n" +
                "            target=\"_blank\"\n" +
                "            style=\"display: inline-block; margin-left: 8px;\"\n" +
                "          >\n" +
                "            <img\n" +
                "              width=\"36px\"\n" +
                "              alt=\"Youtube\"\n" +
                "              src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661503195931_210869/email-template-icon-youtube\"\n"
                +
                "          /></a>\n" +
                "        </div>\n" +
                "        <p style=\"margin: 0; margin-top: 16px; color: #434343;\">\n" +
                "          Copyright © 2024 Inventory System. All rights reserved.\n" +
                "        </p>\n" +
                "      </footer>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";
        return html;
    }

    //For Transaction Module
    private String PurchaseOrder(String _DocNo) {
        return "Your Purchase Order No is " + _DocNo;
    }

    private String GoodReceivedNotes(String _DocNo) {
        return "Your Good Received Notes No is " + _DocNo;
    }

    private String SaleOrder(String _DocNo) {
        return "Your Sale Order No is " + _DocNo;
    }

    private String OrderCancellation(String _DocNo) {
        return "Your Order Cancellation No is " + _DocNo;
    }

    private String Reordering(String _DocNo) {
        return "Reordering No is" + _DocNo;
    }

    private String OrderConfirmation(String _DocNo) {
        return "The Order For " + _DocNo + "is Confirmed";
    }

    private String FollowOrderStatus(String _DiffStock) {
        return "The Stock has been changed by " + _DiffStock;
    }

    public enum TemplateType {
        OTP,
        PURCHASE_ORDER,
        GOOD_RECEIVED_NOTES,
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
            case GOOD_RECEIVED_NOTES:
                this.Content = this.GoodReceivedNotes(_Content);
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
