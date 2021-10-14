package com.example.geocachingapp.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// https://developer.android.com/codelabs/android-room-with-a-view#7

@Database(
        version = 3,
        entities = {QRCode.class}
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract QRDao qrDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                QRDao dao = INSTANCE.qrDao();

//                QRCode qrCode = new QRCode("asoidhfgopweh459p2h34p5h2p3h45p");
//                dao.insert(qrCode);
//                Log.d("RoomDatabase", dao.loadAllQRCodes().toString());
//                qrCode = new QRCode("q3984y312481ph1o343");
//                dao.insert(qrCode);
//
//                Log.d("RoomDatabase", dao.loadAllQRCodes().toString());
            });
        }
    };

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigrationFrom(1)
                            .fallbackToDestructiveMigrationFrom(2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
