package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.quanlyphongtro.pojo.ItemBillPOJO;

import java.util.List;

@Dao
public interface BillDAO
{
    @Query("DELETE FROM BILL")
    void deleteAllData();

    @Query("SELECT ROOM.roomNumber,strftime('%d/%m/%Y', BILL.issueDate) AS issueDate,\n" +
            "SUM(BillDetail.amount) + ROOM.price AS totalAmount,  Bill.status\n" +
            "FROM Room\n" +
            "INNER JOIN BILL ON ROOM.roomId = BILL.roomId\n" +
            "INNER JOIN BillDetail ON BILL.billId = BillDetail.billId\n" +
            "INNER JOIN Service ON BillDetail.serviceId = Service.serviceId\n" +
            "GROUP BY ROOM.roomNumber, ROOM.price")
    List<ItemBillPOJO> getListItemBill();

    @Query("SELECT ROOM.roomNumber,strftime('%d/%m/%Y', BILL.issueDate) AS issueDate,\n" +
            "SUM(BillDetail.amount) + ROOM.price AS totalAmount,  Bill.status\n" +
            "FROM Room\n" +
            "INNER JOIN BILL ON ROOM.roomId = BILL.roomId\n" +
            "INNER JOIN BillDetail ON BILL.billId = BillDetail.billId\n" +
            "INNER JOIN Service ON BillDetail.serviceId = Service.serviceId\n" +
            "WHERE BILL.status = 'Chưa thanh toán' "+
            "GROUP BY ROOM.roomNumber, ROOM.price")
    List<ItemBillPOJO> getListItemBillUnpaid();

    @Query("SELECT ROOM.roomNumber,strftime('%d/%m/%Y', BILL.issueDate) AS issueDate,\n" +
            "SUM(BillDetail.amount) + ROOM.price AS totalAmount,  Bill.status\n" +
            "FROM Room\n" +
            "INNER JOIN BILL ON ROOM.roomId = BILL.roomId\n" +
            "INNER JOIN BillDetail ON BILL.billId = BillDetail.billId\n" +
            "INNER JOIN Service ON BillDetail.serviceId = Service.serviceId\n" +
            "WHERE BILL.status = 'Đã thanh toán' "+
            "GROUP BY ROOM.roomNumber, ROOM.price")
    List<ItemBillPOJO> getListItemBillPaid();

    // lấy ra tổng số tiền dịch vụ của 1 phòng
    @Query("SELECT SUM(BillDetail.amount) as 'totalServiceAmount'\n" +
            "FROM Room\n" +
            "INNER JOIN BILL ON ROOM.roomId = BILL.roomId\n" +
            "INNER JOIN BillDetail ON BILL.billId = BillDetail.billId\n" +
            "INNER JOIN Service ON BillDetail.serviceId = Service.serviceId\n" +
            "WHERE ROOM.roomNumber = :roomNumber")
    Double totalServiceAmount(String roomNumber);

    // lấy ra loại phòng
    @Query("SELECT RoomType.typeName\n" +
            "FROM Room\n" +
            "INNER JOIN RoomType ON ROOM.roomTypeId = RoomType.roomTypeId\n" +
            "WHERE ROOM.roomNumber = :roomNumber")
    String roomType(String roomNumber);

    @Query("SELECT ROOM.roomNumber,strftime('%d/%m/%Y', BILL.issueDate) AS issueDate,\n" +
            "SUM(BillDetail.amount) + ROOM.price AS totalAmount,  Bill.status\n" +
            "FROM Room\n" +
            "INNER JOIN BILL ON ROOM.roomId = BILL.roomId\n" +
            "INNER JOIN BillDetail ON BILL.billId = BillDetail.billId\n" +
            "INNER JOIN Service ON BillDetail.serviceId = Service.serviceId\n" +
            "WHERE ROOM.roomNumber = :roomNumber\n" +
            "GROUP BY ROOM.roomNumber, ROOM.price")
    List<ItemBillPOJO> inforRoomBill(String roomNumber);
}
