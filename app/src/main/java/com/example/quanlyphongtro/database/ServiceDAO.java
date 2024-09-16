package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.quanlyphongtro.model.Service;
import com.example.quanlyphongtro.model.ServiceInBillPOJO;
import com.example.quanlyphongtro.model.ServicePOJO;

import java.util.List;

@Dao
public interface ServiceDAO {
    @Insert
    void insertService(Service service);
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
