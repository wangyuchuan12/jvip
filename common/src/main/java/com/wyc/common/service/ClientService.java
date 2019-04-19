package com.wyc.common.service;
import com.wyc.common.dao.ClientDao;
import com.wyc.common.domain.Client;
import org.springframework.stereotype.Service;

@Service
public class ClientService extends BaseAbstractService<Client> {
    public ClientService(ClientDao clientDao) {
        super(clientDao);
    }
}
