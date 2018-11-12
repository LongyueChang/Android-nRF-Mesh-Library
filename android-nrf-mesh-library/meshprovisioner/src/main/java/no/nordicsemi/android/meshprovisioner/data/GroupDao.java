package no.nordicsemi.android.meshprovisioner.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.RestrictTo;

import java.util.List;

import no.nordicsemi.android.meshprovisioner.Group;

@SuppressWarnings("unused")
@Dao
@RestrictTo(RestrictTo.Scope.LIBRARY)
public interface GroupDao {

    @Insert
    void insert(final Group group);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(final List<Group> groups);

    @Update
    void update(final Group group);

    @Delete
    void delete(final Group group);

    @Query("DELETE FROM groups")
    void deleteAll();

}