package com.finsecure.wallet.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class EmailTemplateUtil {

    private EmailTemplateUtil() {
    }

    /* ============================
       COMMON HEADER & FOOTER
       ============================ */

    private static final String HTML_START =
            "<!DOCTYPE html>" +
                    "<html><head><meta charset='UTF-8'></head>" +
                    "<body style='margin:0; padding:0; background-color:#f2f4f7; font-family:Arial, Helvetica, sans-serif;'>" +
                    "<table width='100%' cellpadding='0' cellspacing='0' style='background-color:#f2f4f7; padding:20px 0;'>" +
                    "<tr><td align='center'>" +
                    "<table width='650' cellpadding='0' cellspacing='0' style='background-color:#ffffff; border-radius:6px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.08);'>";

    private static final String HEADER =
            "<tr>" +
                    "<td style='padding:20px; background-color:#1e3a8a; color:#ffffff; text-align:center;'>" +
                    "<div style='font-size:22px; font-weight:bold;'>FinSecure Wallet</div>" +
                    "<div style='font-size:14px;'>Secure Digital Wallet Platform</div>" +
                    "</td>" +
                    "</tr>";



    private static final String FOOTER =
            "<tr>" +
                    "<td style='padding:15px 30px; background-color:#f8f9fa; border-top:1px solid #e0e0e0;'>" +
                    "<p style='margin:0; font-size:12px; color:#6c757d; text-align:center;'>" +
                    "This is an automated email from FinSecure Wallet. Please do not reply to this email." +
                    "</p>" +
                    "</td>" +
                    "</tr>";

    private static final String HTML_END =
            "</table></td></tr></table></body></html>";

    public static final String FORGET_PASSWORD_OTP =
            "FinSecure Wallet - Password Reset OTP";

    public static final String LOGIN_PASSWORD_OTP =
            "FinSecure Wallet - Login OTP";

    public static final String REGISTER_PASSWORD_OTP =
            "FinSecure Wallet - Registration OTP";

    public static String registrationOtpTemplate(
            String userName,
            String otp) {

        return HTML_START + HEADER +

                emailbodyStart(
                        "ONE-TIME PASSWORD (OTP)",
                        null
                ) +

                paragraph("Dear <strong>" + userName + "</strong>,") +

                paragraph("Thank you for registering with FinSecure Wallet. Your One-Time Password (OTP) for email verification is:") +

                otpBoxStyled(otp) +

                paragraph(
                        "This OTP is valid for <strong>2 minutes</strong>. "
                                + "Please do not share this OTP with anyone. "
                                + "If you did not initiate this request, please ignore this email."
                ) +

                signOff() +

                FOOTER + HTML_END;
    }

    private static String buttonStyled(String text, String url) {
        return "<div style='text-align:center; margin:20px 0;'>"
                + "<a href='" + url + "' "
                + "style='background-color:#0d6efd; color:#ffffff; "
                + "padding:12px 24px; text-decoration:none; "
                + "border-radius:5px; font-weight:bold;'>"
                + text + "</a></div>";
    }

    public static String otpBoxStyled(String otp) {

        String spacedOtp = otp.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .reduce((a, b) -> a + " " + b)
                .orElse("");

        return "<div style='"
                + "width:100%; max-width:520px; margin:20px auto 8px auto; "
                + "padding:14px 10px; text-align:center; "
                + "background-color:#eaf4ff; "
                + "border:1.5px solid #2f80ff; "
                + "border-radius:8px; "
                + "font-size:20px; font-weight:600; "
                + "letter-spacing:6px; color:#000;'>"
                + spacedOtp +
                "</div>"
                + "<div style='text-align:center; font-size:13px; color:#555;'>"
                + "Don&apos;t share this code with anyone."
                + "</div>";
    }

    /* ============================
       HELPER METHODS
       ============================ */

    private static String bodyStart(String title, String refNo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = LocalDate.now().format(formatter);
        return "<tr><td style='padding:25px 30px; color:#1f2d3d; font-size:14px; line-height:22px;'>" +
                "<p style='margin:0 0 10px 0; font-size:18px; font-weight:bold; color:#0b3c5d;'>" + title + "</p>" +
                "<hr style='border:none; border-top:1px solid #e0e0e0; margin:10px 0 20px 0;'>" +
                "<table width='100%' style='margin-bottom:15px;'>" +
                "<tr>" +
                "<td style='font-size:13px;'>" +
                "<strong>Ref No:</strong> <span style='color:#6c757d; font-style:italic;'>" +
                (refNo == null || refNo.trim().isEmpty() ? "Will be generated after approval" : refNo) +
                "</span>"+
                "</td>" +
                "<td style='font-size:13px; text-align:right;'><strong>Date:</strong> " + formattedDate + "</td>" +
                "</tr></table>";
    }

    private static String bodyStartForForward(String title) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = LocalDate.now().format(formatter);
        return "<tr><td style='padding:25px 30px; color:#1f2d3d; font-size:14px; line-height:22px;'>" +
                "<p style='margin:0 0 10px 0; font-size:18px; font-weight:bold; color:#0b3c5d;'>" + title + "</p>" +
                "<hr style='border:none; border-top:1px solid #e0e0e0; margin:10px 0 20px 0;'>" +
                "<table width='100%' style='margin-bottom:15px;'>" +
                "<tr>" +
                "<td style='font-size:13px; text-align:right;'><strong>Date:</strong> " + formattedDate + "</td>" +
                "</tr></table>";
    }


    private static String emailbodyStart(String title, String refNo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = LocalDate.now().format(formatter);
        return "<tr><td style='padding:25px 30px; color:#1f2d3d; font-size:14px; line-height:22px;'>" +
                "<p style='margin:0 0 10px 0; font-size:18px; font-weight:bold; color:#0b3c5d;'>" + title + "</p>" +
                "<hr style='border:none; border-top:1px solid #e0e0e0; margin:10px 0 20px 0;'>" +
                "<table width='100%' style='margin-bottom:15px;'>" +
                "<tr>" +
                "<td style='font-size:13px; text-align:right;'><strong>Date:</strong> " + formattedDate + "</td>" +
                "</tr></table>";
    }

    private static String paragraph(String text) {
        return "<p style='margin:0 0 15px 0;'>" + text + "</p>";
    }

    private static String signOff() {
        return "<p style='margin:20px 0 0 0;'>" +
                "With regards,<br>" +
                "<strong> FinSecure Wallet </strong>" +
                "</p></td></tr>";
    }
}
