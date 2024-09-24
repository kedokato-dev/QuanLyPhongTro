package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyphongtro.pojo.DetailRoomPOJO;
import com.example.quanlyphongtro.model.Room;
import com.example.quanlyphongtro.pojo.RoomWithTenantInfo;
import com.example.quanlyphongtro.pojo.RoomNumber_RoomTypeName;
import com.example.quanlyphongtro.pojo.UserDetailPOJO;

import java.util.List;

@Dao
public interface RoomDAO {
    @Insert
    void insertRoom(Room room);
    @Query("SELECT R.roomNumber, R.status, COUNT(RT.tenantId) AS userNumber From Room AS R LEFT JOIN ROOM_TENANT AS RT ON R.roomId = RT.roomId \n" +
            "GROUP BY R.roomNumber, R.status")
    List<RoomWithTenantInfo> getRoomWithTenantInfo();
    @Query("DELETE FROM ROOM")
    void deleteAllData();

    @Delete
    void deleteRoom(Room room);
    @Update
    void updateRoom(Room room);

    @Query("SELECT * FROM ROOM WHERE roomNumber = :roomNumber")
    Room getInfoRoomByRoomNumber(String roomNumber);

    @Query("SELECT ROOM.roomNumber, ROOM.status, ROOM.price, RoomType.typeName, RoomType.description\n" +
            "FROM Tenant \n" +
            "INNER JOIN Room_Tenant ON Tenant.tenantId = Room_Tenant.tenantId \n" +
            "INNER JOIN ROOM ON Room_Tenant.roomId = ROOM.roomId\n" +
            "INNER JOIN RoomType ON ROOM.roomTypeId = RoomType.roomTypeId\n" +
            "WHERE ROOM.roomNumber = :roomNumber \n" +
            "LIMIT 1;")
    List<DetailRoomPOJO> getDetailRoomPOJO(String roomNumber);

    @Query("SELECT * FROM ROOM")
    List<Room> getAllRoom();

    @Query("SELECT Tenant.fullName, Tenant.phone\n" +
            "FROM Tenant \n" +
            "INNER JOIN Room_Tenant ON Tenant.tenantId = Room_Tenant.tenantId \n" +
            "INNER JOIN ROOM ON Room_Tenant.roomId = ROOM.roomId\n" +
            "INNER JOIN RoomType ON ROOM.roomTypeId = RoomType.roomTypeId\n" +
            "WHERE ROOM.roomNumber = :roomNumber")
    List<UserDetailPOJO> getUserDetailRoom(String roomNumber);

    @Query("SELECT RoomType.typeName, ROOM.price \n" +
            "FROM ROOM\n" +
            "INNER JOIN RoomType ON ROOM.roomTypeId = RoomType.roomTypeId\n" +
            "WHERE ROOM.roomNumber = :roomNumber")
    List<RoomNumber_RoomTypeName> getRoomNumber_RoomTypeName(String roomNumber);
}
