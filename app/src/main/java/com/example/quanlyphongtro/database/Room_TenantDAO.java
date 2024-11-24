package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.quanlyphongtro.model.Tenant;

import java.util.List;

@Dao
public interface Room_TenantDAO {

    @Query("DELETE FROM ROOM_TENANT")
    void deleteAllData();

    @Query("INSERT INTO Room_Tenant (roomId, tenantId, startDate, endDate) VALUES (:roomId, :tenantId, :startDate, :endDate)")
    void insertRoomTenant(int roomId, int tenantId, String startDate, String endDate);

    @Query("SELECT COUNT(*) FROM Room_Tenant WHERE roomId = :roomId AND tenantId = :tenantId")
    int checkMemberExistsInRoom(int roomId, int tenantId);

    @Query("SELECT COUNT(*) FROM Room_Tenant WHERE roomId = :roomId")
    int getCurrentTenantCount(int roomId);

    @Query("SELECT RoomType.maxOccupants FROM Room INNER JOIN RoomType ON Room.roomTypeId = RoomType.roomTypeId WHERE Room.roomId = :roomId")
    int getRoomMaxOccupants(int roomId);

    @Query("SELECT t.* \n" +
            "FROM Tenant t\n" +
            "INNER JOIN Room_Tenant rt ON t.tenantId  = rt.tenantId \n" +
            "WHERE rt.roomId =:roomID")
    List<Tenant> getTenantInRoom(int roomID);

    @Query("DELETE FROM Room_Tenant \n" +
            "WHERE roomID =:roomID and tenantId =:tenantId")
    void deleteTenantFromRoom(int roomID, int tenantId);

}
