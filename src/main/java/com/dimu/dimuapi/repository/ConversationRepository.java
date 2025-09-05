package com.dimu.dimuapi.repository;

import com.dimu.dimuapi.model.Conversation;
import com.dimu.dimuapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation,String> {
    @Query("""
    SELECT c 
    FROM Conversation c 
    JOIN c.participants p 
    WHERE p.user.userId IN (:userAId, :userBId)
    GROUP BY c
    HAVING COUNT(DISTINCT p.user.userId) = 2
""")
    Optional<Conversation> getConversationByParticipants(@Param("userAId") String userAId,
                                                         @Param("userBId") String userBId);


    @Query("SELECT DISTINCT u FROM Conversation c JOIN c.participants p JOIN c.participants u " +
            "WHERE p.id = :userId AND u.id <> :userId")
    List<User> findConversationPartners(@Param("userId") String userId);

    @Query("""
   SELECT DISTINCT c 
   FROM Conversation c
   JOIN c.participants p
   WHERE p.user.userId = :userId
   ORDER BY c.updatedAt DESC
""")
    List<Conversation> findDistinctByUserIdOrderByUpdatedAtDesc(@Param("userId") String userId);


}
