package com.example.quanlyphongtro.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.quanlyphongtro.model.Bill;
import com.example.quanlyphongtro.model.BillDetail;
import com.example.quanlyphongtro.model.Room;
import com.example.quanlyphongtro.model.RoomType;
import com.example.quanlyphongtro.model.Room_Tenant;
import com.example.quanlyphongtro.model.Service;
import com.example.quanlyphongtro.model.Tenant;


@Database(entities = {Room.class, RoomType.class, Room_Tenant.class, Service.class, Bill.class, BillDetail.class, Tenant.class}, version = 1)
    public abstract class QuanLyPhongTroDB extends RoomDatabase {
        private static final String DATABASE_NAME = "QuanLyPhongTro.db";
        private static QuanLyPhongTroDB instance;

    public static synchronized QuanLyPhongTroDB getInstance(Context context){
        if(instance == null){
        instance = androidx.room.Room.databaseBuilder(context.getApplicationContext(), QuanLyPhongTroDB.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .addCallback(roomCallback)
                .build();
          }
        return instance;
        }

        public abstract RoomDAO roomDAO();
        public abstract ServiceDAO serviceDAO();

        public abstract UserDAO userDAO();
        public abstract BillDAO billDAO();

    // Callback để chạy SQL trigger khi database được tạo hoặc mở
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Thực thi các lệnh SQL để tạo trigger khi database được tạo
            db.execSQL("CREATE TRIGGER trg_delete_room_tenant AFTER DELETE ON Room_Tenant " +
                    "FOR EACH ROW BEGIN " +
                    "UPDATE Room SET Status = 'Còn trống' WHERE RoomID = OLD.RoomID " +
                    "AND NOT EXISTS (SELECT 1 FROM Room_Tenant WHERE RoomID = OLD.RoomID); " +
                    "END;");

            db.execSQL("CREATE TRIGGER trg_delete_room_tenant_status_update AFTER DELETE ON Room_Tenant " +
                    "FOR EACH ROW BEGIN " +
                    "UPDATE Room SET Status = (CASE " +
                    "WHEN (SELECT COUNT(*) FROM Room_Tenant WHERE RoomID = OLD.RoomID) = 0 THEN 'Còn trống' " +
                    "WHEN (SELECT COUNT(*) FROM Room_Tenant WHERE RoomID = OLD.RoomID) >= " +
                    "(SELECT MaxOccupants FROM RoomType " +
                    "INNER JOIN Room ON Room.RoomTypeID = RoomType.RoomTypeID " +
                    "WHERE Room.RoomID = OLD.RoomID) THEN 'Đã đủ người' " +
                    "ELSE 'Đã có người thuê' END) " +
                    "WHERE RoomID = OLD.RoomID; END;");

            db.execSQL("CREATE TRIGGER trg_insert_room_tenant BEFORE INSERT ON Room_Tenant " +
                    "FOR EACH ROW BEGIN " +
                    "SELECT CASE WHEN ((SELECT COUNT(*) FROM Room_Tenant WHERE RoomID = NEW.RoomID) >= " +
                    "(SELECT MaxOccupants FROM RoomType " +
                    "INNER JOIN Room ON Room.RoomTypeID = RoomType.RoomTypeID " +
                    "WHERE Room.RoomID = NEW.RoomID)) " +
                    "THEN RAISE(ABORT, 'Phòng đã đủ người, không thể thêm người.') END; END;");

            db.execSQL("CREATE TRIGGER trg_insert_room_tenant_status_update AFTER INSERT ON Room_Tenant " +
                    "FOR EACH ROW BEGIN " +
                    "UPDATE Room SET Status = (CASE " +
                    "WHEN (SELECT COUNT(*) FROM Room_Tenant WHERE RoomID = NEW.RoomID) >= " +
                    "(SELECT MaxOccupants FROM RoomType " +
                    "INNER JOIN Room ON Room.RoomTypeID = RoomType.RoomTypeID " +
                    "WHERE Room.RoomID = NEW.RoomID) THEN 'Đã đủ người' ELSE 'Đã có người thuê' END) " +
                    "WHERE RoomID = NEW.RoomID; END;");

            db.execSQL("CREATE TRIGGER trg_update_room_tenant BEFORE UPDATE ON Room_Tenant " +
                    "FOR EACH ROW BEGIN " +
                    "SELECT CASE WHEN ((SELECT COUNT(*) FROM Room_Tenant WHERE RoomID = NEW.RoomID AND TenantID != NEW.TenantID) >= " +
                    "(SELECT MaxOccupants FROM RoomType " +
                    "INNER JOIN Room ON Room.RoomTypeID = RoomType.RoomTypeID " +
                    "WHERE Room.RoomID = NEW.RoomID)) " +
                    "THEN RAISE(ABORT, 'Phòng đã đủ người, không thể thêm người.') END; END;");

            db.execSQL("CREATE TRIGGER trg_update_room_tenant_status_update AFTER UPDATE ON Room_Tenant " +
                    "FOR EACH ROW BEGIN " +
                    "UPDATE Room SET Status = (CASE " +
                    "WHEN (SELECT COUNT(*) FROM Room_Tenant WHERE RoomID = NEW.RoomID) >= " +
                    "(SELECT MaxOccupants FROM RoomType " +
                    "INNER JOIN Room ON Room.RoomTypeID = RoomType.RoomTypeID " +
                    "WHERE Room.RoomID = NEW.RoomID) THEN 'Đã đủ người' ELSE 'Đã có người thuê' END) " +
                    "WHERE RoomID = NEW.RoomID; END;");
        }
    };
}
