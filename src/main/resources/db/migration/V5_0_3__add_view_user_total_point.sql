CREATE VIEW V_USER_TOTAL_POINT AS
SELECT USERS.USERNAME AS NAME,
       USERS.EMAIL AS EMAIL,
       USERS.AVATAR AS AVATAR,
       SUM(EVENTS.POINT) AS TOTAL_POINT,
       COUNT(EVENTS.ID) AS TOTALEVENTS
FROM EVENTS
         JOIN (SELECT SUBMISSIONS.EVENT_ID,
                      SUBMISSIONS.USER_ID
               FROM SUBMISSIONS
               WHERE SUBMISSIONS.IS_PARTICIPATED = TRUE
               GROUP BY SUBMISSIONS.EVENT_ID,
                        SUBMISSIONS.USER_ID
) SUBMISSIONS ON EVENTS.ID = SUBMISSIONS.EVENT_ID
         JOIN USERS ON SUBMISSIONS.USER_ID = USERS.EMAIL
GROUP BY USERS.EMAIL,
         USERS.USERNAME,
         USERS.AVATAR