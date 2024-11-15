package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.quanlyphongtro.model.Bill;
import com.example.quanlyphongtro.pojo.ItemBillPOJO;

import java.util.List;

@Dao
public interface BillDAO {
    @Query("DELETE FROM BILL")
    void deleteAllData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBill(Bill bill);  // Trả về ID của bill sau khi thêm


    @Query("SELECT ROOM.roomNumber, \n" +
            "       strftime('%d/%m/%Y', BILL.issueDate) AS issueDate, \n" +
            "       SUM(BillDetail.amount) + ROOM.price AS totalAmount,  \n" +
            "       BILL.status\n" +
            "FROM Room\n" +
            "INNER JOIN BILL ON ROOM.roomId = BILL.roomId\n" +
            "INNER JOIN BillDetail ON BILL.billId = BillDetail.billId\n" +
            "INNER JOIN Service ON BillDetail.serviceId = Service.serviceId\n" +
            "GROUP BY ROOM.roomNumber, ROOM.price, BILL.billId;")
    List<ItemBillPOJO> getListItemBill();

    @Query("SELECT ROOM.roomNumber, \n" +
            "       strftime('%d/%m/%Y', BILL.issueDate) AS issueDate, \n" +
            "       SUM(BillDetail.amount) + ROOM.price AS totalAmount,  \n" +
            "       BILL.status\n" +
            "FROM Room\n" +
            "INNER JOIN BILL ON ROOM.roomId = BILL.roomId\n" +
            "INNER JOIN BillDetail ON BILL.billId = BillDetail.billId\n" +
            "INNER JOIN Service ON BillDetail.serviceId = Service.serviceId\n" +
            "WHERE BILL.status = 'Chưa thanh toán'\n" +
            "GROUP BY ROOM.roomNumber, ROOM.price, BILL.billId;")
    List<ItemBillPOJO> getListItemBillUnpaid();

    @Query("SELECT ROOM.roomNumber, \n" +
            "       strftime('%d/%m/%Y', BILL.issueDate) AS issueDate, \n" +
            "       SUM(BillDetail.amount) + ROOM.price AS totalAmount,  \n" +
            "       BILL.status\n" +
            "FROM Room\n" +
            "INNER JOIN BILL ON ROOM.roomId = BILL.roomId\n" +
            "INNER JOIN BillDetail ON BILL.billId = BillDetail.billId\n" +
            "INNER JOIN Service ON BillDetail.serviceId = Service.serviceId\n" +
            "WHERE BILL.status = 'Đã thanh toán'\n" +
            "GROUP BY ROOM.roomNumber, ROOM.price, BILL.billId;")
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

    @Query("UPDATE Bill\n" +
            "SET totalAmount = (\n" +
            "    SELECT IFNULL(SUM(BD.amount), 0) + R.price\n" +
            "    FROM BillDetail BD\n" +
            "    INNER JOIN ROOM R ON Bill.roomId = R.roomId\n" +
            "    WHERE BD.billId = Bill.billId\n" +
            ");")
    void updateTotalAmount();

    @Query("SELECT TENANT.fullname\n" +
            "FROM BILL INNER JOIN TENANT\n" +
            "ON BILL.TENANTID = TENANT.TENANTID\n" +
            "WHERE billId =:id AND issueDate =:date;")
    String  getNamePaidInBillByIdAndIssueDate(int id, String date);

    @Query("SELECT billId from bill\n" +
            "where bill.issueDate =:date and bill.totalAmount =:totalAmount")
    int getBillByIssueDateAndTotal(String date, double totalAmount);

//    @Query("")
//    void deleteBillByRoomNumberAndIssueDate(String roomNumber, String date);
}
