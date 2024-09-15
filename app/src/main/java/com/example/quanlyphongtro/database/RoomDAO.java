package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.quanlyphongtro.Model.DetailRoomPOJO;
import com.example.quanlyphongtro.Model.Room;
import com.example.quanlyphongtro.Model.RoomWithTenantInfo;
import com.example.quanlyphongtro.Model.UserDetailPOJO;

import java.util.List;

@Dao
public interface RoomDAO {
    @Insert
    void insertRoom(Room room);
    @Query("SELECT R.roomNumber, R.status, COUNT(RT.tenantId) AS userNumber From Room AS R LEFT JOIN ROOM_TENANT AS RT ON R.roomId = RT.roomId \n" +
            "GROUP BY R.roomNumber, R.status")
    List<RoomWithTenantInfo> getRoomWithTenantInfo();

    @Query("SELECT ROOM.roomNumber, ROOM.status, ROOM.price, RoomType.typeName, RoomType.description\n" +
            "FROM Tenant \n" +
            "INNER JOIN Room_Tenant ON Tenant.tenantId = Room_Tenant.tenantId \n" +
            "INNER JOIN ROOM ON Room_Tenant.roomId = ROOM.roomId\n" +
            "INNER JOIN RoomType ON ROOM.roomTypeId = RoomType.roomTypeId\n" +
            "WHERE ROOM.roomNumber = :roomNumber \n" +
            "LIMIT 1;")
    List<DetailRoomPOJO> getDetailRoomPOJO(String roomNumber);

    @Query("SELECT Tenant.fullName, Tenant.phone\n" +
            "FROM Tenant \n" +
            "INNER JOIN Room_Tenant ON Tenant.tenantId = Room_Tenant.tenantId \n" +
            "INNER JOIN ROOM ON Room_Tenant.roomId = ROOM.roomId\n" +
            "INNER JOIN RoomType ON ROOM.roomTypeId = RoomType.roomTypeId\n" +
            "WHERE ROOM.roomNumber = :roomNumber")
    List<UserDetailPOJO> getUserDetailRoom(String roomNumber);
}
