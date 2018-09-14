package com.mitac.rotate;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/*
 * @author
 */
public class SQLiteActivity extends Activity {
    /** Called when the activity is first created. */
    private Button createBtn;
    private Button insertBtn;
    private Button updateBtn;
    private Button queryBtn;
    private Button deleteBtn;
    private Button ModifyBtn;
    private static String DB_NAME = "stu.db";
    private static int num = 0;
    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_sql);

            creatView();
            setListener();
            num = 0;
        }

    private void creatView(){
        createBtn = (Button)findViewById(R.id.createDatabase);
        updateBtn = (Button)findViewById(R.id.updateDatabase);
        insertBtn = (Button)findViewById(R.id.insert);
        ModifyBtn = (Button)findViewById(R.id.update);
        queryBtn = (Button)findViewById(R.id.query);
        deleteBtn = (Button)findViewById(R.id.delete);
    }

    private void setListener(){
        createBtn.setOnClickListener(new CreateListener());
        updateBtn.setOnClickListener(new UpdateListener());
        insertBtn.setOnClickListener(new InsertListener());
        ModifyBtn.setOnClickListener(new ModifyListener());
        queryBtn.setOnClickListener(new QueryListener());
        deleteBtn.setOnClickListener(new DeleteListener());
    }

    class CreateListener implements OnClickListener{
        @Override
            public void onClick(View v) {
                StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this, DB_NAME, null, 1);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
            }
    }

    class UpdateListener implements OnClickListener{
        @Override
            public void onClick(View v) {
                //1=>2
                StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this, DB_NAME, null, 1);//2);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
            }
    }

    class InsertListener implements OnClickListener{
        @Override
            public void onClick(View v) {
                Toast.makeText(SQLiteActivity.this, "Insert....", Toast.LENGTH_SHORT).show();
                StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,DB_NAME,null,1);
                SQLiteDatabase db =dbHelper.getWritableDatabase();

                //key:first item, value:second item
                System.out.println("Starting to insert the records!!!");
                //db.beginTransaction();

                //db.execSQL("begin");
                for(num=1; num<10000; num++) {
                    ContentValues cv = new ContentValues();
                    cv.put("id", num);
                    cv.put("sname", "annie");
                    cv.put("sage", 10);
                    cv.put("ssex", "female");
                    db.insert("stu_table", null, cv);
                }
                //db.setTransactionSuccessful();
                //db.endTransaction();
                //db.execSQL("commit");

                db.close();
                System.out.println("End inserting the records!!!");
                Toast.makeText(SQLiteActivity.this, "End", Toast.LENGTH_SHORT).show();
            }
    }

    class QueryListener implements OnClickListener{
        @Override
            public void onClick(View v) {
                StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,DB_NAME,null,1);
                SQLiteDatabase db =dbHelper.getReadableDatabase();
                //parameter 1: table name
                //parameter 2: column
                //parameter 3: where subclause
                //parameter 4: where subcause related value
                //parameter 5: group mode
                //parameter 6: having condition
                //parameter 7: sorting method
                Cursor cursor = db.query("stu_table", new String[]{"id","sname","sage","ssex"}, "sname=?", new String[]{"annie"}, null, null, null);
                while(cursor.moveToNext()){
                    String id = cursor.getString(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("sname"));
                    String age = cursor.getString(cursor.getColumnIndex("sage"));
                    String sex = cursor.getString(cursor.getColumnIndex("ssex"));
                    System.out.println("query------->" + "id: "+id+" "+"Name: "+name+" "+"Age: "+age+" "+"Sex: "+sex);
                }
                db.close();
            }
    }

    class ModifyListener implements OnClickListener{
        @Override
            public void onClick(View v) {
                StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,DB_NAME,null,1);
                SQLiteDatabase db =dbHelper.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put("ssex", "male");
                String whereClause="id=?";
                String [] whereArgs = {String.valueOf(num)};
                db.update("stu_table", cv, whereClause, whereArgs);

                /*
                   StringBuffer sql = new StringBuffer();
                   sql.append("UPDATE m_free_assign");
                   sql.append(" SET free_sts = 1");
                   sql.append(" WHERE EXISTS(");
                   sql.append(" SELECT *");
                   sql.append(" FROM m_free_assign fa");
                   sql.append(" WHERE fa.course_no = m_free_assign.course_no");
                   sql.append(" AND fa.menu_code = m_free_assign.menu_code");
                   sql.append(" AND fa.free_sts = 1");
                   sql.append(" );");

                   boolean result = true;
                   try {
                   db.execSQL(sql.toString());
                   } catch ( SQLException e ) {
                   result = false;
                   }
                 */


                //                String sql_temp = "update stu_table set ssex='male' where id=5";
                //                db.execSQL(sql_temp);


            }
    }

    class DeleteListener implements OnClickListener{
        @Override
            public void onClick(View v) {
                StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,DB_NAME,null,1);
                SQLiteDatabase db =dbHelper.getReadableDatabase();
                String whereClauses = "id=?";
                String [] whereArgs = {String.valueOf(2)};
                db.delete("stu_table", whereClauses, whereArgs);
            }
    }

}


