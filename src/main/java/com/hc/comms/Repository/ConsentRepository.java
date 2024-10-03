package com.hc.comms.Repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hc.comms.Entity.Consent;
import org.springframework.data.repository.query.Param;

public interface ConsentRepository extends JpaRepository<Consent, Long> {

     @Query("SELECT c FROM Consent c WHERE c.fromPhoneNumber = :fromPhoneNumber AND c.clientPhoneNumber = :clientPhoneNumber")
     Consent findByFromPhoneNumberAndClientPhoneNumber(@Param("fromPhoneNumber") String fromPhoneNumber , @Param("clientPhoneNumber") String clientPhoneNumber);
}



