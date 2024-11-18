package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyphongtro.model.Bill;
import com.example.quanlyphongtro.pojo.ItemBillPOJO;
import com.example.quanlyphongtro.pojo.ServiceInBillPOJO;
import com.example.quanlyphongtro.pojo.ServicePOJO;

import java.util.ArrayList;
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
            "INNER JOIN Room ON Bill.roomId = Room.roomId\n" +
            "where bill.issueDate =:date and Room.roomNumber =:roomNumber")
    int getBillByIssueDateAndTotal(String date, String roomNumber);

//     hai hàm sql bên dưới để xóa hóa đơn theo [ngày lập hóa đơn] và [mã hóa đơn]

    @Query("DELETE FROM BillDetail \n" +
            "WHERE billId = (SELECT billId FROM Bill WHERE billId =:billID AND issueDate =:date);")
    void deleteBillDetailByBillIdAndIssueDate(int billID, String date);

    @Query("DELETE FROM Bill \n" +
            "WHERE billId =:billID AND issueDate =:date;")
    void deleteBillByByBillIdAndIssueDate(int billID, String date);

    // VIẾT HÀM LẤY RA ID CỦA HÓA ĐƠN THEO NGÀY LẬP HÓA ĐƠN VÀ SỐ PHÒNG
    @Query("SELECT Bill.billId FROM Bill\n" +
            "INNER JOIN Room ON Bill.roomId = Room.roomId\n" +
            "WHERE Bill.issueDate =:date AND Room.roomNumber =:roomNumber")
    int getBillIdByRoomNumberAndIssueDate(String date, String roomNumber);


    // VIẾT HÀM LẤY RA DANH SÁCH DỊCH VỤ MÀ PHÒNG ĐÓ SỬ DỤNG
    @Query("SELECT Service.serviceName, Service.pricePerUnit ,BillDetail.quantity, BillDetail.amount\n" +
            "FROM BillDetail\n" +
            "INNER JOIN Bill ON Bill.billId = BillDetail.billId\n" +
            "INNER JOIN Service ON BillDetail.serviceId = Service.serviceId\n" +
            "INNER JOIN Room ON Bill.roomId = Room.roomId\n" +
            "WHERE Bill.issueDate =:date AND Room.roomNumber =:roomNumber")
    List<ServicePOJO> getListServiceByIssueDateAndRoomNumber(String date, String roomNumber);

    // VIẾT HÀM LẤY RA THÔNG TIN HÓA ĐƠN CỦA PHÒNG ĐÓ
    @Query("SELECT Bill.billId, Bill.roomId, Bill.tenantId, Bill.issueDate, Bill.totalAmount, Bill.status\n" +
            "FROM Bill\n" +
            "INNER JOIN Room ON Bill.roomId = Room.roomId\n" +
            "INNER JOIN RoomType ON Room.roomTypeId = RoomType.roomTypeId\n" +
            "INNER JOIN Tenant ON Bill.tenantId = Tenant.tenantId\n" +
            "WHERE Bill.issueDate =:date AND Room.roomNumber =:roomNumber ")
    Bill getBillByIssueDateAndRoomNumber(String date, String roomNumber);

    // VIẾT HÀM LẤY RA THÔNG TIN DỊCH VỤ CỦA PHÒNG ĐÓ
    @Query("SELECT Service.serviceName, Service.pricePerUnit, BillDetail.quantity, BillDetail.amount\n" +
            "FROM BillDetail\n" +
            "INNER JOIN Bill ON BillDetail.billId = Bill.billId\n" +
            "INNER JOIN Room ON Bill.roomId = Room.roomId\n" +
            "INNER JOIN Service ON BillDetail.serviceId = Service.serviceId\n" +
            "WHERE Bill.issueDate =:date AND Room.roomNumber =:roomNumber")
    List<ServiceInBillPOJO> getServiceInBillByIssueDateAndRoomNumber(String date, String roomNumber);

    // VIẾT HÀM LẤY RA DANH SÁCH MÃ DỊCH VỤ CỦA HÓA ĐƠN ĐÓ RỒI CẬP NHẬT
    @Query("SELECT BillDetail.billDetailId " +
            "FROM Bill " +
            "INNER JOIN Room ON Bill.roomId = Room.roomId " +
            "INNER JOIN BillDetail ON Bill.billId = BillDetail.billId " +
            "WHERE Bill.issueDate = :date AND Room.roomNumber = :roomNumber")
    List<Integer> listIDBillDetail(String date, String roomNumber);




    // VIẾT HÀM CẬP NHẬT THÔNG TIN CỦA HÓA ĐƠN
    @Update
    void updateBill(Bill bill);


}
