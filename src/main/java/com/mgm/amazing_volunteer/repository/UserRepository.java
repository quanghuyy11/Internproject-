package com.mgm.amazing_volunteer.repository;

import com.mgm.amazing_volunteer.dto.user.LeaderboardUserDto;
import com.mgm.amazing_volunteer.dto.user.UserPointDto;
import com.mgm.amazing_volunteer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    String GET_CURRENT_POINT_OF_USER = "SELECT (totalPoint.TOTAL_POINT-COALESCE(requestedPoint.POINT,0)) AS currentPoint\n" +
            "FROM\n" +
            "V_USER_TOTAL_POINT AS totalPoint\n" +
            "LEFT JOIN\n" +
            "   (SELECT USERS.EMAIL AS EMAIL,\n" +
            "           (SUM(REQUESTS.QUANTITY * PRIZES.POINT_ARCHIVE)) AS POINT\n" +
            "       FROM REQUESTS\n" +
            "       JOIN USERS ON USERS.EMAIL = REQUESTS.USER_ID\n" +
            "       JOIN PRIZES ON PRIZES.ID = REQUESTS.PRIZE_ID\n" +
            "       WHERE REQUESTS.STATUS IN('ACCEPTED','PENDING')\n" +
            "       GROUP BY USERS.EMAIL) AS requestedPoint " +
            "ON totalPoint.EMAIL = requestedPoint.EMAIL\n" +
            "GROUP BY totalPoint.EMAIL, totalPoint.TOTAL_POINT, requestedPoint.POINT\n" +
            "HAVING totalPoint.EMAIL = :email";

    String getLeaderBoard = "SELECT leaderBoard.NAME AS name,\n" +
            "   leaderBoard.EMAIL AS email,\n" +
            "   leaderBoard.AVATAR AS avatar,\n" +
            "   (leaderBoard.TOTAL_POINT - COALESCE(requestedPoint.TOTALARCHIVEDPOINT, 0)) AS totalPoints,\n" +
            "   leaderBoard.TOTALEVENTS AS totalEvents\n" +
            "   FROM\n" +
            "      V_USER_TOTAL_POINT AS leaderBoard\n" +
            "   LEFT JOIN\n" +
            "       (SELECT USERS.EMAIL AS EMAIL,\n" +
            "               SUM(PRIZES.POINT_ARCHIVE * REQUESTS.QUANTITY) AS TOTALARCHIVEDPOINT\n" +
            "       FROM PRIZES\n" +
            "       JOIN REQUESTS ON PRIZES.ID = REQUESTS.PRIZE_ID\n" +
            "       JOIN USERS ON REQUESTS.USER_ID = USERS.EMAIL\n" +
            "       WHERE REQUESTS.STATUS = 'ACCEPTED'\n" +
            "       GROUP BY USERS.EMAIL) requestedPoint\n" +
            "   ON leaderBoard.EMAIL = requestedPoint.EMAIL";

    User getUserByEmail(String username);

    @Query(value = "SELECT password FROM users WHERE email=:email", nativeQuery = true)
    String getPasswordByEmail(String email);

    @Query(value = "SELECT avatar FROM users WHERE email=:email", nativeQuery = true)
    String getAvatarByEmail(String email);

    @Query(value = getLeaderBoard, nativeQuery = true)
    List<LeaderboardUserDto> getLeaderboardUsers();

    @Query(value = GET_CURRENT_POINT_OF_USER, nativeQuery = true)
    Optional<Integer> getCurrentPointByEmail(String email);
}
