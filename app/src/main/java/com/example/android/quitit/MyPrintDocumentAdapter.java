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
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.view.View;

import java.io.FileOutputStream;
import java.io.IOException;

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
    /*public MyPrintDocumentAdapter(Context context) {
        this.context = context;
        //Bundle B = this.getIntent().getExtras();
        //Entry ClickedEntry = B.getParcelable("ClickedEntry");
    }*/

    public MyPrintDocumentAdapter(Context context, String name, String sex, int age, String bussiness, String marrital_status, int consumption, String spent) {
        this.context = context;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.bussiness = bussiness;
        this.marrital_status = marrital_status;
        this.consumption = consumption;
        this.spent = spent;
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
            if (pageInRange(pages, i))
            {
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                        pageHeight, i).create();

                PdfDocument.Page page =
                        myPdfDocument.startPage(newPage);

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

        pagenumber++; // Make sure page numbers start at 1

        /*int titleBaseLine = 72;
        int leftMargin = 54;*/

        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setTextSize(20);
        canvas.drawRect(35,30,550,80,paint);
        paint.setColor(Color.BLACK);
        canvas.drawText("REPORT",265,60,paint);
        paint.setTextSize(30);
        canvas.drawText(
                name,
                50,
                130,
                paint);
        paint.setTextSize(20);
        canvas.drawText(
                sex,
                50,
                160,
                paint);
        canvas.drawText(
                String.valueOf(age),
                100,
                160,
                paint);
        canvas.drawText(
                "Bussiness: " + bussiness,
                50,
                220,
                paint);
        canvas.drawText(
                "Marrital Status: " + marrital_status,
                50,
                250,
                paint);
        canvas.drawText(
                "Cigarettes consumed per day: " + consumption,
                50,
                280,
                paint);
        canvas.drawText(
                "Fraction of salary spent: " + spent,
                50,
                310,
                paint);
        /*paint.setTextSize(14);
        canvas.drawText("This is some test content to verify that custom document printing works", leftMargin, titleBaseLine + 35, paint);*/

        /*if (pagenumber % 2 == 0)
            paint.setColor(Color.RED);
        else
            paint.setColor(Color.GREEN);
        */

        PdfDocument.PageInfo pageInfo = page.getInfo();


        /*canvas.drawCircle(pageInfo.getPageWidth()/2,
                pageInfo.getPageHeight()/2,
                150,
                paint);*/
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