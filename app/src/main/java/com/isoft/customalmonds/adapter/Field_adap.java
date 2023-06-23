package com.isoft.customalmonds.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.isoft.customalmonds.PDFDocumentAdapter;
import com.isoft.customalmonds.R;
import com.isoft.customalmonds.modelclass.field_response;
import com.isoft.customalmonds.modelclass.grs_bin_tag;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Field_adap extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int TYPE_MOVIE = 0;
    static Context context;
    static int ncount=0;
    List<field_response> movies=new ArrayList<>();
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    static List<grs_bin_tag> listtag=new ArrayList<>();
    private Bitmap btMap = null;
    public Field_adap(Context context, List<field_response> moviesz) {

        this.context = context;
        this.movies=new ArrayList<>();
        this.movies = moviesz;
        listtag=new ArrayList<>();

        Log.e("movies","#"+movies.size());
        // Log.e("calling","adapter");
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new MovieHolder(inflater.inflate(R.layout.field_home_list,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Log.e("sizeeeeee",""+getItemCount());
        if(getItemCount()<100)
        {

        }else if(getItemCount()==ncount) {
        }
        else {
            if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
                isLoading = true;
                loadMoreListener.onLoadMore();
            }
        }

        if(getItemViewType(position)==TYPE_MOVIE){
            //Log.e("stat","success");
            ((MovieHolder)holder).bindData(movies.get(position),position);
        }
    }
    @Override
    public int getItemViewType(int position) {
        //Log.e("movlist","q"+movies.toString());
        //Log.e("movlissdt","q"+movies.get(position).type);
//        if(movies.get(position).type.equals("checklist")){
            return TYPE_MOVIE;
//        }else{
//            return TYPE_LOAD;
//        }
    }
    @Override
    public int getItemCount() {

        return movies.size();
    }
    /* VIEW HOLDERS */

    class MovieHolder extends RecyclerView.ViewHolder{

        TextView txtrunid,txtid,txtrepname;
        TextView txtgrower;
        TextView txttype,txtrname;
        TextView txtvariety,txtfticket;
        TextView txttotalwgt,txtdate,txtgrosswgt,txtview;
        ImageView imgprintz;

        public MovieHolder(View itemView) {
            super(itemView);


            txtrunid=  itemView.findViewById(R.id.txt_runid);
            txtid=  itemView.findViewById(R.id.txt_id);
            txtgrower=  itemView.findViewById(R.id.txt_grower);
            txttype=  itemView.findViewById(R.id.txt_type);
            txtrname=  itemView.findViewById(R.id.txt_rname);
            txtvariety=  itemView.findViewById(R.id.txt_variety);
            txtfticket=  itemView.findViewById(R.id.txt_fticket);
            txttotalwgt=  itemView.findViewById(R.id.txt_totalwgt);
            txtdate=  itemView.findViewById(R.id.txt_date);
            txtgrosswgt=itemView.findViewById(R.id.txt_grosswgt);
            txtview=itemView.findViewById(R.id.txt_view);
            txtrepname=itemView.findViewById(R.id.txt_repname);



            imgprintz=  itemView.findViewById(R.id.img_printz);
        }

        void bindData(final field_response movieModel, int position){

int val=position;
val++;
            txtid.setText(""+movieModel.getId());
            txtrunid.setText(""+val);
          //  txtview.setText(""+movieModel.getId());
            txtview.setText(""+movieModel.getField_ticket());
            txtgrower.setText(""+movieModel.getGrower());
            txttype.setText(""+movieModel.getType());
            txtrname.setText(""+movieModel.getRanch_name());
            txtvariety.setText(""+movieModel.getVariety());
            txtfticket.setText(""+movieModel.getField_ticket());
           txttotalwgt.setText(""+movieModel.getNet_wt()+" Lb");
            txtgrosswgt.setText(""+movieModel.getGross_wt()+" Lb");
            txtdate.setText(""+movieModel.getCreated_date());
            txtrepname.setText(""+movieModel.getRep_name());


            imgprintz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        createPDF(movieModel);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            });


        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }


    interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void createPDF(field_response movieModel) throws IOException, DocumentException {

      //  Document doc = new Document(PageSize.B7);
        Document doc = new Document(PageSize.A6);
        try {
            try {
                File dir = context.getFilesDir();
                File filez = new File(dir, "fieldticket.pdf");
                boolean deleted = filez.delete();
                if(deleted)
                {

                }
            }catch (Exception e)
            {

            }
            Log.e("PDFCreator", "PDF Path: " + context.getFilesDir());
            File file = new File(context.getFilesDir(),  "fieldticket.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter.getInstance(doc, fOut);
            doc.open();
            PdfPTable lhtab1z = new PdfPTable(6);
            lhtab1z.setWidthPercentage(100);
            lhtab1z.setHorizontalAlignment(0);
            Font yourCustomFonthead = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
            Font yourCustomFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            PdfPCell lhcell1;
            lhcell1 = new PdfPCell(new Paragraph("ID# "+movieModel.getId(),yourCustomFonthead));
            lhcell1.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell1.setColspan(2);
            lhcell1.setBorder(0);
            lhcell1.setPadding(3);
            lhtab1z.addCell(lhcell1);

            PdfPCell lhcell1z;
            lhcell1z = new PdfPCell(new Paragraph("FIELD TICKET#: "+movieModel.getField_ticket(),yourCustomFonthead));
            lhcell1z.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
            lhcell1z.setColspan(4);
            lhcell1z.setBorder(0);
            lhcell1z.setPadding(3);
            lhtab1z.addCell(lhcell1z);
            doc.add(lhtab1z);
            float fntSize, lineSpacing;
            fntSize = 6.7f;
            lineSpacing = 7f;
            Paragraph p0 = new Paragraph(new Phrase(lineSpacing,"              ",
                    FontFactory.getFont(FontFactory.COURIER, fntSize)));


            doc.add(p0);

            try {
                // get input stream
                Drawable d = context.getResources().getDrawable(R.drawable.logoprintnew);
                BitmapDrawable bitDw = ((BitmapDrawable) d);
                Bitmap bmp = bitDw.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                image.setAlignment(Element.ALIGN_CENTER);
              //  image.scaleToFit(190F, 50F);
                image.scaleToFit(280f, 65f);
                doc.add(image);
            }
            catch(IOException ex)
            {
                return;
            }

            Paragraph pz = new Paragraph(new Phrase(lineSpacing,"              ",
                    FontFactory.getFont(FontFactory.COURIER, fntSize)));


            doc.add(pz);

            PdfPTable lhtab1 = new PdfPTable(10);
            lhtab1.setWidthPercentage(100);
            lhtab1.setHorizontalAlignment(0);




            PdfPCell lhcell2;
            lhcell2 = new PdfPCell(new Paragraph("DATE",yourCustomFonthead));
            lhcell2.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell2.setColspan(3);
            lhcell2.setBorder(0);
            lhcell2.setPadding(2);
            lhcell2.setPaddingBottom(4);
            lhtab1.addCell(lhcell2);

            PdfPCell lhcell2z;
            lhcell2z = new PdfPCell(new Paragraph(": "+movieModel.getCreated_date(),yourCustomFont));
            lhcell2z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell2z.setColspan(7);
            lhcell2z.setBorder(0);
            lhcell2z.setPadding(2);
            lhcell2z.setPaddingBottom(4);
            lhtab1.addCell(lhcell2z);








            PdfPCell lhcell3;
            lhcell3 = new PdfPCell(new Paragraph("GROWER",yourCustomFonthead));
            lhcell3.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell3.setColspan(3);
            lhcell3.setBorder(0);
            lhcell3.setPadding(2);
            lhcell3.setPaddingBottom(4);
            lhtab1.addCell(lhcell3);

            PdfPCell lhcell3z;
            lhcell3z = new PdfPCell(new Paragraph(": "+movieModel.getGrower(),yourCustomFont));
            lhcell3z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell3z.setColspan(7);
            lhcell3z.setBorder(0);
            lhcell3z.setPadding(2);
            lhcell3z.setPaddingBottom(4);
            lhtab1.addCell(lhcell3z);



            PdfPCell lhcell4;
            lhcell4 = new PdfPCell(new Paragraph("RANCH NAME ",yourCustomFonthead));
            lhcell4.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell4.setColspan(3);
            lhcell4.setBorder(0);
            lhcell4.setPadding(2);
            lhcell4.setPaddingBottom(4);
            lhtab1.addCell(lhcell4);
            if(movieModel.getRanch_name() !=null && !movieModel.getRanch_name().contentEquals("null")) {
                PdfPCell lhcell4z;
                lhcell4z = new PdfPCell(new Paragraph(": " + movieModel.getRanch_name(),yourCustomFont));
                lhcell4z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lhcell4z.setColspan(7);
                lhcell4z.setBorder(0);
                lhcell4z.setPadding(2);
                lhcell4z.setPaddingBottom(4);
                lhtab1.addCell(lhcell4z);
            }else{
                PdfPCell lhcell4z;
                lhcell4z = new PdfPCell(new Paragraph(": ",yourCustomFont ));
                lhcell4z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lhcell4z.setColspan(7);
                lhcell4z.setBorder(0);
                lhcell4z.setPadding(2);
                lhcell4z.setPaddingBottom(4);
                lhtab1.addCell(lhcell4z);
            }



            PdfPCell lhcell5;
            lhcell5 = new PdfPCell(new Paragraph("VARIETY ",yourCustomFonthead));
            lhcell5.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell5.setColspan(3);
            lhcell5.setBorder(0);
            lhcell5.setPadding(2);
            lhcell5.setPaddingBottom(4);
            lhtab1.addCell(lhcell5);
            PdfPCell lhcell5z;
            lhcell5z = new PdfPCell(new Paragraph(": "+movieModel.getVariety(),yourCustomFont));
            lhcell5z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell5z.setColspan(7);
            lhcell5z.setBorder(0);
            lhcell5z.setPadding(2);
            lhcell5z.setPaddingBottom(4);
            lhtab1.addCell(lhcell5z);

            PdfPCell lhcell6;
            lhcell6 = new PdfPCell(new Paragraph("GROSS WT. ",yourCustomFonthead));
            lhcell6.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell6.setColspan(3);
            lhcell6.setBorder(0);
            lhcell6.setPadding(2);
            lhcell6.setPaddingBottom(4);
            lhtab1.addCell(lhcell6);

            PdfPCell lhcell6z;
            lhcell6z = new PdfPCell(new Paragraph(": "+movieModel.getGross_wt()+" Lb",yourCustomFont));
            lhcell6z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell6z.setColspan(7);
            lhcell6z.setBorder(0);
            lhcell6z.setPadding(2);
            lhcell6z.setPaddingBottom(4);
            lhtab1.addCell(lhcell6z);




            PdfPCell lhcell67z;
            lhcell67z = new PdfPCell(new Paragraph("TARE WT.  ",yourCustomFonthead));
            lhcell67z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell67z.setColspan(3);
            lhcell67z.setBorder(0);
            lhcell67z.setPadding(2);
            lhcell67z.setPaddingBottom(4);
            lhtab1.addCell(lhcell67z);

            PdfPCell lhcell678z;
            lhcell678z = new PdfPCell(new Paragraph(": "+movieModel.getTare_wt()+" Lb",yourCustomFont));
            lhcell678z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell678z.setColspan(7);
            lhcell678z.setBorder(0);
            lhcell678z.setPadding(2);
            lhcell678z.setPaddingBottom(4);
            lhtab1.addCell(lhcell678z);

            PdfPCell lhcell7;
            lhcell7 = new PdfPCell(new Paragraph("NET WT. ",yourCustomFonthead));
            lhcell7.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell7.setColspan(3);
            lhcell7.setBorder(0);
            lhcell7.setPadding(2);
            lhcell7.setPaddingBottom(4);
            lhtab1.addCell(lhcell7);
            PdfPCell lhcell7z;
            lhcell7z = new PdfPCell(new Paragraph(": "+movieModel.getNet_wt()+" Lb",yourCustomFont));
            lhcell7z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell7z.setColspan(7);
            lhcell7z.setBorder(0);
            lhcell7z.setPadding(2);
            lhcell7z.setPaddingBottom(4);
            lhtab1.addCell(lhcell7z);



            PdfPCell lhcell8;
            lhcell8 = new PdfPCell(new Paragraph("HANDLER ",yourCustomFonthead));
            lhcell8.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell8.setColspan(3);
            lhcell8.setBorder(0);
            lhcell8.setPadding(2);
            lhcell8.setPaddingBottom(4);
            lhtab1.addCell(lhcell8);
            PdfPCell lhcell8z;
            lhcell8z = new PdfPCell(new Paragraph(": "+movieModel.getHandler(),yourCustomFont));
            lhcell8z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell8z.setColspan(7);
            lhcell8z.setBorder(0);
            lhcell8z.setPadding(2);
            lhcell8z.setPaddingBottom(4);
            lhtab1.addCell(lhcell8z);

            PdfPCell lhcell9;
            lhcell9 = new PdfPCell(new Paragraph("TRUCK NO. ",yourCustomFonthead));
            lhcell9.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9.setColspan(3);
            lhcell9.setBorder(0);
            lhcell9.setPadding(2);
            lhcell9.setPaddingBottom(4);
            lhtab1.addCell(lhcell9);
            PdfPCell lhcell9a;
            lhcell9a = new PdfPCell(new Paragraph(": "+movieModel.getTruck_no(),yourCustomFont));
            lhcell9a.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9a.setColspan(3);
            lhcell9a.setBorder(0);
            lhcell9a.setPadding(2);
            lhcell9a.setPaddingBottom(4);
            lhtab1.addCell(lhcell9a);
            PdfPCell lhcell9z;
            lhcell9z = new PdfPCell(new Paragraph("LICENSE ",yourCustomFonthead));
            lhcell9z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9z.setColspan(2);
            lhcell9z.setBorder(0);
            lhcell9z.setPadding(2);
            lhcell9z.setPaddingBottom(4);
            lhtab1.addCell(lhcell9z);
            PdfPCell lhcell9za;
            lhcell9za = new PdfPCell(new Paragraph(": "+movieModel.getTruck_lice(),yourCustomFont));
            lhcell9za.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9za.setColspan(2);
            lhcell9za.setBorder(0);
            lhcell9za.setPadding(2);
            lhcell9za.setPaddingBottom(4);
            lhtab1.addCell(lhcell9za);

            PdfPCell lhcell91;
            lhcell91 = new PdfPCell(new Paragraph("SEMI NO. ",yourCustomFonthead));
            lhcell91.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell91.setColspan(3);
            lhcell91.setBorder(0);
            lhcell91.setPadding(2);
            lhcell91.setPaddingBottom(4);
            lhtab1.addCell(lhcell91);

            PdfPCell lhcell91a;
            lhcell91a = new PdfPCell(new Paragraph(": "+movieModel.getSemi_no(),yourCustomFont));
            lhcell91a.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell91a.setColspan(3);
            lhcell91a.setBorder(0);
            lhcell91a.setPadding(2);
            lhcell91a.setPaddingBottom(4);
            lhtab1.addCell(lhcell91a);

            PdfPCell lhcell91z;
            lhcell91z = new PdfPCell(new Paragraph("LICENSE ",yourCustomFonthead));
            lhcell91z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell91z.setColspan(2);
            lhcell91z.setBorder(0);
            lhcell91z.setPadding(2);
            lhcell91z.setPaddingBottom(4);
            lhtab1.addCell(lhcell91z);

            PdfPCell lhcell91za;
            lhcell91za = new PdfPCell(new Paragraph(": "+movieModel.getSemi_lic(),yourCustomFont));
            lhcell91za.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell91za.setColspan(2);
            lhcell91za.setBorder(0);
            lhcell91za.setPadding(2);
            lhcell91za.setPaddingBottom(4);
            lhtab1.addCell(lhcell91za);

            PdfPCell lhcell912;
            lhcell912 = new PdfPCell(new Paragraph("TRAILER NO. ",yourCustomFonthead));
            lhcell912.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell912.setColspan(3);
            lhcell912.setBorder(0);
            lhcell912.setPadding(2);
            lhcell912.setPaddingBottom(4);
            lhtab1.addCell(lhcell912);
            PdfPCell lhcell912a;
            lhcell912a = new PdfPCell(new Paragraph(": "+movieModel.getTrailer_no(),yourCustomFont));
            lhcell912a.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell912a.setColspan(3);
            lhcell912a.setBorder(0);
            lhcell912a.setPadding(2);
            lhcell912a.setPaddingBottom(4);
            lhtab1.addCell(lhcell912a);
            PdfPCell lhcell912z;
            lhcell912z = new PdfPCell(new Paragraph("LICENSE ",yourCustomFonthead));
            lhcell912z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell912z.setColspan(2);
            lhcell912z.setBorder(0);
            lhcell912z.setPadding(2);
            lhcell912z.setPaddingBottom(4);
            lhtab1.addCell(lhcell912z);

            PdfPCell lhcell912zb;
            lhcell912zb = new PdfPCell(new Paragraph(": "+movieModel.getTrailer_lic(),yourCustomFont));
            lhcell912zb.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell912zb.setColspan(2);
            lhcell912zb.setBorder(0);
            lhcell912zb.setPadding(2);
            lhcell912zb.setPaddingBottom(4);
            lhtab1.addCell(lhcell912zb);

            PdfPCell lhcell9az;
            lhcell9az = new PdfPCell(new Paragraph("DRIVER ",yourCustomFonthead));
            lhcell9az.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9az.setColspan(3);
            lhcell9az.setBorder(0);
            lhcell9az.setPadding(2);
            lhtab1.addCell(lhcell9az);
            PdfPCell lhcell9b;
            lhcell9b = new PdfPCell(new Paragraph(": "+movieModel.getDriver(),yourCustomFont));
            lhcell9b.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9b.setColspan(7);
            lhcell9b.setBorder(0);
            lhcell9b.setPadding(2);
            lhtab1.addCell(lhcell9b);
            doc.add(lhtab1);
            PdfPTable lhtab2 = new PdfPTable(6);
            lhtab2.setWidthPercentage(100);
            lhtab2.setHorizontalAlignment(0);


            if(movieModel.getOrder_type()!=null && movieModel.getOrder_type().contentEquals("1")) {


                try {
                    // get input stream
                    Drawable d = context.getResources().getDrawable(R.drawable.tickk);
                    BitmapDrawable bitDw = ((BitmapDrawable) d);
                    Bitmap bmp = bitDw.getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Image image = Image.getInstance(stream.toByteArray());
                    image.scaleToFit(30F, 30F);
                    doc.add(image);
                }
                catch(IOException ex)
                {
                    return;
                }
                PdfPCell lhcell9c;
                lhcell9c = new PdfPCell(new Paragraph("( MEATS ) ",yourCustomFonthead));
                lhcell9c.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lhcell9c.setColspan(3);
                lhcell9c.setBorder(0);
                lhcell9c.setPadding(2);
                lhtab2.addCell(lhcell9c);
                PdfPCell lhcell9d;
                lhcell9d = new PdfPCell(new Paragraph("INSHELL",yourCustomFonthead));
                lhcell9d.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
                lhcell9d.setColspan(3);
                lhcell9d.setBorder(0);
                lhcell9d.setPadding(2);
                lhtab2.addCell(lhcell9d);
            }else{
                PdfPCell lhcell9c;
                lhcell9c = new PdfPCell(new Paragraph("MEATS ",yourCustomFonthead));
                lhcell9c.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lhcell9c.setColspan(3);
                lhcell9c.setBorder(0);
                lhcell9c.setPadding(2);
                lhtab2.addCell(lhcell9c);
                try {
                    // get input stream
                    Drawable d = context.getResources().getDrawable(R.drawable.tickk);
                    BitmapDrawable bitDw = ((BitmapDrawable) d);
                    Bitmap bmp = bitDw.getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Image image = Image.getInstance(stream.toByteArray());
                    image.scaleToFit(30F, 30F);
                    image.setAlignment(Element.ALIGN_RIGHT);
                    doc.add(image);
                }
                catch(IOException ex)
                {
                    return;
                }
                PdfPCell lhcell9d;
                lhcell9d = new PdfPCell(new Paragraph("( INSHELL )",yourCustomFonthead));
                lhcell9d.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
                lhcell9d.setColspan(3);
                lhcell9d.setBorder(0);
                lhcell9d.setPadding(2);
                lhtab2.addCell(lhcell9d);
            }
            doc.add(lhtab2);

            Paragraph p1 = new Paragraph(new Phrase(lineSpacing,"              ",
                    FontFactory.getFont(FontFactory.COURIER, fntSize)));


            doc.add(p1);
//            Paragraph p2 = new Paragraph(new Phrase(lineSpacing,"              ",
//                    FontFactory.getFont(FontFactory.COURIER, fntSize)));
//
//
//            doc.add(p2);
//            Paragraph p3 = new Paragraph(new Phrase(lineSpacing,"              ",
//                    FontFactory.getFont(FontFactory.COURIER, fntSize)));
//
//
//            doc.add(p3);
            Paragraph p = new Paragraph(new Phrase(lineSpacing,"Almonds are not processed to adequately reduce the presence of microorganisms of public health significance.",
                    FontFactory.getFont(FontFactory.COURIER, fntSize)));


            doc.add(p);
//            Intent mintent = new Intent(context, viewproduction.class);
//            context.startActivity(mintent);
            printz(file,movieModel.getId());
        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

    }
    private void printz(File file, String idval) {
        PrintManager manager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);

        PDFDocumentAdapter adapter = new PDFDocumentAdapter(file, idval);
        PrintAttributes attributes = new PrintAttributes.Builder().build();
        manager.print("Document", adapter, attributes);

    }


}
