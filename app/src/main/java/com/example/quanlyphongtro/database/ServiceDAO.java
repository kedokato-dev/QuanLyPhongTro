package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyphongtro.model.Service;
import com.example.quanlyphongtro.pojo.ServiceInBillPOJO;
import com.example.quanlyphongtro.pojo.ServicePOJO;

import java.util.List;

@Dao
public interface ServiceDAO {
    @Insert
    void insertService(Service service);
    @Delete
    void delete(Service service);

    @Query("DELETE FROM SERVICE")
    void deleteAllData();
    @Update
    void update(Service service);

    @Query("-- lấy ra thông tin về giá dịch vụ\n" +
            "SELECT Service.serviceName ,Service.pricePerUnit\n" +
            "FROM Service\n" +
            "WHERE Service.serviceName = :serviceName")
    List<ServicePOJO> getNameAndPriceService(String serviceName);

    @Query("SELECT * FROM service WHERE serviceName = :serviceName AND pricePerUnit = :servicePrice")
    Service getServiceByNameAndPrice(String serviceName, double servicePrice);
    @Query("SELECT serviceName, pricePerUnit FROM SERVICE")
    List<ServicePOJO> getListService();


    @Query("SELECT Service.serviceName, Service.pricePerUnit ,BillDetail.quantity ,\n" +
            "BillDetail.amount\n" +
            "FROM Room\n" +
            "INNER JOIN BILL ON ROOM.roomId = BILL.roomId\n" +
            "INNER JOIN BillDetail ON BILL.billId = BillDetail.billId\n" +
            "INNER JOIN Service ON BillDetail.serviceId = Service.serviceId\n" +
            "WHERE ROOM.roomNumber = :roomNumber")
    List<ServiceInBillPOJO> getListServiceInBill(String roomNumber);

    @Query("SELECT SUM(BillDetail.amount) as 'totalServiceAmount'\n" +
            "FROM Room\n" +
            "INNER JOIN BILL ON ROOM.roomId = BILL.roomId\n" +
            "INNER JOIN BillDetail ON BILL.billId = BillDetail.billId\n" +
            "INNER JOIN Service ON BillDetail.serviceId = Service.serviceId\n" +
            "WHERE ROOM.roomNumber = :roomNumber")
    double totalServiceAmount(String roomNumber);

    @Query("SELECT ROOM.roomNumber, \n" +
            "       SUM(BillDetail.amount) + ROOM.price AS totalAmount\n" +
            "FROM Room\n" +
            "INNER JOIN BILL ON ROOM.roomId = BILL.roomId\n" +
            "INNER JOIN BillDetail ON BILL.billId = BillDetail.billId\n" +
            "INNER JOIN Service ON BillDetail.serviceId = Service.serviceId\n" +
            "WHERE ROOM.roomNumber = :roomNumber \n" +
            "GROUP BY ROOM.roomNumber, ROOM.price")
    double totalAmount(String roomNumber);

}
