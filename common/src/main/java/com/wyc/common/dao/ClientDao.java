package com.wyc.common.dao;

import com.wyc.common.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientDao extends JpaRepository<Client,String> {
}
