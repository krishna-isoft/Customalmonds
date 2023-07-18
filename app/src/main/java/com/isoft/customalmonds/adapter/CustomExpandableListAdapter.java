package com.isoft.customalmonds.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.print.PrintAttributes;
import android.print.PrintManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isoft.customalmonds.AddSubTagActivity;
import com.isoft.customalmonds.PDFDocumentAdapter;
import com.isoft.customalmonds.R;
import com.isoft.customalmonds.barcodegenerate.BarcodeCreater;
import com.isoft.customalmonds.modelclass.grs_bin_tag;
import com.isoft.customalmonds.modelclass.production_response;
import com.isoft.customalmonds.modelclass.sub_grs_bin_tag;
import com.isoft.customalmonds.modelclass.sub_production_response;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    List<production_response> expandableListTitle = new ArrayList<production_response>();
    List<sub_production_response> listsubproduct=new ArrayList<>();
    private Bitmap btMap = null;
    LinearLayout linsubproduct;
    public CustomExpandableListAdapter(Context context, List<production_response> expandableListTitle) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;

    }

    @Override
    public grs_bin_tag getChild(int listPosition, int expandedListPosition) {
        return this.expandableListTitle.get(listPosition).getGrs_bin_tag().get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View dialogView, ViewGroup parent) {
        final grs_bin_tag gk = getChild(listPosition, expandedListPosition);
        //Log.e("val","@"+gk.getTagno());
        if (dialogView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dialogView = layoutInflater.inflate(R.layout.grn_bin_list, null);
        }
        TextView tagvalue = dialogView.findViewById(R.id.txt_tag_value);
        TextView typevalue = dialogView.findViewById(R.id.txt_type_value);
        TextView txthandlervalue = dialogView.findViewById(R.id.txt_handlervalue);
        TextView txttare = dialogView.findViewById(R.id.txt_tare);
        TextView txtnwt = dialogView.findViewById(R.id.txt_nwt);
        TextView txtgwt = dialogView.findViewById(R.id.txt_gwt);
        ImageView imgsubprint=dialogView.findViewById(R.id.img_subprint);
        TextView txtrepname=dialogView.findViewById(R.id.txt_repname);
        tagvalue.setText(" :  " + gk.getTagno());
        txthandlervalue.setText(" :  " + gk.getHandler());
        txttare.setText(" :  " + gk.getTare()+" Lb");
        txtnwt.setText(" :  " + gk.getNet_wt()+" Lb");
        txtgwt.setText(" :  " + gk.getGross_wt()+" Lb");
        if(gk.getRep_name() !=null && gk.getRep_name().length()>0 &&
                !gk.getRep_name().contentEquals("null")) {
            txtrepname.setText(" :  " + gk.getRep_name());
        }else{
            txtrepname.setText(" :  ");
        }
        typevalue.setText(" :  "+ gk.getType());
        //  movieModel.setViewstatus("0");

        imgsubprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap btMap = BarcodeCreater.creatBarcode(context, gk.getTagno(),
                        10, 10,
                        300, 40, true, 3,
                        40);
                try {
                    createPDFtag(gk,expandableListTitle.get(listPosition),btMap);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            listsubproduct=new ArrayList<>();
            listsubproduct=expandableListTitle.get(listPosition).getSub_product();
            //		Log.e("listsubproduct","@"+listsubproduct.size());
            if (listsubproduct.size() > 0) {
                for (int i = 0; i < listsubproduct.size(); i++) {//Dialog dialogrk;
                    sub_production_response gksub = listsubproduct.get(i);
                    //			Log.e("txtrunid","@"+gk.getPid());
                    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View dialogViewkk = inflater.inflate(R.layout.subproduct_home_list, null);

                    TextView txtrunid=  dialogViewkk.findViewById(R.id.txt_runid);
                    TextView txtid=  dialogViewkk.findViewById(R.id.txt_id);
                    TextView txtgrower=  dialogViewkk.findViewById(R.id.txt_grower);
                    TextView txttype=  dialogViewkk.findViewById(R.id.txt_type);
                    TextView txtrname=  dialogViewkk.findViewById(R.id.txt_rname);
                    TextView txtvariety=  dialogViewkk.findViewById(R.id.txt_variety);
                    TextView txtfticket=  dialogViewkk.findViewById(R.id.txt_fticket);
                    TextView txttotalwgt=  dialogViewkk.findViewById(R.id.txt_totalwgt);
                    TextView txtdate=  dialogViewkk.findViewById(R.id.txt_date);
                    LinearLayout lingrssub=  dialogViewkk.findViewById(R.id.lin_grssub);
                    ImageView imgviewsub=  dialogViewkk.findViewById(R.id.img_view);
                    ImageView imgviewupsub=dialogViewkk.findViewById(R.id.img_viewup);
                    TextView txtview=  dialogViewkk.findViewById(R.id.txt_view);
                    ImageView imgaddsub=  dialogViewkk.findViewById(R.id.img_add);
                    LinearLayout linsubhead=  dialogViewkk.findViewById(R.id.linsubbhead);
                    TextView txtrepnamesub=  dialogViewkk.findViewById(R.id.txt_repname);
                    txtrunid.setText("" + gksub.getPid());
                    txtgrower.setText(" :  " + gksub.getGrower());
                    txttype.setText(" :  " + gksub.getType());
                    txtrname.setText(" :  " + gksub.getRanch_name());
                    txtvariety.setText(" :  " + gksub.getVariety());
                    txtfticket.setText(" :  "+gksub.getField_ticket());
                    txttotalwgt.setText(" :  "+gksub.getTotal_netwt());
                    txtdate.setText(" :  "+gksub.getCreated_date());
                    if(gksub.getRep_name() !=null && gksub.getRep_name().length()>0
                            && !gksub.getRep_name().contentEquals("null")) {
                        txtrepnamesub.setText(" :  " + gksub.getRep_name());
                    }else{
                        txtrepnamesub.setText(" :  ");
                    }
                    //  movieModel.setViewstatus("0");


                    imgaddsub.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent mintent = new Intent(context, AddSubTagActivity.class);
                            //  mintent.putExtra("type",type);
                            mintent.putExtra("tag","subtag");
                            mintent.putExtra("runno",gksub.getPid());
                            mintent.putExtra("runid",gksub.getId());
                            mintent.putExtra("type",gksub.getType());
                            mintent.putExtra("grower",gksub.getGrower());
                            mintent.putExtra("variety",gksub.getVariety());
                            mintent.putExtra("ranchname",gksub.getRanch_name());
                            context.startActivity(mintent);
                            ((Activity)context).finish();
                        }
                    });


                    try {
                        List<sub_grs_bin_tag> listtagsub=new ArrayList<>();
                        listtagsub=gksub.getSub_grs_bin_tag();
                        if (listtagsub.size() > 0) {
                            int val=30;
                            val=listtagsub.size();
                            if(val>30)
                            {
                                val=25;
                            }else{
                                val=listtagsub.size();
                            }
                            for(int j=0;j<val;j++)
                            {//Dialog dialogrk;
                                sub_grs_bin_tag gksubsub=listtagsub.get(j);
                                //   Log.e("listtag","#"+gk.getTagno());
                                LayoutInflater inflatersub = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View dialogViewsub = inflater.inflate(R.layout.grn_bin_listsub, null);

                                TextView tagvaluez = dialogViewsub.findViewById(R.id.txt_tag_value);
                                TextView txthandlervaluez = dialogViewsub.findViewById(R.id.txt_handlervalue);
                                TextView txttarez = dialogViewsub.findViewById(R.id.txt_tare);
                                TextView txtnwtz = dialogViewsub.findViewById(R.id.txt_nwt);
                                TextView txtgwtz= dialogViewsub.findViewById(R.id.txt_gwt);
                                ImageView imgsubprintz=dialogViewsub.findViewById(R.id.img_subprint);
                                TextView txtrepnamesubz = dialogViewsub.findViewById(R.id.txt_repname);
                                tagvaluez.setText(" :  " + gksubsub.getTagno());
                                txthandlervaluez.setText(" :  " + gksubsub.getHandler());
                                txttarez.setText(" :  " + gksubsub.getTare()+" Lb");
                                txtnwtz.setText(" :  " + gksubsub.getNet_wt()+" Lb");
                                txtgwtz.setText(" :  " + gksubsub.getGross_wt()+" Lb");

                                if(gksubsub.getRep_name() !=null && gksubsub.getRep_name().length()>0
                                        && !gksubsub.getRep_name().contentEquals("null")) {
                                    txtrepnamesubz.setText(" :  " + gksubsub.getRep_name());
                                }else{
                                    txtrepnamesubz.setText(" :  " );
                                }
                                //  movieModel.setViewstatus("0");





                                imgsubprintz.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        btMap = BarcodeCreater.creatBarcode(context, gksubsub.getTagno(),
                                                10, 10,
                                                300, 40, true, 3,
                                                40);
                                        try {
                                            createPDFtagsub(gksubsub,gksub,btMap,expandableListTitle.get(listPosition));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (DocumentException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                lingrssub.addView(dialogViewsub);
                            }

                        }

                        //scheduleSendLocation();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    linsubhead.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String val=txtview.getText().toString();

                            //String val=movieModel.getViewstatus();
                            if(val.contentEquals("0") )
                            {
                                //						Log.e("callppppzzzz","upparrow");
                                txtview.setText("1");
                                gksub.setViewstatus("1");
                                lingrssub.setVisibility(View.VISIBLE);
                                // imgview.setBackgroundResource(R.drawable.uparrow);
                                imgviewsub.setVisibility(View.GONE);
                                imgviewupsub.setVisibility(View.VISIBLE);
                            }else if(val.contentEquals("1")){
                                //						Log.e("callpppp","downarrow");
                                txtview.setText("0");
                                gksub.setViewstatus("0");
                                lingrssub.setVisibility(View.GONE);
                                imgviewsub.setVisibility(View.VISIBLE);
                                imgviewupsub.setVisibility(View.GONE);
                                //  imgview.setBackgroundResource(R.drawable.downarrow);
                            }
                        }
                    });
                    imgviewsub.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String val=txtview.getText().toString();

                            //String val=movieModel.getViewstatus();
                            if(val.contentEquals("0") )
                            {
                                //						Log.e("callppppzzzz","upparrow");
                                txtview.setText("1");
                                gksub.setViewstatus("1");
                                lingrssub.setVisibility(View.VISIBLE);
                                // imgview.setBackgroundResource(R.drawable.uparrow);
                                imgviewsub.setVisibility(View.GONE);
                                imgviewupsub.setVisibility(View.VISIBLE);
                            }else if(val.contentEquals("1")){
                                //						Log.e("callpppp","downarrow");
                                txtview.setText("0");
                                gksub.setViewstatus("0");
                                lingrssub.setVisibility(View.GONE);
                                imgviewsub.setVisibility(View.VISIBLE);
                                imgviewupsub.setVisibility(View.GONE);
                                //  imgview.setBackgroundResource(R.drawable.downarrow);
                            }
                        }
                    });
                    imgviewupsub.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // String val=txtview.getText().toString();
                            String val=expandableListTitle.get(listPosition).getViewstatus();
                            if(val.contentEquals("0"))
                            {

                                //				Log.e("callpppp","downarrow"+val);
                                txtview.setText("0");
                                gksub.setViewstatus("0");
                                lingrssub.setVisibility(View.GONE);
                                imgviewsub.setVisibility(View.VISIBLE);
                                imgviewupsub.setVisibility(View.GONE);
                            }else if(val.contentEquals("1")){

                                //				Log.e("callppppdd","upparrow");
                                txtview.setText("1");
                                gksub.setViewstatus("1");
                                lingrssub.setVisibility(View.VISIBLE);
                                // imgview.setBackgroundResource(R.drawable.uparrow);
                                imgviewsub.setVisibility(View.GONE);
                                imgviewupsub.setVisibility(View.VISIBLE);
                                //  imgview.setBackgroundResource(R.drawable.downarrow);
                            }
                        }
                    });

                    linsubproduct.addView(dialogViewkk);
                }
            }
        }catch (Exception e)
        {
            	//	Log.e("ekkk","@"+e.toString());
        }
        return dialogView;
    }

    @Override
    public int getChildrenCount(int listPosition) {


        return this.expandableListTitle.get(listPosition).getGrs_bin_tag()
                .size();
    }

    @Override
    public production_response getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View itemView, ViewGroup parent) {
        production_response movieModel = getGroup(listPosition);
        if (itemView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = layoutInflater.inflate(R.layout.product_home_list, null);
        }
        //Log.e("isExpanded",""+isExpanded);
        TextView txtrunid,txtid,txtrepname;
        TextView txtgrower;
        TextView txttype,txtrname;
        TextView txtvariety,txtfticket;
        TextView txttotalwgt,txtdate,txtview,txtcropyear;
        LinearLayout linhead;
        ImageView imgview,imgviewup,imgadd,imgprintz;
        LinearLayout linmain;
        txtrunid=  itemView.findViewById(R.id.txt_runid);
        txtid=  itemView.findViewById(R.id.txt_id);
        txtgrower=  itemView.findViewById(R.id.txt_grower);
        txttype=  itemView.findViewById(R.id.txt_type);
        txtrname=  itemView.findViewById(R.id.txt_rname);
        txtvariety=  itemView.findViewById(R.id.txt_variety);
        txtfticket=  itemView.findViewById(R.id.txt_fticket);
       txttotalwgt=  itemView.findViewById(R.id.txt_totalwgt);
        txtdate=  itemView.findViewById(R.id.txt_date);
        linmain=  itemView.findViewById(R.id.lin_grs);
        imgview=  itemView.findViewById(R.id.img_view);
       imgviewup=itemView.findViewById(R.id.img_viewup);
        txtview=  itemView.findViewById(R.id.txt_view);
        imgadd=  itemView.findViewById(R.id.img_add);
        txtcropyear=itemView.findViewById(R.id.txt_cropyear);
        imgprintz=  itemView.findViewById(R.id.img_printz);
        linsubproduct=itemView.findViewById(R.id.lin_subproduct);
        linhead=itemView.findViewById(R.id.linhead);
        txtrepname=itemView.findViewById(R.id.txt_repname);
        txtid.setText(""+movieModel.getId());
        txtrunid.setText(""+movieModel.getPid());
        txtgrower.setText(" :  "+movieModel.getGrower());
        txttype.setText(" :  "+movieModel.getType());
        if(movieModel.getCrop_year() !=null && movieModel.getCrop_year().length()>0 &&
                !movieModel.getCrop_year().contentEquals("null")) {
            txtcropyear.setText(" : " + movieModel.getCrop_year());
        }else{
            txtcropyear.setText(" : ");
        }
        if(movieModel.getRep_name() !=null && movieModel.getRep_name().length()>0 &&
                !movieModel.getRep_name().contentEquals("null")) {
            txtrepname.setText(" :  " + movieModel.getRep_name());
        }else {
            txtrepname.setText(" :  ");
        }
        if(movieModel.getRanch_name() !=null && movieModel.getRanch_name().length()>0 &&
                !movieModel.getRanch_name().contentEquals("null"))
        {
            txtrname.setText(" :  "+movieModel.getRanch_name());
        }else{
           txtrname.setText("");
        }
        //Log.e("isExpandedval",""+movieModel.getGrs_bin_tag());
        txtvariety.setText(" :  "+movieModel.getVariety());
        txtfticket.setText(" :  "+movieModel.getField_ticket());
        txttotalwgt.setText(" :  "+movieModel.getTotal_netwt()+" Lb");
        txtdate.setText(" :  "+movieModel.getCreated_date());
//        TextView listTitleTextView = (TextView) convertView
//                .findViewById(R.id.listTitle);
//        listTitleTextView.setTypeface(null, Typeface.BOLD);
//        listTitleTextView.setText(listTitle.);

        if(isExpanded)
        {
                                txtview.setText("1");
                    movieModel.setViewstatus("1");
                   // linmain.setVisibility(View.VISIBLE);
                    imgview.setVisibility(View.GONE);
                    imgviewup.setVisibility(View.VISIBLE);
        }else{
            txtview.setText("0");
                    movieModel.setViewstatus("0");
                   // linmain.setVisibility(View.GONE);
                    imgview.setVisibility(View.VISIBLE);
                    imgviewup.setVisibility(View.GONE);
        }

        imgadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mintent = new Intent(context, AddSubTagActivity.class);
                //  mintent.putExtra("type",type);
                mintent.putExtra("runno",movieModel.getPid());
                mintent.putExtra("runid",movieModel.getId());
                mintent.putExtra("tag","maintag");
                mintent.putExtra("type",movieModel.getType());
                mintent.putExtra("grower",movieModel.getGrower());
                mintent.putExtra("variety",movieModel.getVariety());
                mintent.putExtra("ranchname",movieModel.getRanch_name());
                context.startActivity(mintent);
                //((Activity)context).finish();
            }
        });

        return itemView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public void createPDFtagsub(sub_grs_bin_tag gk, sub_production_response movieModel, Bitmap barcodemap, production_response production_response) throws IOException, DocumentException {

        //Document doc = new Document(PageSize.B7);
        Document doc = new Document(PageSize.A6);
        try {
            try {
                File dir = context.getFilesDir();
                File filez = new File(dir, "gross_bin_tag.pdf");
                boolean deleted = filez.delete();
                if(deleted)
                {

                }
            }catch (Exception e)
            {

            }
            File file = new File(context.getFilesDir(),  "gross_bin_tag.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter.getInstance(doc, fOut);
            doc.open();
            Font subfontsub = FontFactory.getFont(FontFactory.HELVETICA, 9);
            Font subfontsubx = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
            PdfPTable headyr = new PdfPTable(1);
            headyr.setWidthPercentage(100);

            PdfPCell cellyr;
            cellyr = new PdfPCell(new Paragraph("Crop Year : "+ production_response.getCrop_year()
                    ,subfontsubx));
            cellyr.setBorder(0);
            cellyr.setPaddingBottom(5);
            cellyr.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            headyr.addCell(cellyr);
            doc.add(headyr);



            try {
                // get input stream
                Drawable d = context.getResources().getDrawable(R.drawable.logoprintnew);
                BitmapDrawable bitDw = ((BitmapDrawable) d);
                Bitmap bmp = bitDw.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                image.setAlignment(Element.ALIGN_CENTER);
                // image.scaleToFit(190f, 30f);
                image.scaleToFit(260f, 50f);
                doc.add(image);
            }
            catch(IOException ex)
            {
                return;
            }
//            LineSeparator line = new LineSeparator();
//            doc.add(line);
            Font yourCustomFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
            Font yourCustomFontsub = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font yourCustomFontz = FontFactory.getFont(FontFactory.HELVETICA, 13);
            PdfPTable head = new PdfPTable(1);
            head.setWidthPercentage(100);

            PdfPCell cellkzq;
            cellkzq = new PdfPCell(new Paragraph("",yourCustomFontz));
            cellkzq.setBorder(0);
            cellkzq.setPaddingBottom(5);
            cellkzq.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head.addCell(cellkzq);
            PdfPCell cellk;
            cellk = new PdfPCell(new Paragraph("Gross Bin Tag ",yourCustomFont));
            cellk.setBorder(0);
            cellk.setPaddingBottom(2);
			/*cellk.setBorderWidthLeft(1);
			cellk.setBorderWidthRight(1);*/

            cellk.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head.addCell(cellk);
            PdfPCell cellkzqw;
            cellkzqw = new PdfPCell(new Paragraph("",yourCustomFontz));
            cellkzqw.setBorder(0);
            cellkzqw.setPaddingBottom(5);
            cellkzqw.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head.addCell(cellkzqw);
            //	PdfPCell cellkz;
//			cellkz = new PdfPCell(new Paragraph(""+movieModel.getType(),yourCustomFontz));
//			cellkz.setBorder(0);
//			cellkz.setPaddingBottom(2);
//			/*cellk.setBorderWidthLeft(1);
//			cellk.setBorderWidthRight(1);*/
//
//			cellkz.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
//			head.addCell(cellkz);

//			PdfPCell cellkzqwr;
//			cellkzqwr = new PdfPCell(new Paragraph("",yourCustomFontz));
//			cellkzqwr.setBorder(0);
//			cellkzqwr.setPaddingBottom(5);
//			cellkzqwr.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
//			head.addCell(cellkzqwr);

            //
            PdfPCell cellkzh;
            cellkzh = new PdfPCell(new Paragraph("Tag# "+": 0000"+gk.getTagno(),yourCustomFontsub));
            cellkzh.setBorder(0);
            cellkzh.setPaddingBottom(2);
            cellkzh.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head.addCell(cellkzh);

            PdfPCell cellkzqwrh;
            cellkzqwrh = new PdfPCell(new Paragraph("",yourCustomFontz));
            cellkzqwrh.setBorder(0);
            cellkzqwrh.setPaddingBottom(5);
            cellkzqwrh.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head.addCell(cellkzqwrh);
            cellkzqwrh = new PdfPCell(new Paragraph("",yourCustomFontz));
            cellkzqwrh.setBorder(0);
            cellkzqwrh.setPaddingBottom(5);
            cellkzqwrh.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head.addCell(cellkzqwrh);

            doc.add(head);
            Font subfont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
            Font subfontright = FontFactory.getFont(FontFactory.HELVETICA, 9);



//set driver name and date
            PdfPTable lhtab0 = new PdfPTable(9);
            lhtab0.setWidthPercentage(100);
            lhtab0.setHorizontalAlignment(0);


            PdfPCell lhcell2;
            lhcell2 = new PdfPCell(new Paragraph("RUN N0. ",subfont));
            lhcell2.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell2.setColspan(4);
            lhcell2.setBorder(0);
            lhcell2.setPadding(2);
            lhtab0.addCell(lhcell2);

            PdfPCell lhcell2zd;
            lhcell2zd = new PdfPCell(new Paragraph(": ",subfontright));
            lhcell2zd.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell2zd.setColspan(1);
            lhcell2zd.setBorder(0);
            lhcell2zd.setPadding(2);
            lhtab0.addCell(lhcell2zd);

            PdfPCell lhcell2z;
            lhcell2z = new PdfPCell(new Paragraph(""+movieModel.getPid(),subfontright));
            lhcell2z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell2z.setColspan(4);
            lhcell2z.setBorder(0);
            lhcell2z.setPadding(2);
            lhtab0.addCell(lhcell2z);



            //new

            PdfPCell lhcellmk2;
            lhcellmk2 = new PdfPCell(new Paragraph("Type ",subfont));
            lhcellmk2.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellmk2.setColspan(4);
            lhcellmk2.setBorder(0);
            lhcellmk2.setPadding(2);
            lhtab0.addCell(lhcellmk2);

            PdfPCell lhcell2zmkd;
            lhcell2zmkd = new PdfPCell(new Paragraph(": ",subfontright));
            lhcell2zmkd.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell2zmkd.setColspan(1);
            lhcell2zmkd.setBorder(0);
            lhcell2zmkd.setPadding(2);
            lhtab0.addCell(lhcell2zmkd);

            PdfPCell lhcell2mkz;
            lhcell2mkz = new PdfPCell(new Paragraph(""+movieModel.getType(),subfontright));
            lhcell2mkz.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell2mkz.setColspan(4);
            lhcell2mkz.setBorder(0);
            lhcell2mkz.setPadding(2);
            lhtab0.addCell(lhcell2mkz);



            PdfPCell lhcell3;
            lhcell3 = new PdfPCell(new Paragraph("HANDLER ",subfont));
            lhcell3.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell3.setColspan(4);
            lhcell3.setBorder(0);
            lhcell3.setPadding(2);
            lhtab0.addCell(lhcell3);

            PdfPCell lhcellcol;
            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab0.addCell(lhcellcol);

            PdfPCell lhcell3z;
            lhcell3z = new PdfPCell(new Paragraph(""+gk.getHandler(),subfontright));
            lhcell3z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell3z.setColspan(4);
            lhcell3z.setBorder(0);
            lhcell3z.setPadding(2);
            lhtab0.addCell(lhcell3z);


//
            PdfPCell lhcell4;
            lhcell4 = new PdfPCell(new Paragraph("GROWER ",subfont));
            lhcell4.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell4.setColspan(4);
            lhcell4.setBorder(0);
            lhcell4.setPadding(2);
            lhtab0.addCell(lhcell4);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab0.addCell(lhcellcol);

            PdfPCell lhcell4z;
            lhcell4z = new PdfPCell(new Paragraph(""+movieModel.getGrower(),subfontright));
            lhcell4z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell4z.setColspan(4);
            lhcell4z.setBorder(0);
            lhcell4z.setPadding(2);
            lhtab0.addCell(lhcell4z);

//
            PdfPCell lhcell5;
            lhcell5 = new PdfPCell(new Paragraph("VARIETY ",subfont));
            lhcell5.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell5.setColspan(4);
            lhcell5.setBorder(0);
            lhcell5.setPadding(2);
            lhtab0.addCell(lhcell5);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab0.addCell(lhcellcol);

            PdfPCell lhcell5z;
            lhcell5z = new PdfPCell(new Paragraph(""+movieModel.getVariety(),subfontright));
            lhcell5z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell5z.setColspan(4);
            lhcell5z.setBorder(0);
            lhcell5z.setPadding(2);
            lhtab0.addCell(lhcell5z);

            //
            PdfPCell lhcell6;
            lhcell6 = new PdfPCell(new Paragraph("RANCH NAME ",subfont));
            lhcell6.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell6.setColspan(4);
            lhcell6.setBorder(0);
            lhcell6.setPadding(2);
            lhtab0.addCell(lhcell6);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab0.addCell(lhcellcol);

            if(movieModel.getRanch_name() !=null && !movieModel.getRanch_name().contentEquals("null"))
            {
                PdfPCell lhcell6z;
                lhcell6z = new PdfPCell(new Paragraph(""+movieModel.getRanch_name(),subfontright));
                lhcell6z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lhcell6z.setColspan(4);
                lhcell6z.setBorder(0);
                lhcell6z.setPadding(2);
                lhtab0.addCell(lhcell6z);
            }else{
                PdfPCell lhcell6z;
                lhcell6z = new PdfPCell(new Paragraph(": ",subfontright));
                lhcell6z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lhcell6z.setColspan(4);
                lhcell6z.setBorder(0);
                lhcell6z.setPadding(2);
                lhtab0.addCell(lhcell6z);
            }
//

            PdfPCell lhcell9f;
            lhcell9f = new PdfPCell(new Paragraph("FUMIGATION ",subfont));
            lhcell9f.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9f.setColspan(4);
            lhcell9f.setBorder(0);
            lhcell9f.setPadding(2);
            lhcell9f.setPaddingBottom(5);
            lhtab0.addCell(lhcell9f);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab0.addCell(lhcellcol);

            PdfPCell lhcell9ff;
            lhcell9ff = new PdfPCell(new Paragraph(" ",subfontright));
            lhcell9ff.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9ff.setColspan(4);
            lhcell9ff.setBorder(0);
            lhcell9ff.setPadding(2);
            lhcell9ff.setPaddingBottom(5);
            lhtab0.addCell(lhcell9ff);

            //

            PdfPCell lhcell9fz;
            lhcell9fz = new PdfPCell(new Paragraph(" ",subfont));
            lhcell9fz.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9fz.setColspan(9);
            lhcell9fz.setBorder(0);
            lhcell9fz.setPadding(2);
            lhcell9fz.setPaddingBottom(5);
            lhtab0.addCell(lhcell9fz);


            //new fumugation date
            PdfPCell lhcell9af;
            lhcell9af = new PdfPCell(new Paragraph("FUMIGATION DATE",subfont));
            lhcell9af.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9af.setColspan(4);
            lhcell9af.setBorder(0);
            lhcell9af.setPadding(2);
            lhcell9af.setPaddingBottom(5);
            lhtab0.addCell(lhcell9af);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab0.addCell(lhcellcol);

            PdfPCell lhcell9sff;
            lhcell9sff = new PdfPCell(new Paragraph(" ",subfontright));
            lhcell9sff.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9sff.setColspan(4);
            lhcell9sff.setBorder(0);
            lhcell9sff.setPadding(2);
            lhcell9sff.setPaddingBottom(5);
            lhtab0.addCell(lhcell9sff);

            //

//			PdfPCell lhcell9dfz;
//			lhcell9dfz = new PdfPCell(new Paragraph(" ",subfont));
//			lhcell9dfz.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
//			lhcell9dfz.setColspan(8);
//			lhcell9dfz.setBorder(0);
//			lhcell9dfz.setPadding(2);
//			lhcell9dfz.setPaddingBottom(5);
//			lhtab0.addCell(lhcell9dfz);

            doc.add(lhtab0);


            LineSeparator line = new LineSeparator();
            doc.add(line);



            PdfPTable lhtab2 = new PdfPTable(9);
            lhtab2.setWidthPercentage(100);
            lhtab2.setHorizontalAlignment(0);



            PdfPCell lhcell7;
            lhcell7 = new PdfPCell(new Paragraph("GROSS WEIGHT ",subfont));
            lhcell7.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell7.setColspan(4);
            lhcell7.setBorder(0);
            lhcell7.setPadding(2);
            lhtab2.addCell(lhcell7);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab2.addCell(lhcellcol);

            PdfPCell lhcell7z;
            lhcell7z = new PdfPCell(new Paragraph(""+gk.getGross_wt()+" Lb",subfontright));
            lhcell7z.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
            lhcell7z.setColspan(4);
            lhcell7z.setBorder(0);
            lhcell7z.setPadding(2);
            lhtab2.addCell(lhcell7z);



            PdfPCell lhcell8;
            lhcell8 = new PdfPCell(new Paragraph("TARE ",subfont));
            lhcell8.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell8.setColspan(4);
            lhcell8.setBorder(0);
            lhcell8.setPadding(2);
            lhtab2.addCell(lhcell8);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab2.addCell(lhcellcol);
            PdfPCell lhcell8z;
            lhcell8z = new PdfPCell(new Paragraph(""+gk.getTare()+" Lb",subfontright));
            lhcell8z.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
            lhcell8z.setColspan(4);
            lhcell8z.setBorder(0);
            lhcell8z.setPadding(2);
            lhtab2.addCell(lhcell8z);


            PdfPCell lhcell8j;
            lhcell8j = new PdfPCell(new Paragraph("",subfont));
            lhcell8j.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell8j.setColspan(4);
            lhcell8j.setBorder(0);
            lhcell8j.setPadding(2);
            lhtab2.addCell(lhcell8j);

            lhcellcol = new PdfPCell(new Paragraph(" ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab2.addCell(lhcellcol);

            PdfPCell lhcell8zk;
            lhcell8zk = new PdfPCell(new Paragraph(" --------------- ",subfontright));
            lhcell8zk.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
            lhcell8zk.setColspan(4);
            lhcell8zk.setBorder(0);
            lhcell8zk.setPadding(2);
            lhtab2.addCell(lhcell8zk);

            PdfPCell lhcell9;
            lhcell9 = new PdfPCell(new Paragraph("NET ",subfont));
            lhcell9.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9.setColspan(4);
            lhcell9.setBorder(0);
            lhcell9.setPadding(2);
            lhcell9.setPaddingBottom(5);
            lhtab2.addCell(lhcell9);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab2.addCell(lhcellcol);
            PdfPCell lhcell9z;
            lhcell9z = new PdfPCell(new Paragraph(""+gk.getNet_wt()+" Lb",subfontright));
            lhcell9z.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
            lhcell9z.setColspan(4);
            lhcell9z.setBorder(0);
            lhcell9z.setPadding(2);
            lhcell9z.setPaddingBottom(5);
            lhtab2.addCell(lhcell9z);


            doc.add(lhtab2);

            LineSeparator line1 = new LineSeparator();
            doc.add(line1);
            line1 = new LineSeparator();
            doc.add(line1);

            if(gk.getCreated_date() !=null && gk.getCreated_date().length()>0) {

                PdfPTable lhtab3 = new PdfPTable(8);
                lhtab3.setWidthPercentage(100);
                lhtab3.setHorizontalAlignment(0);
                StringTokenizer sk=new StringTokenizer(gk.getCreated_date()," ");
                String dateyy=sk.nextToken();

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
                Date dt = null;
                try {
                    dt = sdf.parse(sk.nextToken());
                    System.out.println("Time Display: " + sdfs.format(dt)); // <-- I got result here
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                PdfPCell lhcell9zaz;
                lhcell9zaz = new PdfPCell(new Paragraph("Bin Stamp No.:"+gk.getSlid(),subfontsub));
                lhcell9zaz.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lhcell9zaz.setColspan(3);
                lhcell9zaz.setBorder(0);
                lhcell9zaz.setPaddingBottom(7);
                lhtab3.addCell(lhcell9zaz);
                PdfPCell lhcell9za;
                lhcell9za = new PdfPCell(new Paragraph("Date Time:"+parseDateToddMMyyyy(dateyy)+ " | "+sdfs.format(dt),subfontsub));
                lhcell9za.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
                lhcell9za.setColspan(5);
                lhcell9za.setBorder(0);
                lhcell9za.setPaddingBottom(7);
                lhtab3.addCell(lhcell9za);
                doc.add(lhtab3);
            }else{
                // doc.add(lhtab1);
            }
            try
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                barcodemap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
                Image myImg = Image.getInstance(stream.toByteArray());
                myImg.setAlignment(Image.ALIGN_CENTER);
                //myImg.scaleToFit(180f, 35f);
                myImg.scaleToFit(260f, 58f);
                doc.add(myImg);
            }
            catch(IOException ex)
            {
                return;
            }
//            Intent mintent = new Intent(context, viewproduction.class);
//            context.startActivity(mintent);
            printz(file, "000"+gk.getTagno());
        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

    }

    public void createPDFtag(grs_bin_tag gk, production_response movieModel, Bitmap barcodemap) throws IOException, DocumentException {

        //Document doc = new Document(PageSize.B7);
        Document doc = new Document(PageSize.A6);
        try {
            try {
                File dir = context.getFilesDir();
                File filez = new File(dir, "gross_bin_tag.pdf");
                boolean deleted = filez.delete();
                if(deleted)
                {

                }
            }catch (Exception e)
            {

            }
            File file = new File(context.getFilesDir(),  "gross_bin_tag.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter.getInstance(doc, fOut);
            doc.open();
            Font subfontsub = FontFactory.getFont(FontFactory.HELVETICA, 9);
            Font subfontsubx = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
            PdfPTable headyr = new PdfPTable(1);
            headyr.setWidthPercentage(100);

            PdfPCell cellyr;
            cellyr = new PdfPCell(new Paragraph("Crop Year : "+movieModel.getCrop_year()
                    ,subfontsubx));
            cellyr.setBorder(0);
            cellyr.setPaddingBottom(5);
            cellyr.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            headyr.addCell(cellyr);
            doc.add(headyr);



            try {
                // get input stream
                Drawable d = context.getResources().getDrawable(R.drawable.logoprintnew);
                BitmapDrawable bitDw = ((BitmapDrawable) d);
                Bitmap bmp = bitDw.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                image.setAlignment(Element.ALIGN_CENTER);
                // image.scaleToFit(190f, 30f);
                image.scaleToFit(260f, 50f);
                doc.add(image);
            }
            catch(IOException ex)
            {
                return;
            }
//            LineSeparator line = new LineSeparator();
//            doc.add(line);
            Font yourCustomFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
            Font yourCustomFontsub = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font yourCustomFontz = FontFactory.getFont(FontFactory.HELVETICA, 13);
            PdfPTable head = new PdfPTable(1);
            head.setWidthPercentage(100);

            PdfPCell cellkzq;
            cellkzq = new PdfPCell(new Paragraph("",yourCustomFontz));
            cellkzq.setBorder(0);
            cellkzq.setPaddingBottom(5);
            cellkzq.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head.addCell(cellkzq);
            PdfPCell cellk;
            cellk = new PdfPCell(new Paragraph("Gross Bin Tag ",yourCustomFont));
            cellk.setBorder(0);
            cellk.setPaddingBottom(2);
			/*cellk.setBorderWidthLeft(1);
			cellk.setBorderWidthRight(1);*/

            cellk.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head.addCell(cellk);
            PdfPCell cellkzqw;
            cellkzqw = new PdfPCell(new Paragraph("",yourCustomFontz));
            cellkzqw.setBorder(0);
            cellkzqw.setPaddingBottom(5);
            cellkzqw.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head.addCell(cellkzqw);
            //PdfPCell cellkz;
//			cellkz = new PdfPCell(new Paragraph(""+movieModel.getType(),yourCustomFontz));
//			cellkz.setBorder(0);
//			cellkz.setPaddingBottom(2);
//			/*cellk.setBorderWidthLeft(1);
//			cellk.setBorderWidthRight(1);*/
//
//			cellkz.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
//			head.addCell(cellkz);

//			PdfPCell cellkzqwr;
//			cellkzqwr = new PdfPCell(new Paragraph("",yourCustomFontz));
//			cellkzqwr.setBorder(0);
//			cellkzqwr.setPaddingBottom(5);
//			cellkzqwr.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
//			head.addCell(cellkzqwr);

            //
            PdfPCell cellkzh;
            cellkzh = new PdfPCell(new Paragraph("Tag# "+": 0000"+gk.getTagno(),yourCustomFontsub));
            cellkzh.setBorder(0);
            cellkzh.setPaddingBottom(2);
            cellkzh.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head.addCell(cellkzh);

            PdfPCell cellkzqwrh;
            cellkzqwrh = new PdfPCell(new Paragraph("",yourCustomFontz));
            cellkzqwrh.setBorder(0);
            cellkzqwrh.setPaddingBottom(5);
            cellkzqwrh.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head.addCell(cellkzqwrh);
            cellkzqwrh = new PdfPCell(new Paragraph("",yourCustomFontz));
            cellkzqwrh.setBorder(0);
            cellkzqwrh.setPaddingBottom(5);
            cellkzqwrh.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head.addCell(cellkzqwrh);

            doc.add(head);
            Font subfont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
            Font subfontright = FontFactory.getFont(FontFactory.HELVETICA, 9);



//set driver name and date
            PdfPTable lhtab0 = new PdfPTable(9);
            lhtab0.setWidthPercentage(100);
            lhtab0.setHorizontalAlignment(0);


            PdfPCell lhcell2;
            lhcell2 = new PdfPCell(new Paragraph("RUN NO. ",subfont));
            lhcell2.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell2.setColspan(4);
            lhcell2.setBorder(0);
            lhcell2.setPadding(2);
            lhtab0.addCell(lhcell2);

            PdfPCell lhcell2zd;
            lhcell2zd = new PdfPCell(new Paragraph(": ",subfontright));
            lhcell2zd.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell2zd.setColspan(1);
            lhcell2zd.setBorder(0);
            lhcell2zd.setPadding(2);
            lhtab0.addCell(lhcell2zd);

            PdfPCell lhcell2z;
            lhcell2z = new PdfPCell(new Paragraph(""+movieModel.getPid(),subfontright));
            lhcell2z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell2z.setColspan(4);
            lhcell2z.setBorder(0);
            lhcell2z.setPadding(2);
            lhtab0.addCell(lhcell2z);


            //new

            PdfPCell lhcellmk2;
            lhcellmk2 = new PdfPCell(new Paragraph("Type ",subfont));
            lhcellmk2.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellmk2.setColspan(4);
            lhcellmk2.setBorder(0);
            lhcellmk2.setPadding(2);
            lhtab0.addCell(lhcellmk2);

            PdfPCell lhcell2zmkd;
            lhcell2zmkd = new PdfPCell(new Paragraph(": ",subfontright));
            lhcell2zmkd.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell2zmkd.setColspan(1);
            lhcell2zmkd.setBorder(0);
            lhcell2zmkd.setPadding(2);
            lhtab0.addCell(lhcell2zmkd);

            PdfPCell lhcell2mkz;
            lhcell2mkz = new PdfPCell(new Paragraph(""+gk.getType(),subfontright));
            lhcell2mkz.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell2mkz.setColspan(4);
            lhcell2mkz.setBorder(0);
            lhcell2mkz.setPadding(2);
            lhtab0.addCell(lhcell2mkz);



            PdfPCell lhcell3;
            lhcell3 = new PdfPCell(new Paragraph("HANDLER ",subfont));
            lhcell3.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell3.setColspan(4);
            lhcell3.setBorder(0);
            lhcell3.setPadding(2);
            lhtab0.addCell(lhcell3);

            PdfPCell lhcellcol;
            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab0.addCell(lhcellcol);

            PdfPCell lhcell3z;
            lhcell3z = new PdfPCell(new Paragraph(""+gk.getHandler(),subfontright));
            lhcell3z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell3z.setColspan(4);
            lhcell3z.setBorder(0);
            lhcell3z.setPadding(2);
            lhtab0.addCell(lhcell3z);


//
            PdfPCell lhcell4;
            lhcell4 = new PdfPCell(new Paragraph("GROWER ",subfont));
            lhcell4.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell4.setColspan(4);
            lhcell4.setBorder(0);
            lhcell4.setPadding(2);
            lhtab0.addCell(lhcell4);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab0.addCell(lhcellcol);

            PdfPCell lhcell4z;
            lhcell4z = new PdfPCell(new Paragraph(""+movieModel.getGrower(),subfontright));
            lhcell4z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell4z.setColspan(4);
            lhcell4z.setBorder(0);
            lhcell4z.setPadding(2);
            lhtab0.addCell(lhcell4z);

//
            PdfPCell lhcell5;
            lhcell5 = new PdfPCell(new Paragraph("VARIETY ",subfont));
            lhcell5.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell5.setColspan(4);
            lhcell5.setBorder(0);
            lhcell5.setPadding(2);
            lhtab0.addCell(lhcell5);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab0.addCell(lhcellcol);

            PdfPCell lhcell5z;
            lhcell5z = new PdfPCell(new Paragraph(""+movieModel.getVariety(),subfontright));
            lhcell5z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell5z.setColspan(4);
            lhcell5z.setBorder(0);
            lhcell5z.setPadding(2);
            lhtab0.addCell(lhcell5z);

            //
            PdfPCell lhcell6;
            lhcell6 = new PdfPCell(new Paragraph("RANCH NAME ",subfont));
            lhcell6.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell6.setColspan(4);
            lhcell6.setBorder(0);
            lhcell6.setPadding(2);
            lhtab0.addCell(lhcell6);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab0.addCell(lhcellcol);

            if(movieModel.getRanch_name() !=null && !movieModel.getRanch_name().contentEquals("null"))
            {
                PdfPCell lhcell6z;
                lhcell6z = new PdfPCell(new Paragraph(""+movieModel.getRanch_name(),subfontright));
                lhcell6z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lhcell6z.setColspan(4);
                lhcell6z.setBorder(0);
                lhcell6z.setPadding(2);
                lhtab0.addCell(lhcell6z);
            }else{
                PdfPCell lhcell6z;
                lhcell6z = new PdfPCell(new Paragraph(": ",subfontright));
                lhcell6z.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lhcell6z.setColspan(4);
                lhcell6z.setBorder(0);
                lhcell6z.setPadding(2);
                lhtab0.addCell(lhcell6z);
            }
//

            PdfPCell lhcell9f;
            lhcell9f = new PdfPCell(new Paragraph("FUMIGATION ",subfont));
            lhcell9f.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9f.setColspan(4);
            lhcell9f.setBorder(0);
            lhcell9f.setPadding(2);
            lhcell9f.setPaddingBottom(5);
            lhtab0.addCell(lhcell9f);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab0.addCell(lhcellcol);

            PdfPCell lhcell9ff;
            lhcell9ff = new PdfPCell(new Paragraph(" ",subfontright));
            lhcell9ff.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9ff.setColspan(4);
            lhcell9ff.setBorder(0);
            lhcell9ff.setPadding(2);
            lhcell9ff.setPaddingBottom(5);
            lhtab0.addCell(lhcell9ff);

            //

            PdfPCell lhcell9fz;
            lhcell9fz = new PdfPCell(new Paragraph(" ",subfont));
            lhcell9fz.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9fz.setColspan(9);
            lhcell9fz.setBorder(0);
            lhcell9fz.setPadding(2);
            lhtab0.addCell(lhcell9fz);

            //new fumugation date
            PdfPCell lhcell9af;
            lhcell9af = new PdfPCell(new Paragraph("FUMIGATION DATE",subfont));
            lhcell9af.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9af.setColspan(4);
            lhcell9af.setBorder(0);
            lhcell9af.setPadding(2);
            lhcell9af.setPaddingBottom(5);
            lhtab0.addCell(lhcell9af);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab0.addCell(lhcellcol);

            PdfPCell lhcell9sff;
            lhcell9sff = new PdfPCell(new Paragraph(" ",subfontright));
            lhcell9sff.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9sff.setColspan(4);
            lhcell9sff.setBorder(0);
            lhcell9sff.setPadding(2);
            lhcell9sff.setPaddingBottom(5);
            lhtab0.addCell(lhcell9sff);

            //

//			PdfPCell lhcell9dfz;
//			lhcell9dfz = new PdfPCell(new Paragraph(" ",subfont));
//			lhcell9dfz.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
//			lhcell9dfz.setColspan(8);
//			lhcell9dfz.setBorder(0);
//			lhcell9dfz.setPadding(2);
//			lhcell9dfz.setPaddingBottom(5);
//			lhtab0.addCell(lhcell9dfz);
            doc.add(lhtab0);


            LineSeparator line = new LineSeparator();
            doc.add(line);



            PdfPTable lhtab2 = new PdfPTable(9);
            lhtab2.setWidthPercentage(100);
            lhtab2.setHorizontalAlignment(0);



            PdfPCell lhcell7;
            lhcell7 = new PdfPCell(new Paragraph("GROSS WEIGHT ",subfont));
            lhcell7.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell7.setColspan(4);
            lhcell7.setBorder(0);
            lhcell7.setPadding(2);
            lhtab2.addCell(lhcell7);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab2.addCell(lhcellcol);

            PdfPCell lhcell7z;
            lhcell7z = new PdfPCell(new Paragraph(""+gk.getGross_wt()+" Lb",subfontright));
            lhcell7z.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
            lhcell7z.setColspan(4);
            lhcell7z.setBorder(0);
            lhcell7z.setPadding(2);
            lhtab2.addCell(lhcell7z);



            PdfPCell lhcell8;
            lhcell8 = new PdfPCell(new Paragraph("TARE ",subfont));
            lhcell8.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell8.setColspan(4);
            lhcell8.setBorder(0);
            lhcell8.setPadding(2);
            lhtab2.addCell(lhcell8);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab2.addCell(lhcellcol);
            PdfPCell lhcell8z;
            lhcell8z = new PdfPCell(new Paragraph(""+gk.getTare()+" Lb",subfontright));
            lhcell8z.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
            lhcell8z.setColspan(4);
            lhcell8z.setBorder(0);
            lhcell8z.setPadding(2);
            lhtab2.addCell(lhcell8z);


            PdfPCell lhcell8j;
            lhcell8j = new PdfPCell(new Paragraph("",subfont));
            lhcell8j.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell8j.setColspan(4);
            lhcell8j.setBorder(0);
            lhcell8j.setPadding(2);
            lhtab2.addCell(lhcell8j);

            lhcellcol = new PdfPCell(new Paragraph(" ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab2.addCell(lhcellcol);

            PdfPCell lhcell8zk;
            lhcell8zk = new PdfPCell(new Paragraph(" --------------- ",subfontright));
            lhcell8zk.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
            lhcell8zk.setColspan(4);
            lhcell8zk.setBorder(0);
            lhcell8zk.setPadding(2);
            lhtab2.addCell(lhcell8zk);

            PdfPCell lhcell9;
            lhcell9 = new PdfPCell(new Paragraph("NET ",subfont));
            lhcell9.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell9.setColspan(4);
            lhcell9.setBorder(0);
            lhcell9.setPadding(2);
            lhcell9.setPaddingBottom(5);
            lhtab2.addCell(lhcell9);

            lhcellcol = new PdfPCell(new Paragraph(": ",subfontright));
            lhcellcol.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcellcol.setColspan(1);
            lhcellcol.setBorder(0);
            lhcellcol.setPadding(2);
            lhtab2.addCell(lhcellcol);
            PdfPCell lhcell9z;
            lhcell9z = new PdfPCell(new Paragraph(""+gk.getNet_wt()+" Lb",subfontright));
            lhcell9z.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
            lhcell9z.setColspan(4);
            lhcell9z.setBorder(0);
            lhcell9z.setPadding(2);
            lhcell9z.setPaddingBottom(5);
            lhtab2.addCell(lhcell9z);


            doc.add(lhtab2);

            LineSeparator line1 = new LineSeparator();
            doc.add(line1);
            line1 = new LineSeparator();
            doc.add(line1);

            if(gk.getCreated_date() !=null && gk.getCreated_date().length()>0) {

                PdfPTable lhtab3 = new PdfPTable(8);
                lhtab3.setWidthPercentage(100);
                lhtab3.setHorizontalAlignment(0);
                StringTokenizer sk=new StringTokenizer(gk.getCreated_date()," ");
                String dateyy=sk.nextToken();

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
                Date dt = null;
                try {
                    dt = sdf.parse(sk.nextToken());
                    System.out.println("Time Display: " + sdfs.format(dt)); // <-- I got result here
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                PdfPCell lhcell9zaz;
                lhcell9zaz = new PdfPCell(new Paragraph("Bin Stamp No.:"+gk.getSlid(),subfontsub));
                lhcell9zaz.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lhcell9zaz.setColspan(3);
                lhcell9zaz.setBorder(0);
                lhcell9zaz.setPaddingBottom(7);
                lhtab3.addCell(lhcell9zaz);
                PdfPCell lhcell9za;
                lhcell9za = new PdfPCell(new Paragraph("Date Time:"+parseDateToddMMyyyy(dateyy)+ " | "+sdfs.format(dt),subfontsub));
                lhcell9za.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
                lhcell9za.setColspan(5);
                lhcell9za.setBorder(0);
                lhcell9za.setPaddingBottom(7);
                lhtab3.addCell(lhcell9za);
                doc.add(lhtab3);
            }else{
                // doc.add(lhtab1);
            }
            try
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                barcodemap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
                Image myImg = Image.getInstance(stream.toByteArray());
                myImg.setAlignment(Image.ALIGN_CENTER);
                //myImg.scaleToFit(180f, 35f);
                myImg.scaleToFit(260f, 58f);
                doc.add(myImg);
            }
            catch(IOException ex)
            {
                return;
            }
//            Intent mintent = new Intent(context, viewproduction.class);
//            context.startActivity(mintent);
            printz(file, "000"+gk.getTagno());
        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "MM/dd/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    private void printz(File file, String pid) {



//        Log.e("file", "@:" + file.toString());
        PrintManager manager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
//        PrintDocumentInfo.Builder builder=
//                new PrintDocumentInfo.Builder(" file name");
        PDFDocumentAdapter adapter = new PDFDocumentAdapter(file,"GBT_"+pid);
        PrintAttributes attributes = new PrintAttributes.Builder().build();
        manager.print("Document", adapter, attributes);



    }
}