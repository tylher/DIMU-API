package com.dimu.dimuapi.repository;

import com.dimu.dimuapi.model.ConversationParticipants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationParticipantsRepository extends JpaRepository<ConversationParticipants,String> {

}
