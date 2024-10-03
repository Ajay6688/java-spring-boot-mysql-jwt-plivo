package com.hc.comms.Repository;

import com.hc.comms.Entity.Consent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hc.comms.Entity.WebhookPayloadDTO;

import java.util.List;


@Repository
public interface SmsWebhookRepository extends JpaRepository<WebhookPayloadDTO, String>{

    @Query("SELECT c FROM WebhookPayloadDTO c WHERE c.toNumber = :phoneNumber AND c.status = 'sent' AND c.clientPhoneNumber = :clientPhoneNumber")
    List<WebhookPayloadDTO> findMessagesByPhoneNumberAndSentAndClientPhoneNumber(@Param("phoneNumber") String phoneNumber , @Param("clientPhoneNumber") String clientPhoneNumber);
}
