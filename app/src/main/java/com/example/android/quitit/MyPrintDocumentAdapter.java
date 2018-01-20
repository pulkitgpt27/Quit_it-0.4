package com.example.android.quitit;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kakrya on 7/11/2017.
 */

@TargetApi(Build.VERSION_CODES.KITKAT)
public class MyPrintDocumentAdapter extends PrintDocumentAdapter {
    Context context;
    private int pageHeight;
    private int pageWidth;
    public PdfDocument myPdfDocument;
    public int totalpages = 1;
    String name,sex,bussiness,marrital_status,spent;
    int age,consumption;
    String personal_msg;
    String lines[];
    ArrayList<String> personal_message_fragments;
    String words[];
    String temp_line;
    int line_count;
    /*public MyPrintDocumentAdapter(Context context) {
        this.context = context;
        //Bundle B = this.getIntent().getExtras();
        //Entry ClickedEntry = B.getParcelable("ClickedEntry");
    }*/

    public MyPrintDocumentAdapter(Context context, String name, String sex, int age, String bussiness, String marrital_status, int consumption, String spent,String msg) {
        this.context = context;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.bussiness = bussiness;
        this.marrital_status = marrital_status;
        this.consumption = consumption;
        this.spent = spent;
        this.personal_msg = msg;
        this.lines = personal_msg.split("\\. ");
        personal_message_fragments = new ArrayList<String>();

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        myPdfDocument = new PrintedPdfDocument(context, newAttributes);

        pageHeight =
                newAttributes.getMediaSize().getHeightMils()/1000 * 72;
        pageWidth =
                newAttributes.getMediaSize().getWidthMils()/1000 * 72;

        if (cancellationSignal.isCanceled() ) {
            callback.onLayoutCancelled();
            return;
        }

        if (totalpages > 0) {
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                    .Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(totalpages);

            PrintDocumentInfo info = builder.build();
            callback.onLayoutFinished(info, true);
        } else {
            callback.onLayoutFailed("Page count is zero.");
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        for (int i = 0; i < totalpages; i++) {
            if(pageInRange(pages, i))
            {
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, i).create();

                PdfDocument.Page page = myPdfDocument.startPage(newPage);

                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    myPdfDocument.close();
                    myPdfDocument = null;
                    return;
                }
                drawPage(page, i);
                myPdfDocument.finishPage(page);
            }
        }
        try {
            myPdfDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            myPdfDocument.close();
            myPdfDocument = null;
        }
        callback.onWriteFinished(pages);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)

    private void drawPage(PdfDocument.Page page, int pagenumber) {
        Canvas canvas = page.getCanvas();
        int y_max = canvas.getHeight();
        pagenumber++; // Make sure page numbers start at 1

        /*int titleBaseLine = 72;
        int leftMargin = 54;*/

        Paint paint = new Paint();
        paint.setTextSize(12);
        paint.setColor(Color.BLACK);
        int y;
        paint.setColor(Color.GRAY);
        canvas.drawRect(35, 30, 550, 80, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText("REPORT", 265, 60, paint);
        paint.setTextSize(20);
        canvas.drawText(name, 50, 130, paint);
        paint.setTextSize(12);
        canvas.drawText(sex, 50, 160, paint);
        canvas.drawText(String.valueOf(age), 150, 160, paint);
        canvas.drawText("Profession: " + bussiness, 50, 180, paint);
        canvas.drawText("Marrital Status: " + marrital_status, 50, 210, paint);
        canvas.drawText("Cigarettes consumed per day: " + consumption, 350, 180, paint);
        canvas.drawText("Fraction of salary spent: " + spent, 350, 210, paint);
        canvas.drawText("Harms:", 50, 240, paint);
        y = 270;
        for(int i=0; i<lines.length; i++){
            if(lines[i].length()>80){
                words = lines[i].split(" ");
                temp_line = words[0];
                for(int j=1; j<words.length; j++){
                    if(temp_line.length() + words[j].length() < 80) {
                        temp_line = temp_line + " " + words[j];
                        if(j+1==words.length){
                            //last word.
                            personal_message_fragments.add(temp_line);
                        }
                    }
                    else{
                        j--;
                        personal_message_fragments.add(temp_line);
                        temp_line = "";
                    }
                }
                int j=0;
                for(String e: personal_message_fragments){
                    if(j==0){
                        canvas.drawText(""+(i+1)+" ."+e, 50, y, paint);
                        y+=20;
                    }
                    else{
                        canvas.drawText("  "+e, 50, y, paint);
                        y+=20;
                    }
                    j++;
                }
                personal_message_fragments.removeAll(personal_message_fragments);
            }
            else{
                canvas.drawText(""+(i+1)+" ."+lines[i], 50, y, paint);
                y+=20;
            }
        }
        PdfDocument.PageInfo pageInfo = page.getInfo();
    }
    private boolean pageInRange(PageRange[] pageRanges, int page)
    {
        for (int i = 0; i<pageRanges.length; i++)
        {
            if ((page >= pageRanges[i].getStart()) &&
                    (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }
}