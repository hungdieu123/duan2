package com.example.gamudaland.Activity.Lo_Dat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.gamudaland.Adapter.LoDat.ChothueLoDat_Adapter;
import com.example.gamudaland.Model.Chothuelodat;
import com.example.gamudaland.Model.Muabanlodat;
import com.example.gamudaland.R;
import com.example.gamudaland.SQLDAO.ChothuelodatDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class ChothueLodat_Fragment extends Fragment {
   private ChothuelodatDAO chothuelodatDAO;
   private RecyclerView recyclerView;
   AlertDialog alertDialog;
    private ChothueLoDat_Adapter chothueLoDat_adapter;
    private List<Chothuelodat> chothuelodats;
    FloatingActionButton floatingActionButton;
    private Chothuelodat chothuelodat;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chothuelodat, container, false);

        recyclerView=view.findViewById(R.id.chothuelodat_rcview);
        floatingActionButton=view.findViewById(R.id.floatingActionButton);

        chothuelodat=new Chothuelodat();
        chothuelodatDAO=new ChothuelodatDAO(getActivity());
        chothuelodats=chothuelodatDAO.getAll();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                View dialog1  = LayoutInflater.from(getActivity()).inflate(R.layout.update_chothuelodat,null);
                builder1.setView(dialog1);

                final TextInputEditText edttiitle,edtdate,edtgia,edtdientich,edtlink,edtma;
                final Button ok1,cancel1,btnSET;

                ok1=dialog1.findViewById(R.id.btnok);
                btnSET=dialog1.findViewById(R.id.btnSet);

                edttiitle=dialog1.findViewById(R.id.edttittle);
                edtdate=dialog1.findViewById(R.id.edtdate);
                edtgia=dialog1.findViewById(R.id.edtgia);
                edtdientich=dialog1.findViewById(R.id.edtdientich);

                edtlink=dialog1.findViewById(R.id.edtlink);
                btnSET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar=Calendar.getInstance(); //khoi tao
                        int nam=calendar.get(Calendar.YEAR);  //thiet lap ngay thang nam
                        int thang=calendar.get(Calendar.MONTH);  //thiet lap ngay thang nam
                        int ngay=calendar.get(Calendar.DAY_OF_MONTH);  //thiet lap ngay thang nam
                        DatePickerDialog dialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                edtdate.setText(view.getDayOfMonth()+"/"+(view.getMonth()+1)+"/"+view.getYear());

                            }
                        },nam,thang,ngay);
                        dialog.show();
                    }
                });

                ok1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        String tittle = edttiitle.getText().toString().trim();
                        String date  = edtdate.getText().toString().trim();
                        String gia = edtgia.getText().toString().trim();
                        String dientich = edtdientich.getText().toString().trim();
                        String link = edtlink.getText().toString().trim();

                        if (tittle.equals("")){
                            Toast.makeText(getActivity(),"Vui Lòng Không Để Trống Tiêu Đề!",Toast.LENGTH_SHORT).show();
                        }else if (date.equals("")){
                            Toast.makeText(getActivity(),"Vui Lòng Không Để Trống Ngày!",Toast.LENGTH_SHORT).show();
                        }else if (gia.equals("")){
                            Toast.makeText(getActivity(),"Vui Lòng Không Để Trống Giá!",Toast.LENGTH_SHORT).show();
                        }else if (dientich.equals("")){
                            Toast.makeText(getActivity(),"Vui Lòng Không Để Trống Diện TÍch!",Toast.LENGTH_SHORT).show();
                        }else if (link.equals("")){
                            Toast.makeText(getActivity(),"Vui Lòng Không Để Trống Link!",Toast.LENGTH_SHORT).show();
                        }else {
                            Random r = new Random();
                            int i1 = (r.nextInt(8000) + 65);

                            chothuelodat =new Chothuelodat();

                            chothuelodat.setTittle(edttiitle.getText().toString().trim());
                            chothuelodat.setDate(edtdate.getText().toString().trim());
                            chothuelodat.setGia(edtgia.getText().toString().trim());
                            chothuelodat.setDientich(edtdientich.getText().toString().trim());
                            chothuelodat.setLink(edtlink.getText().toString().trim());
                            chothuelodat.setMachothuelodat(String.valueOf(i1));



                            chothuelodatDAO = new ChothuelodatDAO(getActivity());

                            long resurt = chothuelodatDAO.insert(chothuelodat);
                            if(resurt>0){
                                Toast.makeText(getActivity(),"Thêm Thành Công!",Toast.LENGTH_SHORT).show();

                                chothuelodats=chothuelodatDAO.getAll();
                                chothueLoDat_adapter=new ChothueLoDat_Adapter(chothuelodats,getActivity());
                                StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(gridLayoutManager);
                                recyclerView.setAdapter(chothueLoDat_adapter);

                                alertDialog.dismiss();


                            }else {
                                Toast.makeText(getActivity(),"Xóa Thất Bại!",Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();


                            }

                        }
                    }
                });



                builder1.create();
                alertDialog=builder1.show();
            }
        });

        chothueLoDat_adapter=new ChothueLoDat_Adapter(chothuelodats,getActivity());
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(chothueLoDat_adapter);

        setHasOptionsMenu(true);

        return view;
    }
    public void chothuelodat(){
        String url = "https://batdongsan.com.vn/Modules/RSS/RssDetail.aspx?catid=326&typeid=1";
        Data1 data = new Data1();
        data.execute(url);
    }







    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        menu.clear();

        inflater.inflate(R.menu.home, menu);

        MenuItem searchItem=menu.findItem(R.id.app_bar_search);
        SearchView searchView=(SearchView) searchItem.getActionView();



        searchView.setQueryHint("Search....");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                chothueLoDat_adapter.getFilter().filter(s);
                return false;
            }
        });
    }
     class Data1 extends AsyncTask<String, Long, Chothuelodat> {


        @Override
        protected Chothuelodat doInBackground(String... strings) {
            String link = strings[0];
            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();
                //Khởi tạo đối tượng bằng XML
                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();

                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();

                xmlPullParser.setInput(inputStream, "utf-8");


                int eventType = xmlPullParser.getEventType();

                String text = "";
                while (eventType != xmlPullParser.END_DOCUMENT) {
                    eventType = xmlPullParser.getEventType();
                    String tag = xmlPullParser.getName();
                    switch (eventType) {
                        //Bắt đầu thẻ
                        case XmlPullParser.START_TAG:
                            if (tag.equalsIgnoreCase("item")) {

                            }
                            break;
                        case XmlPullParser.TEXT:
                            text = xmlPullParser.getText();
                            break;
                        case XmlPullParser.END_TAG:
                            if (true) {


                                if (tag.equalsIgnoreCase("title")) {
                                    chothuelodat.setTittle(text);
                                }else if (tag.equalsIgnoreCase("pubDate")) {
                                    chothuelodat.setDate(text);
                                } else if (tag.equalsIgnoreCase("url")) {
//                                    tinTuc.image = text;
                                } else if (tag.equalsIgnoreCase("link")) {
                                    chothuelodat.setLink(text);
                                    Random r = new Random();
                                    int i1 = (r.nextInt(8000) + 65);
                                    chothuelodat.setMachothuelodat(String.valueOf(i1));
                                    chothuelodat.setDientich(String.valueOf(i1));

                                } else if (tag.equalsIgnoreCase("item")) {
                                    chothuelodatDAO.insert(chothuelodat);
                                }
                            }
                            break;

                    }
                    xmlPullParser.next();
                }


            } catch (MalformedURLException e) {
                //url bị sai : url
                e.printStackTrace();
            } catch (IOException e) {
                //Không kết nối đc : openConnection

                e.printStackTrace();
            } catch (XmlPullParserException e) {
                //Sai định dạng : newInstance
                e.printStackTrace();
            }

            return chothuelodat;
        }


        @Override
        protected void onPreExecute() {
            super.onPostExecute(chothuelodat);
            chothueLoDat_adapter.notifyDataSetChanged();
        }
    }

}
