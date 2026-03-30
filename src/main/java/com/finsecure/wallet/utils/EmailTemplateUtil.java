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
                    "<td style='padding:15px 20px; background-color:#0b3c5d; color:#ffffff; text-align:center;'>" +

                    // ===== EMBLEM IMAGE =====
                    "<img src='cid:govLogo' " +
                    "alt='Emblem of India' " +
                    "style='display:block; margin:0 auto 8px auto; width:60px; height:auto;' />" +

                    // ===== TEXT =====
                    "<div style='font-size:18px; font-weight:bold;'>Appointment Request Portal</div>" +
                    "<div style='font-size:14px;'>LOK BHAVAN, ODISHA</div>" +
//            "<div style='font-size:13px;'>Government of Odisha</div>" +
                    "</td>" +
                    "</tr>";



    private static final String FOOTER =
            "<tr>" +
                    "<td style='padding:15px 30px; background-color:#f8f9fa; border-top:1px solid #e0e0e0;'>" +
                    "<p style='margin:0; font-size:12px; color:#6c757d; text-align:center;'>" +
                    "This is a system-generated official email. Please do not reply." +
                    "</p>" +
                    "</td>" +
                    "</tr>";

    private static final String HTML_END =
            "</table></td></tr></table></body></html>";


    // 1. Pending
    public static final String APPOINTMENT_PENDING =
            "Appointment Request Received and Under Process – Hon’ble Governor";

    public static final String EVENT_URL =
            "Download Event Photos";

    public static final String EVENT_PASS =
            "Download Event Pass";

    // 2. Approval
    public static final String APPOINTMENT_APPROVAL =
            "Official Approval of Appointment with Hon’ble Governor ";

    public static final String FORGET_PASSWORD_OTP =
            "One-Time Password (OTP) for Password Reset – Lok Bhavan, Odisha";

    public static final String LOGIN_PASSWORD_OTP =
            "One-Time Password (OTP) for Login – Lok Bhavan, Odisha";

    public static final String VISITED_PHOTO =
            "Visit Documentation Images Uploaded";

    public static final String REGISTER_PASSWORD_OTP =
            "One-Time Password (OTP) for Register – Lok Bhavan, Odisha";

    // 3. Confirmation
    public static final String APPOINTMENT_CONFIRMATION =
            "Appointment Confirmation – Hon’ble Governor’s Secretariat ";

    // 4. Reminder
    public static final String APPOINTMENT_REMINDER =
            "Reminder: Scheduled Appointment with Hon’ble Governor ";

    // 5. Reschedule
    public static final String APPOINTMENT_RESCHEDULE =
            "Appointment Rescheduled – Hon’ble Governor’s Secretariat ";

    // 6. Cancellation
    public static final String APPOINTMENT_CANCELLATION =
            "Cancellation of Appointment with Hon’ble Governor ";

    public static final String EVENT_ADDED =
            "Official Event Invitation";

    // 7. Expiration
    public static final String APPOINTMENT_EXPIRATION =
            "Appointment Request Expired – Hon’ble Governor’s Secretariat ";

    // 8. After Visit
    public static final String APPOINTMENT_AFTER_VISIT =
            "Acknowledgement of Visit to Lok Bhavan ";

    // 9. Visit Images Uploaded
    public static final String VISIT_IMAGES_UPLOADED =
            "Visit Photographs Uploaded – Lok Bhavan Records ";

    // 10. Rejection
    public static final String APPOINTMENT_REJECTION =
            "Appointment Request Status";

    public static final String APPOINTMENT_FORWARDED =
            "Appointment Request Status-FORWARDED";

    public static final String APPOINTMENT_CANCELLED =
            "Appointment Request Status – Cancelled – Hon’ble Governor’s Secretariat ";

    public static final String PROGRAM_LIST =
            "Program List Send Successfully";

    public static final String IMMIDIATE_PASS =
            "Download Immidiate Pass";

    public static final String CANCEL_EVENT = "Event Cancelled";

    /* =========================================================
       1. APPROVAL TEMPLATE
       ========================================================= */
    public static String approvalTemplate(String applicantName, String purpose,
                                          String date, String time, String gate, String refNo, String deptName) {

        return HTML_START + HEADER +
                bodyStart("APPOINTMENT REQUEST STATUS – <span style='color:#28a745; font-weight:bold;'>APPROVED</span>", refNo) +

                paragraph("Namaskar <strong>" + applicantName + "</strong>,") +

                paragraph("We are pleased to inform you that your request for appointment with the " + deptName +" has been duly approved by the competent authority.") +

                detailsTable(applicantName, purpose, date, time, gate) +

                paragraph("You are requested to report at least 15 minutes prior to the scheduled time and carry a valid Government-issued photo identity card for verification.") +

                paragraph("In the event of any further developments, including cancellation or modification of the scheduled date and time, the same will be duly communicated to you via email prior to the visit.") +

                signOff() +

                FOOTER + HTML_END;
    }


    public static String otpTemplate(
            String userName,
            String otp) {

        return HTML_START + HEADER +

                emailbodyStart(
                        "ONE-TIME PASSWORD (OTP)",
                        null
                ) +

                paragraph("Namaskar <strong>" + userName + "</strong>,") +

                paragraph("Your One-Time Password (OTP) is:") +

                otpBoxStyled(otp) +

                paragraph(
                        "This OTP is valid for <strong>2 minutes</strong>. "
                                + "Please do not share this OTP with anyone."
                ) +

                signOff() +

                FOOTER + HTML_END;
    }

    public static String visitPhotoAccessTemplate(
            String userName,
            String visitDate,
            String accessUrl) {

        return HTML_START + HEADER +

                emailbodyStart(
                        "VISIT PHOTOS AVAILABLE",
                        null
                ) +

                paragraph("Namaskar <strong>" + userName + "</strong>,") +

                paragraph(
                        "Your visit photos dated <strong>" + visitDate + "</strong> "
                                + "are now available for viewing."
                ) +

                paragraph("You can access your photos using the link below:") +

                buttonStyled("View Visited Photos", accessUrl) +

                paragraph(
                        "For security reasons, this link expire after a 15 days. "
                                + "Please access your photos at the earliest."
                ) +

                signOff() +

                FOOTER + HTML_END;
    }

    public static String eventPhotos(
            String userName,
            String visitDate,
            String accessUrl,String eventName) {

        return HTML_START + HEADER +

                emailbodyStart(
                        eventName.toUpperCase()+" EVENT PHOTOS AVAILABLE",
                        null
                ) +

                paragraph("Namaskar <strong>" + userName + "</strong>,") +

                paragraph(
                        "Your event photos dated <strong>" + visitDate + "</strong> "
                                + "are now available for viewing."
                ) +

                paragraph("You can access your photos using the link below:") +

                buttonStyled("View "+eventName+" Photos", accessUrl) +

                paragraph(
                        "For security reasons, this link expire after a 15 days. "
                                + "Please access your photos at the earliest."
                ) +

                signOff() +

                FOOTER + HTML_END;
    }

    public static String eventPassSend(String userName, String eventName, String visitDate, String eventTime, String fileUrl) {

        return HTML_START + HEADER +

                emailbodyStart(
                        eventName.toUpperCase() + " EVENT PASS AVAILABLE",
                        null
                ) +

                paragraph("Namaskar <strong>" + userName + "</strong>,") +

                paragraph("Your entry pass for the <strong>" + eventName + "</strong> program at Lok Bhavan, Odisha on <strong>"
                        + visitDate + "</strong> at <strong>" + eventTime + "</strong> has been successfully generated.") +

                paragraph("Please download your entry pass using the link below.") +

                paragraph(
                        "<a href='" + fileUrl + "' style='background:#0d6efd;color:#ffffff;padding:12px 22px;" +
                                "text-decoration:none;border-radius:6px;font-weight:600;'>Download Event Pass</a>"
                ) +

//                paragraph("If the button does not work, copy and paste the link below into your browser:<br>" +
//                        "<a href='" + fileUrl + "'>" + fileUrl + "</a>") +

                paragraph("Kindly arrive 15 minutes early and present the downloaded pass at Security Gate No. 3 for smooth entry.") +

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



    /* =========================================================
       1A. PENDING TEMPLATE
       ========================================================= */
    public static String pendingTemplate(String applicantName, String purpose, String refNo, String dept) {

        return HTML_START + HEADER +
                bodyStart("APPOINTMENT REQUEST STATUS – <span style='color:orange; font-weight:bold;'>UNDER REVIEW</span>", refNo) +

                paragraph("Namaskar <strong>" + applicantName + "</strong>,") +

                paragraph("This is to inform you that your request for appointment with the " + dept +
                        " has been duly received and is currently under consideration by the competent authority.") +

                detailsTable(applicantName, purpose, null, null, null) +

                paragraph("The scheduled date, time, and gate number will be communicated to you via email once a decision is taken on your request, subject to official availability and scheduling.") +

                signOff() +

                FOOTER + HTML_END;
    }



    /* =========================================================
       2. CANCELLATION TEMPLATE
       ========================================================= */
    public static String cancellationTemplate(String applicantName, String refNo, String reason,String deptId, String date, String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = LocalDate.now().format(formatter);
        return HTML_START + HEADER +
                bodyStart("APPOINTMENT <span style='color:red; font-weight:bold;'>CANCELLATION</span> NOTICE", refNo) +

                paragraph("Namaskar <strong>" + applicantName + "</strong>,") +
                paragraph("This is to inform you that your scheduled appointment with the "+deptId+" on "+formattedDate+" at "+time+" has been cancelled.") +
                paragraph("<strong>Reason:</strong> " + reason) +
                paragraph("You may submit a fresh request for appointment if required.") +

                signOff() +

                FOOTER + HTML_END;

    }

    public static String cancellationTemplateEvent(String name, String eventName, String eventDate, String eventTime) {
        return HTML_START + HEADER +
                bodyStart("EVENT <span style='red; font-weight:bold;'>CANCELLED</span>", eventName) +
                paragraph("Dear <strong>" + name + "</strong>,") +
                paragraph("The "+eventName+" program scheduled at Lok Bhavan Odisha has been cancelled due to unforeseen circumstances. We regret the inconvenience<strong>") +
                signOff() +
                FOOTER + HTML_END;
    }

    public static String eventTemplate(String name, String eventName, String eventDate, String eventTime, String gateNo, String fileLink) {

        return HTML_START + HEADER +

                bodyStart("EVENT <span style='color:green; font-weight:bold;'>INVITATION</span>", eventName) +
                paragraph("Dear <strong>" + name + "</strong>,") +
                paragraph("You are invited to attend the event <strong>" + eventName + "</strong>.") +
                paragraph("<strong>Event Date:</strong> " + eventDate + "<br>" +
                        "<strong>Event Time:</strong> " + eventTime + "<br>" +
                        "<strong>Gate No:</strong> " + gateNo) +
                paragraph("Please carry the required document/file while entering the venue.") +
                paragraph("<strong>File Provided Below</strong>") +
                paragraph("We look forward to your presence at the event.") +
                signOff() +
                FOOTER + HTML_END;
    }

    public static String rejectedTemplate(String applicantName, String refNo, String reason,String deptId, String date, String time,String rejectedBy) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = LocalDate.now().format(formatter);
        return HTML_START + HEADER +
                emailbodyStart("APPOINTMENT REQUEST STATUS",null) +

                paragraph("Namaskar <strong>" + applicantName + "</strong>,") +
                paragraph("Thank you for your request for an appointment with the "+deptId+" at Lok Bhavan Odisha. Due to prior official engagements, we are unable to accommodate your appointment request for "+formattedDate+". We regret the inconvenience and appreciate your understanding. – Lok Bhavan Odisha") +

                signOff() +

                FOOTER + HTML_END;

    }


    /* =========================================================
       3. REMINDER TEMPLATE
       ========================================================= */
    public static String reminderTemplate(String applicantName, String date,
                                          String time, String refNo, String gate) {

        return HTML_START + HEADER +
                bodyStart("APPOINTMENT REMINDER", refNo) +

                paragraph("Namaskar <strong>" + applicantName + "</strong>,") +
                paragraph("This is a gentle reminder regarding your scheduled appointment with the Hon’ble Governor.") +

                detailsTable(applicantName, null, date, time, gate) +

                paragraph("Please ensure to arrive at the Lok Bhavan " + gate + " at least 15 minutes prior. Carry your valid photo identity proof.") +

                signOff() +

                FOOTER + HTML_END;
    }


    /* =========================================================
       4. RESCHEDULE TEMPLATE
       ========================================================= */
    public static String rescheduleTemplate(String applicantName, String refNo,  String purpose,
                                            String newDate, String newTime, String venue, String gate, String deptName) {

        return HTML_START + HEADER +
                bodyStart("APPOINTMENT RESCHEDULE NOTICE", refNo) +

                paragraph("Namaskar <strong>" + applicantName + "</strong>,") +
                paragraph("Your appointment with the Hon’ble Governor has been rescheduled as per the details below:") +

                detailsTable(applicantName, null, newDate, newTime, venue) +

                paragraph("Kindly take note of the above change and cooperate.") +

                signOff() +

                FOOTER + HTML_END;
    }



    /* =========================================================
       5. EXPIRATION TEMPLATE
       ========================================================= */
    public static String expirationTemplate(String applicantName, String refNo) {

        return HTML_START + HEADER +
                bodyStart("APPOINTMENT EXPIRATION NOTICE", refNo) +

                paragraph("Namaskar <strong>" + applicantName + "</strong>,") +
                paragraph("This is to inform you that your appointment request with the Hon’ble Governor has expired due to non-confirmation / non-attendance.") +
                paragraph("You may submit a fresh request if the appointment is still required.") +

                signOff() +

                FOOTER + HTML_END;
    }


    /* =========================================================
       6. AFTER VISIT TEMPLATE
       ========================================================= */
    public static String afterVisitTemplate(String applicantName, String refNo) {

        return HTML_START + HEADER +
                bodyStart("VISIT COMPLETION ACKNOWLEDGEMENT", refNo) +

                paragraph("Namaskar <strong>" + applicantName + "</strong>,") +
                paragraph("We acknowledge your visit to Lok Bhavan and thank you for meeting the Hon’ble Governor.") +
                paragraph("We appreciate your cooperation in adhering to the protocol and security procedures.") +

                signOff() +

                FOOTER + HTML_END;
    }


    /* =========================================================
       7. VISIT IMAGES UPLOADED TEMPLATE
       ========================================================= */
    public static String visitImagesUploadedTemplate(String applicantName, String refNo) {

        return HTML_START + HEADER +
                bodyStart("VISIT PHOTOGRAPHS UPLOADED", refNo) +

                paragraph("Namaskar <strong>" + applicantName + "</strong>,") +
                paragraph("This is to inform you that photographs related to your visit at Lok Bhavan have been successfully uploaded and archived in the official records.") +
                paragraph("Thank you for your cooperation.") +

                signOff() +

                FOOTER + HTML_END;
    }


    /* =========================================================
       8. EL SEND TEMPLATE
       ========================================================= */
    public static String programListTemplete(String applicantName, String elDate){

        return HTML_START + HEADER +

                emailbodyStart(
                        "HON'BLE GOVERNOR PROGRAM LIST AVAILABLE",
                        null
                ) +

                paragraph("Namaskar <strong>" + applicantName + "</strong>,") +

                paragraph(
                        "The Hon'ble Governer's program list dated on <strong>" + elDate + "</strong> "
                                + "are now available for viewing and downloading."
                ) +

                paragraph("You can access the program list pdf shared below:") +

                paragraph(
                        "Please access the pdf at the earliest."
                ) +

                signOff() +

                FOOTER + HTML_END;
    }

    /* =========================================================
    9. FORWARD APPOINTMENT TEMPLATE
    ========================================================= */
    public static String forwardingTemplate(String officerName, String purpose, String refNo, String date, String time, String applicantName, String actionLink) {

        return HTML_START + HEADER +
                bodyStartForForward("APPOINTMENT REQUEST STATUS – <span style='color:orange; font-weight:bold;'>UNDER REVIEW</span>") +

                paragraph("Namaskar <strong>" + officerName + "</strong>,") +

                paragraph("This is to inform you that appointment request with Hon’ble Governor has been forwarded to you by your office for further review.") +

                detailsTable(applicantName, purpose, date, null, null) +

                paragraph("Kindly review the details and take necessary action using the button below.") +

                paragraph("<a href='" + actionLink + "' style='color:#ffffff;background:#007bff;padding:10px 18px;text-decoration:none;border-radius:5px;'>Review Appointment</a>") +

//                paragraph("If the button does not work, copy and paste the link below into your browser:<br>"
//                        + "<a href='" + actionLink + "'>" + actionLink + "</a>") +

                signOff() +

                FOOTER + HTML_END;
    }

    /* =========================================================
    10. APPROVAL APPOINTMENT SENDING TEMPLATE
    ========================================================= */
    public static String approvalSendingTemplate(String officerName, String purpose, String refNo, String date, String time, String applicantName, String actionLink) {

        return HTML_START + HEADER +
                bodyStartForForward("APPOINTMENT REQUEST STATUS – <span style='color:orange; font-weight:bold;'>UNDER REVIEW</span>") +

                paragraph("Namaskar <strong>" + officerName + "</strong>,") +

                paragraph("This is to inform you that a new appointment request has been scheduled with you at Lok Bhavan, Odisha.") +

                detailsTable(applicantName, purpose, date, time, null) +

                paragraph("Please click the button below to review and take necessary action") +

                paragraph("<a href='" + actionLink + "' style='color:#ffffff;background:#007bff;padding:10px 18px;text-decoration:none;border-radius:5px;'>Review Appointment</a>") +

//                paragraph("If the button does not work, copy and paste the link below into your browser:<br>"
//                        + "<a href='" + actionLink + "'>" + actionLink + "</a>") +

                signOff() +

                FOOTER + HTML_END;
    }

    /* =========================================================
         IMMIDIATE PASS TEMPLATE
       ========================================================= */
    public static String immidiatePass(String visitorName, String officerName, String visitingDate, String arrivalTime) {

        return HTML_START + HEADER +
                bodyStartForForward("IMMIDIATE PASS – <span style='color:orange; font-weight:bold;'>Sheduled</span>") +

                paragraph("Namaskar <strong>" + visitorName + "</strong>,") +

                paragraph("This is to inform you that your immidiate pass is sheduled on " + visitingDate + " at " + arrivalTime + " with " + officerName +" in Lok Bhavan, Odisha.") +

                paragraph("Please download the immediate pass shared below.") +

//                paragraph("<a href='" + actionLink + "' style='color:#ffffff;background:#007bff;padding:10px 18px;text-decoration:none;border-radius:5px;'>Download Pass</a>") +

                signOff() +

                FOOTER + HTML_END;
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
                //"<strong>Secretary to the Hon’ble Governor</strong><br>" +
                "<strong> Lok Bhavan, Odisha</strong>" +
                // "Government of Odisha" +
                "</p></td></tr>";
    }

    private static String detailsTable(String applicantName, String purpose,
                                       String date, String time,  String gate) {

        StringBuilder sb = new StringBuilder();

        sb.append("<table width='100%' cellpadding='0' cellspacing='0' style='border-collapse:collapse; border:1px solid #dcdcdc; margin-bottom:20px;'>");

        if (applicantName != null)
            sb.append(row("Applicant Name", applicantName, true));

        if (purpose != null)
            sb.append(row("Purpose", purpose, false));

        if (date != null)
            sb.append(row("Date", date, true));

        if (time != null)
            sb.append(row("Time", time, false));

        if (gate != null)
            sb.append(row("Gate / Entry", gate, false));

        sb.append("</table>");

        return sb.toString();
    }

    private static String row(String label, String value, boolean shaded) {
        String bg = shaded ? " style='background-color:#f5f7fa;'" : "";
        return "<tr" + bg + ">" +
                "<td style='padding:8px 10px; border:1px solid #dcdcdc; width:35%;'><strong>" + label + "</strong></td>" +
                "<td style='padding:8px 10px; border:1px solid #dcdcdc;'>" + value + "</td>" +
                "</tr>";
    }

}
