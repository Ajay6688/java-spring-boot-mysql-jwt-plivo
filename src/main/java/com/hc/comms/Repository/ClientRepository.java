package com.hc.comms.Repository;

import com.hc.comms.Entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client , String> {

}
