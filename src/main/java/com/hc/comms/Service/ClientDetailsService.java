package com.hc.comms.Service;

import com.hc.comms.Entity.Client;
import com.hc.comms.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ClientDetailsService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String clientId) throws UsernameNotFoundException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NullPointerException("Client not found with clientId: " + clientId));

        return new org.springframework.security.core.userdetails.User(
                client.getClientId(),
                client.getClientSecret(),
                new ArrayList<>()  // You can add authorities if needed
        );
    }

    public Client getClientById(String clientId){
        return clientRepository.findById(clientId).orElseThrow(()-> new UsernameNotFoundException("Client not found with id : "+clientId));
    }
}