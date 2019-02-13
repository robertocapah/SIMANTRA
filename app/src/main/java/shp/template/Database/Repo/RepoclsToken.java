package shp.template.Database.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import shp.template.Database.Common.ClsToken;
import shp.template.Database.DatabaseHelper;
import shp.template.Database.DatabaseManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by User on 1/29/2018.
 */

public class RepoclsToken implements CRUD {
    private DatabaseHelper helper;
    public RepoclsToken(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        ClsToken object = (ClsToken) item;
        try {
            index = helper.getTokenDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) {
        int index = -1;
        ClsToken object = (ClsToken) item;
        try {
            Dao.CreateOrUpdateStatus status = helper.getTokenDao().createOrUpdate(object);
            index = status.getNumLinesChanged();
//            index = 1;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int update(Object item) {
        return 0;
    }

    @Override
    public int delete(Object item) {
        return 0;
    }

    @Override
    public Object findById(int id) throws SQLException {
        ClsToken item = null;
        try{
            item = helper.getTokenDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<ClsToken> items = null;
        try{
            items = helper.getTokenDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }
}