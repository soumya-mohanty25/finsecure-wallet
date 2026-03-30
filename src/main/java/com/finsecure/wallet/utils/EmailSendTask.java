package com.finsecure.wallet.utils;

import com.finsecure.wallet.model.MailQueued;
import com.finsecure.wallet.repository.MailQueuedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class EmailSendTask {

    @Autowired
    private MailQueuedRepository mailQueuedRepository;
    @Autowired
    @Qualifier("frameworkMailServiceUtil")
    private MailServiceUtil mailServiceUtil;

    @Scheduled(fixedRate = 60000)
    private void markForProcessiong() {
        changeMailStatus();
        sendEmail();
    }

    private void changeMailStatus() {
        try {
            List<MailQueued> queuedLst = mailQueuedRepository.findEmailList("QUEUED");
            if (queuedLst != null &&  queuedLst.size() > 0) {
                for (MailQueued item : queuedLst) {
                    item.setStatus("PROCESSING");
                    mailQueuedRepository.save(item);
                }
            }
        }catch(Exception ex){
            log.error(String.valueOf(ex));
        }
    }

    private void sendEmail() {
        try {
            List<MailQueued> processedLst = mailQueuedRepository.findEmailList("PROCESSING");
            if (processedLst.size() < 1) {
                return;
            }
            //List<MailRecipantDetails> mailccs = mailRecepantRepository.findAll().stream().filter(mail->mail.getType().equals("CC")).collect(Collectors.toList());
            for (MailQueued queued : processedLst) {
                try {
                    Boolean svc = mailServiceUtil.sendMail(queued.getBody(), queued.getSubject(), queued.getMailTo(),
                            queued.getMailFrom(), queued.getBodyType(),null,null);
                    if (svc) {
                        queued.setStatus("SENT");

                        queued.setLastUpdatedOn(new Date());
                        queued.setLastUpdatedBy(1L);
                        queued.setFailureReason("");

                        mailQueuedRepository.save(queued);
                        log.debug("Email Sent Succussfully");
                    } else {
                        queued.setStatus("FAILED");
                        queued.setFailureReason("Failed");

                        queued.setLastUpdatedOn(new Date());
                        queued.setLastUpdatedBy(1L);

                        mailQueuedRepository.save(queued);
                        log.debug("Email Sending Failed");
                    }
                } catch (Exception ex) {
                    queued.setStatus("FAILED");
                    queued.setFailureReason(ex.getMessage());
                    mailQueuedRepository.save(queued);
                    log.error("ERROR : EmailSendTask  ->  sendEmail -> mail sent fail", ex);
                }
            }
        } catch (Exception ex) {
            log.error("ERROR : EmailSendTask  ->  sendEmail ",ex);
        }
    }

}